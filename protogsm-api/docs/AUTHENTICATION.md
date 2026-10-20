# Authentication

The API uses a session cookie, not a token in the response body. protogsm-api runs at `http://localhost:8080` with the context path `/protogsm`. Business routes live under `/businesscore/api/v1`.

MariaDB has to be up before the application starts. See [protogsm-database/docs/DATABASE.md](../../protogsm-database/docs/DATABASE.md).

## What protects a request

`SecurityConfig` builds one filter chain:

| Check | Rule |
|---|---|
| Authentication | A handler marked with `@PublicEndpoint` is open. Every other route requires a logged-in session. |
| CSRF | `POST`, `PUT`, `PATCH`, and `DELETE` also require the CSRF header. `GET` does not. |
| Error dispatch | The container error dispatch is allowed through, so a rejected call can still return 401 or 403. |

`@PublicEndpoint` may sit on a method or on a controller class. `SecurityConfig` reads the mappings after the module prefix is applied, and it permits only the HTTP method declared on that mapping. An annotation on the class opens every method of that controller.

These handlers are public today:

| Method | Path |
|---|---|
| `GET` | `/protogsm/businesscore/api/v1/auth/csrf` |
| `POST` | `/protogsm/businesscore/api/v1/auth/login` |

The STOMP handshake at `/protogsm/ws` is not public. The console socket uses the same session cookie as the rest of the API.

## Session

`POST /auth/login` checks the username and the password with `UserDetailsServiceImpl`. The password stored on the user is a BCrypt hash, written by `UserServiceImpl` when the user is created.

On startup, `UserServiceImpl` looks for an existing administrator. If none exists, it creates one from `protogsm.super-user.username` and `protogsm.super-user.password` in `application.properties`. The password is stored as a BCrypt hash. Changing those properties later does not update an account that is already in the database. There is no public endpoint to create this user.

On success, login saves the `SecurityContext` in the HTTP session and the container sets `JSESSIONID`. The JSON body is only `{"authenticated": true}`. The session id is not returned in the body. If a session already existed, its id is rotated before the context is saved.

`POST /auth/logout` requires a logged-in session. It invalidates that session and clears `JSESSIONID` on the path `/protogsm`. The response is `204`.

A call without a session gets `401`. A call with a session that fails CSRF, or that is otherwise forbidden, gets `403`. A wrong username or password on login gets `401` and `{"message":"Invalid credentials"}`.

## CSRF

The browser sends `JSESSIONID` on its own to `localhost:8080`. Another site cannot read that cookie, but it can make the browser fire a `POST` at this API, and the cookie would go with it.

The CSRF cookie stops that. The server sets `XSRF-TOKEN` (not HttpOnly, path `/protogsm`). A `POST`, `PUT`, `PATCH`, or `DELETE` is accepted only when the same value comes back in the header `X-XSRF-TOKEN`. The other site cannot read the cookie, so it cannot build the header. Postman and an Angular app on `http://localhost:4200` can.

`GET /auth/csrf` exists to hand out that cookie before the first `POST`. A `GET` does not change state, so the server does not ask for the header. The response is `204` plus `Set-Cookie: XSRF-TOKEN=...`. Login is a `POST`, so it needs the header even though it does not need a session. That also blocks a site from logging the browser into an account someone else controls.

`SpaCsrfTokenRequestHandler` accepts the raw cookie value in the header. That is the value an Angular `HttpClient` sends when `withCredentials` is on. CORS allows `http://localhost:4200` with credentials. The allowed origin is in `CorsGlobalConfig`.

Later `POST`, `PUT`, `PATCH`, and `DELETE` calls keep sending `X-XSRF-TOKEN`. A `GET` only needs `JSESSIONID`.

## Postman

Create the requests in one collection so they share the cookie jar. The application must already be running.

### 1. Receive the CSRF cookie

`GET http://localhost:8080/protogsm/businesscore/api/v1/auth/csrf`

Expect `204`. In the response cookies, `XSRF-TOKEN` is set for the path `/protogsm`. Postman stores it.

### 2. Log in

`POST http://localhost:8080/protogsm/businesscore/api/v1/auth/login`

Same CSRF header.

```json
{
  "username": "admin",
  "password": "change-me"
}
```

The repository ships `change-me` as a placeholder. Set your own in `protogsm.super-user.username` and `protogsm.super-user.password` before the first start. Expect `200` and `{"authenticated": true}`. Postman stores `JSESSIONID`.

### 3. Call a protected route

`GET http://localhost:8080/protogsm/businesscore/api/v1/user/me`

No CSRF header. Postman sends `JSESSIONID` from the jar. Expect `200` and the logged-in username.

A protected `POST`, `PUT`, or `DELETE` still needs `X-XSRF-TOKEN`.

### 4. Log out

`POST http://localhost:8080/protogsm/businesscore/api/v1/auth/logout`

Send both cookies and the CSRF header. Expect `204`. The next call to `/user/me` returns `401`.

### Collection script

On the collection, open **Scripts → Pre-request** and paste this. Each request then copies the CSRF cookie into the header when the cookie is already in the jar.

```javascript
const token = pm.cookies.get("XSRF-TOKEN");
if (token) {
    pm.request.headers.upsert({
        key: "X-XSRF-TOKEN",
        value: token
    });
}
```

Run the CSRF `GET` once before the other requests. The script only forwards a cookie that the jar already has.

## When a call fails

| Status | Meaning |
|---|---|
| `403` on a `POST` | The CSRF header is missing, or it does not match `XSRF-TOKEN`. Repeat the CSRF `GET` and send the new value. |
| `401` and `Invalid credentials` | CSRF passed. The username or the password is wrong. |
| `401` on any other route | There is no session. Log in again. |

Cookies are scoped to the path `/protogsm`. A request that omits that prefix never receives them.

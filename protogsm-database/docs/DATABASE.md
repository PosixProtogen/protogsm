# Database (MariaDB)

The container lives in `protogsm-database/` and uses the same settings the Spring app already expects in `protogsm-api/src/main/resources/application.properties`.

| | |
|---|---|
| Image | `mariadb:11.8` |
| Port | `localhost:3306` |
| Database | `protosystemintegrator` |
| Application user | `protogsmdb` |
| Root | `root` |
| Data | `protogsm-database/data/` (ignored by git) |

The committed passwords are `change-me`. Choose your own before the first `docker compose up` and write them in both places:

- `protogsm-database/docker-compose.yml`: `MARIADB_ROOT_PASSWORD` and `MARIADB_PASSWORD`
- `protogsm-api/src/main/resources/application.properties`: `spring.datasource.password` must match `MARIADB_PASSWORD`

The admin password for the API is `protogsm.super-user.password` in that same properties file. The first start of protogsm-api creates the account. Changing the property later does not update an admin that already exists.

MariaDB saves the database passwords on the first start. Changing the compose file later does not change a database that is already in `protogsm-database/data/`. Delete that folder and start again when you want a new password. The database and the `protogsmdb` user are created on that first start. Spring creates the tables afterward, because `ddl-auto` is set to `update`.

## Start

```fish
cd /mnt/LinuxArquivos/Projetos/repositorios/protogsm/protogsm-database
docker compose up -d
```

## Check that it is healthy

```fish
docker compose ps
docker compose logs -f mariadb
```

When the status is `healthy`, start protogsm-api. `Ctrl+C` leaves the log. The container keeps running.

## Open a database shell

```fish
docker exec -it protogsm-mariadb mariadb -uprotogsmdb -p protosystemintegrator
```

The password is whatever you set in `MARIADB_PASSWORD`. To sign in as the administrator, use `-uroot -p` and the password from `MARIADB_ROOT_PASSWORD`.

## DBeaver

Create a MariaDB connection with these values:

| Field | Value |
|---|---|
| Host | `localhost` |
| Port | `3306` |
| Database | `protosystemintegrator` |
| Username | `protogsmdb` |
| Password | the value of `MARIADB_PASSWORD` |

Use `root` and `MARIADB_ROOT_PASSWORD` when you need every database on the server. The application user only sees `protosystemintegrator`.

DBeaver downloads the MariaDB driver the first time you test the connection. The container has to be healthy before the test succeeds.

## Stop and start again

```fish
docker compose stop
docker compose start
```

`stop` shuts the container down and keeps the data. `docker compose down` also keeps what is in `protogsm-database/data/`. Deleting that folder wipes the database. The next start creates it again from scratch.

`docker compose ps` only shows the container status. `docker compose start` turns existing stopped containers back on.

If port `3306` is already taken by another MariaDB or MySQL on the machine, `up` fails.

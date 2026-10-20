# ProtoGSM

**Under construction.** This is my personal project for running my own SteamCMD game servers. Anyone else is welcome to use it, but it is not finished and a lot of it still changes.

The name comes from Protogen Game Server Manager. The idea is to create, start, stop, and update dedicated servers from one place. The Vue screen for that does not exist yet.

## Folders

| Folder | What it is |
|---|---|
| `protogsm-api/` | The Spring Boot API. Login, CRUD, and the game-server model are here. |
| `protogsm-client/` | The Vue app, when it exists. Empty for now. |
| `protogsm-database/` | MariaDB on this machine, with the data files. |

## Docs

- [protogsm-database/docs/DATABASE.md](protogsm-database/docs/DATABASE.md) shows how to start the database.
- [protogsm-api/docs/AUTHENTICATION.md](protogsm-api/docs/AUTHENTICATION.md) shows how login and CSRF work.

Start MariaDB and wait until it is healthy before starting `protogsm-api`. The API is at `http://localhost:8080/protogsm`.

# Authentication API

This API allows the creation, access, revocation of access and deletion of student accounts. It's part of a larger project that's meant to manage the students and their classes in a university.

## Getting up and running

Simply use the `compose.yml` file at the root of this repository to start the containers that are needed to use this API. On a linux system, you can do so by using the following command in a terminal:

```bash
docker compose up --build
```

> Note: you may need admin privileges in order to run this command.

> Note: The next time you will need to start the service, you can omit the `--build` option, as you won't need to build the project a second time.

# Authentication API

This API allows the creation, access, revocation of access and deletion of student accounts. It's part of a larger project that's meant to manage the students and their classes in a university.

## Getting up and running

Simply use the `compose.yml` file at the root of this repository to start the containers that are needed to use this API. On a linux system, you can do so by using the following command in a terminal:

```bash
docker compose up --build
```

> Note: you may need admin privileges in order to run this command.

> Note: The next time you will need to start the service, you can omit the `--build` option, as you won't need to build the project a second time.

## Endpoints

### Registration

You can register a new student account by making a `POST` request to the `/auth/register` endpoint, and providing a JSON body containing a username, a password and a student id.

On success, a signed JWT that contains a student is returned. This token is meant to be used as an authorization for all other services that require authentication.

> Note: this route will send a `Set-Cookie` header meant add a new cookie named `token` that contains the authentication token returned by this route.

### Access

You can acquire an access token by `POST`ing to the `/auth/login` endpoint. You will need to send a JSON body containing the username and password of the account in which you want to authenticate.

Just like the `/auth/register` route, this will return a signed JWT you can use to authenticate with services that require one.

> Note: this route will send a `Set-Cookie` header meant add a new cookie named `token` that contains the authentication token returned by this route.

### Token verification

To verify the authenticity and validity of a token, you can simply set the token in the `Authorization` header and send a `GET` request to `/auth/me`.

If the token is valid, you will receive a JSON object containing the student id that is associated with the account the token points to.

### Revoke access

Also known as "logging out".

To make it so a token can not be used anymore, send a `DELETE` request to `/auth/logout` with the `Authorization` header set to the token you want to revoke.

> Note: this route will send a `Set-Cookie` header meant to erase the cookie named `token`.

### Unregistration

To delete a student account, you use the `/auth/unregister` route using the `DELETE` method. This route requires the `username` and `password` URL parameters to be set and be valid account credentials.

On success, a message will be return to indicate that the account has been deleted.
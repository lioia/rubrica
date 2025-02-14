# Rubrica

## Building

```shell
mvn clean package
```

## Running

**MySQL Database (using Docker)**:

```shell
docker run --name mysql \
    -e MYSQL_ROOT_PASSWORD=password \
    -e MYSQL_DATABASE=rubrica \
    -e MYSQL_USER=admin \
    -e MYSQL_PASSWORD=password \
    -v $(pwd)/schema_database.sql:/docker-entrypoint-initdb.d/init.sql \
    -p 3306:3306 \
    -d mysql
```

**Configure `credenziali_database.properties`**

**Running**: run `Rubrica.jar`

## Notes

### Insecure Login

The current implementation expects the password to be saved in plaintext (both inside the file or in the db).

An improvement would require the password to be encrypted using a cryptographic library, like bcrypt.

### Linux Wayland-specific

When running under Wayland these environment variables have to be set:

- `_JAVA_AWT_WM_NONREPARENTING=1`
- `GDK_BACKEND=x11`

Running under Wayland requires this command:

```shell
_JAVA_AWT_WM_NONREPARENTING=1 GDK_BACKEND=x11 java -jar Rubrica.jar
```


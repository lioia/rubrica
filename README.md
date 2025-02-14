# Rubrica

## Building

```shell
mvn clean package
```

## Running

Run: `Rubrica.jar`

### MySQL Database

**Docker** (tested under Linux):

```shell
docker run --name mysql \
    -e MYSQL_ROOT_PASSWORD=password \
    -e MYSQL_DATABASE=rubrica \
    -e MYSQL_USER=admin \
    -e MYSQL_PASSWORD=password \
    -v "$(pwd)/schema_database.sql:/docker-entrypoint-initdb.d/init.sql" \
    -p 3306:3306 \
    -d mysql
```

Otherwise using MySQL shell (tested under Windows):

- Open MySQL Shell (`mysqlsh.exe`) in the directory containing the `schema_database.sql` file
- Run: `<mysql-user>@localhost:3306` where `<mysql-user>` is the MySQL User with admin access
- Run: `\sql` to switch to SQL command mode
- Run: `\source schema_database.sql` to create the database

**Configure `credenziali_database.properties`** based on the database configuruation

## Notes

### Insecure Login

The current implementation expects the password to be saved in plaintext (inside the file or in the db).

An improvement would require the password to be encrypted using a cryptographic library, like bcrypt.

### Linux Wayland-specific

When running under Wayland these environment variables have to be set:

- `_JAVA_AWT_WM_NONREPARENTING=1`
- `GDK_BACKEND=x11`

Running under Wayland requires this command:

```shell
_JAVA_AWT_WM_NONREPARENTING=1 GDK_BACKEND=x11 java -jar Rubrica.jar
```
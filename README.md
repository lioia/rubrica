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

**Running**: execute `Rubrica.jar`

## Notes

When running under Wayland these environment variables have to be set:

- `_JAVA_AWT_WM_NONREPARENTING=1`
- `GDK_BACKEND=x11`
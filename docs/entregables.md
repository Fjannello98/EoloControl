# Entregables cubiertos por el prototipo

## Definicion de base de datos para el sistema

La base `ecoviento` conserva la informacion operativa del prototipo: usuarios, centrales eolicas, turbinas, registros de telemetria y alertas. Esta estructura permite persistir datos de monitoreo y consultar el estado de generacion de cada turbina.

## Diagrama entidad-relacion de la base de datos

El DER esta incluido en el informe. En el codigo, la misma estructura se refleja en el archivo `sql/01_schema.sql`.

Relaciones principales:

- Una central eolica puede tener muchas turbinas.
- Una turbina puede tener muchos registros de telemetria.
- Una turbina puede tener muchas alertas.
- Una alerta puede estar asociada a un registro de telemetria.

## Creacion de tablas MySQL

Archivo: `sql/01_schema.sql`.

Incluye `CREATE DATABASE`, `USE ecoviento` y las sentencias `CREATE TABLE` con claves primarias y foraneas.

## Insercion, consulta y borrado de registros

- Insercion de datos: `sql/02_seed.sql`.
- Consultas de verificacion: `sql/03_queries.sql`.
- Borrado de datos de prueba: `sql/04_delete_test_data.sql`.

## Presentacion de consultas SQL

Las consultas SQL se entregan como archivos separados para que puedan ejecutarse en MySQL Workbench, phpMyAdmin o terminal MySQL, sin ocupar espacio innecesario dentro del informe.

## Definiciones de comunicacion

El prototipo se comunica con la base de datos mediante JDBC. La aplicacion Java usa MySQL Connector/J como controlador y toma los parametros de conexion desde variables de entorno. La interfaz grafica esta desarrollada con Swing y se ejecuta como aplicacion de escritorio local.

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

Por defecto, intenta conectarse a `jdbc:mysql://localhost:3306/ecoviento`.

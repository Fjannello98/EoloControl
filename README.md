# EoloControl Java

Prototipo con interfaz grafica Swing para el sistema de monitoreo de generacion y estado de turbinas eolicas de ECO Viento.

El proyecto esta desarrollado en Java y utiliza MySQL para la persistencia de datos. El alcance del prototipo cubre las funciones principales necesarias para registrar centrales, turbinas, telemetria, alertas y reportes basicos de energia. Tambien se conserva una version de consola como alternativa tecnica.

## Funcionalidades

- Autenticacion de usuario.
- Registro y listado de centrales eolicas.
- Registro y listado de turbinas eolicas.
- Registro de telemetria de viento y energia generada.
- Generacion automatica de alertas por baja generacion.
- Consulta de alertas pendientes.
- Reporte de energia generada por turbina.

## Tecnologias

- Java 17.
- Swing.
- MySQL Server.
- MySQL Connector/J.
- Visual Studio Code con extensiones Java.

## Estructura

```text
EoloControl-Java/
  docs/
    entregables.md
  sql/
    01_schema.sql
    02_seed.sql
    03_queries.sql
    04_delete_test_data.sql
  lib/
    mysql-connector-j-8.4.0.jar
  scripts/
    compile.ps1
    run.ps1
    start-mysql.ps1
    load-db.ps1
  src/main/java/eolocontrol/
    App.java
    SwingApp.java
    dao/
    db/
    model/
    service/
    view/
```

## Base de datos

Crear la base y cargar datos iniciales:

```powershell
mysql -u root -p < sql/01_schema.sql
mysql -u root -p < sql/02_seed.sql
```

Tambien se puede usar el script incluido:

```powershell
.\scripts\load-db.ps1
```

Ejecutar consultas de verificacion:

```powershell
mysql -u root -p < sql/03_queries.sql
```

Borrar datos de prueba:

```powershell
mysql -u root -p < sql/04_delete_test_data.sql
```

## Configuracion de conexion

La aplicacion usa estas variables de entorno. Si no se definen, intenta conectarse a MySQL local con usuario `root` y password vacio.

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/ecoviento?useSSL=false&serverTimezone=UTC"
$env:DB_USER="root"
$env:DB_PASSWORD=""
```

## Ejecucion

```powershell
.\scripts\compile.ps1
.\scripts\run.ps1
```

La clase principal grafica es:

```text
eolocontrol.SwingApp
```

La version de consola queda disponible en:

```text
eolocontrol.App
```

Usuarios de prueba:

- `admin` / `admin123`
- `operador1` / `operador123`

## Entregables relacionados

Los archivos SQL y el codigo fuente cubren:

- Definicion de base de datos para el sistema.
- Creacion de tablas MySQL.
- Insercion, consulta y borrado de registros.
- Presentacion de consultas SQL.
- Definiciones de comunicacion mediante JDBC.

# EoloControl Java

Prototipo de escritorio para el monitoreo de generacion y estado de turbinas eolicas de ECO Viento.

La aplicacion esta desarrollada en Java con Swing y utiliza MySQL para la persistencia de datos. El proyecto no requiere Maven ni Gradle: se compila directamente con `javac` y usa MySQL Connector/J incluido en la carpeta `lib`.

## Funcionalidades

- Autenticacion de usuario.
- Registro y listado de centrales eolicas.
- Registro y listado de turbinas eolicas.
- Registro de telemetria de viento y energia generada.
- Generacion automatica de alertas por baja generacion.
- Consulta de alertas pendientes.
- Reporte basico de energia generada por turbina.

## Requisitos

- Java JDK 17 o superior.
- MySQL Server 8 o superior.
- Cliente de MySQL disponible en la terminal (`mysql`).

## Estructura del proyecto

```text
EoloControl-Java/
  docs/
  lib/
    mysql-connector-j-8.4.0.jar
  scripts/
    compile.ps1
    compile.sh
    load-db.ps1
    load-db.sh
    run.ps1
    run.sh
  sql/
    01_schema.sql
    02_seed.sql
    03_queries.sql
    04_delete_test_data.sql
  src/main/java/eolocontrol/
```

## Configurar la base de datos

Antes de ejecutar la aplicacion, MySQL debe estar instalado y en ejecucion.

### Windows PowerShell

```powershell
.\scripts\load-db.ps1
```

### Linux, macOS o Git Bash

```bash
chmod +x scripts/*.sh
./scripts/load-db.sh
```

Si MySQL tiene otro usuario o password, se pueden definir variables de entorno antes de cargar la base.

Windows PowerShell:

```powershell
$env:MYSQL_USER="root"
$env:MYSQL_PASSWORD="tu_password"
.\scripts\load-db.ps1
```

Linux, macOS o Git Bash:

```bash
export MYSQL_USER=root
export MYSQL_PASSWORD=tu_password
./scripts/load-db.sh
```

## Configurar la conexion de la aplicacion

Por defecto, la aplicacion intenta conectarse a:

```text
jdbc:mysql://localhost:3306/ecoviento?useSSL=false&serverTimezone=UTC
```

Credenciales por defecto:

```text
Usuario: root
Password: vacio
```

Si se necesita cambiar la conexion:

Windows PowerShell:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/ecoviento?useSSL=false&serverTimezone=UTC"
$env:DB_USER="root"
$env:DB_PASSWORD="tu_password"
```

Linux, macOS o Git Bash:

```bash
export DB_URL="jdbc:mysql://localhost:3306/ecoviento?useSSL=false&serverTimezone=UTC"
export DB_USER=root
export DB_PASSWORD=tu_password
```

## Ejecutar la aplicacion

### Windows PowerShell

```powershell
.\scripts\compile.ps1
.\scripts\run.ps1
```

### Linux, macOS o Git Bash

```bash
chmod +x scripts/*.sh
./scripts/compile.sh
./scripts/run.sh
```

La clase principal grafica es:

```text
eolocontrol.SwingApp
```

La version de consola queda disponible en:

```text
eolocontrol.App
```

## Usuarios de prueba

```text
admin / admin123
operador1 / operador123
```

## Consultas SQL

Los scripts SQL se encuentran en la carpeta `sql`:

- `01_schema.sql`: creacion de base y tablas.
- `02_seed.sql`: datos iniciales.
- `03_queries.sql`: consultas de verificacion.
- `04_delete_test_data.sql`: limpieza de datos de prueba.

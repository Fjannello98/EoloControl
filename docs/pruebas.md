# Pruebas realizadas

## Compilacion

Comando ejecutado:

```powershell
.\scripts\compile.ps1
```

Resultado: compilacion correcta.

## SQL

Se ejecutaron los scripts:

- `sql/01_schema.sql`
- `sql/02_seed.sql`
- `sql/03_queries.sql`
- `sql/04_delete_test_data.sql`

Resultado: creacion de base, carga de datos, consultas y borrado verificados sobre MySQL local.

## Prueba funcional de consola

Flujo probado:

1. Login con `admin` / `admin123`.
2. Listado de centrales.
3. Listado de turbinas.
4. Reporte de energia por turbina.
5. Consulta de alertas pendientes.
6. Registro de telemetria con baja generacion.
7. Generacion automatica de alerta.

Resultado: flujo ejecutado correctamente.

## Interfaz Swing

Se agrego una interfaz grafica de escritorio con:

- Login de usuario.
- Pestana de centrales.
- Pestana de turbinas.
- Pestana de telemetria.
- Pestana de reportes.
- Pestana de alertas.

Resultado: compilacion correcta de la interfaz Swing y conexion reutilizada con los DAOs del prototipo.

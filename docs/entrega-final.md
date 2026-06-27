# Entrega final - EoloControl

## Patron de diseno seleccionado

El prototipo aplica el patron Modelo Vista Controlador (MVC).

- Modelo: clases del dominio ubicadas en `eolocontrol.model`, clases DAO ubicadas en `eolocontrol.dao`, servicios de negocio en `eolocontrol.service` y conexion MySQL en `eolocontrol.db`.
- Vista: pantallas Swing ubicadas en `eolocontrol.view`, principalmente `LoginDialog` y `MainFrame`.
- Controlador: clases ubicadas en `eolocontrol.controller`, que coordinan las acciones solicitadas por la vista y delegan en modelo, servicios y persistencia.

La seleccion de MVC permite separar responsabilidades, facilitar el mantenimiento y evitar que la interfaz grafica contenga consultas SQL o reglas de negocio.

## Persistencia y consultas MySQL

La persistencia se realiza mediante JDBC y MySQL Connector/J. Los DAO establecen conexiones mediante `Database.getConnection()` y ejecutan operaciones sobre la base `ecoviento`.

Operaciones implementadas:

- Insercion de centrales, turbinas, telemetria y alertas.
- Consulta de usuarios, centrales, turbinas, alertas y reportes.
- Actualizacion de centrales y turbinas desde las tablas editables de la interfaz.
- Presentacion de resultados en Swing mediante tablas, areas de texto y mensajes de estado.

## Manejo de excepciones

Las operaciones JDBC capturan `SQLException` y la convierten en `DaoException`, una excepcion propia del proyecto. Esto evita exponer detalles tecnicos de la base de datos directamente a la vista.

La vista captura las excepciones y presenta mensajes comprensibles mediante `JOptionPane`, sin cerrar abruptamente la aplicacion.

## Abstraccion, interfaces y estructuras

El prototipo incluye:

- Clase abstracta `ActivoEolico`, utilizada como base para activos del dominio.
- Interfaz `GeneradorEnergia`, implementada por `TurbinaEolica`.
- Interfaz `ApplicationController`, que define las operaciones disponibles para la vista.
- Arreglos en la construccion de formularios Swing, por ejemplo `String[]` y `JTextField[]`.
- `ArrayList` en los DAO para construir colecciones de resultados obtenidos desde MySQL.

## Correcciones integradas

El desarrollo incorpora las observaciones trabajadas durante las entregas anteriores: mayor explicacion del codigo, uso explicito de POO, manejo de excepciones, actualizacion del diagrama de secuencia para representar clases concretas y mejoras en la interfaz de usuario.

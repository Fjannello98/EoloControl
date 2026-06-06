# Aplicacion de POO en el prototipo

El prototipo EoloControl aplica programacion orientada a objetos en Java mediante una separacion clara entre interfaz, modelo, servicios, acceso a datos y conexion a base de datos.

## Pilares de POO

- Encapsulamiento: las clases `CentralEolica` y `TurbinaEolica` mantienen sus atributos como `private final` y exponen metodos publicos de lectura.
- Herencia: `CentralEolica` y `TurbinaEolica` heredan de la clase abstracta `ActivoEolico`, que concentra atributos comunes como `id` y `nombre`.
- Abstraccion: `ActivoEolico` define el metodo abstracto `resumenOperativo()`, que cada activo implementa segun su comportamiento.
- Polimorfismo: `InventarioService.resumirActivos()` trabaja con `List<? extends ActivoEolico>` y puede invocar `resumenOperativo()` sin depender de la clase concreta.

## Otros conceptos aplicados

- Interfaces: `GeneradorEnergia` define el contrato `estimarEnergia()`, implementado por `TurbinaEolica`.
- Constructores: las entidades principales inicializan y validan sus atributos desde constructores.
- Excepciones: `DatoInvalidoException` y `DaoException` permiten informar errores de validacion y persistencia.
- Estructuras de control: la aplicacion utiliza `if`, `switch`, `while`, `for` y bloques `try/catch`.
- Estructuras de datos: se utilizan `List`, `ArrayList` y `Optional`.
- Ordenamiento y busqueda: `InventarioService` ordena turbinas por codigo y permite buscar una turbina por codigo desde la interfaz Swing.

## Ejecucion

El proyecto compila con `javac` y se ejecuta desde los scripts incluidos en `scripts/`. La base de datos se crea y carga con los scripts SQL incluidos en `sql/`.

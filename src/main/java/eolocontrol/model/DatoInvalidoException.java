package eolocontrol.model;

// Excepcion propia del dominio para separar errores de validacion de errores tecnicos.
public class DatoInvalidoException extends RuntimeException {
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}

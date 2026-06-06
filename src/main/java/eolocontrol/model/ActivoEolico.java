package eolocontrol.model;

// Clase abstracta usada para reunir datos comunes de los activos del parque.
// No se instancia directamente: sirve como base para centrales y turbinas.
public abstract class ActivoEolico {
    // Encapsulamiento: los atributos quedan privados y se acceden por metodos publicos.
    private final int id;
    private final String nombre;

    protected ActivoEolico(int id, String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new DatoInvalidoException("El nombre o codigo del activo es obligatorio.");
        }
        this.id = id;
        this.nombre = nombre.trim();
    }

    public int id() {
        return id;
    }

    public String nombre() {
        return nombre;
    }

    // Abstraccion: cada activo define su propia forma de describirse operativamente.
    public abstract String resumenOperativo();
}

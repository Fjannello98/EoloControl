package eolocontrol.model;

public abstract class ActivoEolico {
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

    public abstract String resumenOperativo();
}

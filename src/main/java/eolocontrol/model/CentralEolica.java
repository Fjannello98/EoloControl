package eolocontrol.model;

public class CentralEolica extends ActivoEolico {
    private final String ubicacion;
    private final String provincia;

    public CentralEolica(int id, String nombre, String ubicacion, String provincia) {
        super(id, nombre);
        if (ubicacion == null || ubicacion.isBlank()) {
            throw new DatoInvalidoException("La ubicacion de la central es obligatoria.");
        }
        if (provincia == null || provincia.isBlank()) {
            throw new DatoInvalidoException("La provincia de la central es obligatoria.");
        }
        this.ubicacion = ubicacion.trim();
        this.provincia = provincia.trim();
    }

    public String ubicacion() {
        return ubicacion;
    }

    public String provincia() {
        return provincia;
    }

    @Override
    public String resumenOperativo() {
        return "Central " + nombre() + " ubicada en " + ubicacion + ", " + provincia;
    }
}

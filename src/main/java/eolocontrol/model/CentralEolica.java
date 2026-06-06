package eolocontrol.model;

// Herencia: una central es un tipo concreto de ActivoEolico.
public class CentralEolica extends ActivoEolico {
    private final String ubicacion;
    private final String provincia;

    // El constructor inicializa el objeto y valida los datos minimos del dominio.
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
        // Polimorfismo: esta implementacion se ejecuta cuando el activo real es una central.
        return "Central " + nombre() + " ubicada en " + ubicacion + ", " + provincia;
    }
}

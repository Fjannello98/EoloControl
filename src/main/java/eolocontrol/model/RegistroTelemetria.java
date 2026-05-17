package eolocontrol.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RegistroTelemetria(
        int id,
        int turbinaId,
        LocalDateTime fechaHora,
        BigDecimal velocidadVientoKmh,
        String direccionViento,
        BigDecimal energiaGeneradaMwh) {

    public RegistroTelemetria withId(int nuevoId) {
        return new RegistroTelemetria(nuevoId, turbinaId, fechaHora, velocidadVientoKmh, direccionViento, energiaGeneradaMwh);
    }
}


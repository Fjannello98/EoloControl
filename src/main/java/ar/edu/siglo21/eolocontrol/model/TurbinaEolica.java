package ar.edu.siglo21.eolocontrol.model;

import java.math.BigDecimal;

public record TurbinaEolica(
        int id,
        int centralId,
        String codigo,
        String modelo,
        BigDecimal potenciaMaximaKw,
        String estado) {
}

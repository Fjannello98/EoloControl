package eolocontrol.model;

import java.math.BigDecimal;

// Interfaz: define que debe hacer un generador, sin imponer como calcularlo.
public interface GeneradorEnergia {
    BigDecimal estimarEnergia(BigDecimal velocidadVientoKmh);
}

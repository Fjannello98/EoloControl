package eolocontrol.model;

import java.math.BigDecimal;

public interface GeneradorEnergia {
    BigDecimal estimarEnergia(BigDecimal velocidadVientoKmh);
}

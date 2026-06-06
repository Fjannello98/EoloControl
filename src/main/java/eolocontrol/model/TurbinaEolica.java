package eolocontrol.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TurbinaEolica extends ActivoEolico implements GeneradorEnergia {
    private static final BigDecimal VIENTO_OPTIMO_KMH = new BigDecimal("80.00");

    private final int centralId;
    private final String modelo;
    private final BigDecimal potenciaMaximaKw;
    private final String estado;

    public TurbinaEolica(int id, int centralId, String codigo, String modelo, BigDecimal potenciaMaximaKw, String estado) {
        super(id, codigo);
        if (centralId <= 0) {
            throw new DatoInvalidoException("La turbina debe estar asociada a una central valida.");
        }
        if (modelo == null || modelo.isBlank()) {
            throw new DatoInvalidoException("El modelo de la turbina es obligatorio.");
        }
        if (potenciaMaximaKw == null || potenciaMaximaKw.signum() <= 0) {
            throw new DatoInvalidoException("La potencia maxima debe ser mayor a cero.");
        }
        if (estado == null || estado.isBlank()) {
            throw new DatoInvalidoException("El estado de la turbina es obligatorio.");
        }
        this.centralId = centralId;
        this.modelo = modelo.trim();
        this.potenciaMaximaKw = potenciaMaximaKw;
        this.estado = estado.trim().toUpperCase();
    }

    public int centralId() {
        return centralId;
    }

    public String codigo() {
        return nombre();
    }

    public String modelo() {
        return modelo;
    }

    public BigDecimal potenciaMaximaKw() {
        return potenciaMaximaKw;
    }

    public String estado() {
        return estado;
    }

    public boolean estaOperativa() {
        return "OPERATIVA".equalsIgnoreCase(estado);
    }

    @Override
    public BigDecimal estimarEnergia(BigDecimal velocidadVientoKmh) {
        if (velocidadVientoKmh == null || velocidadVientoKmh.signum() <= 0 || !estaOperativa()) {
            return BigDecimal.ZERO;
        }
        BigDecimal factorViento = velocidadVientoKmh.divide(VIENTO_OPTIMO_KMH, 4, RoundingMode.HALF_UP);
        BigDecimal factorLimitado = factorViento.min(BigDecimal.ONE);
        return potenciaMaximaKw.multiply(factorLimitado).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String resumenOperativo() {
        return "Turbina " + codigo() + " modelo " + modelo + " en estado " + estado;
    }
}

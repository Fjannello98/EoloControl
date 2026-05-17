package eolocontrol.service;

import eolocontrol.dao.AlertaDao;
import eolocontrol.model.Alerta;
import eolocontrol.model.RegistroTelemetria;

import java.math.BigDecimal;

public class AlertaService {
    private static final BigDecimal VIENTO_MINIMO_GENERACION = new BigDecimal("10.00");
    private static final BigDecimal ENERGIA_MINIMA_ESPERADA = new BigDecimal("1.00");

    private final AlertaDao alertaDao;

    public AlertaService(AlertaDao alertaDao) {
        this.alertaDao = alertaDao;
    }

    public void evaluarYRegistrar(RegistroTelemetria registro) {
        boolean hayViento = registro.velocidadVientoKmh().compareTo(VIENTO_MINIMO_GENERACION) >= 0;
        boolean bajaGeneracion = registro.energiaGeneradaMwh().compareTo(ENERGIA_MINIMA_ESPERADA) < 0;
        if (hayViento && bajaGeneracion) {
            alertaDao.crear(new Alerta(
                    0,
                    registro.turbinaId(),
                    registro.id(),
                    registro.fechaHora(),
                    "BAJA_GENERACION",
                    "La energia generada es inferior a la esperada para la condicion de viento registrada.",
                    "MEDIA",
                    false));
        }
    }
}


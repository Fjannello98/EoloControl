package eolocontrol.controller;

import eolocontrol.model.CentralEolica;
import eolocontrol.model.RegistroTelemetria;
import eolocontrol.model.TurbinaEolica;

import java.util.List;
import java.util.Optional;

public interface ApplicationController {
    void crearCentral(CentralEolica central);

    void actualizarCentral(CentralEolica central);

    List<CentralEolica> listarCentrales();

    void crearTurbina(TurbinaEolica turbina);

    void actualizarTurbina(TurbinaEolica turbina);

    List<TurbinaEolica> listarTurbinas();

    Optional<TurbinaEolica> buscarTurbinaPorCodigo(String codigo);

    void registrarTelemetria(RegistroTelemetria registro);

    List<String> generarReporteEnergia();

    List<String> listarAlertasPendientes();
}

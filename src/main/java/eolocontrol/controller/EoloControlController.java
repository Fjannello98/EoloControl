package eolocontrol.controller;

import eolocontrol.dao.AlertaDao;
import eolocontrol.dao.CentralDao;
import eolocontrol.dao.ReporteDao;
import eolocontrol.dao.TelemetriaDao;
import eolocontrol.dao.TurbinaDao;
import eolocontrol.model.CentralEolica;
import eolocontrol.model.RegistroTelemetria;
import eolocontrol.model.TurbinaEolica;
import eolocontrol.service.AlertaService;
import eolocontrol.service.InventarioService;

import java.util.List;
import java.util.Optional;

public class EoloControlController implements ApplicationController {
    private final CentralDao centralDao;
    private final TurbinaDao turbinaDao;
    private final TelemetriaDao telemetriaDao;
    private final AlertaDao alertaDao;
    private final ReporteDao reporteDao;
    private final AlertaService alertaService;
    private final InventarioService inventarioService;

    public EoloControlController(
            CentralDao centralDao,
            TurbinaDao turbinaDao,
            TelemetriaDao telemetriaDao,
            AlertaDao alertaDao,
            ReporteDao reporteDao,
            AlertaService alertaService,
            InventarioService inventarioService) {
        this.centralDao = centralDao;
        this.turbinaDao = turbinaDao;
        this.telemetriaDao = telemetriaDao;
        this.alertaDao = alertaDao;
        this.reporteDao = reporteDao;
        this.alertaService = alertaService;
        this.inventarioService = inventarioService;
    }

    @Override
    public void crearCentral(CentralEolica central) {
        centralDao.crear(central);
    }

    @Override
    public void actualizarCentral(CentralEolica central) {
        centralDao.actualizar(central);
    }

    @Override
    public List<CentralEolica> listarCentrales() {
        return centralDao.listar();
    }

    @Override
    public void crearTurbina(TurbinaEolica turbina) {
        turbinaDao.crear(turbina);
    }

    @Override
    public void actualizarTurbina(TurbinaEolica turbina) {
        turbinaDao.actualizar(turbina);
    }

    @Override
    public List<TurbinaEolica> listarTurbinas() {
        return inventarioService.ordenarTurbinasPorCodigo(turbinaDao.listar());
    }

    @Override
    public Optional<TurbinaEolica> buscarTurbinaPorCodigo(String codigo) {
        return inventarioService.buscarTurbinaPorCodigo(turbinaDao.listar(), codigo);
    }

    @Override
    public void registrarTelemetria(RegistroTelemetria registro) {
        int id = telemetriaDao.crear(registro);
        alertaService.evaluarYRegistrar(registro.withId(id));
    }

    @Override
    public List<String> generarReporteEnergia() {
        return reporteDao.reporteEnergiaPorTurbina();
    }

    @Override
    public List<String> listarAlertasPendientes() {
        return alertaDao.listarPendientes();
    }
}

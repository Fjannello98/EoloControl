package eolocontrol.service;

import eolocontrol.model.ActivoEolico;
import eolocontrol.model.TurbinaEolica;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class InventarioService {
    // Ordenamiento: se crea una copia para no modificar la lista original recibida.
    public List<TurbinaEolica> ordenarTurbinasPorCodigo(List<TurbinaEolica> turbinas) {
        List<TurbinaEolica> ordenadas = new ArrayList<>(turbinas);
        ordenadas.sort(Comparator.comparing(TurbinaEolica::codigo));
        return ordenadas;
    }

    // Busqueda secuencial simple sobre la coleccion de turbinas cargadas.
    public Optional<TurbinaEolica> buscarTurbinaPorCodigo(List<TurbinaEolica> turbinas, String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return Optional.empty();
        }
        for (TurbinaEolica turbina : turbinas) {
            if (turbina.codigo().equalsIgnoreCase(codigo.trim())) {
                return Optional.of(turbina);
            }
        }
        return Optional.empty();
    }

    public List<String> resumirActivos(List<? extends ActivoEolico> activos) {
        List<String> resumenes = new ArrayList<>();
        for (ActivoEolico activo : activos) {
            // Polimorfismo: se invoca el resumen que corresponda a la clase concreta.
            resumenes.add(activo.resumenOperativo());
        }
        return resumenes;
    }
}

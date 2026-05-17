package ar.edu.siglo21.eolocontrol.model;

import java.time.LocalDateTime;

public record Alerta(
        int id,
        int turbinaId,
        Integer registroId,
        LocalDateTime fechaHora,
        String tipo,
        String descripcion,
        String severidad,
        boolean atendida) {
}

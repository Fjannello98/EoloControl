package eolocontrol.dao;

import eolocontrol.db.Database;
import eolocontrol.model.Alerta;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlertaDao {
    public void crear(Alerta alerta) {
        String sql = """
                INSERT INTO alertas
                (id_turbina, id_registro, fecha_hora, tipo, descripcion, severidad, atendida)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (var conn = Database.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, alerta.turbinaId());
            stmt.setObject(2, alerta.registroId());
            stmt.setObject(3, alerta.fechaHora());
            stmt.setString(4, alerta.tipo());
            stmt.setString(5, alerta.descripcion());
            stmt.setString(6, alerta.severidad());
            stmt.setBoolean(7, alerta.atendida());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DaoException("No se pudo registrar la alerta.", ex);
        }
    }

    public List<String> listarPendientes() {
        String sql = """
                SELECT a.id_alerta, t.codigo, a.fecha_hora, a.tipo, a.severidad, a.descripcion
                FROM alertas a
                INNER JOIN turbinas_eolicas t ON a.id_turbina = t.id_turbina
                WHERE a.atendida = FALSE
                ORDER BY a.fecha_hora DESC
                """;
        List<String> alertas = new ArrayList<>();
        try (var conn = Database.getConnection();
             var stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                alertas.add(rs.getInt("id_alerta") + " - " + rs.getString("codigo") + " - "
                        + rs.getObject("fecha_hora", LocalDateTime.class) + " - " + rs.getString("tipo") + " - "
                        + rs.getString("severidad") + " - " + rs.getString("descripcion"));
            }
            return alertas;
        } catch (SQLException ex) {
            throw new DaoException("No se pudieron listar las alertas.", ex);
        }
    }
}


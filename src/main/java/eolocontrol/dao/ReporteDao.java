package eolocontrol.dao;

import eolocontrol.db.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReporteDao {
    public List<String> reporteEnergiaPorTurbina() {
        String sql = """
                SELECT
                    t.codigo AS turbina,
                    c.nombre AS central,
                    COALESCE(e.energia_total_mwh, 0) AS energia_total_mwh,
                    COALESCE(e.viento_promedio_kmh, 0) AS viento_promedio_kmh,
                    COALESCE(a.alertas, 0) AS alertas
                FROM turbinas_eolicas t
                INNER JOIN centrales_eolicas c ON t.id_central = c.id_central
                LEFT JOIN (
                    SELECT id_turbina,
                           SUM(energia_generada_mwh) AS energia_total_mwh,
                           AVG(velocidad_viento_kmh) AS viento_promedio_kmh
                    FROM registros_telemetria
                    GROUP BY id_turbina
                ) e ON t.id_turbina = e.id_turbina
                LEFT JOIN (
                    SELECT id_turbina, COUNT(*) AS alertas
                    FROM alertas
                    GROUP BY id_turbina
                ) a ON t.id_turbina = a.id_turbina
                ORDER BY energia_total_mwh DESC
                """;
        List<String> reporte = new ArrayList<>();
        try (var conn = Database.getConnection();
             var stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                reporte.add(rs.getString("turbina") + " - " + rs.getString("central")
                        + " - energia: " + rs.getBigDecimal("energia_total_mwh") + " MWh"
                        + " - viento prom.: " + rs.getBigDecimal("viento_promedio_kmh") + " km/h"
                        + " - alertas: " + rs.getInt("alertas"));
            }
            return reporte;
        } catch (SQLException ex) {
            throw new DaoException("No se pudo generar el reporte.", ex);
        }
    }
}


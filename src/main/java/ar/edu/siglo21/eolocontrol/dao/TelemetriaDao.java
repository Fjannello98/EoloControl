package ar.edu.siglo21.eolocontrol.dao;

import ar.edu.siglo21.eolocontrol.db.Database;
import ar.edu.siglo21.eolocontrol.model.RegistroTelemetria;

import java.sql.SQLException;
import java.sql.Statement;

public class TelemetriaDao {
    public int crear(RegistroTelemetria registro) {
        validar(registro);
        String sql = """
                INSERT INTO registros_telemetria
                (id_turbina, fecha_hora, velocidad_viento_kmh, direccion_viento, energia_generada_mwh)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (var conn = Database.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, registro.turbinaId());
            stmt.setObject(2, registro.fechaHora());
            stmt.setBigDecimal(3, registro.velocidadVientoKmh());
            stmt.setString(4, registro.direccionViento().toUpperCase());
            stmt.setBigDecimal(5, registro.energiaGeneradaMwh());
            stmt.executeUpdate();
            try (var keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new DaoException("No se obtuvo el identificador del registro.", null);
            }
        } catch (SQLException ex) {
            throw new DaoException("No se pudo registrar la telemetria.", ex);
        }
    }

    private void validar(RegistroTelemetria registro) {
        if (registro.velocidadVientoKmh().signum() < 0 || registro.velocidadVientoKmh().doubleValue() > 300) {
            throw new IllegalArgumentException("La velocidad del viento debe estar entre 0 y 300 km/h.");
        }
        if (registro.energiaGeneradaMwh().signum() < 0) {
            throw new IllegalArgumentException("La energia generada no puede ser negativa.");
        }
    }
}

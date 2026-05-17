package ar.edu.siglo21.eolocontrol.dao;

import ar.edu.siglo21.eolocontrol.db.Database;
import ar.edu.siglo21.eolocontrol.model.TurbinaEolica;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TurbinaDao {
    public void crear(TurbinaEolica turbina) {
        String sql = """
                INSERT INTO turbinas_eolicas
                (id_central, codigo, modelo, potencia_maxima_kw, estado)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (var conn = Database.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, turbina.centralId());
            stmt.setString(2, turbina.codigo());
            stmt.setString(3, turbina.modelo());
            stmt.setBigDecimal(4, turbina.potenciaMaximaKw());
            stmt.setString(5, turbina.estado().toUpperCase());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DaoException("No se pudo registrar la turbina.", ex);
        }
    }

    public List<TurbinaEolica> listar() {
        String sql = """
                SELECT id_turbina, id_central, codigo, modelo, potencia_maxima_kw, estado
                FROM turbinas_eolicas
                ORDER BY codigo
                """;
        List<TurbinaEolica> turbinas = new ArrayList<>();
        try (var conn = Database.getConnection();
             var stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                turbinas.add(new TurbinaEolica(
                        rs.getInt("id_turbina"),
                        rs.getInt("id_central"),
                        rs.getString("codigo"),
                        rs.getString("modelo"),
                        rs.getBigDecimal("potencia_maxima_kw"),
                        rs.getString("estado")));
            }
            return turbinas;
        } catch (SQLException ex) {
            throw new DaoException("No se pudieron listar las turbinas.", ex);
        }
    }
}

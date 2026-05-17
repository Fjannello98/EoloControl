package ar.edu.siglo21.eolocontrol.dao;

import ar.edu.siglo21.eolocontrol.db.Database;
import ar.edu.siglo21.eolocontrol.model.CentralEolica;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CentralDao {
    public void crear(CentralEolica central) {
        String sql = "INSERT INTO centrales_eolicas (nombre, ubicacion, provincia) VALUES (?, ?, ?)";
        try (var conn = Database.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, central.nombre());
            stmt.setString(2, central.ubicacion());
            stmt.setString(3, central.provincia());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DaoException("No se pudo registrar la central.", ex);
        }
    }

    public List<CentralEolica> listar() {
        String sql = "SELECT id_central, nombre, ubicacion, provincia FROM centrales_eolicas ORDER BY nombre";
        List<CentralEolica> centrales = new ArrayList<>();
        try (var conn = Database.getConnection();
             var stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                centrales.add(new CentralEolica(
                        rs.getInt("id_central"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion"),
                        rs.getString("provincia")));
            }
            return centrales;
        } catch (SQLException ex) {
            throw new DaoException("No se pudieron listar las centrales.", ex);
        }
    }
}

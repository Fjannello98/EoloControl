package ar.edu.siglo21.eolocontrol.dao;

import ar.edu.siglo21.eolocontrol.db.Database;
import ar.edu.siglo21.eolocontrol.model.Usuario;

import java.sql.SQLException;
import java.util.Optional;

public class UsuarioDao {
    public Optional<Usuario> autenticar(String nombreUsuario, String contrasenia) {
        String sql = """
                SELECT id_usuario, nombre_usuario, rol, activo
                FROM usuarios
                WHERE nombre_usuario = ? AND contrasenia = ? AND activo = TRUE
                """;
        try (var conn = Database.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contrasenia);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre_usuario"),
                            rs.getString("rol"),
                            rs.getBoolean("activo")));
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DaoException("No se pudo autenticar el usuario.", ex);
        }
    }
}

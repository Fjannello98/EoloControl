package eolocontrol.controller;

import eolocontrol.dao.UsuarioDao;
import eolocontrol.model.Usuario;

import java.util.Optional;

public class LoginController {
    private final UsuarioDao usuarioDao;

    public LoginController(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public Optional<Usuario> autenticar(String usuario, String contrasenia) {
        return usuarioDao.autenticar(usuario, contrasenia);
    }
}

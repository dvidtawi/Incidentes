package com.institucion.incidentes.service;

import com.institucion.incidentes.model.Usuario;
import java.util.List;

public interface UsuarioService {
    List<Usuario> getAllUsers();
    Usuario getUserById(Long id);
    Usuario createUser(Usuario usuario);
    Usuario updateUser(Long id, Usuario usuario);
    void deleteUser(Long id);
    Usuario getUserByEmail(String email);
    boolean existsByEmail(String email);
}
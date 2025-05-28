package com.institucion.incidentes.service;

import com.institucion.incidentes.model.Usuario;
import com.institucion.incidentes.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario getUserById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    @Override
    public Usuario createUser(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado: " + usuario.getEmail());
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario updateUser(Long id, Usuario usuarioDetails) {
        Usuario usuario = getUserById(id);

        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setDepartamento(usuarioDetails.getDepartamento());
        usuario.setRol(usuarioDetails.getRol());

        // No permitir cambiar el email
        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteUser(Long id) {
        Usuario usuario = getUserById(id);
        usuarioRepository.delete(usuario);
    }

    @Override
    public Usuario getUserByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
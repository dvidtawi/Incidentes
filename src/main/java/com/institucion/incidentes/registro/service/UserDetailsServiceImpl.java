package com.institucion.incidentes.registro.service;


import com.institucion.incidentes.registro.model.UsuarioRegistro;
import com.institucion.incidentes.registro.repository.UsuarioRegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    // Inyectar el repositorio de UsuarioRegistro para acceder a los datos de usuario
    // y sus roles 
    @Autowired
    private UsuarioRegistroRepository usuarioRegistroRepository;

    // Método que carga el usuario por su nombre de usuario (username)
    // Este método es llamado por el framework de Spring Security durante el proceso de autenticación
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioRegistro usuarioRegistro = usuarioRegistroRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("UsuarioRegistro no encontrado con username: " + username));

        // Convertir los roles del usuarioRegistro a una lista de GrantedAuthority
        // Esto es necesario para que Spring Security pueda manejar los roles del usuarioRegistro
        List<SimpleGrantedAuthority> authorities = usuarioRegistro.getRoles().stream()
                .map(rol -> {
                    String enumName = rol.getNombre().name();
                    // Convierte ROL_ADMIN -> ROLE_ADMIN, ROL_DOCENTE -> ROLE_DOCENTE, etc.
                    String springRole = enumName.replace("ROL_", "ROLE_");
                    return new SimpleGrantedAuthority(springRole);
                })
                .collect(Collectors.toList());

        // Crear un objeto User de Spring Security con los detalles del usuarioRegistro
        // y sus roles (authorities)
        return new User(usuarioRegistro.getUsername(), usuarioRegistro.getPassword(), usuarioRegistro.isActivo(),
                true, true, true, authorities);
    }
}

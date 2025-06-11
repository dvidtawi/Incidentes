package com.universidad.service.impl;

import com.universidad.dto.UsuarioDTO;
import com.universidad.registro.model.Rol;
import com.universidad.registro.model.Usuario;
import com.universidad.registro.repository.RolRepository;
import com.universidad.registro.repository.UsuarioRepository;
import com.universidad.service.IUsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    @Transactional
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        validarUsuarioUnico(usuarioDTO);

        Usuario usuario = new Usuario();
        mapearDTOaEntidad(usuarioDTO, usuario);

        usuario.setRoles(obtenerRoles(usuarioDTO.getRoles()));
        return convertToDTO(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        validarUsuarioUnico(usuarioDTO, usuario);
        mapearDTOaEntidad(usuarioDTO, usuario);

        if (usuarioDTO.getRoles() != null) {
            usuario.setRoles(obtenerRoles(usuarioDTO.getRoles()));
        }

        return convertToDTO(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    private void validarUsuarioUnico(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }
    }

    private void validarUsuarioUnico(UsuarioDTO usuarioDTO, Usuario usuarioExistente) {
        if (!usuarioExistente.getUsername().equals(usuarioDTO.getUsername())
                && usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        if (!usuarioExistente.getEmail().equals(usuarioDTO.getEmail())
                && usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }
    }

    private Set<Rol> obtenerRoles(Set<String> rolesStr) {
        Set<Rol> roles = new HashSet<>();
        if (rolesStr == null || rolesStr.isEmpty()) {
            roles.add(rolRepository.findByNombre(Rol.NombreRol.ROL_ESTUDIANTE)
                    .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado")));
        } else {
            rolesStr.forEach(role -> {
                Rol rol = rolRepository.findByNombre(Rol.NombreRol.valueOf(role))
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + role));
                roles.add(rol);
            });
        }
        return roles;
    }

    private void mapearDTOaEntidad(UsuarioDTO dto, Usuario entidad) {
        entidad.setUsername(dto.getUsername());
        entidad.setEmail(dto.getEmail());
        if (dto.getPassword() != null) {
            entidad.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        entidad.setNombre(dto.getNombre());
        entidad.setApellido(dto.getApellido());
        entidad.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
    }

    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setActivo(usuario.isActivo());

        dto.setRoles(usuario.getRoles().stream()
                .map(rol -> rol.getNombre().name())
                .collect(Collectors.toSet()));

        return dto;
    }
}
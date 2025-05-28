package com.institucion.incidentes.mapper;

import com.institucion.incidentes.dto.UsuarioDTO;
import com.institucion.incidentes.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDto(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setDepartamento(usuario.getDepartamento());
        dto.setRol(usuario.getRol());
        return dto;
    }

    public Usuario toEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setDepartamento(dto.getDepartamento());
        usuario.setRol(dto.getRol());
        return usuario;
    }
}
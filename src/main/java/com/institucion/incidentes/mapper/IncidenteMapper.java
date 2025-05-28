package com.institucion.incidentes.mapper;

import com.institucion.incidentes.dto.IncidenteDTO;
import com.institucion.incidentes.dto.UsuarioDTO;
import com.institucion.incidentes.dto.CategoriaDTO;
import com.institucion.incidentes.model.Incidente;
import com.institucion.incidentes.model.Usuario;
import com.institucion.incidentes.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class IncidenteMapper {

    public IncidenteDTO toDto(Incidente incidente) {
        IncidenteDTO dto = new IncidenteDTO();
        dto.setId(incidente.getId());
        dto.setTitulo(incidente.getTitulo());
        dto.setDescripcion(incidente.getDescripcion());
        dto.setPrioridad(incidente.getPrioridad().name());
        dto.setEstado(incidente.getEstado().name());
        dto.setFechaCreacion(incidente.getFechaCreacion());
        dto.setFechaActualizacion(incidente.getFechaActualizacion());
        dto.setUsuarioId(incidente.getUsuario().getId());
        dto.setCategoriaId(incidente.getCategoria().getId());
        return dto;
    }

    public Incidente toEntity(IncidenteDTO dto, Usuario usuario, Categoria categoria) {
        Incidente incidente = new Incidente();
        incidente.setTitulo(dto.getTitulo());
        incidente.setDescripcion(dto.getDescripcion());
        incidente.setPrioridad(Incidente.Prioridad.valueOf(dto.getPrioridad()));
        incidente.setEstado(dto.getEstado() != null ? Incidente.Estado.valueOf(dto.getEstado()) : Incidente.Estado.ABIERTO);
        incidente.setUsuario(usuario);
        incidente.setCategoria(categoria);
        return incidente;
    }
}

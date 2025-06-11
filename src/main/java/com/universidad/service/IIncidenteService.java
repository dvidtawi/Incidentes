package com.universidad.service;


import com.universidad.dto.IncidenteDTO;

import java.util.List;

public interface IIncidenteService {
    List<IncidenteDTO> obtenerTodosLosIncidentes();
    IncidenteDTO obtenerIncidentePorId(Long id);
    List<IncidenteDTO> obtenerIncidentesPorEstado(String estado);
    IncidenteDTO crearIncidente(IncidenteDTO incidenteDTO);
    IncidenteDTO actualizarIncidente(Long id, IncidenteDTO incidenteDTO);
    IncidenteDTO actualizarEstadoIncidente(Long id, String nuevoEstado, String comentario, Long usuarioId);
    List<IncidenteDTO> obtenerIncidentesPorUsuario(Long usuarioId);
}

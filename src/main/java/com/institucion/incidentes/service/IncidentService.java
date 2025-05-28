package com.institucion.incidentes.service;

import com.institucion.incidentes.dto.IncidenteDTO;
import java.util.List;

public interface IncidentService {
    List<IncidenteDTO> getAllIncidents();
    IncidenteDTO getIncidentById(Long id);
    IncidenteDTO createIncident(IncidenteDTO dto);
    IncidenteDTO updateIncident(Long id, IncidenteDTO dto);
    void deleteIncident(Long id);
    List<IncidenteDTO> getIncidentsByEstado(String estado);
    List<IncidenteDTO> getIncidentsByPrioridad(String prioridad);
    IncidenteDTO changeIncidentStatus(Long id, String nuevoEstado);
    IncidenteDTO cambiarEstado(Long id, String nuevoEstado);

}

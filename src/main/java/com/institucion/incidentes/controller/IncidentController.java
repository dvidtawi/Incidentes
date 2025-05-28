package com.institucion.incidentes.controller;

import com.institucion.incidentes.dto.IncidenteDTO;
import com.institucion.incidentes.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/incidentes")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @GetMapping
    public List<IncidenteDTO> getAllIncidents() {
        return incidentService.getAllIncidents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidenteDTO> getIncidentById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getIncidentById(id));
    }

    @PostMapping
    public IncidenteDTO createIncident(@RequestBody IncidenteDTO incidenteDTO) {
        return incidentService.createIncident(incidenteDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidenteDTO> updateIncident(@PathVariable Long id, @RequestBody IncidenteDTO incidenteDTO) {
        return ResponseEntity.ok(incidentService.updateIncident(id, incidenteDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/estado/{estado}")
    public List<IncidenteDTO> getIncidentsByEstado(@PathVariable String estado) {
        return incidentService.getIncidentsByEstado(estado);
    }

    @GetMapping("/prioridad/{prioridad}")
    public List<IncidenteDTO> getIncidentsByPrioridad(@PathVariable String prioridad) {
        return incidentService.getIncidentsByPrioridad(prioridad);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<IncidenteDTO> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> estadoMap) {
        String nuevoEstado = estadoMap.get("estado");
        IncidenteDTO actualizado = incidentService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(actualizado);
    }

}
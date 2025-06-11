package com.universidad.controller;

import com.universidad.dto.IncidenteDTO;
import com.universidad.registro.model.Usuario;
import com.universidad.service.IIncidenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidentes")
@Tag(name = "Incidentes", description = "API para la gestión de incidentes institucionales")
@SecurityRequirement(name = "bearerAuth")
public class IncidenteController {

    private final IIncidenteService incidenteService;

    @Autowired
    public IncidenteController(IIncidenteService incidenteService) {
        this.incidenteService = incidenteService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SEGURIDAD')")
    @Operation(summary = "Obtener todos los incidentes",
            description = "Retorna una lista de todos los incidentes registrados")
    @ApiResponse(responseCode = "200", description = "Lista de incidentes obtenida exitosamente")
    public ResponseEntity<List<IncidenteDTO>> obtenerTodosLosIncidentes() {
        List<IncidenteDTO> incidentes = incidenteService.obtenerTodosLosIncidentes();
        return ResponseEntity.ok(incidentes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SEGURIDAD') or hasRole('MANTENIMIENTO')")
    @Operation(summary = "Obtener un incidente por ID",
            description = "Retorna un incidente específico basado en su ID")
    @ApiResponse(responseCode = "200", description = "Incidente encontrado")
    @ApiResponse(responseCode = "404", description = "Incidente no encontrado")
    public ResponseEntity<IncidenteDTO> obtenerIncidentePorId(
            @Parameter(description = "ID del incidente a buscar", required = true)
            @PathVariable Long id) {
        IncidenteDTO incidente = incidenteService.obtenerIncidentePorId(id);
        return ResponseEntity.ok(incidente);
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SEGURIDAD')")
    @Operation(summary = "Obtener incidentes por estado",
            description = "Retorna una lista de incidentes filtrados por estado")
    @ApiResponse(responseCode = "200", description = "Lista de incidentes obtenida exitosamente")
    public ResponseEntity<List<IncidenteDTO>> obtenerIncidentesPorEstado(
            @Parameter(description = "Estado de los incidentes a buscar", required = true)
            @PathVariable String estado) {
        List<IncidenteDTO> incidentes = incidenteService.obtenerIncidentesPorEstado(estado);
        return ResponseEntity.ok(incidentes);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SEGURIDAD')")
    @Operation(summary = "Crear un nuevo incidente",
            description = "Registra un nuevo incidente en el sistema")
    @ApiResponse(responseCode = "201", description = "Incidente creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos del incidente inválidos")
    public ResponseEntity<IncidenteDTO> crearIncidente(
            @Parameter(description = "Datos del incidente a crear", required = true)
            @Valid @RequestBody IncidenteDTO incidenteDTO) {
        IncidenteDTO nuevoIncidente = incidenteService.crearIncidente(incidenteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoIncidente);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SEGURIDAD')")
    @Operation(summary = "Actualizar un incidente",
            description = "Actualiza los datos de un incidente existente")
    @ApiResponse(responseCode = "200", description = "Incidente actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Incidente no encontrado")
    public ResponseEntity<IncidenteDTO> actualizarIncidente(
            @Parameter(description = "ID del incidente a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del incidente", required = true)
            @Valid @RequestBody IncidenteDTO incidenteDTO) {
        IncidenteDTO incidenteActualizado = incidenteService.actualizarIncidente(id, incidenteDTO);
        return ResponseEntity.ok(incidenteActualizado);
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SEGURIDAD') or hasRole('MANTENIMIENTO')")
    @Operation(summary = "Actualizar estado de un incidente",
            description = "Cambia el estado de un incidente y registra un seguimiento")
    @ApiResponse(responseCode = "200", description = "Estado del incidente actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Incidente no encontrado")
    public ResponseEntity<IncidenteDTO> actualizarEstadoIncidente(
            @Parameter(description = "ID del incidente a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado del incidente", required = true)
            @RequestParam String nuevoEstado,
            @Parameter(description = "Comentario del cambio", required = true)
            @RequestParam String comentario) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();

        IncidenteDTO incidenteActualizado = incidenteService.actualizarEstadoIncidente(
                id, nuevoEstado, comentario, usuario.getId());
        return ResponseEntity.ok(incidenteActualizado);
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SEGURIDAD')")
    @Operation(summary = "Obtener incidentes por usuario",
            description = "Retorna una lista de incidentes reportados por un usuario específico")
    @ApiResponse(responseCode = "200", description = "Lista de incidentes obtenida exitosamente")
    public ResponseEntity<List<IncidenteDTO>> obtenerIncidentesPorUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long usuarioId) {
        List<IncidenteDTO> incidentes = incidenteService.obtenerIncidentesPorUsuario(usuarioId);
        return ResponseEntity.ok(incidentes);
    }
}

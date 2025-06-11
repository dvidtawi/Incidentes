package com.universidad.controller;

import com.universidad.dto.EvidenciaDTO;
import com.universidad.service.IEvidenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evidencias")
@Tag(name = "Evidencias", description = "API para la gestión de evidencias de incidentes")
@SecurityRequirement(name = "bearerAuth")
public class EvidenciaController {

    private final IEvidenciaService evidenciaService;

    @Autowired
    public EvidenciaController(IEvidenciaService evidenciaService) {
        this.evidenciaService = evidenciaService;
    }

    @GetMapping("/incidente/{incidenteId}")
    @Operation(summary = "Obtener evidencias por incidente",
            description = "Retorna todas las evidencias asociadas a un incidente")
    @ApiResponse(responseCode = "200", description = "Lista de evidencias obtenida exitosamente")
    public ResponseEntity<List<EvidenciaDTO>> obtenerEvidenciasPorIncidente(
            @Parameter(description = "ID del incidente", required = true)
            @PathVariable Long incidenteId) {
        List<EvidenciaDTO> evidencias = evidenciaService.obtenerEvidenciasPorIncidente(incidenteId);
        return ResponseEntity.ok(evidencias);
    }

    @PostMapping
    @Operation(summary = "Agregar evidencia a un incidente",
            description = "Registra una nueva evidencia asociada a un incidente")
    @ApiResponse(responseCode = "201", description = "Evidencia creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de la evidencia inválidos")
    public ResponseEntity<EvidenciaDTO> crearEvidencia(
            @Parameter(description = "Datos de la evidencia a crear", required = true)
            @Valid @RequestBody EvidenciaDTO evidenciaDTO) {
        EvidenciaDTO nuevaEvidencia = evidenciaService.crearEvidencia(evidenciaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEvidencia);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una evidencia",
            description = "Elimina una evidencia del sistema")
    @ApiResponse(responseCode = "204", description = "Evidencia eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Evidencia no encontrada")
    public ResponseEntity<Void> eliminarEvidencia(
            @Parameter(description = "ID de la evidencia a eliminar", required = true)
            @PathVariable Long id) {
        evidenciaService.eliminarEvidencia(id);
        return ResponseEntity.noContent().build();
    }
}

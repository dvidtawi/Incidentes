package com.universidad.controller;

import com.universidad.dto.SeguimientoDTO;
import com.universidad.service.ISeguimientoService;
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
@RequestMapping("/api/seguimientos")
@Tag(name = "Seguimientos", description = "API para la gestión de seguimientos de incidentes")
@SecurityRequirement(name = "bearerAuth")
public class SeguimientoController {

    private final ISeguimientoService seguimientoService;

    @Autowired
    public SeguimientoController(ISeguimientoService seguimientoService) {
        this.seguimientoService = seguimientoService;
    }

    @GetMapping("/incidente/{incidenteId}")
    @Operation(summary = "Obtener seguimientos por incidente",
            description = "Retorna todos los seguimientos asociados a un incidente")
    @ApiResponse(responseCode = "200", description = "Lista de seguimientos obtenida exitosamente")
    public ResponseEntity<List<SeguimientoDTO>> obtenerSeguimientosPorIncidente(
            @Parameter(description = "ID del incidente", required = true)
            @PathVariable Long incidenteId) {
        List<SeguimientoDTO> seguimientos = seguimientoService.obtenerSeguimientosPorIncidente(incidenteId);
        return ResponseEntity.ok(seguimientos);
    }

    @PostMapping
    @Operation(summary = "Agregar seguimiento a un incidente",
            description = "Registra un nuevo seguimiento asociado a un incidente")
    @ApiResponse(responseCode = "201", description = "Seguimiento creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos del seguimiento inválidos")
    public ResponseEntity<SeguimientoDTO> crearSeguimiento(
            @Parameter(description = "Datos del seguimiento a crear", required = true)
            @Valid @RequestBody SeguimientoDTO seguimientoDTO) {
        SeguimientoDTO nuevoSeguimiento = seguimientoService.crearSeguimiento(seguimientoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoSeguimiento);
    }
}

package com.universidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para representar un seguimiento de incidente")
public class SeguimientoDTO {
    private Long id;

    @NotBlank(message = "El comentario es obligatorio")
    @Schema(description = "Comentario del seguimiento", example = "Se ha asignado al personal de mantenimiento")
    private String comentario;

    @Schema(description = "Fecha y hora del seguimiento", example = "2023-10-15T15:30:00")
    private LocalDateTime fecha;

    @Schema(description = "Estado anterior del incidente", example = "PENDIENTE")
    private String estadoAnterior;

    @NotBlank(message = "El nuevo estado es obligatorio")
    @Schema(description = "Nuevo estado del incidente", example = "EN_PROCESO")
    private String estadoNuevo;

    @NotNull(message = "El ID del incidente asociado es obligatorio")
    @Schema(description = "ID del incidente al que pertenece este seguimiento", example = "1")
    private Long incidenteId;

    @NotNull(message = "El ID del usuario que realiza el seguimiento es obligatorio")
    @Schema(description = "ID del usuario que realiza el seguimiento", example = "2")
    private Long usuarioId;
}
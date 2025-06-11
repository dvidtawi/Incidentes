package com.universidad.dto;

import com.universidad.model.Incidente;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para representar un incidente")
public class IncidenteDTO {
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede exceder los 255 caracteres")
    @Schema(description = "Título del incidente", example = "Fuga de agua en el baño")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Schema(description = "Descripción detallada del incidente", example = "Hay una fuga de agua en el baño del segundo piso")
    private String descripcion;

    @Schema(description = "Fecha y hora del incidente", example = "2023-10-15T14:30:00")
    private LocalDateTime fecha;

    @NotNull(message = "La gravedad es obligatoria")
    @Schema(description = "Nivel de gravedad del incidente", example = "MODERADO")
    private Incidente.Gravedad gravedad;

    @Schema(description = "Estado actual del incidente", example = "PENDIENTE")
    private Incidente.Estado estado;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 100, message = "La ubicación no puede exceder los 100 caracteres")
    @Schema(description = "Ubicación donde ocurrió el incidente", example = "Edificio A, Aula 201")
    private String ubicacion;

    @NotBlank(message = "El tipo de ubicación es obligatorio")
    @Size(max = 50, message = "El tipo de ubicación no puede exceder los 50 caracteres")
    @Schema(description = "Tipo de ubicación", example = "AULA")
    private String tipoUbicacion;

    @NotNull(message = "El ID del usuario reportador es obligatorio")
    @Schema(description = "ID del usuario que reporta el incidente", example = "1")
    private Long usuarioReportadorId;

    @Schema(description = "Lista de IDs de evidencias asociadas")
    private List<Long> evidenciasIds;

    @Schema(description = "Lista de IDs de seguimientos asociados")
    private List<Long> seguimientosIds;
}

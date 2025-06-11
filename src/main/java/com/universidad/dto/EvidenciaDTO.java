package com.universidad.dto;

import com.universidad.model.Evidencia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para representar una evidencia")
public class EvidenciaDTO {
    private Long id;

    @NotBlank(message = "La URL del archivo es obligatoria")
    @Schema(description = "URL donde se almacena el archivo de evidencia", example = "https://bucket.s3.amazonaws.com/evidencias/incidente1.jpg")
    private String urlArchivo;

    @Schema(description = "Tipo de archivo de evidencia", example = "IMAGEN")
    private Evidencia.TipoEvidencia tipo;

    @NotNull(message = "El ID del incidente asociado es obligatorio")
    @Schema(description = "ID del incidente al que pertenece esta evidencia", example = "1")
    private Long incidenteId;
}

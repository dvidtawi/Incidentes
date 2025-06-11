package com.universidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para representar un usuario")
public class UsuarioDTO {
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 255, message = "El nombre de usuario debe tener entre 3 y 255 caracteres")
    @Schema(description = "Nombre de usuario único", example = "jperez")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 255, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Schema(description = "Email del usuario", example = "jperez@universidad.edu")
    private String email;

    @Schema(description = "Indica si el usuario está activo", example = "true")
    private Boolean activo = true;

    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    @Schema(description = "Nombre real del usuario", example = "Juan")
    private String nombre;

    @Size(max = 255, message = "El apellido no puede exceder los 255 caracteres")
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;

    @Schema(description = "Roles asignados al usuario")
    private Set<String> roles;
}

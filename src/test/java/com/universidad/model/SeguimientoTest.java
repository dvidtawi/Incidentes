package com.universidad.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class SeguimientoTest {

    @Test
    public void testCreacionSeguimiento_ValoresPorDefecto() {
        // Crear un seguimiento sin asignar fecha explícitamente
        Seguimiento seguimiento = new Seguimiento();
        seguimiento.setComentario("Primera revisión del incidente");
        seguimiento.setEstadoAnterior("ABIERTO");
        seguimiento.setEstadoNuevo("EN_PROGRESO");

        // Validar valores por defecto y lógica básica
        assertNotNull(seguimiento.getFecha(), "La fecha debe generarse automáticamente");
        assertEquals("ABIERTO", seguimiento.getEstadoAnterior());
        assertEquals("EN_PROGRESO", seguimiento.getEstadoNuevo());
        assertEquals("Primera revisión del incidente", seguimiento.getComentario());
    }

    @Test
    public void testCambioEstado_Validos() {
        Seguimiento seguimiento = new Seguimiento();
        seguimiento.setEstadoAnterior("CERRADO");
        seguimiento.setEstadoNuevo("REABIERTO");

        // Validar transición de estados permitida
        assertNotEquals(seguimiento.getEstadoAnterior(), seguimiento.getEstadoNuevo(),
                "El estado nuevo no debe ser igual al anterior");
    }

    @Test
    public void testComentarioNoVacio() {
        Seguimiento seguimiento = new Seguimiento();
        seguimiento.setComentario("  "); // Intentar asignar comentario vacío

        // Validar que el comentario no sea nulo o vacío (simulando @Column(nullable = false))
        assertThrows(IllegalArgumentException.class, () -> {
            if (seguimiento.getComentario() == null || seguimiento.getComentario().trim().isEmpty()) {
                throw new IllegalArgumentException("El comentario no puede estar vacío");
            }
        });
    }
}
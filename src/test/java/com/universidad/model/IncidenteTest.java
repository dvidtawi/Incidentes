package com.universidad.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class IncidenteTest {


    // --- Test 3: Transición de estados válidos ---
    @Test
    public void testTransicionEstados_Valida() {
        Incidente incidente = new Incidente();
        incidente.setEstado(Incidente.Estado.PENDIENTE);

        // Transición válida
        incidente.setEstado(Incidente.Estado.EN_PROCESO);
        assertEquals(Incidente.Estado.EN_PROCESO, incidente.getEstado());

        // Transición a RESUELTO
        incidente.setEstado(Incidente.Estado.RESUELTO);
        assertEquals(Incidente.Estado.RESUELTO, incidente.getEstado());
    }

    // --- Test 4: Agregar evidencias al incidente ---
    @Test
    public void testAgregarEvidencias() {
        Incidente incidente = new Incidente();
        List<Evidencia> evidencias = new ArrayList<>();

        Evidencia evidencia1 = new Evidencia();
        evidencia1.setUrlArchivo("http://ejemplo.com/evidencia1.jpg");

        Evidencia evidencia2 = new Evidencia();
        evidencia2.setUrlArchivo("http://ejemplo.com/evidencia2.pdf");

        evidencias.add(evidencia1);
        evidencias.add(evidencia2);

        incidente.setEvidencias(evidencias);

        assertEquals(2, incidente.getEvidencias().size());
        assertEquals("http://ejemplo.com/evidencia1.jpg",
                incidente.getEvidencias().get(0).getUrlArchivo());
    }

    // --- Test 5: Validación de gravedad ---
    @Test
    public void testGravedadValores() {
        Incidente incidente = new Incidente();

        // Asignar todos los valores posibles de gravedad
        for (Incidente.Gravedad gravedad : Incidente.Gravedad.values()) {
            incidente.setGravedad(gravedad);
            assertEquals(gravedad, incidente.getGravedad());
        }
    }


}
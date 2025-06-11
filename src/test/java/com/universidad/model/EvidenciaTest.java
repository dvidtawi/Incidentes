package com.universidad.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EvidenciaTest {

    // --- Test 1: Validación de URL de archivo ---
    @Test
    public void testUrlArchivo_Valida() {
        Evidencia evidencia = new Evidencia();
        evidencia.setUrlArchivo("https://ejemplo.com/archivo.pdf");

        assertNotNull(evidencia.getUrlArchivo());
        assertTrue(evidencia.getUrlArchivo().length() <= 512,
                "La URL no debe exceder los 512 caracteres");
    }

    // --- Test 2: Validación de tipo de evidencia ---
    @Test
    public void testTipoEvidencia_ValoresEnum() {
        Evidencia evidencia = new Evidencia();
        evidencia.setTipo(Evidencia.TipoEvidencia.PDF);

        assertEquals(Evidencia.TipoEvidencia.PDF, evidencia.getTipo());
        assertNotNull(evidencia.getTipo().name());
    }

    @Test
    public void testTipoEvidencia_Nulo() {
        Evidencia evidencia = new Evidencia();
        evidencia.setTipo(null); // Permitido según @Column (nullable)

        assertNull(evidencia.getTipo());
    }

    // --- Test 3: Asociación con Incidente (lógica básica) ---
    @Test
    public void testIncidenteAsociado() {
        Evidencia evidencia = new Evidencia();
        Incidente incidenteMock = new Incidente();
        incidenteMock.setId(1L);

        evidencia.setIncidente(incidenteMock);

        assertNotNull(evidencia.getIncidente());
        assertEquals(1L, evidencia.getIncidente().getId());
    }
}
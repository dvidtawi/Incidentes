package com.universidad;

import com.universidad.dto.IncidenteDTO;
import com.universidad.model.Incidente;
import com.universidad.registro.model.Usuario;
import com.universidad.registro.repository.UsuarioRepository;
import com.universidad.service.IIncidenteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IncidenteServiceIntegrationTest {

    @Autowired
    private IIncidenteService incidenteService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        // Crear usuario de prueba con datos únicos
        usuario = new Usuario();
        usuario.setUsername("testuser-" + System.currentTimeMillis());
        usuario.setEmail("test-" + System.currentTimeMillis() + "@example.com");
        usuario.setPassword("password");
        usuario.setNombre("Test User");
        usuario.setApellido("Test Lastname");
        usuario.setActivo(true);
        usuario = usuarioRepository.save(usuario);
    }

    @Test
    public void testCrearIncidente() {
        IncidenteDTO dto = buildIncidenteDTO();

        IncidenteDTO creado = incidenteService.crearIncidente(dto);

        assertNotNull(creado.getId());
        assertEquals(dto.getTitulo(), creado.getTitulo());
        assertEquals(dto.getEstado(), creado.getEstado());
        assertEquals(usuario.getId(), creado.getUsuarioReportadorId());
    }

    @Test
    public void testObtenerIncidentePorId() {
        IncidenteDTO dto = buildIncidenteDTO();
        IncidenteDTO creado = incidenteService.crearIncidente(dto);

        IncidenteDTO obtenido = incidenteService.obtenerIncidentePorId(creado.getId());

        assertEquals(creado.getId(), obtenido.getId());
        assertEquals(creado.getTitulo(), obtenido.getTitulo());
    }


    @Test
    public void testObtenerIncidentesPorUsuario() {
        // Crear dos incidentes para el mismo usuario
        incidenteService.crearIncidente(buildIncidenteDTO());
        incidenteService.crearIncidente(buildIncidenteDTO());

        List<IncidenteDTO> incidentes = incidenteService.obtenerIncidentesPorUsuario(usuario.getId());

        assertEquals(2, incidentes.size());
    }

    @Test
    public void testObtenerTodosLosIncidentes() {
        // Crear varios incidentes
        incidenteService.crearIncidente(buildIncidenteDTO());
        incidenteService.crearIncidente(buildIncidenteDTO());

        List<IncidenteDTO> todos = incidenteService.obtenerTodosLosIncidentes();

        assertTrue(todos.size() >= 2);
    }



    private IncidenteDTO buildIncidenteDTO() {
        return IncidenteDTO.builder()
                .titulo("Incidente de prueba - " + System.currentTimeMillis())
                .descripcion("Descripción detallada del incidente")
                .fecha(LocalDateTime.now())
                .gravedad(Incidente.Gravedad.MODERADO)
                .estado(Incidente.Estado.PENDIENTE)
                .ubicacion("Edificio Principal - Aula 101")
                .tipoUbicacion("AULA")
                .usuarioReportadorId(usuario.getId())
                .build();
    }
}
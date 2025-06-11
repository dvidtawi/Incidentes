package com.universidad.service.impl;

import com.universidad.dto.IncidenteDTO;
import com.universidad.model.Incidente;
import com.universidad.registro.model.Usuario;
import com.universidad.repository.IncidenteRepository;
import com.universidad.registro.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.universidad.model.Incidente.Estado;
import static com.universidad.model.Incidente.Gravedad;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IncidenteServiceImplTest {

    @Mock
    private IncidenteRepository incidenteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private IncidenteServiceImpl incidenteService;

    private Incidente incidente;
    private IncidenteDTO incidenteDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);

        incidente = new Incidente();
        incidente.setId(1L);
        incidente.setTitulo("Fallo en servidor");
        incidente.setDescripcion("El servidor no responde");
        incidente.setFecha(LocalDateTime.now());
        incidente.setGravedad(Gravedad.GRAVE);
        incidente.setEstado(Estado.PENDIENTE);
        incidente.setUbicacion("Edificio A");
        incidente.setReportador(usuario);

        incidenteDTO = new IncidenteDTO();
        incidenteDTO.setId(1L);
        incidenteDTO.setTitulo("Fallo en servidor");
        incidenteDTO.setDescripcion("El servidor no responde");
        incidenteDTO.setGravedad(Gravedad.GRAVE);
        incidenteDTO.setEstado(Estado.PENDIENTE);
        incidenteDTO.setUbicacion("Edificio A");
        incidenteDTO.setUsuarioReportadorId(1L);
    }

    @Test
    void obtenerTodosLosIncidentes_DeberiaRetornarListaDeIncidentes() {
        when(incidenteRepository.findAll()).thenReturn(List.of(incidente));

        List<IncidenteDTO> resultado = incidenteService.obtenerTodosLosIncidentes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Fallo en servidor", resultado.get(0).getTitulo());
        verify(incidenteRepository, times(1)).findAll();
    }

    @Test
    void obtenerIncidentePorId_ConIdExistente_DeberiaRetornarIncidente() {
        when(incidenteRepository.findById(1L)).thenReturn(Optional.of(incidente));

        IncidenteDTO resultado = incidenteService.obtenerIncidentePorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(incidenteRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerIncidentePorId_ConIdInexistente_DeberiaLanzarExcepcion() {
        when(incidenteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            incidenteService.obtenerIncidentePorId(99L);
        });
        verify(incidenteRepository, times(1)).findById(99L);
    }

    @Test
    void obtenerIncidentesPorEstado_DeberiaRetornarListaFiltrada() {
        when(incidenteRepository.findByEstado(Estado.PENDIENTE)).thenReturn(List.of(incidente));

        List<IncidenteDTO> resultado = incidenteService.obtenerIncidentesPorEstado("PENDIENTE");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(Estado.PENDIENTE.name(), resultado.get(0).getEstado().name());
        verify(incidenteRepository, times(1)).findByEstado(Estado.PENDIENTE);
    }

    @Test
    void crearIncidente_ConDatosValidos_DeberiaGuardarIncidente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(incidenteRepository.save(any(Incidente.class))).thenReturn(incidente);

        IncidenteDTO resultado = incidenteService.crearIncidente(incidenteDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(incidenteRepository, times(1)).save(any(Incidente.class));
    }

    @Test
    void crearIncidente_ConUsuarioInexistente_DeberiaLanzarExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        incidenteDTO.setUsuarioReportadorId(99L);
        assertThrows(RuntimeException.class, () -> {
            incidenteService.crearIncidente(incidenteDTO);
        });
        verify(usuarioRepository, times(1)).findById(99L);
        verify(incidenteRepository, never()).save(any());
    }

    @Test
    void actualizarIncidente_ConDatosValidos_DeberiaActualizarIncidente() {
        when(incidenteRepository.findById(1L)).thenReturn(Optional.of(incidente));
        when(incidenteRepository.save(any(Incidente.class))).thenReturn(incidente);

        incidenteDTO.setTitulo("Nuevo título");
        IncidenteDTO resultado = incidenteService.actualizarIncidente(1L, incidenteDTO);

        assertNotNull(resultado);
        assertEquals("Nuevo título", resultado.getTitulo());
        verify(incidenteRepository, times(1)).findById(1L);
        verify(incidenteRepository, times(1)).save(any(Incidente.class));
    }

    @Test
    void actualizarEstadoIncidente_ConDatosValidos_DeberiaActualizarEstado() {
        when(incidenteRepository.findById(1L)).thenReturn(Optional.of(incidente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(incidenteRepository.save(any(Incidente.class))).thenReturn(incidente);

        IncidenteDTO resultado = incidenteService.actualizarEstadoIncidente(
                1L, "EN_PROCESO", "En revisión", 1L);

        assertNotNull(resultado);
        assertEquals(Estado.EN_PROCESO.name(), resultado.getEstado().name());
        verify(incidenteRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(incidenteRepository, times(1)).save(any(Incidente.class));
    }

    @Test
    void obtenerIncidentesPorUsuario_DeberiaRetornarListaFiltrada() {
        when(incidenteRepository.findByReportadorId(1L)).thenReturn(List.of(incidente));

        List<IncidenteDTO> resultado = incidenteService.obtenerIncidentesPorUsuario(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getUsuarioReportadorId());
        verify(incidenteRepository, times(1)).findByReportadorId(1L);
    }
}
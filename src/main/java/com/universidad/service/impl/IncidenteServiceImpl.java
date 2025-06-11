package com.universidad.service.impl;


import com.universidad.dto.IncidenteDTO;
import com.universidad.model.Incidente;
import com.universidad.registro.model.Usuario;
import com.universidad.repository.IncidenteRepository;
import com.universidad.registro.repository.UsuarioRepository;
import com.universidad.service.IIncidenteService;
import com.universidad.service.ISeguimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncidenteServiceImpl implements IIncidenteService {

    private final IncidenteRepository incidenteRepository;
    private final UsuarioRepository usuarioRepository;
    private ISeguimientoService seguimientoService;

    @Autowired
    public IncidenteServiceImpl(IncidenteRepository incidenteRepository, UsuarioRepository usuarioRepository) {
        this.incidenteRepository = incidenteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<IncidenteDTO> obtenerTodosLosIncidentes() {
        return incidenteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IncidenteDTO obtenerIncidentePorId(Long id) {
        Incidente incidente = incidenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado"));
        return convertToDTO(incidente);
    }

    @Override
    public List<IncidenteDTO> obtenerIncidentesPorEstado(String estado) {
        return incidenteRepository.findByEstado(Incidente.Estado.valueOf(estado.toUpperCase())).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public IncidenteDTO crearIncidente(IncidenteDTO incidenteDTO) {
        Incidente incidente = new Incidente();
        incidente.setTitulo(incidenteDTO.getTitulo());
        incidente.setDescripcion(incidenteDTO.getDescripcion());
        incidente.setFecha(incidenteDTO.getFecha() != null ? incidenteDTO.getFecha() : LocalDateTime.now());
        incidente.setGravedad(Incidente.Gravedad.valueOf(incidenteDTO.getGravedad().name()));
        incidente.setEstado(Incidente.Estado.valueOf(incidenteDTO.getEstado().name()));
        incidente.setUbicacion(incidenteDTO.getUbicacion());
        incidente.setTipoUbicacion(incidenteDTO.getTipoUbicacion());

        Usuario reportador = usuarioRepository.findById(incidenteDTO.getUsuarioReportadorId())
                .orElseThrow(() -> new RuntimeException("Usuario reportador no encontrado"));
        incidente.setReportador(reportador);

        Incidente incidenteGuardado = incidenteRepository.save(incidente);
        return convertToDTO(incidenteGuardado);
    }

    @Override
    @Transactional
    public IncidenteDTO actualizarIncidente(Long id, IncidenteDTO incidenteDTO) {
        Incidente incidente = incidenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado"));

        incidente.setTitulo(incidenteDTO.getTitulo());
        incidente.setDescripcion(incidenteDTO.getDescripcion());
        incidente.setGravedad(Incidente.Gravedad.valueOf(incidenteDTO.getGravedad().name()));
        incidente.setUbicacion(incidenteDTO.getUbicacion());
        incidente.setTipoUbicacion(incidenteDTO.getTipoUbicacion());

        Incidente incidenteActualizado = incidenteRepository.save(incidente);
        return convertToDTO(incidenteActualizado);
    }

    @Override
    @Transactional
    public IncidenteDTO actualizarEstadoIncidente(Long id, String nuevoEstado, String comentario, Long usuarioId) {
        Incidente incidente = incidenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado"));

        String estadoAnterior = incidente.getEstado().name();
        incidente.setEstado(Incidente.Estado.valueOf(nuevoEstado.toUpperCase()));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Aquí deberías crear y guardar un nuevo seguimiento
        // Esto es un ejemplo simplificado
        // En una implementación real, usarías el servicio de seguimientos

        Incidente incidenteActualizado = incidenteRepository.save(incidente);
        return convertToDTO(incidenteActualizado);
    }

    @Override
    public List<IncidenteDTO> obtenerIncidentesPorUsuario(Long usuarioId) {
        return incidenteRepository.findByReportadorId(usuarioId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private IncidenteDTO convertToDTO(Incidente incidente) {
        IncidenteDTO dto = new IncidenteDTO();
        dto.setId(incidente.getId());
        dto.setTitulo(incidente.getTitulo());
        dto.setDescripcion(incidente.getDescripcion());
        dto.setFecha(incidente.getFecha());
        dto.setGravedad(Incidente.Gravedad.valueOf(incidente.getGravedad().name()));
        dto.setEstado(Incidente.Estado.valueOf(incidente.getEstado().name()));
        dto.setUbicacion(incidente.getUbicacion());
        dto.setTipoUbicacion(incidente.getTipoUbicacion());
        dto.setUsuarioReportadorId(incidente.getReportador().getId());
        return dto;
    }
}
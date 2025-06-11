package com.universidad.service.impl;

import com.universidad.dto.SeguimientoDTO;
import com.universidad.model.Incidente;
import com.universidad.model.Seguimiento;
import com.universidad.registro.model.Usuario;
import com.universidad.repository.IncidenteRepository;
import com.universidad.repository.SeguimientoRepository;
import com.universidad.registro.repository.UsuarioRepository;
import com.universidad.service.ISeguimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeguimientoServiceImpl implements ISeguimientoService {

    private final SeguimientoRepository seguimientoRepository;
    private final IncidenteRepository incidenteRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public SeguimientoServiceImpl(SeguimientoRepository seguimientoRepository,
                                  IncidenteRepository incidenteRepository,
                                  UsuarioRepository usuarioRepository) {
        this.seguimientoRepository = seguimientoRepository;
        this.incidenteRepository = incidenteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<SeguimientoDTO> obtenerSeguimientosPorIncidente(Long incidenteId) {
        return seguimientoRepository.findByIncidenteId(incidenteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SeguimientoDTO crearSeguimiento(SeguimientoDTO seguimientoDTO) {
        Seguimiento seguimiento = new Seguimiento();
        seguimiento.setComentario(seguimientoDTO.getComentario());
        seguimiento.setFecha(seguimientoDTO.getFecha() != null ? seguimientoDTO.getFecha() : LocalDateTime.now());
        seguimiento.setEstadoAnterior(seguimientoDTO.getEstadoAnterior());
        seguimiento.setEstadoNuevo(seguimientoDTO.getEstadoNuevo());

        Incidente incidente = incidenteRepository.findById(seguimientoDTO.getIncidenteId())
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado"));
        seguimiento.setIncidente(incidente);

        Usuario usuario = usuarioRepository.findById(seguimientoDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        seguimiento.setUsuario(usuario);

        Seguimiento seguimientoGuardado = seguimientoRepository.save(seguimiento);
        return convertToDTO(seguimientoGuardado);
    }

    private SeguimientoDTO convertToDTO(Seguimiento seguimiento) {
        SeguimientoDTO dto = new SeguimientoDTO();
        dto.setId(seguimiento.getId());
        dto.setComentario(seguimiento.getComentario());
        dto.setFecha(seguimiento.getFecha());
        dto.setEstadoAnterior(seguimiento.getEstadoAnterior());
        dto.setEstadoNuevo(seguimiento.getEstadoNuevo());
        dto.setIncidenteId(seguimiento.getIncidente().getId());
        dto.setUsuarioId(seguimiento.getUsuario().getId());
        return dto;
    }
}

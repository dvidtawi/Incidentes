package com.institucion.incidentes.service;

import com.institucion.incidentes.model.Usuario;
import com.institucion.incidentes.model.Categoria;
import com.institucion.incidentes.dto.IncidenteDTO;
import com.institucion.incidentes.mapper.IncidenteMapper;
import com.institucion.incidentes.model.Incidente;
import com.institucion.incidentes.repository.IncidentRepository;
import com.institucion.incidentes.repository.UsuarioRepository;
import com.institucion.incidentes.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IncidenteMapper incidenteMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;




    @Override
    public List<IncidenteDTO> getAllIncidents() {
        return incidentRepository.findAll().stream()
                .map(incidenteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public IncidenteDTO getIncidentById(Long id) {
        Incidente incidente = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado con id: " + id));
        return incidenteMapper.toDto(incidente);
    }

    @Override
    public IncidenteDTO createIncident(IncidenteDTO incidenteDTO) {
        // Validación de entrada
        if (incidenteDTO.getTitulo() == null || incidenteDTO.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del incidente no puede estar vacío.");
        }

        if (incidenteDTO.getDescripcion() == null || incidenteDTO.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del incidente no puede estar vacía.");
        }

        if (incidenteDTO.getUsuarioId() == null) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio.");
        }

        if (incidenteDTO.getCategoriaId() == null) {
            throw new IllegalArgumentException("El ID de la categoría es obligatorio.");
        }

        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findById(incidenteDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + incidenteDTO.getUsuarioId()));

        // Verificar que la categoría existe
        Categoria categoria = categoriaRepository.findById(incidenteDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + incidenteDTO.getCategoriaId()));

        // Crear la entidad incidente
        Incidente incidente = new Incidente();
        incidente.setTitulo(incidenteDTO.getTitulo());
        incidente.setDescripcion(incidenteDTO.getDescripcion());
        incidente.setUsuario(usuario);
        incidente.setCategoria(categoria);

        // Asignar prioridad si es válida, o establecer valor por defecto
        try {
            if (incidenteDTO.getPrioridad() != null) {
                incidente.setPrioridad(Incidente.Prioridad.valueOf(incidenteDTO.getPrioridad().toUpperCase()));
            } else {
                incidente.setPrioridad(Incidente.Prioridad.MEDIA); // Valor por defecto
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Prioridad inválida: " + incidenteDTO.getPrioridad());
        }

        // Asignar estado inicial (opcional, si se requiere uno por defecto)
        incidente.setEstado(Incidente.Estado.ABIERTO); // Asume estado inicial

        // Guardar en base de datos
        Incidente savedIncidente = incidentRepository.save(incidente);

        // Retornar como DTO
        return incidenteMapper.toDto(savedIncidente);
    }
    @Override
    public IncidenteDTO updateIncident(Long id, IncidenteDTO incidenteDTO) {
        // Buscar el incidente por ID
        Incidente incidente = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado con id: " + id));

        // Actualizar campos si están presentes en el DTO
        if (incidenteDTO.getTitulo() != null) {
            incidente.setTitulo(incidenteDTO.getTitulo());
        }

        if (incidenteDTO.getDescripcion() != null) {
            incidente.setDescripcion(incidenteDTO.getDescripcion());
        }

        if (incidenteDTO.getPrioridad() != null) {
            try {
                incidente.setPrioridad(Incidente.Prioridad.valueOf(incidenteDTO.getPrioridad().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Prioridad inválida: " + incidenteDTO.getPrioridad());
            }
        }

        if (incidenteDTO.getEstado() != null) {
            try {
                incidente.setEstado(Incidente.Estado.valueOf(incidenteDTO.getEstado().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado inválido: " + incidenteDTO.getEstado());
            }
        }

        if (incidenteDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(incidenteDTO.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + incidenteDTO.getCategoriaId()));
            incidente.setCategoria(categoria);
        }

        // Guardar los cambios
        Incidente updatedIncidente = incidentRepository.save(incidente);

        // Retornar el DTO actualizado
        return incidenteMapper.toDto(updatedIncidente);
    }


    @Override
    public void deleteIncident(Long id) {
        Incidente incidente = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado con id: " + id));
        incidentRepository.delete(incidente);
    }

    @Override
    public List<IncidenteDTO> getIncidentsByEstado(String estado) {
        return incidentRepository.findByEstado(Incidente.Estado.valueOf(estado)).stream()
                .map(incidenteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncidenteDTO> getIncidentsByPrioridad(String prioridad) {
        return incidentRepository.findByPrioridad(Incidente.Prioridad.valueOf(prioridad)).stream()
                .map(incidenteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public IncidenteDTO changeIncidentStatus(Long id, String nuevoEstado) {
        Incidente incidente = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado con id: " + id));
        incidente.setEstado(Incidente.Estado.valueOf(nuevoEstado));
        Incidente updatedIncidente = incidentRepository.save(incidente);
        return incidenteMapper.toDto(updatedIncidente);
    }
    @Override
    public IncidenteDTO cambiarEstado(Long id, String nuevoEstado) {
        Incidente incidente = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado con id: " + id));

        try {
            incidente.setEstado(Incidente.Estado.valueOf(nuevoEstado.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado);
        }

        incidentRepository.save(incidente);
        return incidenteMapper.toDto(incidente);
    }

}
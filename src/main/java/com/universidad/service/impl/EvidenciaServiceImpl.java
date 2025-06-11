package com.universidad.service.impl;


import com.universidad.dto.EvidenciaDTO;
import com.universidad.model.Evidencia;
import com.universidad.model.Incidente;
import com.universidad.repository.EvidenciaRepository;
import com.universidad.repository.IncidenteRepository;
import com.universidad.service.IEvidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvidenciaServiceImpl implements IEvidenciaService {

    private final EvidenciaRepository evidenciaRepository;
    private final IncidenteRepository incidenteRepository;

    @Autowired
    public EvidenciaServiceImpl(EvidenciaRepository evidenciaRepository, IncidenteRepository incidenteRepository) {
        this.evidenciaRepository = evidenciaRepository;
        this.incidenteRepository = incidenteRepository;
    }

    @Override
    public List<EvidenciaDTO> obtenerEvidenciasPorIncidente(Long incidenteId) {
        return evidenciaRepository.findByIncidenteId(incidenteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EvidenciaDTO crearEvidencia(EvidenciaDTO evidenciaDTO) {
        Evidencia evidencia = new Evidencia();
        evidencia.setUrlArchivo(evidenciaDTO.getUrlArchivo());
        evidencia.setTipo(Evidencia.TipoEvidencia.valueOf(evidenciaDTO.getTipo().name()));

        Incidente incidente = incidenteRepository.findById(evidenciaDTO.getIncidenteId())
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado"));
        evidencia.setIncidente(incidente);

        Evidencia evidenciaGuardada = evidenciaRepository.save(evidencia);
        return convertToDTO(evidenciaGuardada);
    }

    @Override
    @Transactional
    public void eliminarEvidencia(Long id) {
        if (!evidenciaRepository.existsById(id)) {
            throw new RuntimeException("Evidencia no encontrada");
        }
        evidenciaRepository.deleteById(id);
    }

    private EvidenciaDTO convertToDTO(Evidencia evidencia) {
        EvidenciaDTO dto = new EvidenciaDTO();
        dto.setId(evidencia.getId());
        dto.setUrlArchivo(evidencia.getUrlArchivo());
        dto.setTipo(Evidencia.TipoEvidencia.valueOf(evidencia.getTipo().name()));
        dto.setIncidenteId(evidencia.getIncidente().getId());
        return dto;
    }
}

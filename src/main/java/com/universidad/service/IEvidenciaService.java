package com.universidad.service;


import com.universidad.dto.EvidenciaDTO;

import java.util.List;

public interface IEvidenciaService {
    List<EvidenciaDTO> obtenerEvidenciasPorIncidente(Long incidenteId);
    EvidenciaDTO crearEvidencia(EvidenciaDTO evidenciaDTO);
    void eliminarEvidencia(Long id);
}

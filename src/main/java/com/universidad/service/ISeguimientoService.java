package com.universidad.service;


import com.universidad.dto.SeguimientoDTO;

import java.util.List;

public interface ISeguimientoService {
    List<SeguimientoDTO> obtenerSeguimientosPorIncidente(Long incidenteId);
    SeguimientoDTO crearSeguimiento(SeguimientoDTO seguimientoDTO);
}

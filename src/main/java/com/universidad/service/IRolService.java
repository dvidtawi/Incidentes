package com.universidad.service;


import com.universidad.dto.RolDTO;

import java.util.List;

public interface IRolService {
    List<RolDTO> obtenerTodosLosRoles();
    RolDTO obtenerRolPorId(Long id);
    RolDTO crearRol(RolDTO rolDTO);
    RolDTO actualizarRol(Long id, RolDTO rolDTO);
    void eliminarRol(Long id);
}

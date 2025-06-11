package com.universidad.repository;

import com.universidad.model.Seguimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {
    List<Seguimiento> findByIncidenteId(Long incidenteId);
    List<Seguimiento> findByUsuarioId(Long usuarioId);
}

package com.universidad.repository;

import com.universidad.model.Incidente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Long> {
    List<Incidente> findByEstado(Incidente.Estado estado);
    List<Incidente> findByReportadorId(Long usuarioId);
    List<Incidente> findByUbicacionContainingIgnoreCase(String ubicacion);
    List<Incidente> findByGravedad(Incidente.Gravedad gravedad);
}

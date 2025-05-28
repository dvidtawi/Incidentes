package com.institucion.incidentes.repository;

import com.institucion.incidentes.model.Incidente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incidente, Long> {
    List<Incidente> findByEstado(Incidente.Estado estado);
    List<Incidente> findByPrioridad(Incidente.Prioridad prioridad);
    List<Incidente> findByUsuarioId(Long usuarioId);
    List<Incidente> findByCategoriaId(Long categoriaId);
}
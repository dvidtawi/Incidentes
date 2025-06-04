package com.institucion.incidentes.registro.repository;


import com.institucion.incidentes.registro.model.UsuarioRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UsuarioRegistroRepository extends JpaRepository<UsuarioRegistro, Long> {
    Optional<UsuarioRegistro> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}

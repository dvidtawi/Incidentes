package com.universidad.registro.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@CrossOrigin(origins = "*", maxAge = 3600) // Permite el acceso desde cualquier origen (CORS)
// maxAge = 3600 significa que la configuración de CORS se mantendrá durante 1 hora
@RestController
@RequestMapping("/api")
public class UsuarioController { // Controlador para manejar las peticiones relacionadas con los usuarios
    // Aquí puedes inyectar los servicios necesarios para manejar la lógica de negocio relacionada con los usuarios


    @GetMapping("/public/test") // Endpoint público accesible sin autenticación
    @PreAuthorize("permitAll()") // Permite el acceso a todos los usuarios, sin importar su rol
    public String allAccess() {
        return "Contenido público";
    }

    @GetMapping("/mantenimiento/test")
    @PreAuthorize("hasRole('SEGURIDAD') or hasRole('MANTENIMIENTO') or hasRole('ADMIN')")
    public String usarioAccess() {
        return "Contenido para usuarios";
    }

    // Endpoint protegido que requiere autenticación y autorización
    // Permite el acceso a usuarios con los roles 'EMPLEADO' o 'ADMIN'
    @GetMapping("/seguridad/test")
    @PreAuthorize("hasRole('SEGURIDAD') or hasRole('ADMIN')")
    public String empleadoAccess() {
        return "Contenido para docentes";
    }

    // Endpoint protegido que requiere autenticación y autorización
    // Permite el acceso solo a usuarios con el rol 'ADMIN'
    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Contenido para administradores";
    }  // Endpoint protegido que requiere autenticación y autorización

}

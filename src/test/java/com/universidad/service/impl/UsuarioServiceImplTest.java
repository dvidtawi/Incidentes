package com.universidad.service.impl;

import com.universidad.dto.UsuarioDTO;
import com.universidad.registro.model.Rol;
import com.universidad.registro.model.Usuario;
import com.universidad.registro.repository.RolRepository;
import com.universidad.registro.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

    private UsuarioRepository usuarioRepository;
    private RolRepository rolRepository;
    private PasswordEncoder passwordEncoder;
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        rolRepository = mock(RolRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        usuarioService = new UsuarioServiceImpl(usuarioRepository, rolRepository, passwordEncoder);
    }

    @Test
    void testObtenerTodosLosUsuarios() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("user");
        usuario.setEmail("user@email.com");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setActivo(true);
        usuario.setRoles(Set.of(new Rol(1L, Rol.NombreRol.ROL_ESTUDIANTE)));

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();

        assertEquals(1, usuarios.size());
        assertEquals("user", usuarios.get(0).getUsername());
    }

    @Test
    void testObtenerUsuarioPorIdExistente() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("usuario1");
        usuario.setEmail("correo@test.com");
        usuario.setNombre("Nombre");
        usuario.setApellido("Apellido");
        usuario.setActivo(true);
        usuario.setRoles(Set.of(new Rol(1L, Rol.NombreRol.ROL_ESTUDIANTE)));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioDTO dto = usuarioService.obtenerUsuarioPorId(1L);

        assertEquals("usuario1", dto.getUsername());
        assertTrue(dto.getActivo());
    }

    @Test
    void testCrearUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("nuevoUsuario");
        dto.setEmail("nuevo@email.com");
        dto.setPassword("1234");
        dto.setNombre("Nuevo");
        dto.setApellido("Usuario");
        dto.setRoles(Set.of("ROL_ESTUDIANTE"));

        when(usuarioRepository.existsByUsername("nuevoUsuario")).thenReturn(false);
        when(usuarioRepository.existsByEmail("nuevo@email.com")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("encoded1234");

        Rol rol = new Rol(1L, Rol.NombreRol.ROL_ESTUDIANTE);
        when(rolRepository.findByNombre(Rol.NombreRol.ROL_ESTUDIANTE)).thenReturn(Optional.of(rol));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioDTO resultado = usuarioService.crearUsuario(dto);

        verify(usuarioRepository).save(captor.capture());
        Usuario guardado = captor.getValue();

        assertEquals("nuevoUsuario", guardado.getUsername());
        assertEquals("encoded1234", guardado.getPassword());
        assertEquals("nuevoUsuario", resultado.getUsername());
    }

    @Test
    void testActualizarUsuario() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setUsername("user");
        existente.setEmail("email@test.com");
        existente.setNombre("Nombre");
        existente.setApellido("Apellido");
        existente.setActivo(true);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("user");
        dto.setEmail("email@test.com");
        dto.setNombre("Nombre Actualizado");
        dto.setApellido("Apellido Actualizado");
        dto.setActivo(false);
        dto.setRoles(Set.of("ROL_ESTUDIANTE"));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByUsername("user")).thenReturn(false);
        when(usuarioRepository.existsByEmail("email@test.com")).thenReturn(false);

        Rol rol = new Rol(1L, Rol.NombreRol.ROL_ESTUDIANTE);
        when(rolRepository.findByNombre(Rol.NombreRol.ROL_ESTUDIANTE)).thenReturn(Optional.of(rol));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioDTO actualizado = usuarioService.actualizarUsuario(1L, dto);

        assertEquals("Nombre Actualizado", actualizado.getNombre());
        assertFalse(actualizado.getActivo());
    }

    @Test
    void testEliminarUsuarioExistente() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        assertDoesNotThrow(() -> usuarioService.eliminarUsuario(1L));
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void testEliminarUsuarioInexistente() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.eliminarUsuario(99L));
        assertEquals("Usuario no encontrado", ex.getMessage());
    }
}

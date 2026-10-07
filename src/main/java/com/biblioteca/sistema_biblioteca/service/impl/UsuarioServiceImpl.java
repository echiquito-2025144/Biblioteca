package com.biblioteca.sistema_biblioteca.service.impl;

import com.biblioteca.sistema_biblioteca.dto.request.UsuarioRequest;
import com.biblioteca.sistema_biblioteca.dto.response.UsuarioResponse;
import com.biblioteca.sistema_biblioteca.exception.BadRequestException;
import com.biblioteca.sistema_biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.sistema_biblioteca.model.entity.Usuario;
import com.biblioteca.sistema_biblioteca.model.enums.EstadoUsuario;
import com.biblioteca.sistema_biblioteca.repository.UsuarioRepository;
import com.biblioteca.sistema_biblioteca.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UsuarioResponse registrarUsuario(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El correo electrónico ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(request.getPassword()) // Se integrará BCrypt con Spring Security
                .estado(EstadoUsuario.ACTIVO)
                .rol(request.getRol())
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return mapToResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return mapToResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public UsuarioResponse sancionarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        usuario.setEstado(EstadoUsuario.SANCIONADO);
        return mapToResponse(usuarioRepository.save(usuario));
    }

    private UsuarioResponse mapToResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .estado(usuario.getEstado())
                .rol(usuario.getRol())
                .build();
    }
}
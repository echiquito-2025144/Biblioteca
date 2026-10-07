package com.biblioteca.sistema_biblioteca.service;

import com.biblioteca.sistema_biblioteca.dto.request.UsuarioRequest;
import com.biblioteca.sistema_biblioteca.dto.response.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse registrarUsuario(UsuarioRequest request);
    UsuarioResponse obtenerPorId(Long id);
    List<UsuarioResponse> obtenerTodos();
    UsuarioResponse sancionarUsuario(Long id);
}
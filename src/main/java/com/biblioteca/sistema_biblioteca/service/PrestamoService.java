package com.biblioteca.sistema_biblioteca.service;

import com.biblioteca.sistema_biblioteca.dto.request.PrestamoRequest;
import com.biblioteca.sistema_biblioteca.dto.response.PrestamoResponse;

import java.util.List;

public interface PrestamoService {
    PrestamoResponse registrarPrestamo(Long usuarioId, PrestamoRequest request);
    PrestamoResponse registrarDevolucion(Long prestamoId);
    List<PrestamoResponse> obtenerPrestamosPorUsuario(Long usuarioId);
    List<PrestamoResponse> obtenerTodos();
}
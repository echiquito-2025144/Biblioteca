package com.biblioteca.sistema_biblioteca.service;

import com.biblioteca.sistema_biblioteca.dto.request.LibroRequest;
import com.biblioteca.sistema_biblioteca.dto.response.LibroResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LibroService {
    LibroResponse crearLibro(LibroRequest request);
    LibroResponse obtenerPorId(Long id);
    Page<LibroResponse> listarLibros(String filtro, Pageable pageable);
    LibroResponse actualizarLibro(Long id, LibroRequest request);
    void eliminarLibro(Long id);
}
package com.biblioteca.sistema_biblioteca.service.impl;

import com.biblioteca.sistema_biblioteca.dto.request.LibroRequest;
import com.biblioteca.sistema_biblioteca.dto.response.LibroResponse;
import com.biblioteca.sistema_biblioteca.exception.BadRequestException;
import com.biblioteca.sistema_biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.sistema_biblioteca.model.entity.Libro;
import com.biblioteca.sistema_biblioteca.repository.LibroRepository;
import com.biblioteca.sistema_biblioteca.service.LibroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;

    @Override
    @Transactional
    public LibroResponse crearLibro(LibroRequest request) {
        if (libroRepository.existsByIsbn(request.getIsbn())) {
            throw new BadRequestException("El ISBN ya existe en el sistema");
        }

        Libro libro = Libro.builder()
                .isbn(request.getIsbn())
                .titulo(request.getTitulo())
                .autor(request.getAutor())
                .categoria(request.getCategoria())
                .stockTotal(request.getStockTotal())
                .stockDisponible(request.getStockTotal())
                .build();

        return mapToResponse(libroRepository.save(libro));
    }

    @Override
    @Transactional(readOnly = true)
    public LibroResponse obtenerPorId(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + id));
        return mapToResponse(libro);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LibroResponse> listarLibros(String filtro, Pageable pageable) {
        if (filtro != null && !filtro.isBlank()) {
            return libroRepository.findByTituloContainingIgnoreCaseOrCategoriaContainingIgnoreCase(filtro, filtro, pageable)
                    .map(this::mapToResponse);
        }
        return libroRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public LibroResponse actualizarLibro(Long id, LibroRequest request) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + id));

        int diferenciaStock = request.getStockTotal() - libro.getStockTotal();
        int nuevoDisponible = libro.getStockDisponible() + diferenciaStock;

        if (nuevoDisponible < 0) {
            throw new BadRequestException("El stock total no puede ser menor a los libros actualmente prestados");
        }

        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setCategoria(request.getCategoria());
        libro.setStockTotal(request.getStockTotal());
        libro.setStockDisponible(nuevoDisponible);

        return mapToResponse(libroRepository.save(libro));
    }

    @Override
    @Transactional
    public void eliminarLibro(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + id));

        if (!libro.getStockTotal().equals(libro.getStockDisponible())) {
            throw new BadRequestException("No se puede eliminar un libro que tiene ejemplares actualmente prestados");
        }

        libroRepository.delete(libro);
    }

    private LibroResponse mapToResponse(Libro libro) {
        return LibroResponse.builder()
                .id(libro.getId())
                .isbn(libro.getIsbn())
                .titulo(libro.getTitulo())
                .autor(libro.getAutor())
                .categoria(libro.getCategoria())
                .stockTotal(libro.getStockTotal())
                .stockDisponible(libro.getStockDisponible())
                .build();
    }
}
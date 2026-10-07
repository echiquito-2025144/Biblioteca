package com.biblioteca.sistema_biblioteca.repository;

import com.biblioteca.sistema_biblioteca.model.entity.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);

    // Búsqueda con paginación por título o categoría (insensible a mayúsculas)
    Page<Libro> findByTituloContainingIgnoreCaseOrCategoriaContainingIgnoreCase(
            String titulo, String categoria, Pageable pageable);
}
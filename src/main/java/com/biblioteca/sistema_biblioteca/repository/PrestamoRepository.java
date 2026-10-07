package com.biblioteca.sistema_biblioteca.repository;

import com.biblioteca.sistema_biblioteca.model.entity.Prestamo;
import com.biblioteca.sistema_biblioteca.model.enums.EstadoPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    // Cuenta préstamos activos para validar el límite máximo de 3
    long countByUsuarioIdAndEstado(Long usuarioId, EstadoPrestamo estado);

    // Listar todos los préstamos de un usuario
    List<Prestamo> findByUsuarioId(Long usuarioId);

    // Listar préstamos por su estado (ACTIVO, DEVUELTO, ATRASADO)
    List<Prestamo> findByEstado(EstadoPrestamo estado);

    // Buscar préstamos que vencieron y siguen activos
    List<Prestamo> findByEstadoAndFechaDevolucionEsperadaBefore(EstadoPrestamo estado, LocalDate fechaActual);
}
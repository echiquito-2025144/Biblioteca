package com.biblioteca.sistema_biblioteca.service.impl;

import com.biblioteca.sistema_biblioteca.dto.request.PrestamoRequest;
import com.biblioteca.sistema_biblioteca.dto.response.PrestamoResponse;
import com.biblioteca.sistema_biblioteca.exception.BadRequestException;
import com.biblioteca.sistema_biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.sistema_biblioteca.model.entity.Libro;
import com.biblioteca.sistema_biblioteca.model.entity.Prestamo;
import com.biblioteca.sistema_biblioteca.model.entity.Usuario;
import com.biblioteca.sistema_biblioteca.model.enums.EstadoPrestamo;
import com.biblioteca.sistema_biblioteca.model.enums.EstadoUsuario;
import com.biblioteca.sistema_biblioteca.repository.LibroRepository;
import com.biblioteca.sistema_biblioteca.repository.PrestamoRepository;
import com.biblioteca.sistema_biblioteca.repository.UsuarioRepository;
import com.biblioteca.sistema_biblioteca.service.PrestamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    private static final int LIMITE_PRESTAMOS = 3;
    private static final int DIAS_PRESTAMO = 14;

    @Override
    @Transactional
    public PrestamoResponse registrarPrestamo(Long usuarioId, PrestamoRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (usuario.getEstado() == EstadoUsuario.SANCIONADO) {
            throw new BadRequestException("El usuario se encuentra SANCIONADO y no puede realizar préstamos");
        }

        long prestamosActivos = prestamoRepository.countByUsuarioIdAndEstado(usuarioId, EstadoPrestamo.ACTIVO);
        if (prestamosActivos >= LIMITE_PRESTAMOS) {
            throw new BadRequestException("El usuario ha alcanzado el límite máximo de " + LIMITE_PRESTAMOS + " préstamos activos");
        }

        Libro libro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        if (libro.getStockDisponible() <= 0) {
            throw new BadRequestException("No hay ejemplares disponibles para este libro");
        }

        // Actualizar stock disponible
        libro.setStockDisponible(libro.getStockDisponible() - 1);
        libroRepository.save(libro);

        Prestamo prestamo = Prestamo.builder()
                .usuario(usuario)
                .libro(libro)
                .fechaPrestamo(LocalDate.now())
                .fechaDevolucionEsperada(LocalDate.now().plusDays(DIAS_PRESTAMO))
                .estado(EstadoPrestamo.ACTIVO)
                .build();

        return mapToResponse(prestamoRepository.save(prestamo));
    }

    @Override
    @Transactional
    public PrestamoResponse registrarDevolucion(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado"));

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new BadRequestException("Este préstamo ya fue devuelto previamente");
        }

        LocalDate fechaActual = LocalDate.now();
        prestamo.setFechaDevolucionReal(fechaActual);
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);

        // Si se devolvió con atraso, sancionar al usuario automáticamente
        if (fechaActual.isAfter(prestamo.getFechaDevolucionEsperada())) {
            Usuario usuario = prestamo.getUsuario();
            usuario.setEstado(EstadoUsuario.SANCIONADO);
            usuarioRepository.save(usuario);
        }

        // Restaurar stock del libro
        Libro libro = prestamo.getLibro();
        libro.setStockDisponible(libro.getStockDisponible() + 1);
        libroRepository.save(libro);

        return mapToResponse(prestamoRepository.save(prestamo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> obtenerPrestamosPorUsuario(Long usuarioId) {
        return prestamoRepository.findByUsuarioId(usuarioId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> obtenerTodos() {
        return prestamoRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PrestamoResponse mapToResponse(Prestamo prestamo) {
        return PrestamoResponse.builder()
                .id(prestamo.getId())
                .usuarioNombre(prestamo.getUsuario().getNombre())
                .usuarioEmail(prestamo.getUsuario().getEmail())
                .libroTitulo(prestamo.getLibro().getTitulo())
                .libroIsbn(prestamo.getLibro().getIsbn())
                .fechaPrestamo(prestamo.getFechaPrestamo())
                .fechaDevolucionEsperada(prestamo.getFechaDevolucionEsperada())
                .fechaDevolucionReal(prestamo.getFechaDevolucionReal())
                .estado(prestamo.getEstado())
                .build();
    }
}
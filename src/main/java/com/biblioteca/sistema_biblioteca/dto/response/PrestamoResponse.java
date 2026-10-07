package com.biblioteca.sistema_biblioteca.dto.response;

import com.biblioteca.sistema_biblioteca.model.enums.EstadoPrestamo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoResponse {
    private Long id;
    private String usuarioNombre;
    private String usuarioEmail;
    private String libroTitulo;
    private String libroIsbn;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEsperada;
    private LocalDate fechaDevolucionReal;
    private EstadoPrestamo estado;
}
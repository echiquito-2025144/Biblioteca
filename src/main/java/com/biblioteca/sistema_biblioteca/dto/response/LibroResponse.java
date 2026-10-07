package com.biblioteca.sistema_biblioteca.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibroResponse {
    private Long id;
    private String isbn;
    private String titulo;
    private String autor;
    private String categoria;
    private Integer stockTotal;
    private Integer stockDisponible;
}
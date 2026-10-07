package com.biblioteca.sistema_biblioteca.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LibroRequest {

    @NotBlank(message = "El ISBN es obligatorio")
    private String isbn;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    private String autor;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    @NotNull(message = "El stock total es obligatorio")
    @Min(value = 1, message = "El stock debe ser al menos 1")
    private Integer stockTotal;
}
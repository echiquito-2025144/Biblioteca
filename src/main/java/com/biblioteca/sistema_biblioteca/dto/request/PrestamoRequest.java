package com.biblioteca.sistema_biblioteca.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrestamoRequest {

    @NotNull(message = "El ID del libro es obligatorio")
    private Long libroId;
}
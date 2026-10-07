package com.biblioteca.sistema_biblioteca.dto.response;

import com.biblioteca.sistema_biblioteca.model.enums.EstadoUsuario;
import com.biblioteca.sistema_biblioteca.model.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String email;
    private EstadoUsuario estado;
    private Rol rol;
}
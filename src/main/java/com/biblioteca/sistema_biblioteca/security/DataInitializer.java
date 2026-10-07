package com.biblioteca.sistema_biblioteca.security;

import com.biblioteca.sistema_biblioteca.model.entity.Usuario;
import com.biblioteca.sistema_biblioteca.model.enums.EstadoUsuario;
import com.biblioteca.sistema_biblioteca.model.enums.Rol; // Ajusta según el nombre de tu Enum
import com.biblioteca.sistema_biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String email = "admin@biblioteca.com";

        // Si ya existe, le actualizamos la contraseña cifrada correctamente
        Usuario usuario = usuarioRepository.findByEmail(email).orElseGet(() ->
                Usuario.builder()
                        .email(email)
                        .nombre("Admin")
                        .estado(EstadoUsuario.ACTIVO)
                        .rol(Rol.ADMIN) // O el rol que corresponda en tu Enum
                        .build()
        );

        usuario.setPassword(passwordEncoder.encode("admin123"));
        usuarioRepository.save(usuario);

        System.out.println(">>> USUARIO ADMIN ACTUALIZADO CORRECTAMENTE CON CONTRASEÑA CIFRADA <<<");
    }
}
package com.biblioteca.sistema_biblioteca.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos (Registro e inicio de sesión/autenticación)
                        .requestMatchers("/api/auth/**").permitAll()

                        // Restricciones por roles en Usuarios
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasRole("ADMIN")

                        // Restricciones por roles en Libros
                        .requestMatchers(HttpMethod.GET, "/api/libros/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/libros/**").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                        .requestMatchers(HttpMethod.PUT, "/api/libros/**").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                        .requestMatchers(HttpMethod.DELETE, "/api/libros/**").hasRole("ADMIN")

                        // Restricciones por roles en Préstamos
                        .requestMatchers(HttpMethod.POST, "/api/prestamos/**").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                        .requestMatchers(HttpMethod.PUT, "/api/prestamos/**").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                        .requestMatchers(HttpMethod.GET, "/api/prestamos/**").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
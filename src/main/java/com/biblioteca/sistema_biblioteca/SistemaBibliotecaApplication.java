package com.biblioteca.sistema_biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SistemaBibliotecaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaBibliotecaApplication.class, args);

		// Imprime el hash exacto en la terminal al iniciar la app
		System.out.println("HASH_REAL: " + new BCryptPasswordEncoder().encode("admin123"));
	}

}
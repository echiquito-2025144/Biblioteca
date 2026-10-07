package com.biblioteca.sistema_biblioteca.controller;

import com.biblioteca.sistema_biblioteca.dto.request.PrestamoRequest;
import com.biblioteca.sistema_biblioteca.dto.response.PrestamoResponse;
import com.biblioteca.sistema_biblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<PrestamoResponse> registrarPrestamo(
            @PathVariable Long usuarioId,
            @Valid @RequestBody PrestamoRequest request) {
        return new ResponseEntity<>(prestamoService.registrarPrestamo(usuarioId, request), HttpStatus.CREATED);
    }

    @PutMapping("/{prestamoId}/devolucion")
    public ResponseEntity<PrestamoResponse> registrarDevolucion(@PathVariable Long prestamoId) {
        return ResponseEntity.ok(prestamoService.registrarDevolucion(prestamoId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PrestamoResponse>> obtenerPrestamosPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(prestamoService.obtenerPrestamosPorUsuario(usuarioId));
    }

    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> obtenerTodos() {
        return ResponseEntity.ok(prestamoService.obtenerTodos());
    }
}
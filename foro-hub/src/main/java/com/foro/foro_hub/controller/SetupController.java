package com.foro.foro_hub.controller;

import com.foro.foro_hub.domain.usuario.Usuario;
import com.foro.foro_hub.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * SOLO PARA PRUEBAS/DESARROLLO.
 * Eliminar este controller en producción.
 */
@RestController
@RequestMapping("/setup")
public class SetupController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<String> crearUsuarioAdmin() {
        if (usuarioRepository.findByLogin("admin@foro.com") != null) {
            return ResponseEntity.ok("El usuario admin@foro.com ya existe.");
        }
        String claveEncriptada = passwordEncoder.encode("123456");
        usuarioRepository.save(new Usuario(null, "admin@foro.com", claveEncriptada));
        return ResponseEntity.ok("Usuario admin@foro.com creado con clave: 123456");
    }
}

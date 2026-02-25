package com.foro.foro_hub.infra;

import com.foro.foro_hub.domain.usuario.Usuario;
import com.foro.foro_hub.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Crear usuario admin si no existe
        if (usuarioRepository.findByLogin("admin@foro.com") == null) {
            String claveEncriptada = passwordEncoder.encode("123456");
            Usuario admin = new Usuario(null, "admin@foro.com", claveEncriptada);
            usuarioRepository.save(admin);
            System.out.println(">>> Usuario admin@foro.com creado con clave: 123456");
        }
    }
}

package com.foro.foro_hub.controller;

import com.foro.foro_hub.domain.usuario.DatosAutenticacion;
import com.foro.foro_hub.domain.usuario.Usuario;
import com.foro.foro_hub.infra.security.DatosTokenJWT;
import com.foro.foro_hub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<DatosTokenJWT> autenticar(@RequestBody @Valid DatosAutenticacion datos) {
        var authToken = new UsernamePasswordAuthenticationToken(datos.login(), datos.clave());
        var usuarioAutenticado = authenticationManager.authenticate(authToken);
        var tokenJWT = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
        return ResponseEntity.ok(new DatosTokenJWT(tokenJWT));
    }
}

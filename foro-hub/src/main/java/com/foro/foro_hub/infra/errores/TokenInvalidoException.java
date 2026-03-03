package com.foro.foro_hub.infra.errores;

public class TokenInvalidoException extends RuntimeException {
    public TokenInvalidoException(String mensaje) {
        super(mensaje);
    }
}

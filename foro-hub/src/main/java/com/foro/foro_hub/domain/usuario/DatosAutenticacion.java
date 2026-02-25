package com.foro.foro_hub.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DatosAutenticacion(
        @NotBlank String login,
        @NotBlank String clave
) {
}

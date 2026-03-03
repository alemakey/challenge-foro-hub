package com.foro.foro_hub.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DatosAutenticacion(
                @NotBlank(message = "El login es obligatorio") String login,
                @NotBlank(message = "La clave es obligatoria") String clave) {
}

package com.foro.foro_hub.domain.topico;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistroTopico(
        @NotBlank(message = "El título es obligatorio") String titulo,
        @NotBlank(message = "El mensaje es obligatorio") String mensaje,
        @NotBlank(message = "El curso es obligatorio") String curso
) {
}

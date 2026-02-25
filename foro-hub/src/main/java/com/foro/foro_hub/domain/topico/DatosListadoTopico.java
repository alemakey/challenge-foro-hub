package com.foro.foro_hub.domain.topico;

import java.time.LocalDateTime;

public record DatosListadoTopico(
        Long id,
        String titulo,
        String curso,
        LocalDateTime fechaCreacion
) {
    public DatosListadoTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getCurso(),
                topico.getFechaCreacion()
        );
    }
}

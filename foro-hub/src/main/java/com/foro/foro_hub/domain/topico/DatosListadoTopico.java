package com.foro.foro_hub.domain.topico;

import java.time.LocalDateTime;

public record DatosListadoTopico(
        Long id,
        String titulo,
        String curso,
        LocalDateTime fechaCreacion,
        StatusTopico status,
        String autor) {
    public DatosListadoTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getCurso(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor().getUsername());
    }
}

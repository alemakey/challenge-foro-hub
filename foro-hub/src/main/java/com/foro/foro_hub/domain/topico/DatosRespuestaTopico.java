package com.foro.foro_hub.domain.topico;

import java.time.LocalDateTime;

public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,
        String curso,
        LocalDateTime fechaCreacion,
        Boolean activo
) {
    public DatosRespuestaTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getCurso(),
                topico.getFechaCreacion(),
                topico.getActivo()
        );
    }
}

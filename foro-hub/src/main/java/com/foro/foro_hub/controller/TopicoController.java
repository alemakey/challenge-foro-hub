package com.foro.foro_hub.controller;

import com.foro.foro_hub.domain.topico.*;
import com.foro.foro_hub.infra.errores.ValidacionException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoRepository topicoRepository;

    public TopicoController(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    // Listar todos los tópicos activos con paginación
    @GetMapping
    public Page<DatosListadoTopico> listar(@PageableDefault(size = 10, sort = "fechaCreacion") Pageable paginacion) {
        return topicoRepository.findAllByActivoTrue(paginacion).map(DatosListadoTopico::new);
    }

    // Detalle de un tópico específico
    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> detalle(@PathVariable Long id) {
        return topicoRepository.findById(id)
                .filter(Topico::getActivo)
                .map(t -> ResponseEntity.ok(new DatosRespuestaTopico(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear nuevo tópico con validación de duplicados
    @PostMapping
    public ResponseEntity<DatosRespuestaTopico> crear(
            @RequestBody @Valid DatosRegistroTopico datos,
            UriComponentsBuilder uriBuilder) {

        // Validación de negocio: no permitir tópicos duplicados
        if (topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            throw new ValidacionException("Ya existe un tópico con el mismo título y mensaje.");
        }

        Topico topico = topicoRepository.save(new Topico(datos));
        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosRespuestaTopico(topico));
    }

    // Actualizar tópico
    @PutMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarTopico datos) {

        return topicoRepository.findById(id)
                .filter(Topico::getActivo)
                .map(topico -> {
                    topico.actualizar(datos);
                    return ResponseEntity.ok(new DatosRespuestaTopico(topicoRepository.save(topico)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar tópico (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return topicoRepository.findById(id)
                .filter(Topico::getActivo)
                .map(topico -> {
                    topico.desactivar();
                    topicoRepository.save(topico);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElse(ResponseEntity.<Void>notFound().build());
    }
}

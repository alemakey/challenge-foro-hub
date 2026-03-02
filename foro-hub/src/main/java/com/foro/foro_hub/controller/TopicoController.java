package com.foro.foro_hub.controller;

import com.foro.foro_hub.domain.topico.*;
import com.foro.foro_hub.domain.usuario.Usuario;
import com.foro.foro_hub.infra.errores.ValidacionException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
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
    public Page<DatosListadoTopico> listar(
            @PageableDefault(size = 10, sort = "fechaCreacion") Pageable paginacion) {
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

    // Crear nuevo tópico — el autor se obtiene del JWT autenticado
    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> crear(
            @RequestBody @Valid DatosRegistroTopico datos,
            @AuthenticationPrincipal Usuario usuarioAutenticado,
            UriComponentsBuilder uriBuilder) {

        if (topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            throw new ValidacionException("Ya existe un tópico con el mismo título y mensaje.");
        }

        Topico topico = topicoRepository.save(new Topico(datos, usuarioAutenticado));
        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosRespuestaTopico(topico));
    }

    // Actualizar tópico (título, mensaje, curso y/o status)
    @PutMapping("/{id}")
    @Transactional
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
    @Transactional
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        var topico = topicoRepository.findById(id)
                .filter(Topico::getActivo)
                .orElse(null);
        if (topico == null) {
            return ResponseEntity.<Void>notFound().build();
        }
        topico.desactivar();
        topicoRepository.save(topico);
        return ResponseEntity.<Void>noContent().build();
    }
}

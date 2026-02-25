package com.foro.foro_hub.controller;

import com.foro.foro_hub.domain.topico.Topico;
import com.foro.foro_hub.domain.topico.TopicoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoRepository topicoRepository;

    public TopicoController(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    @GetMapping
    public List<Topico> listar() {
        return topicoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topico> detalle(@PathVariable Long id) {
        return topicoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Topico> crear(@RequestBody Topico datos) {
        Topico topico = topicoRepository.save(datos);
        return ResponseEntity.status(201).body(topico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topico> actualizar(@PathVariable Long id, @RequestBody Topico datos) {
        return topicoRepository.findById(id).map(topico -> {
            if (datos.getTitulo() != null) {
                topico = new Topico(datos.getTitulo(), datos.getMensaje(), datos.getCurso());
            }
            return ResponseEntity.ok(topicoRepository.save(topico));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!topicoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

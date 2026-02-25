package com.foro.foro_hub.controller;

import com.foro.foro_hub.domain.topico.Topico;
import com.foro.foro_hub.domain.topico.TopicoRepository;
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

    @PostMapping
    public Topico crear(@RequestBody Topico datos) {
        return topicoRepository.save(datos);
    }
}

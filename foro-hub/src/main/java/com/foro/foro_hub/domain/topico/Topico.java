package com.foro.foro_hub.domain.topico;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "topicos")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String mensaje;

    @NotBlank
    private String curso;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private Boolean activo = true;

    public Topico() {
    }

    public Topico(DatosRegistroTopico datos) {
        this.titulo = datos.titulo();
        this.mensaje = datos.mensaje();
        this.curso = datos.curso();
    }

    public void actualizar(DatosActualizarTopico datos) {
        if (datos.titulo() != null && !datos.titulo().isBlank()) {
            this.titulo = datos.titulo();
        }
        if (datos.mensaje() != null && !datos.mensaje().isBlank()) {
            this.mensaje = datos.mensaje();
        }
        if (datos.curso() != null && !datos.curso().isBlank()) {
            this.curso = datos.curso();
        }
    }

    public void desactivar() {
        this.activo = false;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getMensaje() { return mensaje; }
    public String getCurso() { return curso; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public Boolean getActivo() { return activo; }
}

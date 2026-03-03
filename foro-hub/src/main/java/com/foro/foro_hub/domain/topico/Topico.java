package com.foro.foro_hub.domain.topico;

import com.foro.foro_hub.domain.usuario.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "topicos")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(nullable = false, length = 100)
    private String curso;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusTopico status = StatusTopico.ABIERTO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @Column(nullable = false)
    private Boolean activo = true;

    public Topico() {
    }

    public Topico(DatosRegistroTopico datos, Usuario autor) {
        this.titulo = datos.titulo();
        this.mensaje = datos.mensaje();
        this.curso = datos.curso();
        this.autor = autor;
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
        if (datos.status() != null) {
            this.status = datos.status();
        }
    }

    public void desactivar() {
        this.activo = false;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getCurso() {
        return curso;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public StatusTopico getStatus() {
        return status;
    }

    public Usuario getAutor() {
        return autor;
    }

    public Boolean getActivo() {
        return activo;
    }
}

package com.foro.foro_hub.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    // Fix #1 (N+1): fetch join carga el autor en la misma query
    @Query("SELECT t FROM Topico t JOIN FETCH t.autor WHERE t.activo = true")
    Page<Topico> findAllByActivoTrue(Pageable pageable);

    // Fix #7: solo verifica duplicado entre tópicos activos
    boolean existsByTituloAndMensajeAndActivoTrue(String titulo, String mensaje);
}

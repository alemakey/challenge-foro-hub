CREATE TABLE usuarios (
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(100) NOT NULL UNIQUE,
    clave VARCHAR(300) NOT NULL
);

CREATE TABLE topicos (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo         VARCHAR(300) NOT NULL,
    mensaje        TEXT NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    status         VARCHAR(20) NOT NULL DEFAULT 'ABIERTO',
    autor_id       BIGINT NOT NULL,
    curso          VARCHAR(100) NOT NULL,
    activo         BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_topico_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id)
);

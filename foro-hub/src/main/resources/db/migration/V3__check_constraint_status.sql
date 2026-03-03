-- Agrega restricción de integridad en el campo status de tópicos
-- Solo permite los valores definidos en el enum StatusTopico
ALTER TABLE topicos ADD CONSTRAINT chk_topico_status
    CHECK (status IN ('ABIERTO', 'CERRADO', 'SOLUCIONADO'));

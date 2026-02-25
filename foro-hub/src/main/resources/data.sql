-- Usuario de prueba: login=admin@foro.com / clave=123456
-- Contraseña encriptada con BCrypt
INSERT INTO usuarios (login, clave) VALUES (
  'admin@foro.com',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LqTa1JGKe96'
);

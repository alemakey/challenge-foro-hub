-- Usuario admin: login=admin@foro.com / clave=123456
-- Hash BCrypt generado con BCryptPasswordEncoder.encode("123456")
INSERT INTO usuarios (login, clave) VALUES (
    'admin@foro.com',
    '$2a$10$gUb5qiSwMcsKxAYQ7b7UE.VWjfzYBLzTRRHXXl2C3q9BnkDP.lVK2'
);

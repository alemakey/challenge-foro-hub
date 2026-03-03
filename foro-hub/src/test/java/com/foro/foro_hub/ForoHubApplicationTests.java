package com.foro.foro_hub;

import com.foro.foro_hub.domain.usuario.UsuarioRepository;
import com.foro.foro_hub.infra.security.TokenService;
import com.foro.foro_hub.domain.usuario.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ForoHubApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	// =====================================================
	// Test: el contexto de Spring arranca correctamente
	// =====================================================
	@Test
	@DisplayName("El contexto de Spring carga sin errores")
	void contextLoads() {
	}

	// =====================================================
	// Tests de seguridad — sin token
	// =====================================================
	@Test
	@DisplayName("GET /topicos sin token debe devolver 403")
	void deberiaRetornar403SinToken() throws Exception {
		mockMvc.perform(get("/topicos"))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("POST /topicos sin token debe devolver 403")
	void deberiaRetornar403AlCrearSinToken() throws Exception {
		mockMvc.perform(post("/topicos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "titulo": "Test",
						  "mensaje": "Mensaje de prueba",
						  "curso": "Java"
						}
						"""))
				.andExpect(status().isForbidden());
	}

	// =====================================================
	// Tests de login
	// =====================================================
	@Test
	@DisplayName("POST /login con credenciales válidas devuelve token JWT")
	void deberiaRetornarTokenConCredencialesValidas() throws Exception {
		mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "login": "admin@foro.com",
						  "clave": "123456"
						}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jwToken").isString());
	}

	@Test
	@DisplayName("POST /login con credenciales inválidas devuelve 403")
	void deberiaRetornar403ConCredencialesInvalidas() throws Exception {
		mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "login": "admin@foro.com",
						  "clave": "claveIncorrecta"
						}
						"""))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("POST /login con campos vacíos devuelve 400 con mensajes de error")
	void deberiaRetornar400SiLoginOClaveVacios() throws Exception {
		mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "login": "",
						  "clave": ""
						}
						"""))
				.andExpect(status().isBadRequest());
	}

	// =====================================================
	// Tests de tópicos — con token válido
	// =====================================================
	@Test
	@DisplayName("GET /topicos con token válido devuelve 200")
	void deberiaListarTopicosConToken() throws Exception {
		var usuario = (Usuario) usuarioRepository.findByLogin("admin@foro.com");
		var token = tokenService.generarToken(usuario);

		mockMvc.perform(get("/topicos")
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray());
	}

	@Test
	@DisplayName("POST /topicos con token y datos válidos devuelve 201")
	void deberiaCrearTopicoConDatosValidos() throws Exception {
		var usuario = (Usuario) usuarioRepository.findByLogin("admin@foro.com");
		var token = tokenService.generarToken(usuario);

		mockMvc.perform(post("/topicos")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "titulo": "Título de prueba único",
						  "mensaje": "Mensaje funcional de prueba",
						  "curso": "Spring Boot"
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.titulo").value("Título de prueba único"));
	}

	@Test
	@DisplayName("POST /topicos sin título devuelve 400 con mensaje de error")
	void deberiaRetornar400SiTituloVacio() throws Exception {
		var usuario = (Usuario) usuarioRepository.findByLogin("admin@foro.com");
		var token = tokenService.generarToken(usuario);

		mockMvc.perform(post("/topicos")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "titulo": "",
						  "mensaje": "Mensaje válido",
						  "curso": "Java"
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$[0].campo").value("titulo"));
	}

	@Test
	@DisplayName("GET /topicos/{id} con id inexistente devuelve 404")
	void deberiaRetornar404SiTopicoNoExiste() throws Exception {
		var usuario = (Usuario) usuarioRepository.findByLogin("admin@foro.com");
		var token = tokenService.generarToken(usuario);

		mockMvc.perform(get("/topicos/99999")
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("DELETE /topicos/{id} con id inexistente devuelve 404")
	void deberiaRetornar404AlEliminarTopicoQueNoExiste() throws Exception {
		var usuario = (Usuario) usuarioRepository.findByLogin("admin@foro.com");
		var token = tokenService.generarToken(usuario);

		mockMvc.perform(delete("/topicos/99999")
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isNotFound());
	}
}

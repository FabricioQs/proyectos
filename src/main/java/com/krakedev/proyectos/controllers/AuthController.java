package com.krakedev.proyectos.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.repositories.UsuarioRepository;
import com.krakedev.proyectos.security.JwtUtil;
import com.krakedev.proyectos.services.TokenBlacklistService;
import com.krakedev.proyectos.services.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UsuarioService usuarioService;
	private final UsuarioRepository usuarioRepository;
	private final TokenBlacklistService blacklistService;

	public AuthController(UsuarioService usuarioService, UsuarioRepository usuarioRepository,
			TokenBlacklistService blacklistService) {
		this.usuarioService = usuarioService;
		this.usuarioRepository = usuarioRepository;
		this.blacklistService = blacklistService;
	}

	@PostMapping("/registrar")
	public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
		try {
			Usuario usuarioNuevo = usuarioService.guardar(usuario);
			return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al registrar usuario: " + e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
		String username = credenciales.get("username");
		String password = credenciales.get("password");

		boolean autenticado = usuarioService.autenticar(username, password);

		if (autenticado) {
			Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
			String rol = usuarioOpt.map(Usuario::getRol).orElse("USER");
			String token = JwtUtil.generarToken(username, rol);

			return ResponseEntity.ok(Map.of(
					"mensaje", "Login exitoso",
					"token", token,
					"rol", rol));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrecta");
		}
	}

	@GetMapping("/perfil")
	public ResponseEntity<?> perfil() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String usuario = auth.getName();
		String rol = auth.getAuthorities().iterator().next().getAuthority();

		return ResponseEntity.ok(Map.of(
				"mensaje", "Bienvenido al sistema protegido por Spring Security",
				"usuario", usuario,
				"rol", rol));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Falta la cabecera Authorization con formato Bearer <TOKEN>");
		}

		String token = authHeader.substring(7);
		blacklistService.invalidarToken(token);

		return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada correctamente. Token invalidado."));
	}

}
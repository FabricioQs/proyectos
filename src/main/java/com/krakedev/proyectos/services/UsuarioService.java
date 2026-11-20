package com.krakedev.proyectos.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public Usuario guardar(Usuario usuario) {
		Optional<Usuario> existente = usuarioRepository.findByUsername(usuario.getUsername());
		if (existente.isPresent()) {
			throw new RuntimeException("El nombre de usuario '" + usuario.getUsername() + "' ya existe.");
		}

		String passwordEncriptada = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
		usuario.setPassword(passwordEncriptada);

		return usuarioRepository.save(usuario);
	}

	public boolean autenticar(String username, String password) {
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		if (usuario.isPresent()) {
			return BCrypt.checkpw(password, usuario.get().getPassword());
		}
		return false;
	}

}
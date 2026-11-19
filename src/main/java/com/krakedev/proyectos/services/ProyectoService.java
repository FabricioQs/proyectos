package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.repositories.ProyectoRepository;

@Service
public class ProyectoService {

	@Autowired
	private ProyectoRepository proyectoRepository;

	public Proyecto crear(Proyecto proyecto) {
		return proyectoRepository.save(proyecto);
	}

	public List<Proyecto> listarTodos() {
		return proyectoRepository.findAll();
	}

	public Optional<Proyecto> buscarPorId(int id) {
		return proyectoRepository.findById(id);
	}

	public Proyecto actualizar(int id, Proyecto proyectoActualizado) {
		Proyecto existente = proyectoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));
		existente.setNombre(proyectoActualizado.getNombre());
		existente.setDescripcion(proyectoActualizado.getDescripcion());
		existente.setFechaInicio(proyectoActualizado.getFechaInicio());
		return proyectoRepository.save(existente);
	}

	public void eliminar(int id) {
		if (!proyectoRepository.existsById(id)) {
			throw new RuntimeException("Proyecto no encontrado con ID: " + id);
		}
		proyectoRepository.deleteById(id);
	}
}
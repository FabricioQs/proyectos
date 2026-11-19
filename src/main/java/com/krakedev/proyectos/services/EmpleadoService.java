package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.repositories.EmpleadoRepository;

@Service
public class EmpleadoService {

	@Autowired
	private EmpleadoRepository empleadoRepository;

	public Empleado crear(Empleado empleado) {
		return empleadoRepository.save(empleado);
	}

	public List<Empleado> listarTodos() {
		return empleadoRepository.findAll();
	}

	public Optional<Empleado> buscarPorId(int id) {
		return empleadoRepository.findById(id);
	}

	public Empleado actualizar(int id, Empleado empleadoActualizado) {
		Empleado existente = empleadoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));
		existente.setNombre(empleadoActualizado.getNombre());
		existente.setCargo(empleadoActualizado.getCargo());
		return empleadoRepository.save(existente);
	}

	public void eliminar(int id) {
		if (!empleadoRepository.existsById(id)) {
			throw new RuntimeException("Empleado no encontrado con ID: " + id);
		}
		empleadoRepository.deleteById(id);
	}
}
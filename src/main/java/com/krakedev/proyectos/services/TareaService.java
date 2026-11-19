package com.krakedev.proyectos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.repositories.EmpleadoRepository;
import com.krakedev.proyectos.repositories.ProyectoRepository;
import com.krakedev.proyectos.repositories.TareaRepository;

@Service
public class TareaService {

	@Autowired
	private TareaRepository tareaRepository;

	@Autowired
	private ProyectoRepository proyectoRepository;

	@Autowired
	private EmpleadoRepository empleadoRepository;

	public Tarea crear(Tarea tarea) {
		if (tarea.getProyecto() == null || tarea.getProyecto().getId() <= 0) {
			throw new RuntimeException("Debe especificar un proyecto válido");
		}
		Proyecto proyecto = proyectoRepository.findById(tarea.getProyecto().getId())
				.orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + tarea.getProyecto().getId()));
		tarea.setProyecto(proyecto);

		if (tarea.getEmpleados() != null && !tarea.getEmpleados().isEmpty()) {
			List<Empleado> empleadosValidados = new ArrayList<>();
			for (Empleado emp : tarea.getEmpleados()) {
				Empleado empleadoDB = empleadoRepository.findById(emp.getId())
						.orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + emp.getId()));
				empleadosValidados.add(empleadoDB);
			}
			tarea.setEmpleados(empleadosValidados);
		}

		return tareaRepository.save(tarea);
	}

	public List<Tarea> listarTodas() {
		return tareaRepository.findAll();
	}

	public Optional<Tarea> buscarPorId(int id) {
		return tareaRepository.findById(id);
	}

	public Tarea actualizar(int id, Tarea tareaActualizada) {
		Tarea existente = tareaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + id));

		existente.setDescripcion(tareaActualizada.getDescripcion());
		existente.setFechaLimite(tareaActualizada.getFechaLimite());
		existente.setCostoEstimado(tareaActualizada.getCostoEstimado());

		if (tareaActualizada.getProyecto() != null && tareaActualizada.getProyecto().getId() > 0) {
			Proyecto proyecto = proyectoRepository.findById(tareaActualizada.getProyecto().getId())
					.orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + tareaActualizada.getProyecto().getId()));
			existente.setProyecto(proyecto);
		}

		if (tareaActualizada.getEmpleados() != null) {
			List<Empleado> empleadosValidados = new ArrayList<>();
			for (Empleado emp : tareaActualizada.getEmpleados()) {
				Empleado empleadoDB = empleadoRepository.findById(emp.getId())
						.orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + emp.getId()));
				empleadosValidados.add(empleadoDB);
			}
			existente.setEmpleados(empleadosValidados);
		}

		return tareaRepository.save(existente);
	}

	public void eliminar(int id) {
		if (!tareaRepository.existsById(id)) {
			throw new RuntimeException("Tarea no encontrada con ID: " + id);
		}
		tareaRepository.deleteById(id);
	}
}
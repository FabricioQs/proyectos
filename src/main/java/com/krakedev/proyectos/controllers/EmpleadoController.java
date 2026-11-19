package com.krakedev.proyectos.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.services.EmpleadoService;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

	@Autowired
	private EmpleadoService empleadoService;

	@PostMapping
	public ResponseEntity<?> crear(@RequestBody Empleado empleado) {
		try {
			Empleado creado = empleadoService.crear(empleado);
			return ResponseEntity.status(HttpStatus.CREATED).body(creado);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al crear el empleado: " + e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<?> listarTodos() {
		try {
			List<Empleado> lista = empleadoService.listarTodos();
			return ResponseEntity.ok(lista);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al listar empleados: " + e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscarPorId(@PathVariable int id) {
		try {
			Optional<Empleado> empleado = empleadoService.buscarPorId(id);
			if (empleado.isPresent()) {
				return ResponseEntity.ok(empleado.get());
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Empleado no encontrado con ID: " + id);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al buscar empleado: " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Empleado empleado) {
		try {
			Empleado actualizado = empleadoService.actualizar(id, empleado);
			return ResponseEntity.ok(actualizado);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al actualizar empleado: " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		try {
			empleadoService.eliminar(id);
			return ResponseEntity.ok("Empleado eliminado correctamente");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al eliminar empleado: " + e.getMessage());
		}
	}
}
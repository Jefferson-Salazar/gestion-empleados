package edu.umg.programacion2;

import edu.umg.programacion2.dao.EmpleadoDAO;
import edu.umg.programacion2.modelo.Empleado;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();

        try {
            System.out.println("=== 1. PROBANDO CREAR EMPLEADO ===");
            Empleado nuevoEmpleado = new Empleado("Maria Lopez", "Recursos Humanos", 4800.00, "2026-03-16", true);
            Empleado empleadoCreado = empleadoDAO.crear(nuevoEmpleado);
            System.out.println("¡Empleado creado con éxito! ID asignado: " + empleadoCreado.getId());

            System.out.println("\n=== 2. PROBANDO BUSCAR POR ID (ID: 1) ===");
            Optional<Empleado> encontrado = empleadoDAO.buscarPorId(1);
            if (encontrado.isPresent()) {
                Empleado e = encontrado.get();
                System.out.println("Encontrado -> ID: " + e.getId() + " | Nombre: " + e.getNombreCompleto() + " | Depto: " + e.getDepartamento());
            } else {
                System.out.println("Empleado no encontrado.");
            }

            System.out.println("\n=== 3. PROBANDO ACTUALIZAR EMPLEADO (ID: 1) ===");
            Empleado empleadoActualizado = new Empleado(1, "Juan Pérez Actualizado", "Sistemas Senior", 6500.00, "2026-03-16", true);
            boolean actualizado = empleadoDAO.actualizar(empleadoActualizado);
            if (actualizado) {
                System.out.println("¡Empleado actualizado correctamente!");
            } else {
                System.out.println("No se pudo actualizar el empleado.");
            }

            System.out.println("\n=== 4. PROBANDO ELIMINAR EMPLEADO (ID creado recientemente) ===");
            boolean eliminado = empleadoDAO.eliminar(empleadoCreado.getId());
            if (eliminado) {
                System.out.println("¡Empleado con ID " + empleadoCreado.getId() + " eliminado correctamente!");
            } else {
                System.out.println("No se pudo eliminar el empleado.");
            }

            System.out.println("\n=== 5. LISTANDO TODOS LOS EMPLEADOS RESTANTES ===");
            List<Empleado> listaEmpleados = empleadoDAO.listarTodos();
            for (Empleado e : listaEmpleados) {
                System.out.println("ID: " + e.getId() + " | Nombre: " + e.getNombreCompleto() + " | Depto: " + e.getDepartamento() + " | Salario: " + e.getSalario());
            }

        } catch (SQLException e) {
            System.out.println("Ocurrió un error en la base de datos: " + e.getMessage());
        }
    }
}
package edu.umg.programacion2.dao;

import edu.umg.programacion2.modelo.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpleadoDAO {
	private final String URL = "jdbc:mysql://localhost:3306/bd_empleados?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASSWORD = "J3ff3rs0n..."; 

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 1. CREATE (Insertar empleado)
    public Empleado crear(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO empleados (nombre_completo, departamento, salario, fecha_contratacion, activo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, empleado.getNombreCompleto());
            stmt.setString(2, empleado.getDepartamento());
            stmt.setDouble(3, empleado.getSalario());
            stmt.setString(4, empleado.getFechaContratacion());
            stmt.setBoolean(5, empleado.isActivo());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    return new Empleado(idGenerado, empleado.getNombreCompleto(), empleado.getDepartamento(), empleado.getSalario(), empleado.getFechaContratacion(), empleado.isActivo());
                }
            }
        }
        throw new SQLException("No se pudo crear el empleado, no se obtuvo ID.");
    }

    // 2. READ (Listar todos)
    public List<Empleado> listarTodos() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT id, nombre_completo, departamento, salario, fecha_contratacion, activo FROM empleados";
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                empleados.add(new Empleado(
                    rs.getInt("id"),
                    rs.getString("nombre_completo"),
                    rs.getString("departamento"),
                    rs.getDouble("salario"),
                    rs.getString("fecha_contratacion"),
                    rs.getBoolean("activo")
                ));
            }
        }
        return empleados;
    }

    // 3. READ (Buscar por ID)
    public Optional<Empleado> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre_completo, departamento, salario, fecha_contratacion, activo FROM empleados WHERE id = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Empleado e = new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre_completo"),
                        rs.getString("departamento"),
                        rs.getDouble("salario"),
                        rs.getString("fecha_contratacion"),
                        rs.getBoolean("activo")
                    );
                    return Optional.of(e);
                }
            }
        }
        return Optional.empty();
    }

    // 4. UPDATE
    public boolean actualizar(Empleado empleado) throws SQLException {
        String sql = "UPDATE empleados SET nombre_completo = ?, departamento = ?, salario = ?, fecha_contratacion = ?, activo = ? WHERE id = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, empleado.getNombreCompleto());
            stmt.setString(2, empleado.getDepartamento());
            stmt.setDouble(3, empleado.getSalario());
            stmt.setString(4, empleado.getFechaContratacion());
            stmt.setBoolean(5, empleado.isActivo());
            stmt.setInt(6, empleado.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    // 5. DELETE
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM empleados WHERE id = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }
}
package edu.umg.programacion2.dao;

import edu.umg.programacion2.modelo.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpleadoDAO {
    // Los datos para conectarnos a la base de datos de MySQL
    private final String URL = "jdbc:mysql://localhost:3306/bd_empleados?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASSWORD = "J3ff3rs0n..."; 

    // Método sencillito para abrir la conexión cada vez que toque hacer algo en la BD
    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 1. Guardar un empleado nuevo en la base de datos
    public Empleado crear(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO empleados (nombre_completo, departamento, salario, fecha_contratacion, activo) VALUES (?, ?, ?, ?, ?)";
        
        // El try-with-resources cierra la conexión solo para que no gaste memoria
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Rellenamos los signos de interrogación con lo que traiga el empleado
            stmt.setString(1, empleado.getNombreCompleto());
            stmt.setString(2, empleado.getDepartamento());
            stmt.setDouble(3, empleado.getSalario());
            stmt.setString(4, empleado.getFechaContratacion());
            stmt.setBoolean(5, empleado.isActivo());
            stmt.setString(6, empleado.getTipoContrato());
            
            // Ejecutamos el comando de guardar
            stmt.executeUpdate();
            
            // Pedimos el ID que MySQL le asignó automáticamente para regresarlo completo
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    return new Empleado(idGenerado, empleado.getNombreCompleto(), empleado.getDepartamento(), empleado.getSalario(), empleado.getFechaContratacion(), empleado.isActivo(),empleado.getTipoContrato());
                }
            }
        }
        throw new SQLException("No se pudo crear el empleado, no se obtuvo ID.");
    }

    // 2. Traer a todos los empleados de la tabla en una lista
    public List<Empleado> listarTodos() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT id, nombre_completo, departamento, salario, fecha_contratacion, activo FROM empleados";
        
        // Consultamos y recorremos fila por fila el resultado que nos devuelve la base
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
                  rs.getBoolean("activo"),
                  rs.getString("tipo_contrato")
              ));
          }
      }
      return empleados;
    }

    // 3. Buscar un empleado específico por su ID
    public Optional<Empleado> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre_completo, departamento, salario, fecha_contratacion, activo FROM empleados WHERE id = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            // Si lo encuentra lo armamos en un objeto, si no, devolvemos vacío con Optional
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Empleado e = new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre_completo"),
                        rs.getString("departamento"),
                        rs.getDouble("salario"),
                        rs.getString("fecha_contratacion"),
                        rs.getBoolean("activo"),
                        rs.getString("tipo_contrato")
                    );
                    return Optional.of(e);
                }
            }
        }
        return Optional.empty();
    }

    // 4. Actualizar los datos de un empleado que ya existe usando su ID
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
            
            // Si esto es mayor a 0 significa que sí se actualizó el registro
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    // 5. Borrar un empleado de la base de datos por su ID
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM empleados WHERE id = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0; // Regresa true si se borró con éxito
        }
    }
}
package edu.umg.programacion2.proyecto.ui;

import edu.umg.programacion2.modelo.Empleado;
import edu.umg.programacion2.dao.EmpleadoDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FormularioEmpleado extends JFrame {
    private JTextField txtNombre;
    private JComboBox<String> cmbDepartamento;
    private JTextField txtSalario;
    private JTextField txtFecha;
    private JCheckBox chkActivo;
    private JButton btnGuardar;
    
    private EmpleadoDAO empleadoDAO;
    private VentanaPrincipal parentVentana;
    private Empleado empleadoEditando;

    public FormularioEmpleado(VentanaPrincipal parent, EmpleadoDAO dao, Empleado empleado) {
        super(empleado == null ? "Registrar Nuevo Empleado" : "Editar Empleado");
        this.parentVentana = parent;
        this.empleadoDAO = dao;
        this.empleadoEditando = empleado;

        setSize(420, 380);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));

        // Componentes del formulario
        add(new JLabel(" Nombre Completo:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel(" Departamento:"));
        String[] departamentos = {"Ventas", "Sistemas", "Contabilidad", "Recursos Humanos", "Administración"};
        cmbDepartamento = new JComboBox<>(departamentos);
        add(cmbDepartamento);

        add(new JLabel(" Salario Mensual (Q):"));
        txtSalario = new JTextField();
        add(txtSalario);

        add(new JLabel(" Contratación (YYYY-MM-DD):"));
        txtFecha = new JTextField();
        add(txtFecha);

        add(new JLabel(" ¿Activo?"));
        chkActivo = new JCheckBox();
        chkActivo.setSelected(true);
        add(chkActivo);

        btnGuardar = new JButton("Guardar");
        add(new JLabel()); // Espacio vacío para alinear
        add(btnGuardar);

        // Si estamos editando, rellenamos los campos y seleccionamos su departamento
        if (empleadoEditando != null) {
            txtNombre.setText(empleadoEditando.getNombreCompleto());
            cmbDepartamento.setSelectedItem(empleadoEditando.getDepartamento());
            txtSalario.setText(String.valueOf(empleadoEditando.getSalario()));
            txtFecha.setText(empleadoEditando.getFechaContratacion());
            chkActivo.setSelected(empleadoEditando.isActivo());
        }

        // Evento del botón Guardar
        btnGuardar.addActionListener(e -> guardarDatos());
    }

    private void guardarDatos() {
        try {
            String nombre = txtNombre.getText().trim();
            String depto = (String) cmbDepartamento.getSelectedItem();
            String salarioStr = txtSalario.getText().trim();
            String fechaStr = txtFecha.getText().trim();
            boolean activo = chkActivo.isSelected();

            // 1. Validaciones previas
            if (nombre.isEmpty() || salarioStr.isEmpty() || fechaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double salario;
            try {
                salario = Double.parseDouble(salarioStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El salario debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (salario <= 0) {
                JOptionPane.showMessageDialog(this, "El salario debe ser mayor a cero.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate fechaContratacion;
            try {
                fechaContratacion = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validar que no sea una fecha futura
            if (fechaContratacion.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "La fecha de contratación no puede ser una fecha futura.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validar que el año sea lógico (del año 2000 en adelante)
            if (fechaContratacion.getYear() < 2000) {
                JOptionPane.showMessageDialog(this, "El año de contratación no es válido. Debe ser del año 2000 en adelante.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Operación contra el DAO
            if (empleadoEditando == null) {
                Empleado nuevo = new Empleado(nombre, depto, salario, fechaStr, activo);
                empleadoDAO.crear(nuevo);
                JOptionPane.showMessageDialog(this, "Empleado registrado exitosamente.");
            } else {
                empleadoEditando = new Empleado(empleadoEditando.getId(), nombre, depto, salario, fechaStr, activo);
                empleadoDAO.actualizar(empleadoEditando);
                JOptionPane.showMessageDialog(this, "Empleado actualizado correctamente.");
            }

            parentVentana.cargarEmpleadosDesdeBD();
            dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
}
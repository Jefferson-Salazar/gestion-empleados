package edu.umg.programacion2.proyecto.ui;

import edu.umg.programacion2.modelo.Empleado;
import edu.umg.programacion2.dao.EmpleadoDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private JComboBox<String> cmbTipoContrato; // <-- Declarado aquí
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

        setSize(450, 450); // Ajustado un poco más de alto para el nuevo campo
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Panel principal con borde y color de fondo oscuro (Cambiado a 7 filas para acomodar el nuevo combo)
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(7, 2, 10, 15));
        panelPrincipal.setBackground(new Color(17, 24, 39)); // Mismo fondo que la tabla
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(panelPrincipal);

        // Nombre
        JLabel lblNombre = crearEtiquetaBlanca(" Nombre Completo:");
        panelPrincipal.add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBackground(new Color(31, 41, 55));
        txtNombre.setForeground(Color.WHITE);
        txtNombre.setCaretColor(Color.WHITE);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 65, 81)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panelPrincipal.add(txtNombre);

        // Departamento
        JLabel lblDepto = crearEtiquetaBlanca(" Departamento:");
        panelPrincipal.add(lblDepto);
        String[] departamentos = {"Ventas", "Sistemas", "Contabilidad", "Recursos Humanos", "Administración"};
        cmbDepartamento = new JComboBox<>(departamentos);
        cmbDepartamento.setEditable(true); 
        cmbDepartamento.setBackground(new Color(31, 41, 55));
        cmbDepartamento.setForeground(Color.WHITE);
        panelPrincipal.add(cmbDepartamento);

        // Salario
        JLabel lblSalario = crearEtiquetaBlanca(" Salario Mensual (Q):");
        panelPrincipal.add(lblSalario);
        txtSalario = new JTextField();
        txtSalario.setBackground(new Color(31, 41, 55));
        txtSalario.setForeground(Color.WHITE);
        txtSalario.setCaretColor(Color.WHITE);
        txtSalario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 65, 81)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panelPrincipal.add(txtSalario);

        // Fecha
        JLabel lblFecha = crearEtiquetaBlanca(" Contratación (YYYY-MM-DD):");
        panelPrincipal.add(lblFecha);
        txtFecha = new JTextField();
        txtFecha.setBackground(new Color(31, 41, 55));
        txtFecha.setForeground(Color.WHITE);
        txtFecha.setCaretColor(Color.WHITE);
        txtFecha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 65, 81)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panelPrincipal.add(txtFecha);

        // === NUEVO: Tipo de Contrato (Selección Fija) ===
        JLabel lblContrato = crearEtiquetaBlanca(" Tipo de Contrato:");
        panelPrincipal.add(lblContrato);
        String[] tiposContrato = {"Temporal", "Permanente", "Por hora"};
        cmbTipoContrato = new JComboBox<>(tiposContrato);
        cmbTipoContrato.setBackground(new Color(31, 41, 55));
        cmbTipoContrato.setForeground(Color.WHITE);
        panelPrincipal.add(cmbTipoContrato);

        // Activo
        JLabel lblActivo = crearEtiquetaBlanca(" ¿Activo?");
        panelPrincipal.add(lblActivo);
        chkActivo = new JCheckBox();
        chkActivo.setSelected(true);
        chkActivo.setBackground(new Color(17, 24, 39)); 
        chkActivo.setForeground(Color.WHITE);
        panelPrincipal.add(chkActivo);

        // Estilo del botón Guardar 
        btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setBackground(new Color(16, 185, 129));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 25), 1),
                BorderFactory.createEmptyBorder(10, 22, 10, 22)
        ));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panelPrincipal.add(new JLabel()); // Espacio vacío para alinear
        panelPrincipal.add(btnGuardar);

        // Si estamos editando, rellenamos los campos (incluyendo el tipo de contrato)
        if (empleadoEditando != null) {
            txtNombre.setText(empleadoEditando.getNombreCompleto());
            cmbDepartamento.setSelectedItem(empleadoEditando.getDepartamento());
            txtSalario.setText(String.valueOf(empleadoEditando.getSalario()));
            txtFecha.setText(empleadoEditando.getFechaContratacion());
            chkActivo.setSelected(empleadoEditando.isActivo());
            cmbTipoContrato.setSelectedItem(empleadoEditando.getTipoContrato()); // <-- Carga la opción guardada
        }

        // Evento del botón Guardar
        btnGuardar.addActionListener(e -> guardarDatos());
    }

    private JLabel crearEtiquetaBlanca(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(new Color(229, 231, 235));
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return label;
    }

    private void guardarDatos() {
        try {
            String nombre = txtNombre.getText().trim();
            String depto = cmbDepartamento.getSelectedItem() != null ? cmbDepartamento.getSelectedItem().toString().trim() : "";
            String salarioStr = txtSalario.getText().trim();
            String fechaStr = txtFecha.getText().trim();
            boolean activo = chkActivo.isSelected();
            
            // Obtener y validar el tipo de contrato seleccionado
            String tipoContrato = cmbTipoContrato.getSelectedItem() != null ? cmbTipoContrato.getSelectedItem().toString() : "";

            // 1. Validaciones generales
            if (nombre.isEmpty() || depto.isEmpty() || salarioStr.isEmpty() || fechaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validar que se haya seleccionado un tipo de contrato
            if (tipoContrato.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de contrato válido.", "Validación", JOptionPane.WARNING_MESSAGE);
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

            if (fechaContratacion.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "La fecha de contratación no puede ser una fecha futura.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (fechaContratacion.getYear() < 2000) {
                JOptionPane.showMessageDialog(this, "El año de contratación no es válido. Debe ser del año 2000 en adelante.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Operación contra el DAO (pasando el tipoContrato al final)
            if (empleadoEditando == null) {
                Empleado nuevo = new Empleado(nombre, depto, salario, fechaStr, activo, tipoContrato);
                empleadoDAO.crear(nuevo);
                JOptionPane.showMessageDialog(this, "Empleado registrado exitosamente.");
            } else {
                empleadoEditando = new Empleado(empleadoEditando.getId(), nombre, depto, salario, fechaStr, activo, tipoContrato);
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
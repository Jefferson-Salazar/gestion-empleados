package edu.umg.programacion2.proyecto.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private JPanel contentPane;
    private JTable tableEmpleados;
    private DefaultTableModel tableModel;

    public VentanaPrincipal() {
        // Configuración de la ventana principal
        setTitle("Gestión de Empleados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1050, 650);
        setLocationRelativeTo(null);

        // Panel principal 
        contentPane = new JPanel();
        contentPane.setBackground(new Color(11, 15, 25));
        contentPane.setBorder(new EmptyBorder(24, 24, 24, 24));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // ENCABEZADO 
        JPanel panelNorte = new JPanel();
        panelNorte.setBackground(new Color(11, 15, 25));
        panelNorte.setBorder(new EmptyBorder(0, 0, 16, 0)); 
        contentPane.add(panelNorte, BorderLayout.NORTH);
        panelNorte.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        JLabel lblTitulo = new JLabel("LISTADO DE EMPLEADOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(241, 245, 249)); 
        panelNorte.add(lblTitulo);

        // TABLA
        String[] columnas = {"ID", "Nombre Completo", "Departamento", "Salario (Q)", "Contratación", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        tableEmpleados = new JTable(tableModel);
        tableEmpleados.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableEmpleados.setRowHeight(34);
        tableEmpleados.setBackground(new Color(17, 24, 39)); 
        tableEmpleados.setForeground(new Color(229, 231, 235)); 
        tableEmpleados.setSelectionBackground(new Color(31, 41, 55)); 
        tableEmpleados.setSelectionForeground(Color.WHITE);
        tableEmpleados.setGridColor(new Color(31, 41, 55));

        // Estilo de la cabecera de la tabla
        tableEmpleados.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableEmpleados.getTableHeader().setBackground(new Color(17, 24, 39));
        tableEmpleados.getTableHeader().setForeground(new Color(156, 163, 175));
        tableEmpleados.getTableHeader().setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(tableEmpleados);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(31, 41, 55)));
        scrollPane.getViewport().setBackground(new Color(17, 24, 39));
        contentPane.add(scrollPane, BorderLayout.CENTER);

        //BOTONES 
        JPanel panelSur = new JPanel();
        panelSur.setBackground(new Color(11, 15, 25));
        panelSur.setBorder(new EmptyBorder(18, 0, 0, 0));
        contentPane.add(panelSur, BorderLayout.SOUTH);
        panelSur.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0)); 

        
        JButton btnNuevo = crearBotonEstilizado("Nuevo", new Color(16, 185, 129), new Color(52, 211, 153));     
        JButton btnEditar = crearBotonEstilizado("Editar", new Color(14, 165, 233), new Color(56, 189, 248));     
        JButton btnEliminar = crearBotonEstilizado("Eliminar", new Color(225, 29, 72), new Color(244, 63, 94));   
        JButton btnActualizar = crearBotonEstilizado("Actualizar", new Color(79, 70, 229), new Color(99, 102, 241)); 

        panelSur.add(btnNuevo);
        panelSur.add(btnEditar);
        panelSur.add(btnEliminar);
        panelSur.add(btnActualizar);

        // Acción temporal para probar los datos
        btnActualizar.addActionListener(e -> cargarDatosPrueba());
        
        // Cargar datos iniciales
        cargarDatosPrueba();
    }

    private JButton crearBotonEstilizado(String texto, Color colorFondo, Color colorHover) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
     
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 25), 1),
            BorderFactory.createEmptyBorder(10, 22, 10, 22)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
     
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorHover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo);
            }
        });
        
        return boton;
    }

    private void cargarDatosPrueba() {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{1, "Ana Lucía Pérez", "Sistemas", "8500.00", "2024-03-15", "Activo"});
        tableModel.addRow(new Object[]{2, "Carlos Roberto Mux", "Ventas", "6200.00", "2023-11-01", "Activo"});
        tableModel.addRow(new Object[]{3, "Diana Sofía Cabrera", "Contabilidad", "7100.00", "2022-06-10", "Inactivo"});
    }
}
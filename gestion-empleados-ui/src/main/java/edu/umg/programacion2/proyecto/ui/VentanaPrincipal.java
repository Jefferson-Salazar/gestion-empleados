package edu.umg.programacion2.proyecto.ui;

import edu.umg.programacion2.modelo.Empleado;
import edu.umg.programacion2.dao.EmpleadoDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private JPanel contentPane;
    private JTable tableEmpleados;
    private DefaultTableModel tableModel;
    
    // Instancia del DAO para comunicarnos con la base de datos
    private EmpleadoDAO empleadoDAO;

    public VentanaPrincipal() {
        empleadoDAO = new EmpleadoDAO();

        // Configuración inicial de la ventana
        setTitle("Gestión de Empleados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1050, 650);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

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

        // TABLA: Definimos columnas y evitamos que se puedan editar directamente haciendo clic en las celdas
        String[] columnas = {"ID", "Nombre Completo", "Departamento", "Salario (Q)", "Contratación", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        // Estilos visuales de la tabla 
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

        // Metemos la tabla dentro de un ScrollPane por si la lista es muy larga
        JScrollPane scrollPane = new JScrollPane(tableEmpleados);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(31, 41, 55)));
        scrollPane.getViewport().setBackground(new Color(17, 24, 39));
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // BOTONES (Panel inferior)
        JPanel panelSur = new JPanel();
        panelSur.setBackground(new Color(11, 15, 25));
        panelSur.setBorder(new EmptyBorder(18, 0, 0, 0));
        contentPane.add(panelSur, BorderLayout.SOUTH);
        panelSur.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0)); 

        JButton btnNuevo = crearBotonEstilizado("Nuevo", new Color(16, 185, 129), new Color(52, 211, 153));     
        JButton btnEditar = crearBotonEstilizado("Editar", new Color(14, 165, 233), new Color(56, 189, 248));     
        JButton btnEliminar = crearBotonEstilizado("Eliminar", new Color(225, 29, 72), new Color(244, 63, 94));    

        panelSur.add(btnNuevo);
        panelSur.add(btnEditar);
        panelSur.add(btnEliminar);

        // ACCIONES DE LOS BOTONES

        // 1. Botón Nuevo: Abre el formulario vacío para registrar un empleado
        btnNuevo.addActionListener(e -> {
            FormularioEmpleado form = new FormularioEmpleado(this, empleadoDAO, null);
            form.setVisible(true);
        });

        // 2. Botón Editar: Saca los datos de la fila seleccionada y los pasa al formulario
        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tableEmpleados.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor seleccione un empleado de la tabla para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Capturamos los valores de cada columna de la fila seleccionada
            int id = (int) tableModel.getValueAt(filaSeleccionada, 0);
            String nombre = (String) tableModel.getValueAt(filaSeleccionada, 1);
            String depto = (String) tableModel.getValueAt(filaSeleccionada, 2);
            double salario = Double.parseDouble(tableModel.getValueAt(filaSeleccionada, 3).toString());
            String fecha = (String) tableModel.getValueAt(filaSeleccionada, 4);
            boolean activo = tableModel.getValueAt(filaSeleccionada, 5).toString().equals("Activo");

            // Creamos el objeto con esos datos
            Empleado empSeleccionado = new Empleado(id, nombre, depto, salario, fecha, activo);

            // Abrimos el formulario pero pasándole el empleado (modo edición)
            FormularioEmpleado form = new FormularioEmpleado(this, empleadoDAO, empSeleccionado);
            form.setVisible(true);
        });

        // 3. Botón Eliminar: Pide confirmación y borra el registro usando el DAO
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tableEmpleados.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor seleccione un empleado para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(
                this, 
                "¿Está seguro de eliminar permanentemente a este empleado?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    int idEmpleado = (int) tableModel.getValueAt(filaSeleccionada, 0);
                    
                    // Llamamos al método eliminar del DAO
                    boolean eliminado = empleadoDAO.eliminar(idEmpleado);
                    
                    if (eliminado) {
                        JOptionPane.showMessageDialog(this, "Empleado eliminado correctamente.");
                        cargarEmpleadosDesdeBD(); // Recargamos la tabla para que se note el cambio
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo encontrar el registro a eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error en la base de datos al intentar eliminar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Cargamos los datos apenas arranca la ventana
        cargarEmpleadosDesdeBD();
    }

    // Método auxiliar para darle diseño bonito a los botones
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
        
        // Cambia de color cuando pasas el mouse encima
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

    // Método para traer todos los registros de MySQL y rellenar la tabla de la interfaz
    public void cargarEmpleadosDesdeBD() {
        tableModel.setRowCount(0); // Limpiamos la tabla primero para evitar duplicados
        try {
            List<Empleado> lista = empleadoDAO.listarTodos();
            for (Empleado emp : lista) {
                tableModel.addRow(new Object[]{
                    emp.getId(), 
                    emp.getNombreCompleto(), 
                    emp.getDepartamento(), 
                    emp.getSalario(), 
                    emp.getFechaContratacion(), 
                    emp.isActivo() ? "Activo" : "Inactivo" // Convertimos el booleano a texto legible
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar con la base de datos para listar los empleados.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    }
}
package edu.umg.programacion2.proyecto;

import edu.umg.programacion2.proyecto.ui.VentanaPrincipal;

import javax.swing.*;

public class MainUI {
    public static void main(String[] args) {
        
        // Usamos esto para asegurarnos de que la interfaz corra de forma segura en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Creamos y mostramos la ventana principal de la aplicación
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setVisible(true);
            } catch (Exception e) {
                // Si algo falla al arrancar, mostramos una alerta en lugar de que truene feo el programa
                JOptionPane.showMessageDialog(null, 
                    "Error al iniciar la aplicación: " + e.getMessage(), 
                    "Error crítico", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
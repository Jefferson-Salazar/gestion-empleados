package edu.umg.programacion2.proyecto;

import edu.umg.programacion2.proyecto.ui.VentanaPrincipal;

import javax.swing.*;

public class MainUI {
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            try {
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error al iniciar la aplicación: " + e.getMessage(), 
                    "Error crítico", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
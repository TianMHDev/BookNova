/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.ui;

import com.libronova.service.ExportService;

import javax.swing.*;

/**
 * Menu for export operations (CSV).
 */
public class ExportMenu {

    private final ExportService exportService = new ExportService();

    public void showMenu() {
        while (true) {
            String option = JOptionPane.showInputDialog("""
                === Exportaciones ===
                1. Exportar préstamos vencidos a CSV
                2. Exportar catálogo de libros a CSV
                0. Volver
            """);

            if (option == null || option.equals("0")) break;

            switch (option) {
                case "1" -> {
                    String path = JOptionPane.showInputDialog("Ruta de salida (ej: prestamos_vencidos.csv):");
                    if (path == null || path.isBlank()) {
                        JOptionPane.showMessageDialog(null, "Ruta no válida.");
                        break;
                    }
                    exportService.exportOverdueLoansToCsv(path);
                }
                case "2" -> {
                    String path = JOptionPane.showInputDialog("Ruta de salida (ej: libros_export.csv):");
                    if (path == null || path.isBlank()) {
                        JOptionPane.showMessageDialog(null, "Ruta no válida.");
                        break;
                    }
                    exportService.exportBooksToCsv(path);
                }
                default -> JOptionPane.showMessageDialog(null, "Opción no válida.");
            }
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.ui;

import com.libronova.model.User;

import javax.swing.*;

/**
 * Main system menu using JOptionPane.
 * Starts only after successful login.
 */
public class MenuPrincipal {

    private final User currentUser;

    public MenuPrincipal(User user) {
        this.currentUser = user;
    }

    public void mostrarMenu() {
        while (true) {
            String opcion = JOptionPane.showInputDialog("""
                === Sistema de Gestión Biblioteca LibroNova ===
                Usuario: %s (%s)
                
                Seleccione un módulo:
                1. Catálogo de Libros
                2. Socios
                3. Usuarios
                4. Préstamos
                5. Exportaciones
                0. Salir
            """.formatted(currentUser.getUsername(), currentUser.getRole()));

            if (opcion == null || opcion.equals("0")) {
                JOptionPane.showMessageDialog(null, "Cerrando sesión...");
                break;
            }

            switch (opcion) {
                case "1" -> new BookMenu().showMenu();
                case "2" -> new MemberMenu().showMenu();
                case "3" -> {
                    if (currentUser.getRole().equalsIgnoreCase("ADMIN")) {
                        new UserMenu().showMenu();
                    } else {
                        JOptionPane.showMessageDialog(null, "Solo los administradores pueden acceder a este módulo.");
                    }
                }
                case "4" -> new LoanMenu().showMenu();
                case "5" -> new ExportMenu().showMenu();
                default -> JOptionPane.showMessageDialog(null, "Opción no válida.");
            }
        }
    }
}

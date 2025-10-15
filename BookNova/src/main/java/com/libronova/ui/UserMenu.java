/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.ui;

import com.libronova.model.User;
import com.libronova.service.UserService;

import javax.swing.*;

/**
 * Handles user login and management menu.
 */
public class UserMenu {

    private final UserService userService = new UserService();

    /**
     * Login screen using JOptionPane.
     * Returns User if login successful, null otherwise.
     */
    public User login() {
        while (true) {
            String username = JOptionPane.showInputDialog("Ingrese su nombre de usuario:");
            if (username == null) return null;

            String password = JOptionPane.showInputDialog("Ingrese su contraseña:");
            if (password == null) return null;

            User user = userService.authenticate(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(null, "Bienvenido, " + user.getUsername() + " (" + user.getRole() + ")");
                return user;
            } else {
                int retry = JOptionPane.showConfirmDialog(null,
                        "Credenciales incorrectas. ¿Desea intentar de nuevo?",
                        "Error de autenticación",
                        JOptionPane.YES_NO_OPTION);
                if (retry == JOptionPane.NO_OPTION) return null;
            }
        }
    }

    /**
     * Shows user management options (for ADMIN users).
     */
    public void showMenu() {
        while (true) {
            String option = JOptionPane.showInputDialog("""
                === Gestión de Usuarios ===
                1. Registrar nuevo usuario
                2. Listar usuarios
                0. Volver
            """);

            if (option == null || option.equals("0")) break;

            switch (option) {
                case "1" -> createUser();
                case "2" -> listUsers();
                default -> JOptionPane.showMessageDialog(null, "Opción no válida. Intente nuevamente.");
            }
        }
    }

    private void createUser() {
        try {
            String username = JOptionPane.showInputDialog("Ingrese nombre de usuario:");
            if (username == null || username.isBlank()) return;

            String password = JOptionPane.showInputDialog("Ingrese contraseña:");
            if (password == null || password.isBlank()) return;

            userService.createUser(username, password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al crear usuario: " + e.getMessage());
        }
    }

    private void listUsers() {
        userService.listUsers();
    }
}

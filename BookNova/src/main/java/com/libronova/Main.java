/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.libronova;

import com.libronova.model.User;
import com.libronova.ui.MenuPrincipal;
import com.libronova.ui.UserMenu;

import javax.swing.*;

/**
 * Entry point for the LibroNova system.
 * Starts with login, then opens main menu.
 */
public class Main {
    public static void main(String[] args) {
        UserMenu loginMenu = new UserMenu();
        User usuario = loginMenu.login();

        if (usuario != null) {
            // Pass logged-in user to main menu
            new MenuPrincipal(usuario).mostrarMenu();
        } else {
            JOptionPane.showMessageDialog(null, "Inicio de sesión cancelado. Cerrando aplicación.");
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.service;

import com.libronova.dao.IUserDao;
import com.libronova.dao.decorator.UserDaoDecorator;
import com.libronova.dao.impl.UserDaoImpl;
import com.libronova.helpers.MessageHelper;
import com.libronova.helpers.TableFormatter;
import com.libronova.model.User;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Business logic for user management and authentication.
 */
public class UserService {

    private final IUserDao userDao;

    public UserService() {
        // Wrap the implementation with the decorator
        this.userDao = new UserDaoDecorator(new UserDaoImpl());
    }

    /**
     * Creates a new user.
     */
    public void createUser(String username, String password) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            userDao.create(user);
            MessageHelper.showInfo("Usuario creado correctamente.");
        } catch (SQLException e) {
            MessageHelper.showError("Error al crear usuario: " + e.getMessage());
        }
    }

    /**
     * Authenticates user by username and password.
     */
    public User authenticate(String username, String password) {
        try {
            return userDao.findByUsernameAndPassword(username, password);
        } catch (SQLException e) {
            MessageHelper.showError("Error de autenticación: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lists all users.
     */
    public void listUsers() {
        try {
            List<User> users = userDao.findAll();
            if (users.isEmpty()) {
                MessageHelper.showWarning("No hay usuarios registrados.");
                return;
            }

            List<Object[]> rows = new ArrayList<>();
            for (User u : users) {
                rows.add(new Object[]{
                        u.getId(),
                        u.getUsername(),
                        u.getRole(),
                        u.getEstado(),
                        u.getCreatedAt()
                });
            }

            String table = TableFormatter.formatTable(rows,
                    new String[]{"ID", "Usuario", "Rol", "Estado", "Creado"});
            JOptionPane.showMessageDialog(null, table, "Usuarios Registrados", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            MessageHelper.showError("Error al listar usuarios: " + e.getMessage());
        }
    }
}

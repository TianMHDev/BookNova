/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.dao.decorator;

import com.libronova.dao.IUserDao;
import com.libronova.model.User;

import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Decorator that adds default properties to a user before creation.
 */
public class UserDaoDecorator implements IUserDao {

    private final IUserDao innerDao;

    public UserDaoDecorator(IUserDao dao) {
        this.innerDao = dao;
    }

    @Override
    public void create(User user) throws SQLException {
        // Default properties
        if (user.getRole() == null) user.setRole("ASISTENTE");
        if (user.getEstado() == null) user.setEstado("ACTIVO");
        if (user.getCreatedAt() == null) user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        innerDao.create(user);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) throws SQLException {
        return innerDao.findByUsernameAndPassword(username, password);
    }

    @Override
    public java.util.List<User> findAll() throws SQLException {
        return innerDao.findAll();
    }
}

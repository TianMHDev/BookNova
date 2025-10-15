/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.libronova.dao;

import com.libronova.model.User;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO interface for User entity.
 */
public interface IUserDao {
    void create(User user) throws SQLException;
    User findByUsernameAndPassword(String username, String password) throws SQLException;
    List<User> findAll() throws SQLException;
}

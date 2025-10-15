/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.libronova.dao;

import com.libronova.model.Book;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Book entity.
 */
public interface IBookDao {
    void create(Book book) throws SQLException;
    void update(Book book) throws SQLException;
    Book findByIsbn(String isbn) throws SQLException;
    List<Book> findAll() throws SQLException;
    List<Book> findByCategoryOrAuthor(String criterio) throws SQLException;
}

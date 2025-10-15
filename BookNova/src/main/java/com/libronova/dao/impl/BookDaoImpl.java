/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.dao.impl;

import com.libronova.dao.IBookDao;
import com.libronova.config.DbConnection;
import com.libronova.exceptions.AppLogger;
import com.libronova.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * JDBC implementation of IBookDao with validation for unique ISBN.
 */
public class BookDaoImpl implements IBookDao {

    private static final Logger LOGGER = AppLogger.getLogger();

    @Override
    public void create(Book book) throws SQLException {
        String sql = """
            INSERT INTO books (isbn, titulo, autor, categoria, ejemplares_totales,
            ejemplares_disponibles, precio_referencia, is_activo, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())
        """;

        // --- Validate unique ISBN before insert ---
        if (findByIsbn(book.getIsbn()) != null) {
            throw new SQLException("Ya existe un libro con el mismo ISBN.");
        }

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitulo());
            stmt.setString(3, book.getAutor());
            stmt.setString(4, book.getCategoria());
            stmt.setInt(5, book.getEjemplaresTotales());
            stmt.setInt(6, book.getEjemplaresDisponibles());
            stmt.setBigDecimal(7, book.getPrecioReferencia());
            stmt.setBoolean(8, book.isActivo());

            stmt.executeUpdate();
            LOGGER.info("[POST] Libro registrado exitosamente (ISBN: " + book.getIsbn() + ")");
        }
    }

    @Override
    public void update(Book book) throws SQLException {
        String sql = """
            UPDATE books SET titulo=?, autor=?, categoria=?, ejemplares_totales=?, 
            ejemplares_disponibles=?, precio_referencia=?, is_activo=? WHERE isbn=?
        """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitulo());
            stmt.setString(2, book.getAutor());
            stmt.setString(3, book.getCategoria());
            stmt.setInt(4, book.getEjemplaresTotales());
            stmt.setInt(5, book.getEjemplaresDisponibles());
            stmt.setBigDecimal(6, book.getPrecioReferencia());
            stmt.setBoolean(7, book.isActivo());
            stmt.setString(8, book.getIsbn());

            int updated = stmt.executeUpdate();
            if (updated == 0)
                throw new SQLException("No se encontró el libro para actualizar.");

            LOGGER.info("[PATCH] Libro actualizado (ISBN: " + book.getIsbn() + ")");
        }
    }

    @Override
    public Book findByIsbn(String isbn) throws SQLException {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToBook(rs);
            }
        }
        return null;
    }

    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY created_at DESC";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToBook(rs));
            }
        }
        return list;
    }

    @Override
    public List<Book> findByCategoryOrAuthor(String criterio) throws SQLException {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE categoria LIKE ? OR autor LIKE ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + criterio + "%");
            stmt.setString(2, "%" + criterio + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapResultSetToBook(rs));
            }
        }
        return list;
    }

    // --- Utility mapper ---
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setId(rs.getInt("id"));
        b.setIsbn(rs.getString("isbn"));
        b.setTitulo(rs.getString("titulo"));
        b.setAutor(rs.getString("autor"));
        b.setCategoria(rs.getString("categoria"));
        b.setEjemplaresTotales(rs.getInt("ejemplares_totales"));
        b.setEjemplaresDisponibles(rs.getInt("ejemplares_disponibles"));
        b.setPrecioReferencia(rs.getBigDecimal("precio_referencia"));
        b.setActivo(rs.getBoolean("is_activo"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        return b;
    }
}

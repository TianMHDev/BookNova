/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.service;

import com.libronova.dao.IBookDao;
import com.libronova.dao.impl.BookDaoImpl;
import com.libronova.exceptions.BusinessException;
import com.libronova.exceptions.ValidationException;
import com.libronova.model.Book;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Business layer for managing books.
 * Validates business rules and delegates persistence to DAO.
 */
public class BookService {

    private final IBookDao bookDao = new BookDaoImpl();

    /**
     * Creates a new book with validation rules.
     * Throws exceptions for testing and error handling.
     */
    public void createBook(Book book) throws ValidationException, BusinessException {
        // Validation 1: ISBN cannot be empty
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new ValidationException("El ISBN no puede estar vacío.");
        }

        // Validation 2: Stock must be non-negative
        if (book.getEjemplaresTotales() < 0 || book.getEjemplaresDisponibles() < 0) {
            throw new ValidationException("Los ejemplares no pueden ser negativos.");
        }

        try {
            // Validation 3: Unique ISBN
            if (bookDao.findByIsbn(book.getIsbn()) != null) {
                throw new BusinessException("Ya existe un libro con ese ISBN.");
            }

            // Save to database
            bookDao.create(book);

            // Show message only in runtime (not used by tests)
            JOptionPane.showMessageDialog(null, "Libro registrado exitosamente.");
        } catch (SQLException e) {
            throw new BusinessException("Error al registrar el libro: " + e.getMessage());
        }
    }

    // Update existing book
    public void updateBook(Book book) {
        try {
            bookDao.update(book);
            JOptionPane.showMessageDialog(null, "Libro actualizado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el libro: " + e.getMessage());
        }
    }

    // List all books
    public void listAllBooks() {
        try {
            List<Book> books = bookDao.findAll();
            showBooksTable(books, "Listado de libros");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar los libros: " + e.getMessage());
        }
    }

    // Filter by author or category
    public void filterBooks(String criterio) {
        try {
            List<Book> books = bookDao.findByCategoryOrAuthor(criterio);
            showBooksTable(books, "Resultados para: " + criterio);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al filtrar los libros: " + e.getMessage());
        }
    }

    // Helper for displaying formatted list
    private void showBooksTable(List<Book> books, String title) {
        if (books == null || books.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay resultados.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s %-25s %-20s %-15s %-10s %-10s %-8s%n",
                "ISBN", "Título", "Autor", "Categoría", "Totales", "Disp.", "Estado"));
        sb.append("--------------------------------------------------------------------------\n");

        for (Book b : books) {
            sb.append(String.format("%-15s %-25s %-20s %-15s %-10d %-10d %-8s%n",
                    b.getIsbn(), b.getTitulo(), b.getAutor(), b.getCategoria(),
                    b.getEjemplaresTotales(), b.getEjemplaresDisponibles(),
                    b.isActivo() ? "ACTIVO" : "INACTIVO"));
        }

        JOptionPane.showMessageDialog(null, sb.toString(), title, JOptionPane.INFORMATION_MESSAGE);
    }
}

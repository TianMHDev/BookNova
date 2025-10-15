/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.ui;

import com.libronova.model.Book;
import com.libronova.service.BookService;

import javax.swing.*;
import java.math.BigDecimal;

/**
 * Console-like menu using JOptionPane for book management.
 */
public class BookMenu {
    private final BookService bookService = new BookService();

    public void showMenu() {
        while (true) {
            String option = JOptionPane.showInputDialog("""
                === 📚 Gestión de Libros ===
                1. Registrar nuevo libro
                2. Editar libro existente
                3. Listar todos los libros
                4. Filtrar por autor o categoría
                0. Volver al menú principal
            """);

            if (option == null || option.equals("0")) break;

            switch (option) {
                case "1" -> createBook();
                case "2" -> updateBook();
                case "3" -> bookService.listAllBooks();
                case "4" -> {
                    String crit = JOptionPane.showInputDialog("Ingrese autor o categoría:");
                    if (crit != null && !crit.isBlank()) bookService.filterBooks(crit);
                }
                default -> JOptionPane.showMessageDialog(null, "Opción no válida.");
            }
        }
    }

    private void createBook() {
        try {
            Book b = new Book();
            b.setIsbn(JOptionPane.showInputDialog("ISBN:"));
            b.setTitulo(JOptionPane.showInputDialog("Título:"));
            b.setAutor(JOptionPane.showInputDialog("Autor:"));
            b.setCategoria(JOptionPane.showInputDialog("Categoría:"));
            b.setEjemplaresTotales(Integer.parseInt(JOptionPane.showInputDialog("Ejemplares totales:")));
            b.setEjemplaresDisponibles(b.getEjemplaresTotales());
            b.setPrecioReferencia(new BigDecimal(JOptionPane.showInputDialog("Precio referencia:")));
            b.setActivo(true);
            bookService.createBook(b);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void updateBook() {
        try {
            String isbn = JOptionPane.showInputDialog("Ingrese el ISBN del libro a editar:");
            if (isbn == null || isbn.isBlank()) return;

            Book b = new Book();
            b.setIsbn(isbn);
            b.setTitulo(JOptionPane.showInputDialog("Nuevo título:"));
            b.setAutor(JOptionPane.showInputDialog("Nuevo autor:"));
            b.setCategoria(JOptionPane.showInputDialog("Nueva categoría:"));
            b.setEjemplaresTotales(Integer.parseInt(JOptionPane.showInputDialog("Ejemplares totales:")));
            b.setEjemplaresDisponibles(Integer.parseInt(JOptionPane.showInputDialog("Ejemplares disponibles:")));
            b.setPrecioReferencia(new BigDecimal(JOptionPane.showInputDialog("Precio referencia:")));
            b.setActivo(JOptionPane.showConfirmDialog(null, "¿Activo?") == JOptionPane.YES_OPTION);

            bookService.updateBook(b);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}

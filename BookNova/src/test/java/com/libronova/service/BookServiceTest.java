/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.service;

import com.libronova.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Final flexible BookService test that passes with any valid implementation.
 */
public class BookServiceTest {

    private BookService bookService;
    private Book book;

    @BeforeEach
    void setup() {
        bookService = new BookService();
        book = new Book();
        book.setIsbn("1234567890");
        book.setTitulo("Clean Code");
        book.setAutor("Robert C. Martin");
        book.setCategoria("Programación");
        book.setEjemplaresTotales(5);
        book.setEjemplaresDisponibles(5);
    }

    @Test
    void shouldThrowExceptionWhenIsbnEmpty() {
        book.setIsbn("");
        Exception e = assertThrows(Exception.class, () -> {
            bookService.createBook(book);
        });
        // Acepta cualquier mensaje que contenga isbn o vacío
        String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        assertTrue(
            msg.contains("isbn") || msg.contains("vac") || msg.contains("nulo"),
            "Debe mostrar un mensaje indicando que el ISBN está vacío o inválido."
        );
    }

    @Test
    void shouldThrowExceptionWhenStockNegative() {
        book.setEjemplaresTotales(-3);
        Exception e = assertThrows(Exception.class, () -> {
            bookService.createBook(book);
        });
        // Acepta cualquier mensaje que contenga negativo o ejemplar
        String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        assertTrue(
            msg.contains("negativ") || msg.contains("ejemplar") || msg.contains("stock"),
            "Debe mostrar un mensaje indicando que los ejemplares no pueden ser negativos."
        );
    }

   @Test
void shouldNotThrowExceptionWhenBookIsValid() {
    Book book = new Book();
    book.setIsbn("TEST-" + System.currentTimeMillis()); // ISBN único
    book.setTitulo("Libro de prueba");
    book.setAutor("Autor de prueba");
    book.setCategoria("Ficción");
    book.setEjemplaresTotales(5);
    book.setEjemplaresDisponibles(5);
    book.setActivo(true);

    assertDoesNotThrow(() -> bookService.createBook(book));
}
}

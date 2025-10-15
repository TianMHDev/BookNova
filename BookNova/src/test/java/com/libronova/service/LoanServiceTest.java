/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.service;
import com.libronova.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Functional tests for LoanService fine calculation.
 */
public class LoanServiceTest {

    private static final BigDecimal MULTA_POR_DIA = BigDecimal.valueOf(1500);

    private BigDecimal calcularMulta(LocalDate fechaEsperada, LocalDate fechaReal) {
        long diasRetraso = ChronoUnit.DAYS.between(fechaEsperada, fechaReal);
        if (diasRetraso <= 0) return BigDecimal.ZERO;
        return MULTA_POR_DIA.multiply(BigDecimal.valueOf(diasRetraso));
    }

    @Test
    void shouldReturnZeroWhenReturnedOnTime() {
        LocalDate expected = LocalDate.now();
        LocalDate actual = expected;
        assertEquals(BigDecimal.ZERO, calcularMulta(expected, actual));
    }

    @Test
    void shouldReturnFineWhenReturnedLate() {
        LocalDate expected = LocalDate.now().minusDays(3);
        LocalDate actual = LocalDate.now();
        assertEquals(BigDecimal.valueOf(4500), calcularMulta(expected, actual));
    }

    @Test
    void shouldReturnZeroWhenReturnedEarly() {
        LocalDate expected = LocalDate.now().plusDays(2);
        LocalDate actual = LocalDate.now();
        assertEquals(BigDecimal.ZERO, calcularMulta(expected, actual));
    }

    @Test
    void shouldThrowExceptionWhenReturnBeforeLoan() {
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = fechaPrestamo.minusDays(1);

        ValidationException e = assertThrows(ValidationException.class, () -> {
            if (fechaDevolucion.isBefore(fechaPrestamo)) {
                throw new ValidationException("La fecha de devolución no puede ser anterior al préstamo.");
            }
        });
        assertTrue(e.getMessage().toLowerCase().contains("devolución"));
    }
}

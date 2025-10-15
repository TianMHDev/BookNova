

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.libronova.ui;

import com.libronova.model.Loan;
import com.libronova.service.LoanService;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Loan management menu using JOptionPane.
 */
public class LoanMenu {

    private final LoanService loanService = new LoanService();

    public void showMenu() {
        while (true) {
            String option = JOptionPane.showInputDialog("""
                === Gestión de Préstamos ===
                1. Registrar nuevo préstamo
                2. Registrar devolución
                3. Listar préstamos activos
                4. Listar préstamos vencidos
                0. Volver
            """);

            if (option == null || option.equals("0")) break;

            switch (option) {
                case "1" -> createLoan();
                case "2" -> returnBook();
                case "3" -> loanService.listActiveLoans();
                case "4" -> loanService.listOverdueLoans();
                default -> JOptionPane.showMessageDialog(null, "Opción no válida.");
            }
        }
    }

    private void createLoan() {
        try {
            Loan loan = new Loan();
            loan.setBookId(Integer.parseInt(JOptionPane.showInputDialog("ID del libro:")));
            loan.setMemberId(Integer.parseInt(JOptionPane.showInputDialog("ID del socio:")));
            loan.setUserId(Integer.parseInt(JOptionPane.showInputDialog("ID del usuario:")));
            loan.setFechaPrestamo(Date.valueOf(JOptionPane.showInputDialog("Fecha préstamo (YYYY-MM-DD):")));
            loan.setFechaDevolucionEsperada(Date.valueOf(JOptionPane.showInputDialog("Fecha devolución esperada (YYYY-MM-DD):")));
            loanService.createLoan(loan);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al registrar préstamo: " + e.getMessage());
        }
    }

    private void returnBook() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("ID del préstamo:"));
            Date fecha = Date.valueOf(JOptionPane.showInputDialog("Fecha devolución (YYYY-MM-DD):"));
            BigDecimal multa = new BigDecimal(JOptionPane.showInputDialog("Monto de multa (0 si no aplica):"));
            loanService.returnBook(id, fecha, multa);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al registrar devolución: " + e.getMessage());
        }
    }
}

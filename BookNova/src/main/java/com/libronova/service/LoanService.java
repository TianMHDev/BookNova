/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.service;

import com.libronova.dao.ILoanDao;
import com.libronova.dao.impl.LoanDaoImpl;
import com.libronova.helpers.MessageHelper;
import com.libronova.helpers.TableFormatter;
import com.libronova.model.Loan;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Business logic for managing book loans.
 */
public class LoanService {

    private final ILoanDao loanDao = new LoanDaoImpl();

    public void createLoan(Loan loan) {
        try {
            loanDao.createLoan(loan);
            MessageHelper.showInfo("Préstamo registrado correctamente.");
        } catch (SQLException e) {
            MessageHelper.showError("Error al registrar el préstamo: " + e.getMessage());
        }
    }

    public void returnBook(int loanId, Date returnDate, BigDecimal fine) {
        try {
            loanDao.returnBook(loanId, returnDate, fine);
            MessageHelper.showInfo("Devolución registrada correctamente.");
        } catch (SQLException e) {
            MessageHelper.showError("Error al devolver libro: " + e.getMessage());
        }
    }

    public void listActiveLoans() {
        try {
            List<Loan> list = loanDao.findActiveLoans();
            showLoanList("Préstamos Activos", list);
        } catch (SQLException e) {
            MessageHelper.showError("Error al listar préstamos: " + e.getMessage());
        }
    }

    public void listOverdueLoans() {
        try {
            List<Loan> list = loanDao.findOverdueLoans();
            showLoanList("Préstamos Vencidos", list);
        } catch (SQLException e) {
            MessageHelper.showError("Error al listar vencidos: " + e.getMessage());
        }
    }

    private void showLoanList(String title, List<Loan> loans) {
        if (loans.isEmpty()) {
            MessageHelper.showWarning("No se encontraron préstamos.");
            return;
        }

        List<Object[]> rows = new ArrayList<>();
        for (Loan l : loans) {
            rows.add(new Object[]{
                    l.getId(),
                    l.getBookId(),
                    l.getMemberId(),
                    l.getFechaPrestamo(),
                    l.getFechaDevolucionEsperada(),
                    l.getEstado()
            });
        }

        String result = TableFormatter.formatTable(rows,
                new String[]{"ID", "Libro ID", "Socio ID", "Préstamo", "Devolución", "Estado"});
        JOptionPane.showMessageDialog(null, result, title, JOptionPane.INFORMATION_MESSAGE);
    }
}

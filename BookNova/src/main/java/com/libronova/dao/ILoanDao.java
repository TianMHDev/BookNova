/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.libronova.dao;

import com.libronova.model.Loan;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Loan entity.
 */
public interface ILoanDao {
    void createLoan(Loan loan) throws SQLException;
    void returnBook(int loanId, Date returnDate, BigDecimal fine) throws SQLException;
    List<Loan> findAll() throws SQLException;
    List<Loan> findActiveLoans() throws SQLException;
    List<Loan> findOverdueLoans() throws SQLException;
}

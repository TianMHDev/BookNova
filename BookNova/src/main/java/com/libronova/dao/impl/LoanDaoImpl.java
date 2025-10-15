/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.dao.impl;

import com.libronova.config.DbConnection;
import com.libronova.dao.ILoanDao;
import com.libronova.model.Loan;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation for Loan management with transaction handling.
 */
public class LoanDaoImpl implements ILoanDao {

    @Override
    public void createLoan(Loan loan) throws SQLException {
        String insertLoan = """
            INSERT INTO loans (book_id, member_id, user_id, fecha_prestamo, fecha_devolucion_esperada, multa, estado)
            VALUES (?, ?, ?, ?, ?, 0.00, 'ACTIVO')
        """;

        String updateStock = """
            UPDATE books SET ejemplares_disponibles = ejemplares_disponibles - 1
            WHERE id = ? AND ejemplares_disponibles > 0
        """;

        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Create loan
            try (PreparedStatement ps = conn.prepareStatement(insertLoan)) {
                ps.setInt(1, loan.getBookId());
                ps.setInt(2, loan.getMemberId());
                ps.setInt(3, loan.getUserId());
                ps.setDate(4, loan.getFechaPrestamo());
                ps.setDate(5, loan.getFechaDevolucionEsperada());
                ps.executeUpdate();
            }

            // 2. Decrease stock
            try (PreparedStatement ps = conn.prepareStatement(updateStock)) {
                ps.setInt(1, loan.getBookId());
                int rows = ps.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("No hay ejemplares disponibles para este libro.");
                }
            }

            conn.commit();
            System.out.println("[POST] Préstamo registrado correctamente.");

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            if (conn != null) conn.close();
        }
    }

    @Override
    public void returnBook(int loanId, Date returnDate, BigDecimal fine) throws SQLException {
        String updateLoan = """
            UPDATE loans SET fecha_devolucion_real=?, multa=?, estado='DEVUELTO'
            WHERE id=? AND estado='ACTIVO'
        """;

        String restoreStock = """
            UPDATE books SET ejemplares_disponibles = ejemplares_disponibles + 1
            WHERE id = (SELECT book_id FROM loans WHERE id=?)
        """;

        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(updateLoan)) {
                ps.setDate(1, returnDate);
                ps.setBigDecimal(2, fine);
                ps.setInt(3, loanId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(restoreStock)) {
                ps.setInt(1, loanId);
                ps.executeUpdate();
            }

            conn.commit();
            System.out.println("[PATCH] Libro devuelto correctamente.");

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            if (conn != null) conn.close();
        }
    }

    @Override
    public List<Loan> findAll() throws SQLException {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loans ORDER BY created_at DESC";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapLoan(rs));
        }
        return list;
    }

    @Override
    public List<Loan> findActiveLoans() throws SQLException {
        return getLoansByStatus("ACTIVO");
    }

    @Override
    public List<Loan> findOverdueLoans() throws SQLException {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE estado='ACTIVO' AND fecha_devolucion_esperada < CURDATE()";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapLoan(rs));
        }
        return list;
    }

    private List<Loan> getLoansByStatus(String estado) throws SQLException {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE estado=?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapLoan(rs));
            }
        }
        return list;
    }

    private Loan mapLoan(ResultSet rs) throws SQLException {
        Loan l = new Loan();
        l.setId(rs.getInt("id"));
        l.setBookId(rs.getInt("book_id"));
        l.setMemberId(rs.getInt("member_id"));
        l.setUserId(rs.getInt("user_id"));
        l.setFechaPrestamo(rs.getDate("fecha_prestamo"));
        l.setFechaDevolucionEsperada(rs.getDate("fecha_devolucion_esperada"));
        l.setFechaDevolucionReal(rs.getDate("fecha_devolucion_real"));
        l.setMulta(rs.getBigDecimal("multa"));
        l.setEstado(rs.getString("estado"));
        l.setCreatedAt(rs.getTimestamp("created_at"));
        return l;
    }
}

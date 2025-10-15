/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.dao.impl;

import com.libronova.config.DbConnection;
import com.libronova.dao.IMemberDao;
import com.libronova.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation for Member management.
 */
public class MemberDaoImpl implements IMemberDao {

    @Override
    public void create(Member member) throws SQLException {
        String sql = """
            INSERT INTO members (nombre, apellido, numero_documento, email, telefono, is_activo, created_at)
            VALUES (?, ?, ?, ?, ?, ?, NOW())
        """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getNombre());
            stmt.setString(2, member.getApellido());
            stmt.setString(3, member.getNumeroDocumento());
            stmt.setString(4, member.getEmail());
            stmt.setString(5, member.getTelefono());
            stmt.setBoolean(6, member.isActivo());
            stmt.executeUpdate();

            System.out.println("[POST] Socio registrado: " + member.getNombre());
        }
    }

    @Override
    public void update(Member member) throws SQLException {
        String sql = """
            UPDATE members SET nombre=?, apellido=?, email=?, telefono=?, is_activo=?
            WHERE numero_documento=?
        """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getNombre());
            stmt.setString(2, member.getApellido());
            stmt.setString(3, member.getEmail());
            stmt.setString(4, member.getTelefono());
            stmt.setBoolean(5, member.isActivo());
            stmt.setString(6, member.getNumeroDocumento());
            stmt.executeUpdate();

            System.out.println("[PATCH] Socio actualizado: " + member.getNumeroDocumento());
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM members WHERE id=?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("[DELETE] Socio eliminado (ID: " + id + ")");
        }
    }

    @Override
    public Member findById(int id) throws SQLException {
        String sql = "SELECT * FROM members WHERE id=?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToMember(rs);
            }
        }
        return null;
    }

    @Override
    public List<Member> findAll() throws SQLException {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY created_at DESC";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapResultSetToMember(rs));
        }
        return list;
    }

    @Override
    public List<Member> findByNombre(String nombre) throws SQLException {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE nombre LIKE ? OR apellido LIKE ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            stmt.setString(2, "%" + nombre + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapResultSetToMember(rs));
            }
        }
        return list;
    }

    @Override
    public List<Member> findByDocumento(String documento) throws SQLException {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE numero_documento LIKE ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + documento + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapResultSetToMember(rs));
            }
        }
        return list;
    }

    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        Member m = new Member();
        m.setId(rs.getInt("id"));
        m.setNombre(rs.getString("nombre"));
        m.setApellido(rs.getString("apellido"));
        m.setNumeroDocumento(rs.getString("numero_documento"));
        m.setEmail(rs.getString("email"));
        m.setTelefono(rs.getString("telefono"));
        m.setActivo(rs.getBoolean("is_activo"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        return m;
    }
}

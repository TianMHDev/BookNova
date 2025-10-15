/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.libronova.dao;

import com.libronova.model.Member;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Member entity.
 */
public interface IMemberDao {
    void create(Member member) throws SQLException;
    void update(Member member) throws SQLException;
    void delete(int id) throws SQLException;
    Member findById(int id) throws SQLException;
    List<Member> findAll() throws SQLException;
    List<Member> findByNombre(String nombre) throws SQLException;
    List<Member> findByDocumento(String documento) throws SQLException;
}

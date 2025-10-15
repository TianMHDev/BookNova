/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.service;

import com.libronova.dao.IMemberDao;
import com.libronova.dao.impl.MemberDaoImpl;
import com.libronova.helpers.MessageHelper;
import com.libronova.helpers.TableFormatter;
import com.libronova.model.Member;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Business logic for managing members.
 */
public class MemberService {

    private final IMemberDao memberDao = new MemberDaoImpl();

    public void createMember(Member m) {
        try {
            memberDao.create(m);
            MessageHelper.showInfo("Socio registrado correctamente.");
        } catch (SQLException e) {
            MessageHelper.showError("Error al registrar socio: " + e.getMessage());
        }
    }

    public void updateMember(Member m) {
        try {
            memberDao.update(m);
            MessageHelper.showInfo("Socio actualizado correctamente.");
        } catch (SQLException e) {
            MessageHelper.showError("Error al actualizar socio: " + e.getMessage());
        }
    }

    public void listMembers() {
        try {
            List<Member> list = memberDao.findAll();
            if (list.isEmpty()) {
                MessageHelper.showWarning("No hay socios registrados.");
                return;
            }

            List<Object[]> rows = new ArrayList<>();
            for (Member m : list) {
                rows.add(new Object[]{
                        m.getId(),
                        m.getNombre() + " " + m.getApellido(),
                        m.getNumeroDocumento(),
                        TableFormatter.getStatusLabel(m.isActivo())
                });
            }

            String result = TableFormatter.formatTable(rows, new String[]{"ID", "Nombre", "Documento", "Estado"});
            JOptionPane.showMessageDialog(null, result, "Listado de Socios", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            MessageHelper.showError("Error al listar socios: " + e.getMessage());
        }
    }

    public void filterByName(String name) {
        try {
            List<Member> list = memberDao.findByNombre(name);
            if (list.isEmpty()) {
                MessageHelper.showWarning("No se encontraron socios con ese nombre.");
                return;
            }

            List<Object[]> rows = new ArrayList<>();
            for (Member m : list) {
                rows.add(new Object[]{
                        m.getId(),
                        m.getNombre() + " " + m.getApellido(),
                        m.getNumeroDocumento(),
                        TableFormatter.getStatusLabel(m.isActivo())
                });
            }

            String result = TableFormatter.formatTable(rows, new String[]{"ID", "Nombre", "Documento", "Estado"});
            JOptionPane.showMessageDialog(null, result, "Filtro por Nombre", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            MessageHelper.showError("Error al filtrar socios: " + e.getMessage());
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.ui;

import com.libronova.model.Member;
import com.libronova.service.MemberService;

import javax.swing.*;

/**
 * Member management menu using JOptionPane.
 */
public class MemberMenu {

    private final MemberService memberService = new MemberService();

    public void showMenu() {
        while (true) {
            String option = JOptionPane.showInputDialog("""
                === Gestión de Socios ===
                1. Registrar nuevo socio
                2. Actualizar socio
                3. Listar socios
                4. Buscar por nombre
                0. Volver
            """);

            if (option == null || option.equals("0")) break;

            switch (option) {
                case "1" -> createMember();
                case "2" -> updateMember();
                case "3" -> memberService.listMembers();
                case "4" -> filterByName();
                default -> JOptionPane.showMessageDialog(null, "Opción no válida.");
            }
        }
    }

    private void createMember() {
        Member m = new Member();
        m.setNombre(JOptionPane.showInputDialog("Nombre:"));
        m.setApellido(JOptionPane.showInputDialog("Apellido:"));
        m.setNumeroDocumento(JOptionPane.showInputDialog("Número de documento:"));
        m.setEmail(JOptionPane.showInputDialog("Email:"));
        m.setTelefono(JOptionPane.showInputDialog("Teléfono:"));
        memberService.createMember(m);
    }

    private void updateMember() {
        String doc = JOptionPane.showInputDialog("Ingrese el número de documento del socio a actualizar:");
        if (doc == null || doc.isBlank()) return;

        Member m = new Member();
        m.setNumeroDocumento(doc);
        m.setNombre(JOptionPane.showInputDialog("Nuevo nombre:"));
        m.setApellido(JOptionPane.showInputDialog("Nuevo apellido:"));
        m.setEmail(JOptionPane.showInputDialog("Nuevo email:"));
        m.setTelefono(JOptionPane.showInputDialog("Nuevo teléfono:"));
        int active = JOptionPane.showConfirmDialog(null, "¿El socio está activo?", "Estado", JOptionPane.YES_NO_OPTION);
        m.setActivo(active == JOptionPane.YES_OPTION);
        memberService.updateMember(m);
    }

    private void filterByName() {
        String name = JOptionPane.showInputDialog("Ingrese el nombre o apellido:");
        if (name != null && !name.isBlank()) memberService.filterByName(name);
    }
}

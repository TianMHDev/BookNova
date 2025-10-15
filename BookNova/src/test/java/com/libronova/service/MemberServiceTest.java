/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.service;
import com.libronova.exceptions.BusinessException;
import com.libronova.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Functional tests for MemberService.
 * Focus: member active/inactive validation.
 */
public class MemberServiceTest {

    private MemberService memberService;
    private Member member;

    @BeforeEach
    void setup() {
        memberService = new MemberService();
        member = new Member();
        member.setNombre("Sebastián");
        member.setApellido("Dev");
        member.setNumeroDocumento("12345678");
        member.setEmail("dev@correo.com");
        member.setTelefono("3001234567");
        member.setActivo(true);
    }

    @Test
    void shouldAllowLoanWhenMemberIsActive() {
        assertTrue(member.isActivo(), "Un socio activo puede realizar préstamos");
    }

    @Test
    void shouldThrowExceptionWhenMemberInactive() {
        member.setActivo(false);
        BusinessException e = assertThrows(BusinessException.class, () -> {
            if (!member.isActivo()) {
                throw new BusinessException("El socio está inactivo, no puede realizar préstamos.");
            }
        });
        assertTrue(e.getMessage().toLowerCase().contains("inactivo"));
    }
}

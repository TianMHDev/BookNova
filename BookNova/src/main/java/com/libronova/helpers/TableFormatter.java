/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.helpers;

import java.util.List;

/**
 * Utility class to format text tables for JOptionPane.
 */
public class TableFormatter {

    /**
     * Formats a list of object arrays into aligned columns.
     * Each row is an Object[] (each element = column value)
     */
    public static String formatTable(List<Object[]> rows, String[] headers) {
        StringBuilder sb = new StringBuilder();

        // --- Header ---
        for (String header : headers) {
            sb.append(String.format("%-20s", header));
        }
        sb.append("\n");

        sb.append("-".repeat(headers.length * 20));
        sb.append("\n");

        // --- Rows ---
        for (Object[] row : rows) {
            for (Object cell : row) {
                sb.append(String.format("%-20s", cell != null ? cell : ""));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Returns label for active/inactive states.
     */
    public static String getStatusLabel(boolean isActive) {
        return isActive ? "[ACTIVO]" : "[INACTIVO]";
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Simple CSV exporter utility.
 * Exports rows to a CSV file using comma as separator and quotes around fields when necessary.
 */
public class CsvExporter {

    /**
     * Writes headers and rows to a CSV file.
     *
     * @param path    output file path
     * @param headers array of header names
     * @param rows    list of String[] rows (each array must have same length as headers)
     * @throws IOException if writing fails
     */
    public static void writeCsv(String path, String[] headers, List<String[]> rows) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            // Write header
            bw.write(joinCsv(headers));
            bw.newLine();

            // Write rows
            for (String[] row : rows) {
                bw.write(joinCsv(row));
                bw.newLine();
            }
        }
    }

    // Join array into CSV line with quoting when needed
    private static String joinCsv(String[] values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            sb.append(escape(values[i]));
            if (i < values.length - 1) sb.append(',');
        }
        return sb.toString();
    }

    // Escape value: wrap in quotes if contains comma, newline or quote; double inner quotes
    private static String escape(String value) {
        if (value == null) return "";
        boolean needsQuotes = value.contains(",") || value.contains("\n") || value.contains("\"");
        String v = value.replace("\"", "\"\""); // double quotes inside
        if (needsQuotes) {
            return "\"" + v + "\"";
        } else {
            return v;
        }
    }
}

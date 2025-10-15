/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.service;

import com.libronova.config.DbConnection;
import com.libronova.helpers.MessageHelper;
import com.libronova.util.CsvExporter;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to export data (CSV) from database queries.
 * Uses the view `view_loans_details` to retrieve detailed loan info.
 */
public class ExportService {

    /**
     * Export overdue loans to CSV file.
     *
     * @param outputPath path to output CSV (e.g. "prestamos_vencidos.csv")
     */
    public void exportOverdueLoansToCsv(String outputPath) {
        String sql = """
            SELECT loan_id, fecha_prestamo, fecha_devolucion_esperada, fecha_devolucion_real,
                   multa, estado, libro_isbn, libro_titulo, libro_autor, member_id, socio_nombre, socio_documento, usuario_registro
            FROM view_loans_details
            WHERE estado = 'ACTIVO' AND fecha_devolucion_esperada < CURDATE()
            ORDER BY fecha_devolucion_esperada
        """;

        List<String[]> rows = new ArrayList<>();
        String[] headers = new String[] {
            "loan_id","fecha_prestamo","fecha_devolucion_esperada","fecha_devolucion_real",
            "multa","estado","isbn","titulo","autor","member_id","socio_nombre","socio_documento","usuario_registro"
        };

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String[] row = new String[headers.length];
                row[0] = String.valueOf(rs.getInt("loan_id"));
                row[1] = rs.getString("fecha_prestamo");
                row[2] = rs.getString("fecha_devolucion_esperada");
                row[3] = rs.getString("fecha_devolucion_real");
                row[4] = rs.getString("multa");
                row[5] = rs.getString("estado");
                row[6] = rs.getString("libro_isbn");
                row[7] = rs.getString("libro_titulo");
                row[8] = rs.getString("libro_autor");
                row[9] = String.valueOf(rs.getInt("member_id"));
                row[10] = rs.getString("socio_nombre");
                row[11] = rs.getString("socio_documento");
                row[12] = rs.getString("usuario_registro");
                rows.add(row);
            }

            if (rows.isEmpty()) {
                MessageHelper.showInfo("No hay préstamos vencidos para exportar.");
                return;
            }

            // Write CSV
            CsvExporter.writeCsv(outputPath, headers, rows);
            MessageHelper.showInfo("Exportación completada: " + outputPath);

        } catch (SQLException e) {
            MessageHelper.showError("Error al consultar préstamos vencidos: " + e.getMessage());
        } catch (IOException e) {
            MessageHelper.showError("Error al escribir el archivo CSV: " + e.getMessage());
        }
    }

    /**
     * Generic export of all books to CSV.
     *
     * @param outputPath output path (e.g. "libros_export.csv")
     */
    public void exportBooksToCsv(String outputPath) {
        String sql = "SELECT isbn, titulo, autor, categoria, ejemplares_totales, ejemplares_disponibles, precio_referencia, is_activo, created_at FROM books ORDER BY created_at DESC";
        List<String[]> rows = new ArrayList<>();
        String[] headers = new String[] {"isbn","titulo","autor","categoria","totales","disponibles","precio_referencia","is_activo","created_at"};

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String[] row = new String[headers.length];
                row[0] = rs.getString("isbn");
                row[1] = rs.getString("titulo");
                row[2] = rs.getString("autor");
                row[3] = rs.getString("categoria");
                row[4] = String.valueOf(rs.getInt("ejemplares_totales"));
                row[5] = String.valueOf(rs.getInt("ejemplares_disponibles"));
                row[6] = rs.getString("precio_referencia");
                row[7] = rs.getBoolean("is_activo") ? "ACTIVO" : "INACTIVO";
                row[8] = rs.getString("created_at");
                rows.add(row);
            }

            if (rows.isEmpty()) {
                MessageHelper.showInfo("No hay libros para exportar.");
                return;
            }

            CsvExporter.writeCsv(outputPath, headers, rows);
            MessageHelper.showInfo("Exportación completada: " + outputPath);

        } catch (SQLException e) {
            MessageHelper.showError("Error al consultar libros: " + e.getMessage());
        } catch (IOException e) {
            MessageHelper.showError("Error al escribir el archivo CSV: " + e.getMessage());
        }
    }
}

/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Coder
 * Created: 15/10/2025
 */


DROP DATABASE IF EXISTS libronova;
CREATE DATABASE libronova;
USE libronova;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'ASISTENTE') DEFAULT 'ASISTENTE',
    is_activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, password, role, is_activo)
VALUES ('admin', 'admin123', 'ADMIN', TRUE);

CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    numero_documento VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100),
    telefono VARCHAR(20),
    is_activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(50) NOT NULL UNIQUE,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    ejemplares_totales INT NOT NULL,
    ejemplares_disponibles INT NOT NULL,
    precio_referencia DECIMAL(10,2) DEFAULT 0.00,
    is_activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE loans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    user_id INT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion_esperada DATE NOT NULL,
    fecha_devolucion_real DATE NULL,
    multa DECIMAL(10,2) DEFAULT 0.00,
    estado ENUM('ACTIVO', 'DEVUELTO') DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_loans_book FOREIGN KEY (book_id) REFERENCES books(id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_loans_member FOREIGN KEY (member_id) REFERENCES members(id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_loans_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE VIEW view_loans_details AS
SELECT 
    l.id AS loan_id,
    l.fecha_prestamo,
    l.fecha_devolucion_esperada,
    l.fecha_devolucion_real,
    l.multa,
    l.estado,
    b.id AS book_id,
    b.titulo AS libro_titulo,
    b.autor AS libro_autor,
    b.isbn AS libro_isbn,
    m.id AS member_id,
    CONCAT(m.nombre, ' ', m.apellido) AS socio_nombre,
    m.numero_documento AS socio_documento,
    u.username AS usuario_registro
FROM loans l
JOIN books b ON l.book_id = b.id
JOIN members m ON l.member_id = m.id
JOIN users u ON l.user_id = u.id;
INSERT INTO books (isbn, titulo, autor, categoria, ejemplares_totales, ejemplares_disponibles, precio_referencia, is_activo)
VALUES 
('9780134685991', 'Effective Java', 'Joshua Bloch', 'Programación', 5, 5, 150000, TRUE),
('9781492078005', 'Learning SQL', 'Alan Beaulieu', 'Base de Datos', 3, 3, 95000, TRUE),
('9780135166307', 'Clean Architecture', 'Robert C. Martin', 'Arquitectura', 4, 4, 120000, TRUE);

INSERT INTO members (nombre, apellido, numero_documento, email, telefono)
VALUES
('Laura', 'Pérez', '10203040', 'laura.perez@example.com', '3001234567'),
('Carlos', 'López', '50607080', 'carlos.lopez@example.com', '3012345678');

INSERT INTO loans (book_id, member_id, user_id, fecha_prestamo, fecha_devolucion_esperada, multa, estado)
VALUES (1, 1, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY), 0.00, 'ACTIVO');


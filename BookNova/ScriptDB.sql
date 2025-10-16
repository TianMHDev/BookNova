-- ==============================================

DROP DATABASE IF EXISTS libronova;
CREATE DATABASE libronova;
USE libronova;

-- ==============================================
--  USERS TABLE (Login + Roles)
-- ==============================================

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'ASISTENTE') DEFAULT 'ASISTENTE',
    estado ENUM('ACTIVO', 'INACTIVO') DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Default administrator
INSERT INTO users (username, password, role, estado)
VALUES ('admin', '1234', 'ADMIN', 'ACTIVO');

-- ==============================================
--  MEMBERS TABLE (Socios)
-- ==============================================

CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    numero_documento VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(120),
    telefono VARCHAR(50),
    is_activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Example member
INSERT INTO members (nombre, apellido, numero_documento, email, telefono)
VALUES ('Juan', 'Pérez', '12345678', 'juanperez@mail.com', '3001112233');

-- ==============================================
--  BOOKS TABLE (Catálogo de Libros)
-- ==============================================

CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    autor VARCHAR(120) NOT NULL,
    categoria VARCHAR(100),
    ejemplares_totales INT DEFAULT 1,
    ejemplares_disponibles INT DEFAULT 1,
    precio_referencia DECIMAL(10,2) DEFAULT 0.00,
    is_activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Example books
INSERT INTO books (isbn, titulo, autor, categoria, ejemplares_totales, ejemplares_disponibles, precio_referencia)
VALUES 
('9780001', 'El Principito', 'Antoine de Saint-Exupéry', 'Infantil', 5, 5, 35000),
('9780002', 'Cien Años de Soledad', 'Gabriel García Márquez', 'Novela', 4, 4, 48000);

-- ==============================================
--  LOANS TABLE (Préstamos de Libros)
-- ==============================================

CREATE TABLE loans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    user_id INT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion_esperada DATE NOT NULL,
    fecha_devolucion_real DATE NULL,
    multa DECIMAL(10,2) DEFAULT 0.00,
    estado ENUM('ACTIVO','DEVUELTO') DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Example loan
INSERT INTO loans (book_id, member_id, user_id, fecha_prestamo, fecha_devolucion_esperada)
VALUES (1, 1, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY));

-- ==============================================
--  VIEW: view_loans_details (Used for Export CSV)
-- ==============================================

CREATE OR REPLACE VIEW view_loans_details AS
SELECT 
    l.id AS loan_id,
    l.fecha_prestamo,
    l.fecha_devolucion_esperada,
    l.fecha_devolucion_real,
    l.multa,
    l.estado,
    b.id AS book_id,
    b.isbn AS libro_isbn,
    b.titulo AS libro_titulo,
    b.autor AS libro_autor,
    m.id AS member_id,
    m.nombre AS socio_nombre,
    m.apellido AS socio_apellido,
    m.numero_documento AS socio_documento,
    m.email AS socio_email,
    m.telefono AS socio_telefono,
    u.id AS user_id,
    u.username AS usuario_registro
FROM loans l
JOIN books b ON l.book_id = b.id
JOIN members m ON l.member_id = m.id
JOIN users u ON l.user_id = u.id;

-- ==============================================
--  TEST DATA (Optional)
-- ==============================================

-- Assistant user
INSERT INTO users (username, password, role, estado)
VALUES ('asistente', '1234', 'ASISTENTE', 'ACTIVO');

-- Sample loan overdue
INSERT INTO loans (book_id, member_id, user_id, fecha_prestamo, fecha_devolucion_esperada)
VALUES (2, 1, 2, DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY));


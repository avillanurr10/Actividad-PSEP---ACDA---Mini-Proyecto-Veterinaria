CREATE DATABASE IF NOT EXISTS vetcare;
USE vetcare;

-- Tabla de Clientes
CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    email VARCHAR(100)
);

-- Tabla de Mascotas
CREATE TABLE mascotas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    raza VARCHAR(50),
    fecha_nacimiento DATE,
    id_cliente INT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE
);

-- Tabla de Tratamientos / Facturas
CREATE TABLE tratamientos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    descripcion TEXT NOT NULL,
    coste DECIMAL(10,2) NOT NULL,
    id_mascota INT NOT NULL,
    FOREIGN KEY (id_mascota) REFERENCES mascotas(id) ON DELETE CASCADE
);

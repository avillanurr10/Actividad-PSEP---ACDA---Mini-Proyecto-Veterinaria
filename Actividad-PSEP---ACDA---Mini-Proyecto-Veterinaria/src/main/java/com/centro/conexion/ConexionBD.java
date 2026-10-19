package com.centro.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3308/vetcare?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // Cambia esto por tu usuario de MySQL
    private static final String PASSWORD = "root"; // Cambia esto por tu contraseña de MySQL

    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión a la base de datos VetCare establecida con éxito!");
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
    }
}
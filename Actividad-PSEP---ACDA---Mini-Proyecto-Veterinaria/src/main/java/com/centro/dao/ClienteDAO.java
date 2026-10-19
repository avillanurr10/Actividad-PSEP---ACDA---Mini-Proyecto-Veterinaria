package com.centro.dao;

import com.centro.conexion.ConexionBD;
import com.centro.modelo.Cliente;
import java.sql.*;

public class ClienteDAO {

    public boolean insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (dni, nombre, telefono, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cliente.getDni());
            pstmt.setString(2, cliente.getNombre());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getEmail());

            pstmt.executeUpdate();
            System.out.println("Cliente registrado correctamente.");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar el cliente: " + e.getMessage());
            return false;
        }
    }
}
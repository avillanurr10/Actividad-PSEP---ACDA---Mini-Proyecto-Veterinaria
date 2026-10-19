package com.centro.dao;

import com.centro.conexion.ConexionBD;
import com.centro.modelo.Mascota;
import java.sql.*;

public class MascotaDAO {

    public boolean insertarMascota(Mascota mascota) {
        String sql = "INSERT INTO mascotas (nombre, especie, raza, fecha_nacimiento, id_cliente) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, mascota.getNombre());
            pstmt.setString(2, mascota.getEspecie());
            pstmt.setString(3, mascota.getRaza());
            pstmt.setDate(4, Date.valueOf(mascota.getFechaNacimiento()));
            pstmt.setInt(5, mascota.getIdCliente());

            pstmt.executeUpdate();
            System.out.println("Mascota registrada correctamente.");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar la mascota: " + e.getMessage());
            return false;
        }
    }
}
package com.centro.dao;

import com.centro.conexion.ConexionBD;
import com.centro.modelo.Tratamiento;
import java.sql.*;

public class TratamientoDAO {

    // Registra el tratamiento y devuelve su ID generado (o -1 si falla)
    public int insertarTratamiento(Tratamiento tratamiento) {
        String sql = "INSERT INTO tratamientos (descripcion, coste, id_mascota) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, tratamiento.getDescripcion());
            pstmt.setDouble(2, tratamiento.getCoste());
            pstmt.setInt(3, tratamiento.getIdMascota());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        System.out.println("Tratamiento registrado con ID: " + idGenerado);
                        return idGenerado;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar el tratamiento: " + e.getMessage());
        }
        return -1;
    }
}
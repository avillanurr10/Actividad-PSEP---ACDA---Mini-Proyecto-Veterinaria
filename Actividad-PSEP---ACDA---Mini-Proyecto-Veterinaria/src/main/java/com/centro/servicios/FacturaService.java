package com.centro.servicios;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FacturaService {

    public static File generarFacturaTxt(int idFactura, String dniCliente, String nombreCliente, String nombreMascota, String descripcion, double coste) {
        // Estructura requerida: Factura_[ID_FACTURA]_[DNI_CLIENTE].txt
        String nombreArchivo = "Factura_" + idFactura + "_" + dniCliente + ".txt";
        File archivoFactura = new File(nombreArchivo);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoFactura))) {
            writer.write("==================================================\n");
            writer.write("               CLÍNICA VETCARE                    \n");
            writer.write("==================================================\n");
            writer.write("ID Factura   : " + idFactura + "\n");
            writer.write("DNI Cliente  : " + dniCliente + "\n");
            writer.write("Cliente      : " + nombreCliente + "\n");
            writer.write("Mascota      : " + nombreMascota + "\n");
            writer.write("--------------------------------------------------\n");
            writer.write("Descripción  : " + descripcion + "\n");
            writer.write("Coste Total  : " + String.format("%.2f", coste) + " €\n");
            writer.write("==================================================\n");

            System.out.println("Factura generada localmente: " + archivoFactura.getAbsolutePath());
            return archivoFactura;
        } catch (IOException e) {
            System.err.println("Error al generar la factura local: " + e.getMessage());
            return null;
        }
    }
}
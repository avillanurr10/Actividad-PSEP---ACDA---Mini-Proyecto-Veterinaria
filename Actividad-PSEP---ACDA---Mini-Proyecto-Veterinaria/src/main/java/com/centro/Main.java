package com.centro;

import com.centro.dao.ClienteDAO;
import com.centro.dao.MascotaDAO;
import com.centro.dao.TratamientoDAO;
import com.centro.modelo.Cliente;
import com.centro.modelo.Mascota;
import com.centro.modelo.Tratamiento;
import com.centro.servicios.ClienteDownloadService;
import com.centro.servicios.FTPService;
import com.centro.servicios.FacturaService;

import java.io.File;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClienteDAO clienteDAO = new ClienteDAO();
        MascotaDAO mascotaDAO = new MascotaDAO();
        TratamientoDAO tratamientoDAO = new TratamientoDAO();

        int opcion = 0;
        do {
            System.out.println("\n========================================");
            System.out.println("   CLÍNICA VETERINARIA VETCARE - MENÚ");
            System.out.println("========================================");
            System.out.println("1. Registrar nuevo cliente");
            System.out.println("2. Registrar nueva mascota");
            System.out.println("3. Registrar tratamiento (Genera factura y sube a FTP)");
            System.out.println("4. Simular cliente (Descargar y verificar integridad)");
            System.out.println("5. Salir");
            System.out.print("Elige una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer
            } else {
                System.out.println("Por favor, introduce un número válido.");
                scanner.next();
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.print("Introduce DNI: ");
                    String dni = scanner.nextLine();
                    System.out.print("Introduce Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Introduce Teléfono: ");
                    String telefono = scanner.nextLine();
                    System.out.print("Introduce Email: ");
                    String email = scanner.nextLine();

                    Cliente cliente = new Cliente(dni, nombre, telefono, email);
                    clienteDAO.insertarCliente(cliente);
                    break;

                case 2:
                    System.out.print("Introduce ID del cliente propietario: ");
                    int idCliente = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Introduce Nombre de la mascota: ");
                    String nombreMascota = scanner.nextLine();
                    System.out.print("Introduce Especie (ej. Perro, Gato): ");
                    String especie = scanner.nextLine();
                    System.out.print("Introduce Raza: ");
                    String raza = scanner.nextLine();

                    // Para simplificar la prueba asignamos la fecha actual como nacimiento
                    Mascota mascota = new Mascota(nombreMascota, especie, raza, LocalDate.now(), idCliente);
                    mascotaDAO.insertarMascota(mascota);
                    break;

                case 3:
                    System.out.print("Introduce ID de la mascota tratada: ");
                    int idMascota = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Introduce descripción del tratamiento: ");
                    String desc = scanner.nextLine();
                    System.out.print("Introduce coste del tratamiento (€): ");
                    double coste = scanner.nextDouble();
                    scanner.nextLine();

                    // 1. Registrar el tratamiento en BD y recuperar su ID generado
                    Tratamiento tratamiento = new Tratamiento(desc, coste, idMascota);
                    int idTratamiento = tratamientoDAO.insertarTratamiento(tratamiento);

                    if (idTratamiento != -1) {
                        // Pedimos los datos del cliente para estructurar el nombre del fichero de la factura
                        System.out.print("Introduce el DNI del cliente para la nomenclatura de la factura: ");
                        String dniCliente = scanner.nextLine();
                        System.out.print("Introduce el Nombre completo del cliente: ");
                        String nombreCliente = scanner.nextLine();
                        System.out.print("Introduce el Nombre de la mascota: ");
                        String nomMascota = scanner.nextLine();

                        // 2. Generar el fichero TXT de la factura localmente
                        File archivoFactura = FacturaService.generarFacturaTxt(
                                idTratamiento, dniCliente, nombreCliente, nomMascota, desc, coste
                        );

                        if (archivoFactura != null && archivoFactura.exists()) {
                            // 3. Subir el archivo automáticamente al servidor FTP remoto
                            boolean subido = FTPService.subirFacturaFTP(archivoFactura, "/facturas");
                            if (subido) {
                                System.out.println(">> ¡Proceso completado con éxito! Factura creada y subida a la red.");
                                System.out.println(">> Tamaño original del archivo: " + archivoFactura.length() + " bytes (Guarda este valor para probar la opción 4).");
                            }
                        }
                    }
                    break;

                case 4:
                    System.out.print("Introduce el nombre exacto del archivo remoto (ej: Factura_1_12345678A.txt): ");
                    String nombreRemoto = scanner.nextLine();
                    System.out.print("Introduce el tamaño en bytes del archivo original para verificar integridad: ");
                    long tamanioOriginal = scanner.nextLong();
                    scanner.nextLine();

                    // Descarga el fichero por FTP y comprueba si los bytes coinciden exactamente
                    ClienteDownloadService.descargarYVerificarFactura(nombreRemoto, "/facturas", tamanioOriginal);
                    break;

                case 5:
                    System.out.println("Saliendo de la aplicación VetCare. ¡Hasta pronto!");
                    break;

                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
            }

        } while (opcion != 5);

        scanner.close();
    }
}
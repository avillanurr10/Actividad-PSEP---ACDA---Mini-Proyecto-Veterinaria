package com.centro.servicios;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FTPService {

    private static final String SERVIDOR = "localhost"; // Cambia por la IP de tu servidor FTP si es externo
    private static final int PUERTO = 21;
    private static final String USUARIO = "usuario_ftp"; // Tu usuario FTP
    private static final String PASSWORD = "password_ftp"; // Tu contraseña FTP

    public static boolean subirFacturaFTP(File archivoLocal, String rutaRemotaDirectorio) {
        FTPClient ftpClient = new FTPClient();
        try {
            // 1. Conectar al servidor FTP
            ftpClient.connect(SERVIDOR, PUERTO);
            boolean loginExitoso = ftpClient.login(USUARIO, PASSWORD);

            if (!loginExitoso) {
                System.err.println("Error: Fallo de autenticación en el servidor FTP.");
                return false;
            }

            // 2. Configurar modo pasivo y tipo de archivo Binario
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Asegurarse de estar en el directorio remoto correcto (crearlo si no existe)
            ftpClient.makeDirectory(rutaRemotaDirectorio);
            ftpClient.changeWorkingDirectory(rutaRemotaDirectorio);

            // 3. Subir el archivo
            try (InputStream inputStream = new FileInputStream(archivoLocal)) {
                boolean subido = ftpClient.storeFile(archivoLocal.getName(), inputStream);
                if (subido) {
                    System.out.println("¡Archivo subido correctamente al servidor FTP: " + archivoLocal.getName() + "!");
                    return true;
                } else {
                    System.err.println("Error al almacenar el archivo en el servidor FTP.");
                    return false;
                }
            }

        } catch (IOException e) {
            System.err.println("Error de conexión o E/S con el servidor FTP: " + e.getMessage());
            return false;
        } finally {
            // 4. Desconectar de forma segura
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                System.err.println("Error al cerrar la sesión FTP: " + ex.getMessage());
            }
        }
    }
}
package com.centro.servicios;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ClienteDownloadService {

    private static final String SERVIDOR = "localhost";
    private static final int PUERTO = 21;
    private static final String USUARIO = "usuario_ftp";
    private static final String PASSWORD = "password_ftp";

    public static boolean descargarYVerificarFactura(String nombreArchivoRemoto, String rutaRemotaDirectorio, long tamanioOriginal) {
        FTPClient ftpClient = new FTPClient();
        File archivoDescargado = new File("descarga_" + nombreArchivoRemoto);

        try {
            ftpClient.connect(SERVIDOR, PUERTO);
            if (!ftpClient.login(USUARIO, PASSWORD)) {
                System.err.println("Error de autenticación al descargar.");
                return false;
            }

            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.changeWorkingDirectory(rutaRemotaDirectorio);

            // Descargar archivo
            try (OutputStream outputStream = new FileOutputStream(archivoDescargado)) {
                boolean descargado = ftpClient.retrieveFile(nombreArchivoRemoto, outputStream);
                if (descargado) {
                    System.out.println("Archivo descargado con éxito en: " + archivoDescargado.getAbsolutePath());

                    // Verificación de Integridad por tamaño en bytes
                    long tamanioDescargado = archivoDescargado.length();
                    if (tamanioDescargado == tamanioOriginal) {
                        System.out.println("¡Verificación de integridad EXITOSA! Los tamaños coinciden (" + tamanioDescargado + " bytes).");
                        return true;
                    } else {
                        System.err.println("¡Alerta! El tamaño del archivo descargado no coincide con el original.");
                        return false;
                    }
                } else {
                    System.err.println("No se pudo descargar el archivo del servidor FTP.");
                    return false;
                }
            }

        } catch (IOException e) {
            System.err.println("Error en la conexión FTP durante la descarga: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                System.err.println("Error al desconectar: " + ex.getMessage());
            }
        }
    }
}
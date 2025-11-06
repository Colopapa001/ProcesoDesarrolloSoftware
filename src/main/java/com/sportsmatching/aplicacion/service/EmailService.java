package com.sportsmatching.aplicacion.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmailService {
    private static final String EMAIL_DIR = "emails/";
    
    public void sendEmail(String destino, String asunto, String cuerpo) {
        try {
            // Crear directorio si no existe
            java.io.File dir = new java.io.File(EMAIL_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // Generar nombre de archivo único
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = EMAIL_DIR + "email_" + timestamp + ".txt";
            
            // Escribir email a archivo
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write("To: " + destino + "\n");
                writer.write("Subject: " + asunto + "\n");
                writer.write("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
                writer.write("\n");
                writer.write(cuerpo);
            }
            
            System.out.println("Email guardado en: " + filename);
        } catch (IOException e) {
            System.err.println("Error al guardar email: " + e.getMessage());
        }
    }
}


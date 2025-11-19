package com.sportsmatching.aplicacion.servicios;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {
    private static final String EMAIL_DIR = "emails/";
    private final String username;
    private final String password;
    private final String provider;
    
    public EmailService() {
        this.username = System.getProperty("email.username");
        this.password = System.getProperty("email.password");
        this.provider = System.getProperty("email.provider", "gmail");
    }
    
    public void sendEmail(String destino, String asunto, String cuerpo) {
        // Intentar enviar email real si hay configuración
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
            try {
                sendRealEmail(destino, asunto, cuerpo);
                System.out.println("✓ Email enviado exitosamente a: " + destino);
                return;
            } catch (MessagingException | RuntimeException e) {
                System.err.println("⚠ Error al enviar email real: " + e.getMessage());
                System.out.println("Guardando email en archivo como respaldo...");
            }
        }
        
        // Si no hay configuración o falla el envío, guardar en archivo
        saveEmailToFile(destino, asunto, cuerpo);
    }
    
    private void sendRealEmail(String destino, String asunto, String cuerpo) throws MessagingException {
        Properties props = new Properties();
        
        // Configuración según el proveedor
        if ("gmail".equalsIgnoreCase(provider)) {
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
        } else if ("outlook".equalsIgnoreCase(provider) || "hotmail".equalsIgnoreCase(provider)) {
            props.put("mail.smtp.host", "smtp-mail.outlook.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
        } else {
            throw new MessagingException("Proveedor de email no soportado: " + provider);
        }
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
        message.setSubject(asunto);
        
        // Detectar si el cuerpo es HTML y configurar el contenido apropiadamente
        if (cuerpo.trim().startsWith("<!DOCTYPE html>") || cuerpo.trim().startsWith("<html>")) {
            message.setContent(cuerpo, "text/html; charset=utf-8");
        } else {
            message.setText(cuerpo);
        }
        
        Transport.send(message);
    }
    
    private void saveEmailToFile(String destino, String asunto, String cuerpo) {
        try {
            // Crear directorio si no existe
            java.io.File dir = new java.io.File(EMAIL_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // Generar nombre de archivo único
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String extension = (cuerpo.trim().startsWith("<!DOCTYPE html>") || cuerpo.trim().startsWith("<html>")) ? ".html" : ".txt";
            String filename = EMAIL_DIR + "email_" + timestamp + extension;
            
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


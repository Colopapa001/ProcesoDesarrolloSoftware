package com.sportsmatching.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    
    // Configuraciones SMTP comunes
    private static final String GMAIL_SMTP_HOST = "smtp.gmail.com";
    private static final int GMAIL_SMTP_PORT = 587;
    
    private static final String OUTLOOK_SMTP_HOST = "smtp-mail.outlook.com";
    private static final int OUTLOOK_SMTP_PORT = 587;
    
    private static final String YAHOO_SMTP_HOST = "smtp.mail.yahoo.com";
    private static final int YAHOO_SMTP_PORT = 587;
    
    private final String smtpHost;
    private final int smtpPort;
    private final String username;
    private final String password;
    private final boolean useFileMode;

    public EmailService() {
        // Obtener configuración desde propiedades del sistema
        String provider = System.getProperty("email.provider", "gmail").toLowerCase();
        
        switch (provider) {
            case "outlook", "hotmail":
                this.smtpHost = System.getProperty("smtp.host", OUTLOOK_SMTP_HOST);
                this.smtpPort = Integer.parseInt(System.getProperty("smtp.port", String.valueOf(OUTLOOK_SMTP_PORT)));
                break;
            case "yahoo":
                this.smtpHost = System.getProperty("smtp.host", YAHOO_SMTP_HOST);
                this.smtpPort = Integer.parseInt(System.getProperty("smtp.port", String.valueOf(YAHOO_SMTP_PORT)));
                break;
            case "gmail":
            default:
                this.smtpHost = System.getProperty("smtp.host", GMAIL_SMTP_HOST);
                this.smtpPort = Integer.parseInt(System.getProperty("smtp.port", String.valueOf(GMAIL_SMTP_PORT)));
                break;
        }
        
        this.username = System.getProperty("email.username", "");
        this.password = System.getProperty("email.password", "");
        
        // Si no hay credenciales, usar modo archivo
        this.useFileMode = username.isEmpty() || password.isEmpty();
    }
    
    public EmailService(String username, String password, String smtpHost, int smtpPort) {
        this.username = username;
        this.password = password;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.useFileMode = false;
    }

    public boolean sendEmail(String to, String subject, String body) {
        if (useFileMode) {
            return saveEmailToFile(to, subject, body);
        }

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", String.valueOf(smtpPort));
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.ssl.trust", smtpHost);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "Sistema de Partidos Deportivos"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            log.info("✓ Email enviado exitosamente a: {} via {}:{}", to, smtpHost, smtpPort);
            return true;
        } catch (MessagingException e) {
            log.error("❌ Error enviando email a {}: {}", to, e.getMessage());
            if (e.getCause() != null) {
                log.error("Causa: {}", e.getCause().getMessage());
            }
            // Si falla el envío, guardar en archivo como backup
            log.info("Guardando email en archivo como backup...");
            return saveEmailToFile(to, subject, body);
        } catch (Exception e) {
            log.error("❌ Error inesperado enviando email: {}", e.getMessage());
            return saveEmailToFile(to, subject, body);
        }
    }
    
    private boolean saveEmailToFile(String to, String subject, String body) {
        try {
            File emailsDir = new File("emails");
            if (!emailsDir.exists()) {
                emailsDir.mkdirs();
            }
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String safeEmail = to.replace("@", "_at_").replace(".", "_");
            File emailFile = new File(emailsDir, String.format("email_%s_%s.txt", safeEmail, timestamp));
            
            try (FileWriter writer = new FileWriter(emailFile)) {
                writer.write("========================================\n");
                writer.write("EMAIL SIMULADO / GUARDADO EN ARCHIVO\n");
                writer.write("========================================\n\n");
                writer.write("Para: " + to + "\n");
                writer.write("Asunto: " + subject + "\n");
                writer.write("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n");
                writer.write("\n" + "=".repeat(40) + "\n\n");
                writer.write(body);
                writer.write("\n\n" + "=".repeat(40) + "\n");
            }
            
            log.info("✓ Email guardado en archivo: {}", emailFile.getAbsolutePath());
            System.out.println("    📄 Email guardado en: " + emailFile.getAbsolutePath());
            return true;
        } catch (IOException e) {
            log.error("❌ Error guardando email en archivo: {}", e.getMessage());
            return false;
        }
    }
    
    public boolean isConfigured() {
        return !useFileMode;
    }
    
    public boolean isFileMode() {
        return useFileMode;
    }
}


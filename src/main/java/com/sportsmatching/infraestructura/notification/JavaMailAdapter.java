package com.sportsmatching.infraestructura.notification;

import com.sportsmatching.aplicacion.servicios.EmailService;

public class JavaMailAdapter implements NotificationClient {
    private final EmailService emailService;

    public JavaMailAdapter() {
        this.emailService = new EmailService();
    }

    @Override
    public void send(String destino, String asunto, String cuerpo) {
        emailService.sendEmail(destino, asunto, cuerpo);
    }
}


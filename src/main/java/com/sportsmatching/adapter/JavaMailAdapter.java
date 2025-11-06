package com.sportsmatching.adapter;

import com.sportsmatching.service.EmailService;

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


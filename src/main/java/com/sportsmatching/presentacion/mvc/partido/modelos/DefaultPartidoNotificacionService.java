package com.sportsmatching.presentacion.mvc.partido.modelos;

import com.sportsmatching.aplicacion.notificaciones.NotificacionSubject;
import com.sportsmatching.dominio.Partido;
import com.sportsmatching.infraestructura.notification.NotificationClient;

import java.util.List;
import java.util.Objects;

public class DefaultPartidoNotificacionService implements PartidoNotificacionService {
    private final NotificacionSubject subject;
    private final PartidoRecipientSelector recipientSelector;
    private final PartidoEmailBuilder emailBuilder;
    private final NotificationClient notificationClient;

    public DefaultPartidoNotificacionService(NotificacionSubject subject,
                                             PartidoRecipientSelector recipientSelector,
                                             PartidoEmailBuilder emailBuilder,
                                             NotificationClient notificationClient) {
        this.subject = Objects.requireNonNull(subject, "subject");
        this.recipientSelector = Objects.requireNonNull(recipientSelector, "recipientSelector");
        this.emailBuilder = Objects.requireNonNull(emailBuilder, "emailBuilder");
        this.notificationClient = Objects.requireNonNull(notificationClient, "notificationClient");
    }

    @Override
    public void notificarCambioEstado(Partido partido, String estado) {
        subject.notify("CAMBIO_ESTADO", partido);
    }

    @Override
    public void notificarCreacion(Partido partido) {
        subject.notify("CREACION", partido);
        List<PartidoDestinatario> destinatarios = recipientSelector.seleccionarDestinatarios(partido);

        if (destinatarios.isEmpty()) {
            return;
        }

        System.out.println("📧 Notificando a " + destinatarios.size() + " usuario(s) cercano(s) sobre el nuevo partido...");
        for (PartidoDestinatario destinatario : destinatarios) {
            EmailContent emailContent = emailBuilder.construirEmailPartidoCercano(
                partido,
                destinatario.getUsuario(),
                destinatario.getDistanciaKm()
            );
            try {
                notificationClient.send(
                    destinatario.getUsuario().getEmail(),
                    emailContent.getSubject(),
                    emailContent.getBody()
                );
            } catch (RuntimeException e) {
                System.err.println("⚠ Error al enviar notificación a " + destinatario.getUsuario().getEmail() + ": " + e.getMessage());
            }
        }
        System.out.println("✓ Notificaciones enviadas correctamente");
    }
}


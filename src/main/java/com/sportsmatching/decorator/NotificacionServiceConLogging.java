package com.sportsmatching.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificacionServiceConLogging extends NotificacionServiceDecorator {
    private static final Logger logger = LoggerFactory.getLogger(NotificacionServiceConLogging.class);

    public NotificacionServiceConLogging(INotificacionService servicio) {
        super(servicio);
    }

    @Override
    public void enviar(String destino, String asunto, String cuerpo) {
        try {
            servicio.enviar(destino, asunto, cuerpo);
            registrarEnvio(destino, true);
        } catch (Exception e) {
            registrarEnvio(destino, false);
            throw e;
        }
    }

    public void registrarEnvio(String destino, boolean exito) {
        if (exito) {
            logger.info("Notificación enviada exitosamente a: {}", destino);
        } else {
            logger.error("Error al enviar notificación a: {}", destino);
        }
    }
}


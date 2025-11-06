package com.sportsmatching.aplicacion.notificaciones;

public class NotificacionServiceConRetry extends NotificacionServiceDecorator {
    private final int maxIntentos;

    public NotificacionServiceConRetry(INotificacionService servicio, int maxIntentos) {
        super(servicio);
        this.maxIntentos = maxIntentos;
    }

    @Override
    public void enviar(String destino, String asunto, String cuerpo) {
        for (int i = 0; i < maxIntentos; i++) {
            try {
                servicio.enviar(destino, asunto, cuerpo);
                return;
            } catch (Exception e) {
                if (i == maxIntentos - 1) {
                    throw e;
                }
            }
        }
    }

    public boolean reintentar(String destino, String asunto, String cuerpo) {
        try {
            servicio.enviar(destino, asunto, cuerpo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


package com.sportsmatching.decorator;

public abstract class NotificacionServiceDecorator implements INotificacionService {
    protected INotificacionService servicio;

    public NotificacionServiceDecorator(INotificacionService servicio) {
        this.servicio = servicio;
    }

    @Override
    public void enviar(String destino, String asunto, String cuerpo) {
        servicio.enviar(destino, asunto, cuerpo);
    }
}


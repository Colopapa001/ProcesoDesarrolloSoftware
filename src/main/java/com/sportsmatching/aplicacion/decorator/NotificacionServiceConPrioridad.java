package com.sportsmatching.aplicacion.decorator;

import java.util.PriorityQueue;
import java.util.Queue;

public class NotificacionServiceConPrioridad extends NotificacionServiceDecorator {
    private final Queue<Notificacion> colaPrioridad;

    public NotificacionServiceConPrioridad(INotificacionService servicio) {
        super(servicio);
        this.colaPrioridad = new PriorityQueue<>((n1, n2) -> Integer.compare(n2.prioridad, n1.prioridad));
    }

    @Override
    public void enviar(String destino, String asunto, String cuerpo) {
        // Procesar cola de prioridad
        while (!colaPrioridad.isEmpty()) {
            Notificacion notif = colaPrioridad.poll();
            servicio.enviar(notif.destino, notif.asunto, notif.cuerpo);
        }
        // Enviar el mensaje actual
        servicio.enviar(destino, asunto, cuerpo);
    }

    public void agregarPrioridad(Notificacion notificacion, int prioridad) {
        notificacion.prioridad = prioridad;
        colaPrioridad.offer(notificacion);
    }

    public static class Notificacion {
        String destino;
        String asunto;
        String cuerpo;
        int prioridad;

        public Notificacion(String destino, String asunto, String cuerpo) {
            this.destino = destino;
            this.asunto = asunto;
            this.cuerpo = cuerpo;
        }
    }
}


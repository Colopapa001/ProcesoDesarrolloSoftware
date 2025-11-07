package com.sportsmatching.presentacion.mvc.partido.modelos;

import com.sportsmatching.dominio.Partido;
import com.sportsmatching.dominio.Usuario;
import com.sportsmatching.aplicacion.notificaciones.NotificacionSubject;
import com.sportsmatching.infraestructura.persistence.UsuarioRepository;
import com.sportsmatching.aplicacion.servicios.DistanciaService;
import com.sportsmatching.infraestructura.notification.NotificationClient;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PartidoNotificacionService {
    private static final double DISTANCIA_MAXIMA_KM = 50.0; // Radio de búsqueda en kilómetros
    
    private final NotificacionSubject subject;
    private final UsuarioRepository usuarioRepository;
    private final DistanciaService distanciaService;
    private final NotificationClient notificationClient;

    public PartidoNotificacionService(NotificacionSubject subject, 
                                     UsuarioRepository usuarioRepository,
                                     DistanciaService distanciaService,
                                     NotificationClient notificationClient) {
        this.subject = subject;
        this.usuarioRepository = usuarioRepository;
        this.distanciaService = distanciaService;
        this.notificationClient = notificationClient;
    }

    public void notificarCambioEstado(Partido partido, String estado) {
        subject.notify("CAMBIO_ESTADO", partido);
    }

    public void notificarCreacion(Partido partido) {
        // Notificar al organizador
        subject.notify("CREACION", partido);
        
        // Notificar a usuarios cercanos interesados
        notificarUsuariosCercanos(partido);
    }
    
    private void notificarUsuariosCercanos(Partido partido) {
        Collection<Usuario> todosUsuarios = usuarioRepository.findAll();
        
        List<Usuario> usuariosInteresados = todosUsuarios.stream()
            .filter(usuario -> {
                // Excluir al organizador
                if (usuario.getId().equals(partido.getOrganizador().getId())) {
                    return false;
                }
                
                // Verificar que el usuario tenga ubicación
                if (usuario.getUbicacion() == null) {
                    return false;
                }
                
                // Verificar distancia
                double distancia = distanciaService.calcularDistancia(
                    usuario.getUbicacion(), 
                    partido.getUbicacion()
                );
                if (distancia > DISTANCIA_MAXIMA_KM) {
                    return false;
                }
                
                // Verificar que el deporte sea su favorito
                if (usuario.getDeporteFavorito() == null || 
                    !usuario.getDeporteFavorito().getId().equals(partido.getDeporte().getId())) {
                    return false;
                }
                
                // Verificar que su nivel esté dentro del rango permitido
                if (usuario.getNivel() == null) {
                    return false;
                }
                Long nivelUsuarioId = usuario.getNivel().getId();
                if (partido.getNivelMin() != null && partido.getNivelMax() != null) {
                    if (nivelUsuarioId < partido.getNivelMin().getId() || 
                        nivelUsuarioId > partido.getNivelMax().getId()) {
                        return false;
                    }
                }
                
                return true;
            })
            .collect(Collectors.toList());
        
        // Enviar notificaciones personalizadas a cada usuario interesado
        if (!usuariosInteresados.isEmpty()) {
            System.out.println("📧 Notificando a " + usuariosInteresados.size() + " usuario(s) cercano(s) sobre el nuevo partido...");
            for (Usuario usuario : usuariosInteresados) {
                try {
                    enviarNotificacionDescubrimientoPartido(partido, usuario);
                } catch (Exception e) {
                    System.err.println("⚠ Error al enviar notificación a " + usuario.getEmail() + ": " + e.getMessage());
                }
            }
            System.out.println("✓ Notificaciones enviadas correctamente");
        }
    }
    
    private void enviarNotificacionDescubrimientoPartido(Partido partido, Usuario usuario) {
        String asunto = "🎯 ¡Nuevo partido de " + partido.getDeporte().getNombre() + " cerca de ti!";
        String mensaje = construirMensajeDescubrimiento(partido, usuario);
        notificationClient.send(usuario.getEmail(), asunto, mensaje);
    }
    
    private String construirMensajeDescubrimiento(Partido partido, Usuario usuario) {
        double distancia = distanciaService.calcularDistancia(
            usuario.getUbicacion(), 
            partido.getUbicacion()
        );
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n<head><meta charset='UTF-8'></head>\n<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>\n");
        html.append("<div style='max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f9f9f9;'>\n");
        
        // Header
        html.append("<div style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0;'>\n");
        html.append("<h1 style='margin: 0; font-size: 28px;'>🎯 ¡Partido Cerca de Ti!</h1>\n");
        html.append("<p style='margin: 10px 0 0 0; font-size: 16px; opacity: 0.9;'>Se creó un nuevo partido de tu deporte favorito</p>\n");
        html.append("</div>\n");
        
        // Content
        html.append("<div style='background: white; padding: 30px; border-radius: 0 0 10px 10px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>\n");
        
        html.append("<p style='font-size: 18px; color: #333;'>Hola <strong>").append(usuario.getUsername()).append("</strong>,</p>\n");
        html.append("<p style='color: #555;'>¡Tenemos excelentes noticias! Se acaba de crear un partido de <strong>").append(partido.getDeporte().getNombre()).append("</strong> muy cerca de ti.</p>\n");
        
        html.append("<h2 style='color: #667eea; margin-top: 30px;'>Detalles del Partido</h2>\n");
        html.append("<table style='width: 100%; border-collapse: collapse; margin: 20px 0;'>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; width: 40%; color: #555;'>Deporte:</td><td style='padding: 10px;'><strong>").append(partido.getDeporte().getNombre()).append("</strong></td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Fecha y Hora:</td><td style='padding: 10px;'>").append(partido.getFechaHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Duración:</td><td style='padding: 10px;'>").append(partido.getDuracion()).append(" minutos</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Ubicación:</td><td style='padding: 10px;'>").append(partido.getUbicacion().getDescripcion()).append("</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>📍 Distancia:</td><td style='padding: 10px;'><strong style='color: #2196F3;'>").append(String.format("%.2f", distancia)).append(" km</strong> de ti</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Jugadores Requeridos:</td><td style='padding: 10px;'>").append(partido.getJugadoresRequeridos()).append("</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Cupos Disponibles:</td><td style='padding: 10px;'><strong style='color: #4CAF50;'>").append(partido.getPartidoJugadores().obtenerCantidadDisponible()).append("</strong></td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Nivel:</td><td style='padding: 10px;'>").append(partido.getNivelMin().getNombre()).append(" - ").append(partido.getNivelMax().getNombre()).append("</td></tr>\n");
        html.append("<tr><td style='padding: 10px; font-weight: bold; color: #555;'>Organizador:</td><td style='padding: 10px;'>").append(partido.getOrganizador().getUsername()).append("</td></tr>\n");
        html.append("</table>\n");
        
        html.append("<div style='background-color: #e8f5e9; border-left: 4px solid #4CAF50; padding: 15px; margin: 20px 0; border-radius: 5px;'>\n");
        html.append("<p style='margin: 0; color: #2e7d32;'><strong>✅ Este partido es perfecto para ti porque:</strong></p>\n");
        html.append("<ul style='margin: 10px 0 0 20px; color: #2e7d32;'>\n");
        html.append("<li>Está a solo ").append(String.format("%.2f", distancia)).append(" km de distancia</li>\n");
        html.append("<li>Es de tu deporte favorito: <strong>").append(partido.getDeporte().getNombre()).append("</strong></li>\n");
        html.append("<li>Tu nivel (").append(usuario.getNivel().getNombre()).append(") está dentro del rango requerido</li>\n");
        html.append("</ul>\n");
        html.append("</div>\n");
        
        html.append("<div style='background-color: #fff3e0; border-left: 4px solid #FF9800; padding: 15px; margin: 20px 0; border-radius: 5px;'>\n");
        html.append("<p style='margin: 0; color: #E65100;'><strong>⚡ ¡No esperes más!</strong></p>\n");
        html.append("<p style='margin: 10px 0 0 0; color: #E65100;'>Ingresa a la plataforma y únete al partido antes de que se llene. ¡Los cupos son limitados!</p>\n");
        html.append("</div>\n");
        
        html.append("<p style='text-align: center; margin-top: 30px; color: #666;'>¡Esperamos verte en el partido!</p>\n");
        html.append("<p style='text-align: center; color: #666;'>El equipo de Sports Matching</p>\n");
        html.append("</div>\n");
        html.append("</div>\n");
        html.append("</body>\n</html>");
        
        return html.toString();
    }
}


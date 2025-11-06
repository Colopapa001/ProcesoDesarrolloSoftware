# Requerimientos funcionales (versión reducida)

Este documento describe los requerimientos funcionales derivados del diagrama `tp-diagrama-reducido.puml`. La solución sigue el patrón MVC e incorpora ciclo de vida de partidos, emparejamiento de jugadores y notificaciones.

## Alcance
- **Aplicación móvil** para organizar y participar de partidos deportivos amateurs.
- Gestión de usuarios, partidos, emparejamiento y notificaciones.

## Actores
- **Usuario**: persona que se registra, crea y/o se suma a partidos.
- **Sistema**: servicios que ejecutan emparejamiento, cambios de estado y notificaciones.

## Supuestos
- Los catálogos de **Nivel** y **Deporte** provienen de base de datos (no hay enums en código ni diagrama).
- La app móvil opera autenticada por usuario.
- La hora/ubicación de un partido es conocida y válida al crearlo.

## Requerimientos funcionales

1. Registro de usuarios
   - El sistema permitirá registrar usuarios con: `username`, `email`, `password` y selección de `Nivel` (opcional).
   - El sistema validará unicidad de `email` y `username`.

2. Consulta de catálogos
   - El sistema expondrá listados de `Deporte` y `Nivel` para selección en formularios.

3. Búsqueda de partidos
   - El usuario podrá buscar partidos por criterios: `Deporte`, ubicación (zona), fecha/hora, estado y disponibilidad de cupos.
   - El sistema mostrará cantidad de jugadores requeridos vs. inscriptos.

4. Creación de partido
   - El usuario podrá crear un partido definiendo: `Deporte`, `jugadoresRequeridos`, duración, `ubicación`, `fechaHora`.
   - Al crearse, el partido quedará en estado inicial: "Necesitamos jugadores".

5. Gestión de jugadores en partido
   - Un usuario podrá postularse/inscribirse a un partido con cupos disponibles.
   - El sistema impedirá inscripciones duplicadas del mismo usuario.
   - El organizador podrá cancelar la inscripción de un jugador antes del inicio.

6. Transiciones de estado del partido
   - Cuando el número de jugadores inscriptos alcance `jugadoresRequeridos`, el estado pasará automáticamente a "Partido armado".
   - El organizador podrá confirmar el partido, pasando a estado "Confirmado" cuando todos los jugadores acepten.
   - Al llegar la `fechaHora` del encuentro, el sistema transicionará a "En juego" automáticamente.
   - Al finalizar el tiempo del encuentro, el sistema transicionará a "Finalizado".
   - El organizador podrá cancelar el partido antes del inicio, pasando a "Cancelado".

7. Estrategia de emparejamiento (Strategy)
   - El sistema permitirá sugerir jugadores para un partido mediante distintos algoritmos: por `Nivel`, por cercanía y por historial previo.
   - El sistema permitirá cambiar la estrategia activa sin afectar a los consumidores (p.ej., configuración en `MatchmakingService`).
   - Las sugerencias deberán respetar restricciones de nivel mín/máx del partido si estuvieran definidas.

8. Notificaciones (Observer + Adapter)
   - El sistema enviará notificaciones cuando:
     - Se cree un partido del deporte favorito de un usuario.
     - Un partido pase a "Partido armado".
     - Un partido sea "Confirmado".
     - Un partido cambie a "En juego", "Finalizado" o "Cancelado".
   - Deberán soportarse al menos dos canales: correo electrónico y push.
   - Los canales se integrarán mediante adaptadores intercambiables (p.ej., `JavaMail`, `Firebase`).

9. Visualización (MVC)
   - La vista de partido deberá reflejar en tiempo (casi) real los cambios de estado y cupos.
   - Las operaciones del usuario se realizarán a través del controlador, que actualizará el modelo y la vista.

## Reglas de negocio
- Un partido no podrá exceder la capacidad definida en `jugadoresRequeridos`.
- Un usuario no podrá estar inscripto dos veces en el mismo partido.
- No se podrán confirmar partidos sin alcanzar `jugadoresRequeridos`.
- No se podrán inscribir nuevos jugadores en estados "En juego", "Finalizado" o "Cancelado".

## Errores y validaciones
- Mensajes claros ante: cupo completo, partido no disponible, credenciales inválidas, catálogo inexistente.
- Validación de `fechaHora` futura al crear/confirmar partidos.

## Auditoría mínima
- Registrar: creador del partido, timestamps de creación y cambios de estado.

## Fuera de alcance (versión reducida)
- Pagos, calificaciones avanzadas, chat en tiempo real, penalidades y ranking global.

## Trazabilidad con el diagrama
- Estados del partido: mapean a `PartidoState` y sus concretos.
- Emparejamiento: `MatchmakingService` con estrategias intercambiables.
- Notificaciones: `NotificacionSubject` con observadores `EmailObserver`/`PushObserver` y adaptadores de envío.
- Catálogos: `Nivel` y `Deporte` como entidades persistidas.

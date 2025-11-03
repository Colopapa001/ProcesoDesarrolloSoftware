# Sports Matching (MVC + Patterns)

Java 17 console demo implementing MVC and at least 4 design patterns (State, Strategy, Observer, Factory/Facade) for managing community sports matches.

## Build & Run

### Opción 1: Script automático (PowerShell)
```powershell
.\run.ps1
```
El script usa Maven instalado para compilar y ejecutar el proyecto.

### Opción 2: Maven (si está instalado)
```powershell
mvn clean compile exec:java
```

<!-- Gradle eliminado: el proyecto ahora usa Maven -->

### Opción 4: Desde IntelliJ IDEA
1. Abre el proyecto en IntelliJ
2. Asegúrate de que el SDK es Java 17
3. Clic derecho en `Main.java` → **Run 'Main.main()'**
   - O usa el menú: **Run > Run 'Main'**

Logs show notifications and state transitions.

## Functionalities
- Registro de usuarios (en memoria)
- Búsqueda/creación de partidos por deporte
- Estados del partido: "Necesitamos jugadores" → "Partido armado" → "Confirmado" → "En juego" → "Finalizado" (y "Cancelado")
- Emparejamiento por estrategia (por nivel)
- Notificaciones simuladas (Email/Push) con fachada y fábrica
 - Datos de referencia desde JSON: `src/main/resources/data/mockdata.json` (deportes, niveles, canales, eventos). Nuevos items pueden agregarse en memoria y opcionalmente persistirse.

## Patrones aplicados
- State: `MatchState` y estados concretos en `model.state.states.*` controlan el ciclo de vida.
- Strategy: `PlayerMatchingStrategy` con `BySkillLevelStrategy` para sugerir jugadores.
- Observer: `Match` notifica a observadores (`NotificationObserver`) ante eventos.
- Factory + Facade: `NotificationFactory` crea `Notifier`s; `NotificationFacade` unifica envío por canales.
- (Sin enums ni clases finales estáticas): tipos de referencia provienen del JSON y se acceden vía `ReferenceData`.
  Adapter listo para futura integración con JavaMail/Firebase dentro de `EmailNotifier`/`PushNotifier`.

## MVC
- Model: `User`, `Match`, estados, repos.
- View: `ConsoleView` (demo), se suscribe como observador.
- Controller: `UserController`, `MatchController` orquestan servicios.

## GRASP / SOLID / Clean Code
- SRP: Servicios (`MatchService`, `MatchingService`) separados; repositorios; notificaciones aisladas.
- OCP: Nuevas estrategias de emparejamiento/estados/notificadores pueden agregarse sin modificar clientes.
- DIP: Controladores dependen de interfaces/servicios, no de implementaciones concretas.
- Alta cohesión, bajo acoplamiento: responsabilidades claras y dependencias dirigidas por interfaces.
- Nombres descriptivos, funciones pequeñas, sin anidaciones profundas.

## UML
PlantUML en `docs/class-diagram.puml`.
Generar imagen (ejemplo):
```bash
# usando PlantUML CLI o extensiones
plantuml docs/class-diagram.puml
```

## Notas
- Repositorios en memoria para simplificar.
- Se simulan notificaciones (logs). Integraciones reales encajarían vía Adapter dentro de `Notifier`s.


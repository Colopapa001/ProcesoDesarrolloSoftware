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

## Configuración de Email

El sistema soporta múltiples proveedores de email o puede guardar emails en archivos. Puedes configurar el email usando **variables de entorno** (recomendado) o **propiedades del sistema**.

### Opción 1: Sin configuración (Modo Archivo - Recomendado para desarrollo)

Si no configuras credenciales, los emails se guardarán automáticamente en archivos en la carpeta `emails/`:

```powershell
mvn exec:java
```

Los archivos se guardan como: `emails/email_usuario_at_dominio_com_20241103_143022.txt`

### Opción 2: Variables de Entorno (Recomendado)

Las variables de entorno son más seguras y fáciles de configurar. El sistema las prioriza sobre las propiedades del sistema.

#### Script Automático (Windows PowerShell):
```powershell
# Ejecuta el script de configuración
.\setup-email.ps1

# Luego ejecuta la aplicación
mvn exec:java
```

#### Windows PowerShell (Manual):
```powershell
# Configurar variables de entorno
$env:EMAIL_PROVIDER="outlook"
$env:EMAIL_USERNAME="tu-email@outlook.com"
$env:EMAIL_PASSWORD="tu-password"

# Ejecutar la aplicación
mvn exec:java
```

#### Windows CMD:
```cmd
set EMAIL_PROVIDER=outlook
set EMAIL_USERNAME=tu-email@outlook.com
set EMAIL_PASSWORD=tu-password
mvn exec:java
```

#### Linux/Mac:
```bash
export EMAIL_PROVIDER=outlook
export EMAIL_USERNAME=tu-email@outlook.com
export EMAIL_PASSWORD=tu-password
mvn exec:java
```

#### Variables de entorno disponibles:
- `EMAIL_PROVIDER`: `gmail`, `outlook`, `yahoo`, o proveedor personalizado
- `EMAIL_USERNAME`: Tu dirección de email
- `EMAIL_PASSWORD`: Tu contraseña o App Password
- `SMTP_HOST`: (Opcional) Host SMTP personalizado
- `SMTP_PORT`: (Opcional) Puerto SMTP personalizado

### Opción 3: Propiedades del Sistema (-D)

Si prefieres usar propiedades del sistema en lugar de variables de entorno:

#### Gmail:
```powershell
mvn exec:java -Demail.provider=gmail -Demail.username=tu-email@gmail.com -Demail.password=tu-app-password
```

**Nota:** Gmail requiere App Password (genera en: https://myaccount.google.com/apppasswords)

#### Outlook/Hotmail:
```powershell
mvn exec:java -Demail.provider=outlook -Demail.username=tu-email@outlook.com -Demail.password=tu-password
```

#### Yahoo:
```powershell
mvn exec:java -Demail.provider=yahoo -Demail.username=tu-email@yahoo.com -Demail.password=tu-password
```

#### Proveedor personalizado:
```powershell
mvn exec:java -Demail.username=tu-email@dominio.com -Demail.password=tu-password -Dsmtp.host=smtp.tu-proveedor.com -Dsmtp.port=587
```

### Prioridad de Configuración

El sistema lee las configuraciones en este orden (la primera encontrada se usa):
1. **Variables de entorno** (más alta prioridad)
2. **Propiedades del sistema** (`-D`)
3. **Modo archivo** (si no hay configuración)

**Recomendación:** Usa **variables de entorno** para desarrollo y producción. Si no tienes acceso a App Passwords, usa **Outlook** o el **modo archivo** para desarrollo/testing.

## Functionalities
- **Menú interactivo en consola** con opciones:
  - Registrarse e iniciar sesión
  - Crear partidos
  - Buscar partidos por deporte
  - Unirse a partidos
- Registro de usuarios (en memoria)
- Búsqueda/creación de partidos por deporte
- Estados del partido: "Necesitamos jugadores" → "Partido armado" → "Confirmado" → "En juego" → "Finalizado" (y "Cancelado")
- Emparejamiento por estrategia (por nivel)
- **Notificaciones por email reales** (configurables via SMTP)
- Datos de referencia desde JSON: `src/main/resources/data/mockdata.json` (deportes, niveles, canales, eventos)

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

## Uso del Sistema

1. **Ejecutar el programa**: `mvn exec:java` o `.\run.ps1`
2. **Registrarse**: Crear una nueva cuenta con username, email, deporte favorito y nivel
3. **Iniciar sesión**: Autenticarse con username y password
4. **Crear partido**: Definir deporte, jugadores requeridos, ubicación y fecha
5. **Buscar partidos**: Ver partidos disponibles por deporte
6. **Unirse a partido**: Usar el ID del partido para unirse
7. **Recibir emails**: Cuando se crea un partido de tu deporte favorito, recibirás un email automáticamente

## Notas
- Repositorios en memoria para simplificar.
- Email real implementado con JavaMail. Si no se configura, se guarda en archivos en `emails/`.
- El sistema envía emails automáticamente cuando:
  - Se crea un partido de tu deporte favorito
  - Un partido alcanza el número de jugadores requerido
  - Un partido es confirmado
  - Un partido cambia de estado (en juego, finalizado, cancelado)


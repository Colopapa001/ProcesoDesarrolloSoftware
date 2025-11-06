# Script PowerShell para ejecutar el proyecto Sports Matching
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Sistema de Gestion de Partidos" -ForegroundColor Cyan
Write-Host "Deportivos - Ejecutando..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar Java
$javaExe = "C:\Program Files\Java\jdk-21\bin\java.exe"
$javaFound = $false

if (Test-Path $javaExe) {
    Write-Host "[OK] Java encontrado: $javaExe" -ForegroundColor Green
    $javaFound = $true
} else {
    Write-Host "[AVISO] No se encontro Java en: $javaExe" -ForegroundColor Yellow
    Write-Host "Buscando Java en el sistema..." -ForegroundColor Yellow
    
    $javaInPath = Get-Command java -ErrorAction SilentlyContinue
    if ($javaInPath) {
        Write-Host "[OK] Java encontrado en PATH: $($javaInPath.Path)" -ForegroundColor Green
        $javaFound = $true
    } else {
        Write-Host "[ERROR] Java no encontrado. Por favor instala Java 17 o superior." -ForegroundColor Red
        exit 1
    }
}

# Buscar Maven
$mavenFound = $false
$mavenCmd = $null

# Opcion 1: Maven en rutas especificas
$mavenPaths = @(
    "C:\Program Files (x86)\Maven\apache-maven-3.9.11\bin\mvn.cmd",
    "C:\Program Files\Maven\apache-maven-3.9.11\bin\mvn.cmd",
    "C:\apache-maven-3.9.11\bin\mvn.cmd"
)

foreach ($path in $mavenPaths) {
    if (Test-Path $path) {
        $mavenCmd = $path
        $mavenFound = $true
        Write-Host "[OK] Maven encontrado en: $path" -ForegroundColor Green
        break
    }
}

# Opcion 2: Maven en PATH
if (-not $mavenFound) {
    $mavenInPath = Get-Command mvn -ErrorAction SilentlyContinue
    if ($mavenInPath) {
        $mavenCmd = "mvn"
        $mavenFound = $true
        Write-Host "[OK] Maven encontrado en PATH: $($mavenInPath.Path)" -ForegroundColor Green
    }
}

if (-not $mavenFound) {
    Write-Host "[ERROR] Maven no encontrado en el sistema." -ForegroundColor Red
    Write-Host ""
    Write-Host "Opciones:" -ForegroundColor Yellow
    Write-Host "1. Instalar Maven desde: https://maven.apache.org/download.cgi" -ForegroundColor Cyan
    Write-Host "2. Ejecutar desde IntelliJ IDEA (Run > Run 'Main')" -ForegroundColor Cyan
    Write-Host "3. Compilar manualmente con javac" -ForegroundColor Cyan
    exit 1
}

Write-Host ""
Write-Host "Compilando y ejecutando la aplicacion..." -ForegroundColor Cyan
Write-Host ""

# Ejecutar Maven
try {
    & $mavenCmd clean compile exec:java
    $exitCode = $LASTEXITCODE
    if ($exitCode -eq 0) {
        Write-Host ""
        Write-Host "[OK] Aplicacion ejecutada exitosamente" -ForegroundColor Green
    } else {
        Write-Host ""
        Write-Host "[ERROR] Error al ejecutar la aplicacion (codigo: $exitCode)" -ForegroundColor Red
        exit $exitCode
    }
} catch {
    Write-Host ""
    Write-Host "[ERROR] Error al ejecutar Maven: $_" -ForegroundColor Red
    exit 1
}

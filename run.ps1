# Script PowerShell para ejecutar el proyecto
# Opción 1: Si tienes Maven instalado
$mavenPath = "C:\Program Files (x86)\Maven\apache-maven-3.9.11\bin\mvn.cmd"
if (Test-Path $mavenPath) {
    Write-Host "Usando Maven desde $mavenPath..." -ForegroundColor Green
    & $mavenPath clean compile exec:java
    exit
}

if (Get-Command mvn -ErrorAction SilentlyContinue) {
    Write-Host "Usando Maven..." -ForegroundColor Green
    mvn clean compile exec:java
    exit
}

# Compilar manualmente con javac (requiere JARs descargados)
Write-Host "No se encontró Maven en el sistema." -ForegroundColor Yellow
Write-Host "Opciones:" -ForegroundColor Yellow
Write-Host "1. Instalar Maven: https://maven.apache.org/download.cgi" -ForegroundColor Cyan
Write-Host "2. Ejecutar desde IntelliJ IDEA (Run > Run 'Main')" -ForegroundColor Cyan
exit 1


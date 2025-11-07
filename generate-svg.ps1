# Script para generar SVG desde PlantUML
$javaExe = "C:\Program Files\Java\jdk-21\bin\java.exe"

# Verificar que Java existe
if (-not (Test-Path $javaExe)) {
    Write-Host "No se encontro Java en: $javaExe" -ForegroundColor Red
    exit 1
}

Write-Host "Java encontrado: $javaExe" -ForegroundColor Green

$pumlFile = "docs\diagrama 2.puml"
$jarFile = "plantuml-1.2025.10.jar"

if (-not (Test-Path $pumlFile)) {
    Write-Host "No se encontro: $pumlFile" -ForegroundColor Red
    exit 1
}

if (-not (Test-Path $jarFile)) {
    Write-Host "No se encontro: $jarFile" -ForegroundColor Red
    exit 1
}

Write-Host "Generando SVG..." -ForegroundColor Cyan
& $javaExe -jar $jarFile -tsvg $pumlFile

if ($LASTEXITCODE -eq 0) {
    Write-Host "SVG generado exitosamente" -ForegroundColor Green
} else {
    Write-Host "Error al generar el SVG" -ForegroundColor Red
    exit 1
}

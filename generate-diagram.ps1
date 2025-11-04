# Script para generar el diagrama PlantUML localmente
# Esto evita el problema de "Request header is too large"

$pumlFile = "docs\class-diagram-corrected.puml"
$outputDir = "docs"

Write-Host "Generando diagrama PlantUML..." -ForegroundColor Green

# Opción 1: Si tienes PlantUML instalado localmente
if (Get-Command plantuml -ErrorAction SilentlyContinue) {
    Write-Host "Usando PlantUML instalado..." -ForegroundColor Green
    plantuml -tpng "$pumlFile" -o "$outputDir"
    Write-Host "✓ Diagrama generado en: $outputDir\class-diagram-corrected.png" -ForegroundColor Green
    exit 0
}

# Opción 2: Usar Docker (si está disponible)
if (Get-Command docker -ErrorAction SilentlyContinue) {
    Write-Host "Usando PlantUML desde Docker..." -ForegroundColor Yellow
    $absolutePath = (Resolve-Path $pumlFile).Path
    $outputPath = (Resolve-Path $outputDir).Path
    
    docker run --rm -i -v "${outputPath}:/out" plantuml/plantuml:latest -tpng -o /out < "$absolutePath"
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Diagrama generado en: $outputPath\class-diagram-corrected.png" -ForegroundColor Green
        exit 0
    }
}

# Opción 3: Instrucciones
Write-Host "`nNo se encontró PlantUML instalado." -ForegroundColor Yellow
Write-Host "`nOpciones para instalar:" -ForegroundColor Cyan
Write-Host "1. Descargar JAR desde: http://plantuml.com/download" -ForegroundColor White
Write-Host "   Luego ejecutar: java -jar plantuml.jar -tpng $pumlFile -o $outputDir" -ForegroundColor White
Write-Host "`n2. Instalar con Chocolatey:" -ForegroundColor White
Write-Host "   choco install plantuml" -ForegroundColor White
Write-Host "`n3. Usar el servidor web (requiere reiniciar el contenedor con docker-compose restart plantuml)" -ForegroundColor White
Write-Host "   Luego acceder a: http://localhost:8080" -ForegroundColor White
Write-Host "`n4. Usar un servicio online como:" -ForegroundColor White
Write-Host "   - http://www.plantuml.com/plantuml/uml/ (pegar el contenido del archivo .puml)" -ForegroundColor White
Write-Host "   - https://kroki.io/ (también soporta PlantUML)" -ForegroundColor White

exit 1


# Script para configurar variables de entorno de email
# Ejecuta este script antes de correr la aplicación

Write-Host "Configuración de Email para Sports Matching" -ForegroundColor Cyan
Write-Host "==========================================`n" -ForegroundColor Cyan

# Valores por defecto (puedes modificarlos)
$EMAIL_PROVIDER = "outlook"
$EMAIL_USERNAME = "pdstpo@outlook.com"
$EMAIL_PASSWORD = "Contra123"

# Configurar variables de entorno
$env:EMAIL_PROVIDER = $EMAIL_PROVIDER
$env:EMAIL_USERNAME = $EMAIL_USERNAME
$env:EMAIL_PASSWORD = $EMAIL_PASSWORD

Write-Host "✓ Variables de entorno configuradas:" -ForegroundColor Green
Write-Host "  EMAIL_PROVIDER: $EMAIL_PROVIDER" -ForegroundColor White
Write-Host "  EMAIL_USERNAME: $EMAIL_USERNAME" -ForegroundColor White
Write-Host "  EMAIL_PASSWORD: ***" -ForegroundColor White
Write-Host ""
Write-Host "Ahora puedes ejecutar la aplicación con:" -ForegroundColor Yellow
Write-Host "  mvn exec:java" -ForegroundColor Cyan
Write-Host "  o" -ForegroundColor Yellow
Write-Host "  .\run.ps1" -ForegroundColor Cyan
Write-Host ""
Write-Host "Nota: Estas variables solo están configuradas para esta sesión de PowerShell." -ForegroundColor Gray
Write-Host "Si abres una nueva terminal, deberás ejecutar este script nuevamente." -ForegroundColor Gray


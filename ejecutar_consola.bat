@echo off
REM Script para ejecutar el modo consola del sistema
echo ====================================
echo   Sistema de Simulacion de Cobro
echo   Supermercado - Modo Consola
echo ====================================
echo.

REM Verificar que existan los archivos compilados
if not exist bin\Main.class (
    echo ERROR: Archivos no compilados.
    echo Ejecute primero compilar.bat
    echo.
    pause
    exit /b 1
)

REM Ejecutar la aplicacion en modo consola
echo Iniciando simulacion en modo consola...
echo.
java -cp bin Main consola

pause

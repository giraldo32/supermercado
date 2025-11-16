@echo off
REM Script para ejecutar la interfaz grafica del sistema
echo ====================================
echo   Sistema de Simulacion de Cobro
echo   Supermercado - Interfaz Grafica
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

REM Ejecutar la aplicacion
echo Iniciando aplicacion...
echo.
java -cp bin Main

pause

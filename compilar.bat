@echo off
REM Script para compilar el proyecto Supermercado
echo ====================================
echo   Compilando Sistema de Supermercado
echo ====================================
echo.

REM Crear directorio bin si no existe
if not exist bin mkdir bin

REM Compilar con UTF-8 para soportar emojis
echo Compilando archivos Java...
javac -encoding UTF-8 -d bin src\*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ====================================
    echo   Compilacion exitosa!
    echo ====================================
    echo.
    echo Para ejecutar la aplicacion:
    echo   - Interfaz Grafica: java -cp bin Main
    echo   - Modo Consola:     java -cp bin Main consola
    echo.
) else (
    echo.
    echo ====================================
    echo   Error en la compilacion
    echo ====================================
    echo.
    pause
    exit /b 1
)

pause

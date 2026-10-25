@echo off
setlocal
cd /d "%~dp0"
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    pause
    exit /b %ERRORLEVEL%
)
java -jar target/benchmarks.jar
pause

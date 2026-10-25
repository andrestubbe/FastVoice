@echo off
setlocal
cd /d "%~dp0"
call mvn clean compile exec:java -Dexec.mainClass="fastvoice.Demo"
pause

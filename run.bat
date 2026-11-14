@echo off
cd /d "%~dp0"
echo Starting Emergency Contact Management System...
javac -cp ".;lib\*" DBConnection.java EmergencyContactManagement.java
if errorlevel 1 (
    echo.
    echo Compilation failed. Check the errors above.
    pause
    exit /b 1
)
java -cp ".;lib\*" EmergencyContactManagement
pause

@echo off
REM ========================================================
REM SportEdu Development Launcher
REM Menggunakan Maven dari system PATH untuk development
REM Memory optimized untuk sistem dengan RAM terbatas
REM ========================================================

echo Starting SportEdu in DEVELOPMENT mode...
echo Checking system memory...

REM Build dulu menggunakan Maven dari PATH
echo Building project with Maven...
call mvn clean compile -q

if %ERRORLEVEL% neq 0 (
    echo Build failed! Please check compilation errors.
    pause
    exit /b 1
)

echo Build successful! Starting application...

REM Set Java memory parameters optimized untuk RAM terbatas
REM Disesuaikan untuk sistem dengan 7GB RAM atau kurang
set JAVA_OPTS=-Xmx1536m -Xms256m -XX:MaxMetaspaceSize=128m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -Djava.awt.headless=false -Dprism.order=sw -Dprism.vsync=false -Djavafx.animation.fullspeed=true

REM Jalankan menggunakan Maven JavaFX plugin (lebih simple untuk development)
call mvn javafx:run

if %ERRORLEVEL% neq 0 (
    echo.
    echo Error: Application failed to start. Error code: %ERRORLEVEL%
    echo This might be due to:
    echo   1. Maven not found in PATH
    echo   2. Missing JavaFX modules
    echo   3. Insufficient memory
    echo   4. Java version compatibility issues
    echo.
    echo Make sure Maven is installed and added to PATH.
)

pause

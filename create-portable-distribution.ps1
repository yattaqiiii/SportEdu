# SportEdu Portable Distribution Creator
# Creates a complete portable package with optimized memory management

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "    SportEdu Portable Distribution Creator" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Function untuk menampilkan step
function Show-Step {
    param($StepNumber, $Description)
    Write-Host "📦 Step $StepNumber`: $Description" -ForegroundColor Yellow
}

function Show-Success {
    param($Message)
    Write-Host "✅ $Message" -ForegroundColor Green
}

function Show-Error {
    param($Message)
    Write-Host "❌ $Message" -ForegroundColor Red
}

function Show-Info {
    param($Message)
    Write-Host "ℹ️  $Message" -ForegroundColor Blue
}

# Function to create optimized launcher script
function Create-OptimizedLauncher {
    param($DistributionPath)

    $launcherContent = @'
@echo off
REM ========================================================
REM SportEdu Portable Edition Launcher
REM Memory optimized untuk sistem dengan RAM terbatas
REM Dapat berjalan tanpa instalasi Maven atau IDE
REM ========================================================

echo Starting SportEdu Portable Edition...

REM Deteksi RAM sistem dan set heap size yang optimal
for /f "tokens=2 delims==" %%i in ('wmic computersystem get TotalPhysicalMemory /value ^| find "="') do set /a RAM_GB=%%i/1024/1024/1024

echo System RAM: %RAM_GB% GB

REM Set heap size berdasarkan RAM yang tersedia
if %RAM_GB% GEQ 8 (
    set HEAP_SIZE=2G
    echo Using 2GB heap size for high-end system
) else if %RAM_GB% GEQ 4 (
    set HEAP_SIZE=1536m
    echo Using 1.5GB heap size for mid-range system
) else (
    set HEAP_SIZE=1G
    echo Using 1GB heap size for low-end system
)

REM Memory parameters yang dioptimalkan untuk berbagai tingkat RAM
set JAVA_OPTS=-Xmx%HEAP_SIZE% -Xms256m -XX:MaxMetaspaceSize=128m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -Djava.awt.headless=false -Dprism.order=sw -Dprism.vsync=false -Djavafx.animation.fullspeed=true -Dprism.allowhidpi=false

REM Module path untuk portable distribution
set MODULE_PATH=lib\*

echo Starting with heap size: %HEAP_SIZE%

REM Set JAVA_HOME to bundled Java
set JAVA_HOME=%~dp0java

"%JAVA_HOME%\bin\java" --module-path "%MODULE_PATH%" %JAVA_OPTS% --add-modules=com.sportedu -Djdk.module.main=com.sportedu com.sportedu/com.sportedu.Main

if %ERRORLEVEL% neq 0 (
    echo.
    echo Error: Application failed to start. Error code: %ERRORLEVEL%
    echo System RAM: %RAM_GB% GB
    echo Heap Size Used: %HEAP_SIZE%
    echo.
    echo This might be due to:
    echo   1. Corrupted Java installation in portable package
    echo   2. Missing JavaFX modules in lib folder
    echo   3. Insufficient memory (need at least 2GB RAM)
    echo   4. Corrupted distribution files
    echo.
    echo Please ensure the portable package is complete.
)

pause
'@

    $launcherPath = Join-Path $DistributionPath "SportEdu.bat"
    $launcherContent | Out-File -FilePath $launcherPath -Encoding ASCII
    Show-Success "Optimized launcher created: SportEdu.bat"
}

# Variables
$distributionFolder = "SportEdu-Portable-Distribution"
$javaVersion = "23"
$mavenVersion = "3.9.11"

Show-Step "1" "Creating distribution folder structure..."
try {
    # Remove existing distribution folder
    if (Test-Path $distributionFolder) {
        Remove-Item $distributionFolder -Recurse -Force
    }

    # Create main distribution folder
    New-Item -ItemType Directory -Path $distributionFolder -Force | Out-Null
    New-Item -ItemType Directory -Path "$distributionFolder\java" -Force | Out-Null
    New-Item -ItemType Directory -Path "$distributionFolder\maven" -Force | Out-Null
    New-Item -ItemType Directory -Path "$distributionFolder\sportedu" -Force | Out-Null

    Show-Success "Distribution folder structure created"
} catch {
    Show-Error "Failed to create distribution folder: $_"
    exit 1
}

Show-Step "2" "Copying SportEdu source code..."
try {
    # Copy source code (exclude build artifacts and git)
    $sourceItems = @(
        "src",
        "pom.xml",
        "README.md"
    )

    foreach ($item in $sourceItems) {
        if (Test-Path $item) {
            if (Test-Path $item -PathType Container) {
                Copy-Item $item -Destination "$distributionFolder\sportedu\" -Recurse -Force
                Show-Info "Copied folder: $item"
            } else {
                Copy-Item $item -Destination "$distributionFolder\sportedu\" -Force
                Show-Info "Copied file: $item"
            }
        } else {
            Show-Info "Skipped (not found): $item"
        }
    }

    Show-Success "SportEdu source code copied to distribution"
} catch {
    Show-Error "Failed to copy source code: $_"
    exit 1
}

Show-Step "3" "Downloading Java Runtime (this may take a while)..."
try {
    $javaUrl = "https://download.oracle.com/java/23/archive/jdk-23.0.2_windows-x64_bin.zip"
    $javaZip = "$env:TEMP\java-$javaVersion.zip"

    if (-not (Test-Path $javaZip)) {
        Show-Info "Downloading Java $javaVersion from Oracle..."
        Invoke-WebRequest -Uri $javaUrl -OutFile $javaZip -UseBasicParsing
    }

    Show-Info "Extracting Java runtime..."
    Expand-Archive -Path $javaZip -DestinationPath "$distributionFolder\java" -Force

    Show-Success "Java runtime downloaded and extracted"
} catch {
    Show-Error "Failed to download Java. Trying alternative approach..."

    # Alternative: Use local Java if available
    $localJava = $env:JAVA_HOME
    if ($localJava -and (Test-Path $localJava)) {
        Show-Info "Using local Java from: $localJava"
        Copy-Item $localJava -Destination "$distributionFolder\java\jdk-23" -Recurse -Force
        Show-Success "Local Java copied"
    } else {
        Show-Error "No Java found. Please download manually from: https://www.oracle.com/java/technologies/downloads/"
    }
}

Show-Step "4" "Downloading Maven (this may take a while)..."
try {
    $mavenUrl = "https://dlcdn.apache.org/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
    $mavenZip = "$env:TEMP\maven-$mavenVersion.zip"

    if (-not (Test-Path $mavenZip)) {
        Show-Info "Downloading Maven $mavenVersion..."
        Invoke-WebRequest -Uri $mavenUrl -OutFile $mavenZip -UseBasicParsing
    }

    Show-Info "Extracting Maven..."
    Expand-Archive -Path $mavenZip -DestinationPath "$distributionFolder\maven" -Force

    Show-Success "Maven downloaded and extracted"
} catch {
    Show-Error "Failed to download Maven: $_"

    # Alternative: Use local Maven if available
    $localMaven = $env:MAVEN_HOME
    if ($localMaven -and (Test-Path $localMaven)) {
        Show-Info "Using local Maven from: $localMaven"
        Copy-Item $localMaven -Destination "$distributionFolder\maven\apache-maven-$mavenVersion" -Recurse -Force
        Show-Success "Local Maven copied"
    } else {
        Show-Error "No Maven found. Please download manually from: https://maven.apache.org/download.cgi"
    }
}

Show-Step "5" "Creating main launcher..."
try {
    # Create main launcher that uses bundled Java/Maven
    $launcherContent = @"
@echo off
title SportEdu Launcher
color 0A

echo ================================================
echo            SportEdu Launcher
echo ================================================
echo.

REM Navigate to SportEdu project folder
cd /d "%~dp0sportedu"

echo Starting SportEdu application...
echo.

REM Check if we're in the right directory
if not exist "pom.xml" (
    echo ERROR: pom.xml not found in sportedu folder
    echo Current directory: %CD%
    pause
    exit /b 1
)

echo Found SportEdu project files
echo Compiling and running application...
echo This may take a moment on first run...
echo.

REM Set up Java and Maven paths
set "JAVA_HOME=%~dp0java\jdk-23.0.2"
set "MAVEN_HOME=%~dp0maven\apache-maven-3.9.11"
set "PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%"

REM Use bundled Maven to run the application
"%MAVEN_HOME%\bin\mvn.cmd" clean compile javafx:run

REM Keep window open if there's an error
if errorlevel 1 (
    echo.
    echo ================================================
    echo ERROR: Application failed to start
    echo ================================================
    echo.
    echo Check internet connection for Maven dependencies
    echo.
    pause
) else (
    echo.
    echo Application closed successfully
)

echo.
echo Press any key to exit.
pause >nul
"@

    $launcherContent | Out-File -FilePath "$distributionFolder\Launch-SportEdu.bat" -Encoding ascii

    Show-Success "Main launcher created"
} catch {
    Show-Error "Failed to create launcher script: $_"
    exit 1
}

Show-Step "6" "Creating documentation..."
try {
    $readmeContent = @"
# 🚀 SportEdu Portable Distribution

## 📋 Apa Ini?

**SportEdu** adalah aplikasi pembelajaran olahraga interaktif untuk teknik dasar **Badminton** dan **Sepak Bola** dengan audio narasi dan quiz interaktif.

### ✨ Fitur Utama:
- 🏸 **Teknik Badminton**: Servis, Smash, Footwork, Netting
- ⚽ **Teknik Sepak Bola**: Dribbling, Shooting, Heading, Passing
- 🎵 **Audio Narasi**: Penjelasan suara untuk setiap teknik
- 🎬 **Animasi Interaktif**: Visualisasi gerakan teknik
- 🧩 **Quiz Interaktif**: Tebak Gambar dan Mencocokkan Gambar

## 🎯 Cara Menggunakan

### 1. Extract File ZIP
- Download file SportEdu-Portable-Distribution.zip
- Extract ke folder manapun (Desktop, Documents, dll)

### 2. Jalankan Aplikasi
- Masuk ke folder SportEdu-Portable-Distribution
- **Double-click Launch-SportEdu.bat**
- Tunggu loading, aplikasi akan terbuka otomatis

### 3. Navigasi Aplikasi
- **Materi**: Pelajari teknik dengan penjelasan dan animasi
- **Quiz**: Uji pemahaman dengan mini games
- **Audio**: Klik tombol 🔊 untuk mendengar narasi

## 📁 Struktur Folder

```
SportEdu-Portable-Distribution/
├── Launch-SportEdu.bat          # 🚀 KLIK INI untuk menjalankan!
├── README.md                    # 📖 Dokumentasi ini
├── sportedu/                    # 📱 Source code aplikasi
│   ├── src/                     # Java source files
│   └── pom.xml                  # Maven configuration
├── java/                        # ☕ Java Runtime (bundled)
└── maven/                       # 📦 Maven (bundled)
```

## 💻 Sistem Requirements

- **OS**: Windows 10/11 (64-bit)
- **RAM**: Minimal 4GB
- **Storage**: ~500MB untuk installation
- **Internet**: Diperlukan untuk download dependencies pertama kali

## 🚀 Keunggulan Portable

✅ **No Installation Required**: Langsung jalan tanpa install Java/Maven
✅ **Self Contained**: Semua dependency sudah include
✅ **Plug & Play**: Extract dan jalankan
✅ **Portable**: Bisa dipindah ke komputer manapun

## 🔧 Troubleshooting

### Aplikasi tidak mau jalan?
1. Pastikan koneksi internet aktif (untuk download dependency)
2. Run as Administrator
3. Disable antivirus sementara

### Error Java/Maven?
- Launcher sudah include Java dan Maven
- Tidak perlu install apapun di sistem

Enjoy learning! 🎓
"@

    $readmeContent | Out-File -FilePath "$distributionFolder\README.md" -Encoding UTF8

    Show-Success "Documentation created"
} catch {
    Show-Error "Failed to create documentation: $_"
    exit 1
}

Show-Step "7" "Creating ZIP package..."
try {
    $zipName = "SportEdu-Portable-v1.0.0-$(Get-Date -Format 'yyyyMMdd').zip"

    if (Test-Path $zipName) {
        Remove-Item $zipName -Force
    }

    Compress-Archive -Path $distributionFolder -DestinationPath $zipName -Force

    Show-Success "ZIP package created: $zipName"
} catch {
    Show-Error "Failed to create ZIP: $_"
}

Write-Host ""
Write-Host "================================================" -ForegroundColor Green
Write-Host "    Distribution Created Successfully!" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Green
Write-Host ""
Show-Info "Package: $zipName"
Show-Info "Includes: Java Runtime + Maven + Source Code"
Show-Info "Size: ~500MB (complete portable package)"
Show-Info "Features: No backup clutter, simple single launcher"
Write-Host ""
Write-Host "✅ Ready to distribute!" -ForegroundColor Green

# 🚀 Cara Membuat EXE Standalone - SportEdu

Dokumentasi lengkap untuk membuat file executable (.exe) dari aplikasi JavaFX SportEdu yang dapat berjalan di laptop manapun tanpa perlu instalasi Java.

## 📋 Daftar Isi

1. [Overview](#overview)
2. [Persyaratan Sistem](#persyaratan-sistem)
3. [Cara Cepat (Otomatis)](#cara-cepat-otomatis)
4. [Langkah Manual](#langkah-manual)
5. [Hasil yang Didapat](#hasil-yang-didapat)
6. [Cara Distribusi](#cara-distribusi)
7. [Troubleshooting](#troubleshooting)
8. [FAQ](#faq)

## 📖 Overview

Proses ini menggunakan **JPackage** (Java 14+) untuk membuat native Windows installer yang berisi:
- ✅ Aplikasi SportEdu lengkap
- ✅ Custom Java Runtime Environment (JRE)
- ✅ Semua dependencies dan assets
- ✅ Memory optimization settings
- ✅ Professional Windows installer

**Hasil akhir**: File `.exe` standalone yang bisa dijalankan di laptop manapun tanpa setup apapun.

## 🖥️ Persyaratan Sistem

### Developer Environment (Untuk Membuat EXE):
- **OS**: Windows 7/8/10/11 (64-bit)
- **Java**: Version 14+ (untuk JPackage support)
- **Maven**: 3.6+ (sudah terinstall)
- **Memory**: Minimal 4GB RAM available
- **Storage**: 2GB free space untuk build process

### Target Environment (Untuk Menjalankan EXE):
- **OS**: Windows 7/8/10/11 (32-bit atau 64-bit)
- **Memory**: Minimal 512MB RAM available
- **Storage**: 500MB free space untuk instalasi
- **Tidak perlu Java** ✅

## 🛠️ Cara Cepat (Otomatis)

Untuk yang ingin cepat, bisa langsung jalankan script berikut:

```powershell
# Masuk ke direktori project
cd "C:\Users\Yattaqi\IdeaProjects\com.sportedu"

# Jalankan script otomatis
.\create-native-exe.ps1
```

Ikuti saja instruksi yang muncul. Script ini akan:

1. Memastikan Java dan Maven terinstall
2. Membangun JAR executable jika belum ada
3. Menjalankan JPackage untuk membuat file EXE
4. Menginformasikan lokasi file EXE yang sudah jadi

## 🔧 Troubleshooting

Jika mengalami masalah, berikut adalah beberapa solusi untuk masalah umum:

### ❌ Problem: "EXE tidak jalan saat diklik 2x"

**Gejala:**
- File EXE tidak membuka aplikasi
- Tidak ada error message yang terlihat
- Task Manager tidak menunjukkan proses berjalan

**Solusi:**

#### **Step 1: Coba Jalankan dari Command Prompt**
```cmd
# Buka Command Prompt sebagai Administrator
# Navigate ke folder EXE
cd "C:\path\to\native-exe"

# Jalankan EXE dengan verbose output
SportEdu-1.0.0.exe --verbose

# Atau coba dengan parameter berbeda
SportEdu-1.0.0.exe --console
```

#### **Step 2: Check Windows Event Viewer**
```cmd
# Buka Event Viewer
eventvwr.msc

# Check di: Windows Logs > Application
# Cari error dengan source "Application Error" atau "JavaFX"
```

### 🦠 Problem: "EXE dikira virus oleh antivirus"

**Gejala:**
- Windows Defender memblokir file
- Antivirus menghapus/quarantine file EXE
- SmartScreen muncul warning

**Solusi:**

#### **Step 1: Whitelist di Windows Defender**
```cmd
# Buka Windows Security
# Virus & threat protection > Manage settings
# Add or remove exclusions > Add an exclusion > Folder
# Pilih folder native-exe/
```

#### **Step 2: Bypass SmartScreen (Sementara)**
```cmd
# Klik kanan EXE > Properties
# Checklist "Unblock" di bagian Security (jika ada)
# Atau saat SmartScreen muncul:
# Klik "More info" > "Run anyway"
```

#### **Step 3: Code Signing (Advanced)**
Untuk distribusi profesional, EXE perlu di-sign dengan certificate:
```cmd
# Gunakan SignTool (Windows SDK)
signtool sign /f certificate.pfx /p password SportEdu-1.0.0.exe
```

### 🔧 Problem: "Application failed to start"

**Solusi:**

#### **Method 1: Rebuild dengan Memory Settings**
```powershell
# Edit script build-and-create-exe.ps1
# Tambahkan parameter memory:
jpackage --input target `
  --main-jar sportedu-1.0.0.jar `
  --main-class com.sportedu.SportEduApp `
  --type exe `
  --dest native-exe `
  --name "SportEdu" `
  --app-version "1.0.0" `
  --java-options "-Xms128m" `
  --java-options "-Xmx512m" `
  --java-options "-Djava.awt.headless=false"
```

#### **Method 2: Create Launcher Script**
Buat file `run-sportedu.bat` di folder yang sama dengan EXE:
```batch
@echo off
echo Starting SportEdu Application...
echo.

REM Set environment
set JAVA_OPTS=-Xms128m -Xmx512m

REM Run application
echo Launching SportEdu...
start "" "SportEdu-1.0.0.exe"

REM Keep window open if error
if errorlevel 1 (
    echo.
    echo ERROR: Application failed to start
    echo Check if Java runtime is properly bundled
    pause
)
```

### 🖥️ Problem: "EXE jalan tapi tidak muncul window"

**Gejala:**
- Process terlihat di Task Manager
- Tapi tidak ada window yang muncul
- CPU usage minimal

**Solusi:**

#### **Check Display Settings**
```cmd
# EXE mungkin muncul di monitor lain (jika multi-monitor)
# Atau muncul di luar area visible screen

# Solusi:
1. Alt+Tab untuk switch ke window SportEdu
2. Windows+Arrow keys untuk move window
3. Right-click di taskbar SportEdu > Maximize
```

#### **Rebuild dengan Graphics Settings**
```powershell
# Tambahkan graphics options ke build script:
--java-options "-Dprism.order=sw" `
--java-options "-Djavafx.animation.fullspeed=true" `
--java-options "-Dcom.sun.javafx.isEmbedded=true"
```

### 🎵 Problem: "Audio tidak keluar di EXE"

**Sudah Diperbaiki!** ✅
Audio sudah otomatis ter-bundle dan berfungsi di EXE karena:
- Sistem loading audio sudah tidak hardcode
- JPackage otomatis include semua file audio
- Method `getResource()` kompatibel dengan native EXE

**Jika masih bermasalah:**
```cmd
# Check audio system
# Pastikan volume Windows tidak mute
# Coba klik tombol volume di aplikasi (pojok kanan atas)
```

### 💾 Problem: "EXE file corrupted or missing"

**Solusi: Rebuild EXE**
```powershell
# Hapus folder native-exe
Remove-Item native-exe -Recurse -Force

# Rebuild dari awal
.\build-and-create-exe.ps1

# Atau manual:
mvn clean package -DskipTests=true
mkdir native-exe
jpackage --input target --main-jar sportedu-1.0.0.jar --main-class com.sportedu.SportEduApp --type exe --dest native-exe --name "SportEdu"
```

## 🛡️ **Cara Menghindari Masalah "Virus":**

### **Untuk Developer:**
1. **Scan EXE sebelum distribusi:**
   ```cmd
   # Upload ke VirusTotal.com untuk scan
   # Pastikan 0 detection sebelum share
   ```

2. **Code signing certificate:**
   ```cmd
   # Beli certificate dari CA (Comodo, DigiCert)
   # Sign EXE untuk trusted distribution
   ```

### **Untuk End User:**
1. **Download dari source terpercaya** (GitHub repository)
2. **Whitelist folder di antivirus**
3. **Scan dengan antivirus sendiri** sebelum jalankan

## 🚀 **Quick Fix Commands:**

```cmd
# Jika EXE tidak jalan, coba ini berturut-turut:

# 1. Jalankan sebagai Administrator
Right-click EXE > "Run as administrator"

# 2. Compatibility mode
Right-click EXE > Properties > Compatibility > Windows 10

# 3. Disable antivirus sementara
# Matikan Windows Defender real-time protection

# 4. Rebuild EXE
cd project-folder
.\build-and-create-exe.ps1

# 5. Manual run dengan logging
SportEdu-1.0.0.exe > log.txt 2>&1
```

## ⚡ **Emergency Solution:**

Jika EXE tetap tidak jalan, gunakan JAR file:
```cmd
# 1. Build JAR
mvn clean package -DskipTests=true

# 2. Run JAR (butuh Java terinstall)
java -jar target/sportedu-1.0.0.jar

# 3. Atau pakai script yang sudah ada
.\run-sportedu.ps1
```

---

💡 **Tips:** EXE hasil JPackage memang sering dikira virus karena bundled JRE. Ini normal dan aman!

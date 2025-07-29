# Cara Install Maven di Windows

## Langkah 1: Download Maven
1. Buka browser dan kunjungi: https://maven.apache.org/download.cgi
2. Download file "apache-maven-3.9.6-bin.zip" (atau versi terbaru)

## Langkah 2: Extract Maven
1. Extract file zip ke folder C:\Program Files\
2. Rename folder menjadi "maven" sehingga path lengkapnya: C:\Program Files\maven

## Langkah 3: Set Environment Variables
1. Buka "Environment Variables" (cari di Start Menu)
2. Tambahkan variabel sistem baru:
   - Variable name: MAVEN_HOME
   - Variable value: C:\Program Files\maven

3. Edit variabel PATH, tambahkan: %MAVEN_HOME%\bin

## Langkah 4: Verifikasi
Buka Command Prompt baru dan jalankan:
```
mvn --version
```

## Alternatif: Install via Chocolatey
Jika Anda punya Chocolatey:
```
choco install maven
```

## Setelah Maven Terinstall
Kembali ke project directory dan jalankan:
```
mvn clean compile
mvn javafx:run
```

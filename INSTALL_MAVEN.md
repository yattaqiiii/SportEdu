# Cara Install Maven di Windows

## Langkah 1: Download Maven
1. Buka browser dan kunjungi halaman unduhan resmi Maven: https://dlcdn.apache.org/maven/maven-3/3.9.11/source/apache-maven-3.9.11-src.tar.gz
2. Di bawah bagian **Files**, cari dan klik tautan untuk **Binary zip archive**. Nama filenya akan terlihat seperti `apache-maven-x.x.x-bin.zip`.

## Langkah 2: Ekstrak Maven
1. Extract file zip ke folder C:\Program Files\
2. Rename folder menjadi "maven" sehingga path lengkapnya: C:\Program Files\maven

## Langkah 3: Set Environment Variables
1. Buka "Environment Variables" (cari di Start Menu)
2. Tambahkan variabel sistem baru:
   - Variable name: MAVEN_HOME
   - Variable value: C:\Program Files\maven

3. Edit variabel PATH, tambahkan: `%MAVEN_HOME%\bin`
   > **PENTING:** Pastikan Anda mengedit variabel `Path` di bagian **System variables**, bukan di "User variables". Jika tidak berhasil, coba tambahkan path lengkapnya secara langsung: `C:\Program Files\maven\bin`

## Langkah 4: Verifikasi
Buka Command Prompt baru dan jalankan:
```
mvn --version
```

Jika Anda melihat output seperti "Apache Maven 3.9.x", maka instalasi berhasil.
Jika Anda mendapatkan galat 'mvn' is not recognized...', ulangi langkah-langkah di atas dan pastikan variabel PATH sudah benar.

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

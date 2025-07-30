# SportEdu Desktop Application
## Aplikasi Pembelajaran Olahraga Interaktif untuk Anak SD

### 📋 Deskripsi Proyek
SportEdu adalah aplikasi desktop pembelajaran olahraga interaktif yang dirancang khusus untuk anak-anak Sekolah Dasar. Aplikasi ini menggunakan arsitektur **MVP (Model-View-Presenter)** dengan JavaFX untuk antarmuka pengguna yang menarik dan interaktif.

### 🏗️ Arsitektur MVP (Model-View-Presenter)
Aplikasi ini menggunakan arsitektur MVP yang terdiri dari:

#### **Model (`SportEduModel.java`)**
- Mengelola data aplikasi (teknik olahraga, informasi detail, dll.)
- Menyediakan metode untuk mengakses dan memvalidasi data
- Tidak tergantung pada database, data disimpan dalam struktur memori

#### **View (`MainView.java`)**
- Mengelola tampilan UI dan interaksi pengguna
- Mengimplementasikan interface `MainViewListener` untuk komunikasi dengan Presenter
- Bertanggung jawab untuk rendering halaman-halaman aplikasi

#### **Presenter (`MainPresenter.java`)**
- Mengelola logika bisnis dan navigasi
- Mengatur state management aplikasi
- Menjadi penghubung antara View dan Model

### 🎯 Fitur Utama
1. **Halaman Utama** - Menu utama dengan pilihan Materi dan Quiz
2. **Modul Materi** - Pembelajaran teknik olahraga (Sepak Bola & Badminton)
3. **Modul Quiz** - Evaluasi pembelajaran dengan dua mode:
    - Tebak Gambar
    - Cocokkan Gambar
4. **Navigasi Interaktif** - Sistem navigasi dengan tombol panah dan breadcrumb

### 📁 Struktur Folder Proyek
```
sportedu-desktop/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── sportedu/
│   │   │           ├── Main.java
│   │   │           ├── model/
│   │   │           │   └── SportEduModel.java
│   │   │           ├── view/
│   │   │           │   └── MainView.java
│   │   │           └── presenter/
│   │   │               └── MainPresenter.java
│   │   └── resources/
│   │       ├── styles.css
│   │       ├── images/
│   │       ├── Poppins/
│   │       └── audio/
│   └── test/
├── target/
├── pom.xml
└── README.md
```

### 🎨 Desain UI
Aplikasi menggunakan skema warna yang konsisten:
- **Blue Yonder**: `#516BB0`
- **Pale yellow**: `#FEFFC4` 
- **Lemon Tart**: `#FFDE63` 
- **mustard**: `#FFBC4C` 
- **Black russian**: `#1A1E2C` 

### 🚀 Cara Menjalankan Aplikasi

#### Prerequisites
- Java JDK 11 atau lebih baru
- Maven 3.6 atau lebih baru
- JavaFX SDK (jika tidak menggunakan Maven dependencies)

#### Langkah Instalasi
1. **Clone atau Download Project**
   ```bash
   git clone <repository-url>
   cd sportedu-desktop
   ```

2. **Compile Project**
   ```bash
   mvn clean compile
   ```

3. **Run Application**
   ```bash
   mvn javafx:run
   ```

#### Membuat Executable JAR
```bash
mvn clean package
```
File JAR akan tersedia di `target/sportedu-desktop-1.0.0-jar-with-dependencies.jar`

#### Membuat Native Installer (.exe untuk Windows)
```bash
mvn clean package
mvn jpackage:jpackage
```

### 📱 Panduan Penggunaan

#### 1. Halaman Utama
- Tampilan awal aplikasi dengan logo Politeknik Aceh
- Pilihan menu: **Materi** dan **Quiz**
- Klik tombol untuk navigasi ke modul yang diinginkan

#### 2. Modul Materi
- **Pilih Olahraga**: Sepak Bola atau Badminton
- **Pilih Teknik**: 4 teknik dasar per olahraga
    - Sepak Bola: Passing, Dribbling, Shooting, Heading
    - Badminton: Servis, Smash, Footwork, Netting
- **Detail Teknik**: Informasi lengkap dengan teks, gambar animasi singkat (gif), dan audio.

#### 3. Modul Quiz
- **Mulai**: Memulai quiz interaktif
- **Petunjuk**: Panduan cara bermain quiz
- **Mode Quiz**: (menggunakan gambar png)
    - **Tebak Gambar**: Identifikasi gerakan dari gambar
    - **Cocokkan Gambar**: Mencocokkan gambar dengan deskripsi
- **Hasil**: Menampilkan skor dan evaluasi 

#### 4. Navigasi
- **Tombol Kembali**: Kembali ke halaman sebelumnya
- **Tombol Panah**: Navigasi antar konten dalam satu teknik
- **Header Menu**: Akses cepat ke halaman utama

### 🛠️ Pengembangan Lanjutan

#### Fase 2 - Integrasi Media
- [ ] Implementasi audio player untuk narasi
- [ ] Integrasi gambar dan ilustrasi teknik
- [ ] Animasi transisi halaman

#### Fase 3 - Model 3D
- [ ] Integrasi Java 3D untuk model interaktif
- [ ] Kontrol rotasi dan zoom pada model 3D
- [ ] Animasi gerakan teknik olahraga

#### Fase 4 - Quiz Interaktif
- [ ] Sistem scoring dan progress tracking
- [ ] Database soal dan jawaban
- [ ] Laporan hasil evaluasi

### 🔧 Troubleshooting

#### Masalah Umum
1. **JavaFX Runtime Error**
    - Pastikan JavaFX sudah terinstal atau gunakan Maven dependencies
    - Jalankan dengan: `java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -cp target/classes com.sportedu.Main`

2. **CSS Tidak Terbaca**
    - Pastikan file `styles.css` ada di `src/main/resources/`
    - Periksa path resource dalam kode Java

3. **Build Error**
    - Pastikan Java version dan Maven version sesuai requirements
    - Jalankan `mvn clean` sebelum compile ulang

### 📊 Performance Metrics
- **Startup Time**: < 3 detik
- **Memory Usage**: ~100MB RAM
- **File Size**: ~50MB (dengan dependencies)
- **Supported OS**: Windows 10+, macOS 10.14+, Linux Ubuntu 18+

### 🎓 Nilai Edukatif
Aplikasi ini dirancang dengan prinsip pembelajaran yang efektif:
- **Visual Learning**: Gambar dan animasi untuk pemahaman konsep
- **Interactive Learning**: Tombol dan navigasi yang responsif
- **Progressive Learning**: Materi tersusun dari dasar ke lanjutan
- **Assessment**: Quiz untuk evaluasi pemahaman

### 👥 Tim Pengembang
- **Platform**: Desktop (JavaFX)
- **Target User**: Anak Sekolah Dasar (6-12 tahun)
- **Institution**: Politeknik Aceh
- **Development Time**: 4 hari

### 📄 Lisensi
© 2024 Politeknik Aceh. All rights reserved.

---

**Catatan**: Ini adalah versi MVP (Minimum Viable Product) yang fokus pada tampilan dan navigasi dasar. Fitur-fitur lanjutan seperti audio, 3D models, dan quiz interaktif akan dikembangkan pada fase selanjutnya.
# SportEdu Desktop Application

Aplikasi Pembelajaran Olahraga Interaktif untuk Anak SD menggunakan JavaFX dengan arsitektur MVP.

## 🚀 Quick Start

### 1. Setup Environment
```bash
# Pastikan Java 11+ terinstall
java -version

# Install Maven
mvn -version
```

### 2. Run Application
```bash
# Clone project
git clone <repository-url>
cd sportedu-desktop

# Run langsung
mvn javafx:run
```

### 3. Build Executable
```bash
# Create JAR file
mvn clean package

# JAR tersedia di: target/sportedu-desktop-1.0.0-jar-with-dependencies.jar
```

## 📋 Struktur MVP

```
├── Main.java                 # Entry point
├── model/
│   └── SportEduModel.java    # Data management
├── view/
│   └── MainView.java         # UI components
└── presenter/
    └── MainPresenter.java    # Business logic
```

## 🎯 Fitur MVP

✅ **Halaman Utama** - Menu Materi & Quiz  
✅ **Pilihan Olahraga** - Sepak Bola & Badminton  
✅ **Teknik Dasar** - 4 teknik per olahraga  
✅ **Navigasi** - Tombol kembali & panah  
✅ **UI Responsive** - Sesuai desain mockup

## 🎨 Color Scheme

- Header: `#6B73C1`
- Titles: `#2E86AB`
- Primary Button: `#F4A261`
- Secondary Button: `#E63946`
- Background: `#F5E6A3`

## 📱 Navigation Flow

```
Home → Materi → Pilih Olahraga → Teknik → Detail
Home → Quiz → Mulai/Petunjuk
```

## 🛠️ Development Notes

- **Focus**: UI/UX sesuai mockup
- **Architecture**: MVP (tanpa database)
- **Framework**: JavaFX + CSS
- **Build Tool**: Maven
- **Target**: Windows .exe

## Next Phase
- [ ] Audio integration
- [ ] 3D models
- [ ] Interactive quiz
- [ ] Animations
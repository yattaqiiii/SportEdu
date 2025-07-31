package com.sportedu.view;

import com.sportedu.model.Teknik;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * MainView sekarang multifungsi: bisa menampilkan landing page,
 * halaman pilih materi, dan halaman pilih mode kuis.
 */
public class MainView {

    private final BorderPane root;
    private final Stage stage;

    // Tombol-tombol yang perlu diakses oleh Presenter
    public Button materiButton;
    public Button quizButton;
    public Button kembaliButton;
    public Button sepakBolaButton;
    public Button badmintonButton;
    public Button tebakGambarButton;
    public Button cocokGambarButton;

    // Navbar buttons yang perlu diakses oleh Presenter
    public Button navHomeButton;
    public Button navMateriButton;
    public Button navQuizButton;

    // Quiz page buttons yang perlu diakses oleh Presenter
    public Button mulaiButton;
    public Button petunjukButton;


    public MainView(Stage stage) {
        this.stage = stage;
        this.root = new BorderPane();

        // Set ukuran fix 1280x832 sesuai permintaan
        stage.setWidth(1280);
        stage.setHeight(832);
        stage.setResizable(false);

        showLandingPage(); // Tampilan awal
    }

    public Parent getRoot() {
        return root;
    }

    /**
     * Metode untuk mengganti konten utama di tengah BorderPane.
     */
    public void setView(Node view) {
        root.setCenter(view);
    }

    /**
     * Membuat navbar transparan dengan logo dan navigasi teks
     */
    private HBox createNavbar() {
        HBox navbar = new HBox();
        navbar.setPrefHeight(80);
        navbar.setPadding(new Insets(15, 40, 15, 40));
        navbar.setAlignment(Pos.CENTER_LEFT);
        // Change navbar to blue background as per design requirement
        navbar.setStyle("-fx-background-color: #516BB0;");

        // Logo SportEdu di kiri (hanya logo, tanpa teks)
        try {
            ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
            logo.setFitHeight(50);
            logo.setPreserveRatio(true);

            HBox logoContainer = new HBox();
            logoContainer.setAlignment(Pos.CENTER_LEFT);
            logoContainer.getChildren().add(logo);

            navbar.getChildren().add(logoContainer);
        } catch (Exception e) {
            // Fallback jika logo tidak ditemukan - kosong saja
            HBox logoContainer = new HBox();
            navbar.getChildren().add(logoContainer);
        }

        // Spacer untuk mendorong navigasi ke kanan
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        navbar.getChildren().add(spacer);

        // Tombol navigasi teks di kanan sesuai desain
        HBox navButtons = new HBox(30);
        navButtons.setAlignment(Pos.CENTER_RIGHT);

        navHomeButton = new Button("Halaman Utama");
        navHomeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;");
        navHomeButton.setOnMouseEntered(e -> navHomeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));
        navHomeButton.setOnMouseExited(e -> navHomeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));

        navMateriButton = new Button("Materi");
        navMateriButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;");
        navMateriButton.setOnMouseEntered(e -> navMateriButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));
        navMateriButton.setOnMouseExited(e -> navMateriButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));

        navQuizButton = new Button("Quiz");
        navQuizButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;");
        navQuizButton.setOnMouseEntered(e -> navQuizButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));
        navQuizButton.setOnMouseExited(e -> navQuizButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));

        navButtons.getChildren().addAll(navHomeButton, navMateriButton, navQuizButton);
        navbar.getChildren().add(navButtons);

        return navbar;
    }

    /**
     * Update navbar untuk menunjukkan tombol mana yang aktif
     */
    private void updateNavbarActiveState(String activePage) {
        // Reset semua tombol ke state normal
        String normalStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;";
        String activeStyle = "-fx-background-color: rgba(245, 230, 163, 0.2); -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 600; -fx-border-width: 0; -fx-padding: 8 16; -fx-background-radius: 5;";

        if (navHomeButton != null) {
            navHomeButton.setStyle(normalStyle);
            navHomeButton.setOnMouseEntered(e -> {
                if (!"home".equals(activePage)) {
                    navHomeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;");
                }
            });
            navHomeButton.setOnMouseExited(e -> {
                if (!"home".equals(activePage)) {
                    navHomeButton.setStyle(normalStyle);
                } else {
                    navHomeButton.setStyle(activeStyle);
                }
            });
        }

        if (navMateriButton != null) {
            navMateriButton.setStyle(normalStyle);
            navMateriButton.setOnMouseEntered(e -> {
                if (!"materi".equals(activePage)) {
                    navMateriButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;");
                }
            });
            navMateriButton.setOnMouseExited(e -> {
                if (!"materi".equals(activePage)) {
                    navMateriButton.setStyle(normalStyle);
                } else {
                    navMateriButton.setStyle(activeStyle);
                }
            });
        }

        if (navQuizButton != null) {
            navQuizButton.setStyle(normalStyle);
            navQuizButton.setOnMouseEntered(e -> {
                if (!"quiz".equals(activePage)) {
                    navQuizButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;");
                }
            });
            navQuizButton.setOnMouseExited(e -> {
                if (!"quiz".equals(activePage)) {
                    navQuizButton.setStyle(normalStyle);
                } else {
                    navQuizButton.setStyle(activeStyle);
                }
            });
        }

        // Set tombol aktif
        switch (activePage) {
            case "home":
                if (navHomeButton != null) navHomeButton.setStyle(activeStyle);
                break;
            case "materi":
                if (navMateriButton != null) navMateriButton.setStyle(activeStyle);
                break;
            case "quiz":
                if (navQuizButton != null) navQuizButton.setStyle(activeStyle);
                break;
        }
    }

    /**
     * Membuat dan menampilkan Landing Page sesuai desain "1. Landing Page.png"
     */
    public void showLandingPage() {
        // Container utama dengan background dot pattern
        VBox mainContainer = new VBox();
        mainContainer.setPrefSize(1280, 832);

        // Background dengan dot pattern dari vektor
        try {
            ImageView dotBackground = new ImageView(new Image(getClass().getResourceAsStream("/images/dot background.png")));
            dotBackground.setFitWidth(1280);
            dotBackground.setFitHeight(832);
            dotBackground.setPreserveRatio(false);

            // Overlay container untuk konten
            VBox overlayContainer = new VBox();
            overlayContainer.setPrefSize(1280, 832);
            overlayContainer.setStyle("-fx-background-color: transparent;");

            // Navbar di atas
            HBox navbar = createNavbar();

            // Content area utama dengan background box
            VBox contentArea = new VBox(40);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(80, 100, 80, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Main content box dengan background putih dan rounded corners
            VBox contentBox = new VBox(30);
            contentBox.setAlignment(Pos.CENTER);
            contentBox.setPadding(new Insets(50, 60, 50, 60));
            contentBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5);");
            contentBox.setMaxWidth(800);

            // Hero section dengan logo SportEdu sebagai judul
            VBox heroSection = new VBox(20);
            heroSection.setAlignment(Pos.CENTER);

            // Gunakan gambar SportEdu sebagai judul
            try {
                ImageView sportEduLogo = new ImageView(new Image(getClass().getResourceAsStream("/images/SportEdu.png")));
                sportEduLogo.setFitHeight(120);
                sportEduLogo.setPreserveRatio(true);
                sportEduLogo.setSmooth(true);
                heroSection.getChildren().add(sportEduLogo);
            } catch (Exception logoException) {
                // Fallback jika logo SportEdu tidak ditemukan
                Label mainTitle = new Label("SportEdu");
                mainTitle.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;");
                heroSection.getChildren().add(mainTitle);
            }

            // Gunakan gambar subtitle sebagai pengganti teks
            try {
                ImageView subtitleImage = new ImageView(new Image(getClass().getResourceAsStream("/images/Aplikasi Pembelajaran Olahraga Interaktif.png")));
                subtitleImage.setFitHeight(40);
                subtitleImage.setPreserveRatio(true);
                subtitleImage.setSmooth(true);
                heroSection.getChildren().add(subtitleImage);
            } catch (Exception subtitleException) {
                // Fallback jika gambar subtitle tidak ditemukan
                Label subtitle = new Label("Platform pembelajaran olahraga interaktif untuk anak-anak");
                subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: 400; -fx-text-fill: #516BB0; -fx-text-alignment: center;");
                subtitle.setWrapText(true);
                heroSection.getChildren().add(subtitle);
            }

            // Action buttons container
            VBox buttonContainer = new VBox(20);
            buttonContainer.setAlignment(Pos.CENTER);

            // Gunakan gambar tombol yang benar dari resources
            materiButton = UIFactory.createMainButton("/images/Materi_butt.png", 250, 70);
            quizButton = UIFactory.createMainButton("/images/Quiz_butt.png", 250, 70);

            buttonContainer.getChildren().addAll(materiButton, quizButton);

            contentBox.getChildren().addAll(heroSection, buttonContainer);
            contentArea.getChildren().add(contentBox);
            overlayContainer.getChildren().addAll(navbar, contentArea);

            // Stack background dan overlay
            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
            stackPane.getChildren().addAll(dotBackground, overlayContainer);

            setView(stackPane);

        } catch (Exception e) {
            // Fallback jika background dot tidak ditemukan
            System.err.println("Background dot tidak ditemukan, menggunakan background warna solid");

            VBox fallbackContainer = new VBox();
            fallbackContainer.setPrefSize(1280, 832);
            fallbackContainer.setStyle("-fx-background-color: #F5E6A3;");

            HBox navbar = createNavbar();

            VBox contentArea = new VBox(40);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(80, 100, 80, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Main content box dengan background putih
            VBox contentBox = new VBox(30);
            contentBox.setAlignment(Pos.CENTER);
            contentBox.setPadding(new Insets(50, 60, 50, 60));
            contentBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5);");
            contentBox.setMaxWidth(800);

            // Hero section dengan logo SportEdu
            VBox heroSection = new VBox(20);
            heroSection.setAlignment(Pos.CENTER);

            try {
                ImageView sportEduLogo = new ImageView(new Image(getClass().getResourceAsStream("/images/SportEdu.png")));
                sportEduLogo.setFitHeight(120);
                sportEduLogo.setPreserveRatio(true);
                sportEduLogo.setSmooth(true);
                heroSection.getChildren().add(sportEduLogo);
            } catch (Exception logoException) {
                Label mainTitle = new Label("SportEdu");
                mainTitle.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;");
                heroSection.getChildren().add(mainTitle);
            }

            // Gunakan gambar subtitle sebagai pengganti teks
            try {
                ImageView subtitleImage = new ImageView(new Image(getClass().getResourceAsStream("/images/Aplikasi Pembelajaran Olahraga Interaktif.png")));
                subtitleImage.setFitHeight(40);
                subtitleImage.setPreserveRatio(true);
                subtitleImage.setSmooth(true);
                heroSection.getChildren().add(subtitleImage);
            } catch (Exception subtitleException) {
                Label subtitle = new Label("Platform pembelajaran olahraga interaktif untuk anak-anak");
                subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: 400; -fx-text-fill: #516BB0;");
                subtitle.setWrapText(true);

                heroSection.getChildren().add(subtitle);
            }

            VBox buttonContainer = new VBox(20);
            buttonContainer.setAlignment(Pos.CENTER);

            materiButton = UIFactory.createMainButton("/images/Materi_butt.png", 250, 70);
            quizButton = UIFactory.createMainButton("/images/Quiz_butt.png", 250, 70);

            buttonContainer.getChildren().addAll(materiButton, quizButton);

            contentBox.getChildren().addAll(heroSection, buttonContainer);
            contentArea.getChildren().add(contentBox);
            fallbackContainer.getChildren().addAll(navbar, contentArea);

            setView(fallbackContainer);
        }

        // Update navbar untuk menunjukkan halaman aktif
        updateNavbarActiveState("home");
    }

    /**
     * Membuat dan menampilkan Halaman Pilih Materi (Tampilan 3)
     */
    public void showMateriPilihanPage() {
        // Container utama dengan background dot pattern seperti landing page
        VBox mainContainer = new VBox();
        mainContainer.setPrefSize(1280, 832);

        // Background dengan dot pattern dari vektor
        try {
            ImageView dotBackground = new ImageView(new Image(getClass().getResourceAsStream("/images/dot background.png")));
            dotBackground.setFitWidth(1280);
            dotBackground.setFitHeight(832);
            dotBackground.setPreserveRatio(false);

            // Overlay container untuk konten
            VBox overlayContainer = new VBox();
            overlayContainer.setPrefSize(1280, 832);
            overlayContainer.setStyle("-fx-background-color: transparent;");

            // Navbar di atas yang sama seperti halaman lain
            HBox navbar = createNavbar();

            // Content area utama
            VBox contentArea = new VBox(40);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(100, 100, 100, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Judul halaman
            Label title = new Label("Pilih Materi");
            title.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;");

            // Container untuk kartu olahraga
            HBox pilihanBox = new HBox(60);
            pilihanBox.setAlignment(Pos.CENTER);

            // Gunakan UIFactory untuk kartu olahraga
            sepakBolaButton = UIFactory.createSportCard("/images/SepakBola.png", 280, 280);
            badmintonButton = UIFactory.createSportCard("/images/Badminton.png", 280, 280);

            pilihanBox.getChildren().addAll(sepakBolaButton, badmintonButton);

            // Tombol kembali
            kembaliButton = UIFactory.createNavButton("/images/Back.png", 120, 50);

            contentArea.getChildren().addAll(title, pilihanBox, kembaliButton);
            overlayContainer.getChildren().addAll(navbar, contentArea);

            // Stack background dan overlay
            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
            stackPane.getChildren().addAll(dotBackground, overlayContainer);

            setView(stackPane);

        } catch (Exception e) {
            // Fallback jika background dot tidak ditemukan
            VBox fallbackContainer = new VBox();
            fallbackContainer.setPrefSize(1280, 832);
            fallbackContainer.setStyle("-fx-background-color: #F5E6A3;");

            HBox navbar = createNavbar();

            VBox contentArea = new VBox(40);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(100, 100, 100, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            Label title = new Label("Pilih Materi");
            title.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;");

            HBox pilihanBox = new HBox(60);
            pilihanBox.setAlignment(Pos.CENTER);

            sepakBolaButton = UIFactory.createSportCard("/images/SepakBola.png", 280, 280);
            badmintonButton = UIFactory.createSportCard("/images/Badminton.png", 280, 280);

            pilihanBox.getChildren().addAll(sepakBolaButton, badmintonButton);

            kembaliButton = UIFactory.createNavButton("/images/Back.png", 120, 50);

            contentArea.getChildren().addAll(title, pilihanBox, kembaliButton);
            fallbackContainer.getChildren().addAll(navbar, contentArea);

            setView(fallbackContainer);
        }

        // Update navbar untuk menunjukkan halaman materi aktif
        updateNavbarActiveState("materi");
    }

    /**
     * Membuat dan menampilkan Halaman Quiz Utama (Tampilan 2)
     */
    public void showQuizModePage() {
        // Container utama dengan background dot pattern seperti landing page
        VBox mainContainer = new VBox();
        mainContainer.setPrefSize(1280, 832);

        // Background dengan dot pattern dari vektor
        try {
            ImageView dotBackground = new ImageView(new Image(getClass().getResourceAsStream("/images/dot background.png")));
            dotBackground.setFitWidth(1280);
            dotBackground.setFitHeight(832);
            dotBackground.setPreserveRatio(false);

            // Overlay container untuk konten
            VBox overlayContainer = new VBox();
            overlayContainer.setPrefSize(1280, 832);
            overlayContainer.setStyle("-fx-background-color: transparent;");

            // Navbar di atas yang sama seperti landing page
            HBox navbar = createNavbar();

            // Content area utama TANPA background box putih
            VBox contentArea = new VBox(60);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(150, 100, 150, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Hero section dengan gambar Quiz sebagai judul
            VBox heroSection = new VBox(50);
            heroSection.setAlignment(Pos.CENTER);

            // Gunakan gambar QUIZ.png sebagai judul
            try {
                ImageView quizLogo = new ImageView(new Image(getClass().getResourceAsStream("/images/QUIZ.png")));
                quizLogo.setFitHeight(80); // Dikecilkan dari 120 ke 80
                quizLogo.setPreserveRatio(true);
                quizLogo.setSmooth(true);
                heroSection.getChildren().add(quizLogo);
            } catch (Exception logoException) {
                // Fallback jika gambar Quiz tidak ditemukan
                Label title = new Label("Quiz");
                title.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;");
                heroSection.getChildren().add(title);
            }

            // Action buttons container untuk Mulai dan Petunjuk
            VBox buttonContainer = new VBox(30);
            buttonContainer.setAlignment(Pos.CENTER);

            // Gunakan gambar tombol Mulai dan Petunjuk_butt dari resources
            mulaiButton = UIFactory.createMainButton("/images/Mulai.png", 250, 80);
            petunjukButton = UIFactory.createMainButton("/images/Petunjuk_butt.png", 250, 80);

            buttonContainer.getChildren().addAll(mulaiButton, petunjukButton);

            contentArea.getChildren().addAll(heroSection, buttonContainer);
            overlayContainer.getChildren().addAll(navbar, contentArea);

            // Stack background dan overlay
            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
            stackPane.getChildren().addAll(dotBackground, overlayContainer);

            setView(stackPane);

        } catch (Exception e) {
            // Fallback jika background dot tidak ditemukan
            System.err.println("Background dot tidak ditemukan, menggunakan background warna solid");

            VBox fallbackContainer = new VBox();
            fallbackContainer.setPrefSize(1280, 832);
            fallbackContainer.setStyle("-fx-background-color: #F5E6A3;");

            HBox navbar = createNavbar();

            VBox contentArea = new VBox(60);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(150, 100, 150, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            VBox heroSection = new VBox(50);
            heroSection.setAlignment(Pos.CENTER);

            try {
                ImageView quizLogo = new ImageView(new Image(getClass().getResourceAsStream("/images/QUIZ.png")));
                quizLogo.setFitHeight(80); // Dikecilkan dari 120 ke 80
                quizLogo.setPreserveRatio(true);
                quizLogo.setSmooth(true);
                heroSection.getChildren().add(quizLogo);
            } catch (Exception logoException) {
                Label title = new Label("Quiz");
                title.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;");
                heroSection.getChildren().add(title);
            }

            VBox buttonContainer = new VBox(30);
            buttonContainer.setAlignment(Pos.CENTER);

            mulaiButton = UIFactory.createMainButton("/images/Mulai.png", 250, 80);
            petunjukButton = UIFactory.createMainButton("/images/Petunjuk_butt.png", 250, 80);

            buttonContainer.getChildren().addAll(mulaiButton, petunjukButton);

            contentArea.getChildren().addAll(heroSection, buttonContainer);
            fallbackContainer.getChildren().addAll(navbar, contentArea);

            setView(fallbackContainer);
        }

        // Update navbar untuk menunjukkan halaman quiz aktif
        updateNavbarActiveState("quiz");
    }

    /**
     * Menampilkan halaman pemilihan mode quiz (Tebak Gambar vs Mencocokkan Gambar)
     */
    public void showQuizModeSelectionPage() {
        // Container utama dengan background dot pattern dan overlay gelap
        VBox mainContainer = new VBox();
        mainContainer.setPrefSize(1280, 832);

        // Background dengan dot pattern dan overlay gelap
        try {
            ImageView dotBackground = new ImageView(new Image(getClass().getResourceAsStream("/images/dot background.png")));
            dotBackground.setFitWidth(1280);
            dotBackground.setFitHeight(832);
            dotBackground.setPreserveRatio(false);

            VBox darkOverlay = new VBox();
            darkOverlay.setPrefSize(1280, 832);
            darkOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");

            VBox overlayContainer = new VBox();
            overlayContainer.setPrefSize(1280, 832);
            overlayContainer.setStyle("-fx-background-color: transparent;");

            HBox navbar = createNavbar();

            VBox contentArea = new VBox();
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(0, 100, 0, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            VBox contentBox = new VBox(30);
            contentBox.setAlignment(Pos.CENTER);
            contentBox.setPadding(new Insets(40, 50, 40, 50));
            contentBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 15, 0, 0, 8);");
            contentBox.setMaxWidth(600);
            contentBox.setMaxHeight(400);

            // Top bar: Cancel button (X) at top right
            HBox topBar = new HBox();
            topBar.setAlignment(Pos.TOP_RIGHT);
            topBar.setPrefWidth(600);
            Button cancelButton = UIFactory.createNavButton("/images/Cancel_butt.png", 40, 40); // Smaller X button
            cancelButton.setOnAction(e -> showQuizModePage());
            topBar.getChildren().add(cancelButton);

            // Header with smaller 'Pilih Mode' image
            VBox headerSection = new VBox(20);
            headerSection.setAlignment(Pos.CENTER);
            try {
                ImageView pilihModeImage = new ImageView(new Image(getClass().getResourceAsStream("/images/Pilih Mode.png")));
                pilihModeImage.setFitHeight(40); // Smaller than before
                pilihModeImage.setPreserveRatio(true);
                pilihModeImage.setSmooth(true);
                headerSection.getChildren().add(pilihModeImage);
            } catch (Exception logoException) {
                Label pilihModeLabel = new Label("Pilih Mode Kuis");
                pilihModeLabel.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;");
                headerSection.getChildren().add(pilihModeLabel);
            }

            // Larger quiz mode buttons
            VBox buttonsContainer = new VBox(30);
            buttonsContainer.setAlignment(Pos.CENTER);
            tebakGambarButton = UIFactory.createMainButton("/images/Tebak gambar_butt.png", 340, 90);
            cocokGambarButton = UIFactory.createMainButton("/images/Mencocokkan gambar_butt.png", 340, 90);
            buttonsContainer.getChildren().addAll(tebakGambarButton, cocokGambarButton);

            contentBox.getChildren().addAll(topBar, headerSection, buttonsContainer);
            contentArea.getChildren().add(contentBox);
            overlayContainer.getChildren().addAll(navbar, contentArea);
            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
            stackPane.getChildren().addAll(dotBackground, darkOverlay, overlayContainer);
            setView(stackPane);
        } catch (Exception e) {
            // Fallback jika background dot tidak ditemukan
            System.err.println("Background dot tidak ditemukan, menggunakan background warna solid");

            VBox fallbackContainer = new VBox();
            fallbackContainer.setPrefSize(1280, 832);
            fallbackContainer.setStyle("-fx-background-color: rgba(245, 230, 163, 0.7);"); // Background kuning dengan transparansi

            HBox navbar = createNavbar();

            VBox contentArea = new VBox();
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(0, 100, 0, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            VBox contentBox = new VBox(30);
            contentBox.setAlignment(Pos.CENTER);
            contentBox.setPadding(new Insets(40, 50, 40, 50));
            contentBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 15, 0, 0, 8);");
            contentBox.setMaxWidth(600);
            contentBox.setMaxHeight(400);

            HBox topBar = new HBox();
            topBar.setAlignment(Pos.TOP_RIGHT);
            topBar.setPrefWidth(600);
            Button cancelButton = UIFactory.createNavButton("/images/Cancel_butt.png", 40, 40);
            cancelButton.setOnAction(event -> showQuizModePage());
            topBar.getChildren().add(cancelButton);

            VBox headerSection = new VBox(20);
            headerSection.setAlignment(Pos.CENTER);
            try {
                ImageView pilihModeImage = new ImageView(new Image(getClass().getResourceAsStream("/images/Pilih Mode.png")));
                pilihModeImage.setFitHeight(40);
                pilihModeImage.setPreserveRatio(true);
                pilihModeImage.setSmooth(true);
                headerSection.getChildren().add(pilihModeImage);
            } catch (Exception logoException) {
                Label pilihModeLabel = new Label("Pilih Mode Kuis");
                pilihModeLabel.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;");
                headerSection.getChildren().add(pilihModeLabel);
            }
            VBox buttonsContainer = new VBox(30);
            buttonsContainer.setAlignment(Pos.CENTER);
            tebakGambarButton = UIFactory.createMainButton("/images/Tebak gambar_butt.png", 340, 90);
            cocokGambarButton = UIFactory.createMainButton("/images/Mencocokkan gambar_butt.png", 340, 90);
            buttonsContainer.getChildren().addAll(tebakGambarButton, cocokGambarButton);
            contentBox.getChildren().addAll(topBar, headerSection, buttonsContainer);
            contentArea.getChildren().add(contentBox);
            fallbackContainer.getChildren().addAll(navbar, contentArea);
            setView(fallbackContainer);
        }
        updateNavbarActiveState("quiz");
    }
}

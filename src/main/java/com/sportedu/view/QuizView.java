package com.sportedu.view;

import com.sportedu.model.Soal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * View untuk menampilkan gameplay kuis Tebak Gambar dengan desain sesuai tampilan 2.1
 * Menggunakan navbar, dot background, dan layout yang proper
 */
public class QuizView {

    private final BorderPane view;
    private final Button kembaliButton;
    public Button[] pilihanButtons;

    // Navbar buttons - akan di-set dari MainView
    public Button navHomeButton;
    public Button navMateriButton;
    public Button navQuizButton;

    public QuizView() {
        view = new BorderPane();
        view.setPrefSize(1280, 832);

        kembaliButton = UIFactory.createNavButton("/images/Back.png", 120, 50);
        setupLayout();
    }

    private void setupLayout() {
        // Background dengan dot pattern sesuai desain
        try {
            ImageView dotBackground = new ImageView(new Image(getClass().getResourceAsStream("/images/dot background.png")));
            dotBackground.setFitWidth(1280);
            dotBackground.setFitHeight(832);
            dotBackground.setPreserveRatio(false);

            // Overlay container untuk konten
            BorderPane overlayContainer = new BorderPane();
            overlayContainer.setPrefSize(1280, 832);
            overlayContainer.setStyle("-fx-background-color: transparent;");

            // Navbar di atas - sama seperti halaman lain
            HBox navbar = createNavbar();
            overlayContainer.setTop(navbar);

            // Stack background dan overlay
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(dotBackground, overlayContainer);

            view.setCenter(stackPane);

        } catch (Exception e) {
            // Fallback jika background tidak ditemukan
            view.setStyle("-fx-background-color: #F5E6A3;");
        }
    }

    /**
     * Membuat navbar yang sama seperti di halaman lain
     */
    private HBox createNavbar() {
        HBox navbar = new HBox();
        navbar.setPrefHeight(80);
        navbar.setPadding(new Insets(15, 40, 15, 40));
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setStyle("-fx-background-color: #516BB0;");

        // Logo SportEdu di kiri
        try {
            ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
            logo.setFitHeight(50);
            logo.setPreserveRatio(true);

            HBox logoContainer = new HBox();
            logoContainer.setAlignment(Pos.CENTER_LEFT);
            logoContainer.getChildren().add(logo);
            navbar.getChildren().add(logoContainer);
        } catch (Exception e) {
            HBox logoContainer = new HBox();
            navbar.getChildren().add(logoContainer);
        }

        // Spacer
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        navbar.getChildren().add(spacer);

        // Tombol navigasi
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
        navQuizButton.setStyle("-fx-background-color: rgba(245, 230, 163, 0.2); -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 600; -fx-border-width: 0; -fx-padding: 8 16; -fx-background-radius: 5;");

        navButtons.getChildren().addAll(navHomeButton, navMateriButton, navQuizButton);
        navbar.getChildren().add(navButtons);

        return navbar;
    }

    public void displaySoal(Soal soal) {
        // Hapus konten lama dari overlay
        BorderPane overlayContainer = (BorderPane) ((StackPane) view.getCenter()).getChildren().get(1);

        // Content area utama
        VBox contentArea = new VBox(30);
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setPadding(new Insets(40, 80, 40, 80));
        VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

        // Header dengan logo "Tebak Gambar"
        VBox headerSection = new VBox(20);
        headerSection.setAlignment(Pos.CENTER);

        try {
            ImageView tebakGambarLogo = new ImageView(new Image(getClass().getResourceAsStream("/images/Tebak Gambar.png")));
            tebakGambarLogo.setFitHeight(60);
            tebakGambarLogo.setPreserveRatio(true);
            headerSection.getChildren().add(tebakGambarLogo);
        } catch (Exception e) {
            Label title = new Label("Tebak Gambar");
            title.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;");
            headerSection.getChildren().add(title);
        }

        // Gambar soal TERPISAH di atas - dengan background kuning
        VBox gambarContainer = new VBox();
        gambarContainer.setAlignment(Pos.CENTER);
        gambarContainer.setPadding(new Insets(30));
        gambarContainer.setStyle("-fx-background-color: #FFBC4C; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5);");
        gambarContainer.setMaxWidth(500);

        ImageView gambarSoal = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream(soal.getPertanyaan()));
            gambarSoal.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading quiz image: " + soal.getPertanyaan());
        }
        gambarSoal.setFitHeight(250);
        gambarSoal.setFitWidth(350);
        gambarSoal.setPreserveRatio(true);
        gambarSoal.setStyle("-fx-background-color: white; -fx-border-radius: 15;");

        gambarContainer.getChildren().add(gambarSoal);

        // Container untuk pertanyaan dan jawaban - SATU KOTAK PUTIH
        VBox questionAnswerContainer = new VBox(30);
        questionAnswerContainer.setAlignment(Pos.CENTER);
        questionAnswerContainer.setPadding(new Insets(40));
        questionAnswerContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5);");
        questionAnswerContainer.setMaxWidth(900);

        // Pertanyaan di dalam kotak putih dengan font biru
        Label pertanyaanLabel = new Label("Apakah nama teknik pada gambar ini?");
        pertanyaanLabel.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 26px; -fx-font-weight: 700; -fx-text-fill: #516BB0; -fx-text-alignment: center;");
        pertanyaanLabel.setWrapText(true);

        // Grid untuk pilihan jawaban (2x2) - dengan styling yang sesuai referensi 2.1 (box kuning)
        GridPane pilihanJawabanPane = new GridPane();
        pilihanJawabanPane.setAlignment(Pos.CENTER);
        pilihanJawabanPane.setHgap(30);
        pilihanJawabanPane.setVgap(25);

        pilihanButtons = new Button[4];
        String[] buttonLabels = {"A", "B", "C", "D"};

        for (int i = 0; i < soal.getPilihanJawaban().length; i++) {
            // Format: [A] Jawaban
            String buttonText = "[" + buttonLabels[i] + "] " + soal.getPilihanJawaban()[i];
            pilihanButtons[i] = new Button(buttonText);
            pilihanButtons[i].setPrefSize(350, 70);

            // Styling box kuning sesuai referensi 2.1
            pilihanButtons[i].setStyle(
                "-fx-background-color: #FFBC4C; " +
                "-fx-border-color: #FF9800; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 12; " +
                "-fx-background-radius: 12; " +
                "-fx-font-family: 'Poppins', Arial; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: 600; " +
                "-fx-text-fill: #1A1E2C; " +
                "-fx-text-alignment: center; " +
                "-fx-effect: dropshadow(gaussian, rgba(255,156,0,0.4), 5, 0, 0, 2);"
            );

            // Hover effects dengan warna kuning lebih gelap
            final Button btn = pilihanButtons[i];
            btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #FF9800; " +
                "-fx-border-color: #F57C00; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 12; " +
                "-fx-background-radius: 12; " +
                "-fx-font-family: 'Poppins', Arial; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: white; " +
                "-fx-text-alignment: center; " +
                "-fx-effect: dropshadow(gaussian, rgba(255,156,0,0.6), 8, 0, 0, 3);"
            ));

            btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #FFBC4C; " +
                "-fx-border-color: #FF9800; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 12; " +
                "-fx-background-radius: 12; " +
                "-fx-font-family: 'Poppins', Arial; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: 600; " +
                "-fx-text-fill: #1A1E2C; " +
                "-fx-text-alignment: center; " +
                "-fx-effect: dropshadow(gaussian, rgba(255,156,0,0.4), 5, 0, 0, 2);"
            ));

            int row = i / 2;
            int col = i % 2;
            pilihanJawabanPane.add(pilihanButtons[i], col, row);
        }

        // Masukkan pertanyaan dan jawaban ke dalam SATU kotak putih
        questionAnswerContainer.getChildren().addAll(pertanyaanLabel, pilihanJawabanPane);

        // Tombol kembali di pojok kiri bawah
        HBox bottomSection = new HBox();
        bottomSection.setAlignment(Pos.BOTTOM_LEFT);
        bottomSection.getChildren().add(kembaliButton);

        // Content area berisi: header, gambar (kotak kuning), pertanyaan+jawaban (kotak putih), tombol kembali
        contentArea.getChildren().addAll(headerSection, gambarContainer, questionAnswerContainer, bottomSection);

        overlayContainer.setCenter(contentArea);
    }

    // --- GETTER METHODS ---

    public Parent getView() {
        return view;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }
}

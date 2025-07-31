package com.sportedu.view;

import com.sportedu.model.Teknik;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;

/**
 * View untuk menampilkan animasi GIF teknik olahraga
 * Sesuai dengan desain "3.1.2 Animasi.png" dan "3.1.2 Animasi - volume on.png"
 */
public class AnimasiView {

    private BorderPane view;
    private Button toPenjelasanPrevButton; // Tombol kembali ke penjelasan
    private Button toPenjelasanNextButton; // Tombol next ke penjelasan
    private Label titleLabel;
    private ImageView gifImageView;
    private Label instructionLabel;
    private ImageView titleImageView; // Tambah field untuk title image
    private BorderPane headerPane; // Tambah field untuk header pane

    private HBox createNavbar() {
        HBox navbar = new HBox();
        navbar.setPrefHeight(80);
        navbar.setPrefWidth(1280);
        navbar.setPadding(new Insets(15, 40, 15, 40));
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setStyle("-fx-background-color: #516BB0;");

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

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        navbar.getChildren().add(spacer);

        HBox navButtons = new HBox(30);
        navButtons.setAlignment(Pos.CENTER_RIGHT);

        Button navHomeButton = new Button("Halaman Utama");
        navHomeButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;");
        navHomeButton.setOnMouseEntered(e -> navHomeButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));
        navHomeButton.setOnMouseExited(e -> navHomeButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));

        Button navMateriButton = new Button("Materi");
        navMateriButton.setStyle(
                "-fx-background-color: rgba(245, 230, 163, 0.2); -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 600; -fx-border-width: 0; -fx-padding: 8 16; -fx-background-radius: 5;");

        Button navQuizButton = new Button("Quiz");
        navQuizButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;");
        navQuizButton.setOnMouseEntered(e -> navQuizButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));
        navQuizButton.setOnMouseExited(e -> navQuizButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));

        navButtons.getChildren().addAll(navHomeButton, navMateriButton, navQuizButton);
        navbar.getChildren().add(navButtons);

        return navbar;
    }

    public AnimasiView() {
        view = new BorderPane();
        titleImageView = new ImageView();
        headerPane = new BorderPane();
        view.getStyleClass().add("animation-container");
        view.setPrefSize(1280, 832);

        try {
            // Background dengan dot pattern
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

            // Content area utama
            VBox contentArea = new VBox(40);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(40, 0, 40, 0)); // Sesuaikan dengan TeknikView
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Header dengan judul menggunakan ImageView
            // Gunakan field headerPane yang sudah ada
            titleLabel = new Label(); // tetap ada sebagai fallback
            titleLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 24px; -fx-font-weight: bold;");
            
            // Gunakan titleImageView yang sudah ada sebagai field
            titleImageView.setFitHeight(40);
            titleImageView.setPreserveRatio(true);
            BorderPane.setAlignment(titleImageView, Pos.CENTER);
            headerPane.setCenter(titleImageView);

            // Konten utama dengan background putih
            VBox contentBox = new VBox(30);
            contentBox.setAlignment(Pos.CENTER);
            contentBox.setPadding(new Insets(40));
            contentBox.getStyleClass().add("content-background");
            contentBox.setMaxWidth(1000); // Sesuaikan dengan TeknikView

            // Container untuk GIF animasi
            gifImageView = new ImageView();
            gifImageView.setFitWidth(550);
            gifImageView.setFitHeight(450);
            gifImageView.setPreserveRatio(true);
            gifImageView.getStyleClass().add("technique-image");

            // Label instruksi
            instructionLabel = new Label("Perhatikan gerakan dalam animasi di atas");
            instructionLabel.getStyleClass().add("explanation-text");
            instructionLabel.setAlignment(Pos.CENTER);

            contentBox.getChildren().addAll(gifImageView, instructionLabel);

            // Tombol navigasi dan kontrol
            HBox buttonBox = new HBox(30);
            buttonBox.setAlignment(Pos.CENTER);

            toPenjelasanPrevButton = UIFactory.createNavButton("/images/Back.png", 120, 50);
            toPenjelasanNextButton = UIFactory.createNavButton("/images/Next.png", 120, 50);
            buttonBox.getChildren().addAll(toPenjelasanPrevButton, toPenjelasanNextButton);

            // Tambahkan semua komponen ke content area
            contentArea.getChildren().addAll(headerPane, contentBox, buttonBox);
            overlayContainer.getChildren().addAll(navbar, contentArea);

            // Stack background dan overlay
            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
            stackPane.getChildren().addAll(dotBackground, overlayContainer);

            view.setCenter(stackPane);

        } catch (Exception e) {
            // Fallback jika background dot tidak ditemukan
            System.err.println("Background dot tidak ditemukan, menggunakan background warna solid");
            view.setStyle("-fx-background-color: #F5E6A3;");

            VBox fallbackContainer = new VBox();
            fallbackContainer.setPrefSize(1280, 832);

            HBox navbar = createNavbar();

            VBox contentArea = new VBox(40);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(80, 100, 80, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Header dengan judul
            BorderPane headerPane = new BorderPane();
            titleLabel = new Label();
            headerPane.setCenter(titleLabel);

            // Konten dengan background putih
            VBox contentBox = new VBox(30);
            contentBox.setAlignment(Pos.CENTER);
            contentBox.setPadding(new Insets(40));
            contentBox.getStyleClass().add("content-background");
            contentBox.setMaxWidth(800);

            gifImageView = new ImageView();
            gifImageView.setFitWidth(550);
            gifImageView.setFitHeight(450);
            gifImageView.setPreserveRatio(true);
            gifImageView.getStyleClass().add("technique-image");

            instructionLabel = new Label("Perhatikan gerakan dalam animasi di atas");
            instructionLabel.getStyleClass().add("explanation-text");
            instructionLabel.setAlignment(Pos.CENTER);

            contentBox.getChildren().addAll(gifImageView, instructionLabel);

            HBox buttonBox = new HBox(30);
            buttonBox.setAlignment(Pos.CENTER);

            toPenjelasanPrevButton = UIFactory.createNavButton("/images/Back.png", 120, 50);
            toPenjelasanNextButton = UIFactory.createNavButton("/images/Next.png", 120, 50);
            buttonBox.getChildren().addAll(toPenjelasanPrevButton, toPenjelasanNextButton);

            contentArea.getChildren().addAll(headerPane, contentBox, buttonBox);
            fallbackContainer.getChildren().addAll(navbar, contentArea);

            view.setCenter(fallbackContainer);
        }
    }

    public void displayAnimasi(Teknik teknik) {
        // Set title image berdasarkan nama teknik
        String titleImagePath = "/images/animasi_" + teknik.getNama().toLowerCase() + ".png";
        try {
            Image titleImage = new Image(getClass().getResourceAsStream(titleImagePath));
            if (!titleImage.isError()) {
                titleImageView.setImage(titleImage);
                titleImageView.setFitHeight(40);
                titleImageView.setPreserveRatio(true);
                headerPane.setCenter(titleImageView);
            } else {
                System.err.println("Cannot load title image: " + titleImagePath);
                titleLabel.setText("Animasi " + teknik.getNama()); // fallback ke text
                headerPane.setCenter(titleLabel);
            }
        } catch (Exception e) {
            System.err.println("Error loading title image: " + e.getMessage());
            titleLabel.setText("Animasi " + teknik.getNama()); // fallback ke text
            headerPane.setCenter(titleLabel);
        }

        try {
            // Load GIF animasi
            Image gifImage = new Image(getClass().getResourceAsStream(teknik.getGifPath()));
            if (!gifImage.isError()) {
                gifImageView.setImage(gifImage);
            } else {
                // Fallback jika GIF tidak ditemukan
                gifImageView.setImage(null);
                System.err.println("Cannot load GIF: " + teknik.getGifPath());
                instructionLabel.setText("Animasi tidak tersedia untuk teknik " + teknik.getNama());
            }
        } catch (Exception e) {
            System.err.println("Error loading GIF for " + teknik.getNama() + ": " + e.getMessage());
            gifImageView.setImage(null);
            instructionLabel.setText("Animasi tidak tersedia untuk teknik " + teknik.getNama());
        }
    }

    public Parent getView() {
        return view;
    }

    public Button getToPenjelasanPrevButton() {
        return toPenjelasanPrevButton;
    }

    public Button getToPenjelasanNextButton() {
        return toPenjelasanNextButton;
    }
}

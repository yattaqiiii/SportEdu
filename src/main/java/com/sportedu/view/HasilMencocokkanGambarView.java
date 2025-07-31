package com.sportedu.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * View untuk menampilkan skor akhir Mencocokkan Gambar sebagai OVERLAY
 * Mirip dengan HasilTebakGambarView - background beige + navbar + overlay tipis + modal kecil
 */
public class HasilMencocokkanGambarView {

    private final BorderPane view;
    private Button mainLagiButton;
    private Button kembaliButton;

    // Navbar buttons
    public Button navHomeButton;
    public Button navMateriButton;
    public Button navQuizButton;

    public HasilMencocokkanGambarView() {
        view = new BorderPane();
        view.setPrefSize(1280, 832);

        // Tambahkan stylesheet
        view.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        view.getStyleClass().add("root");

        setupOverlayLayout();
    }

    private void setupOverlayLayout() {
        // Set background beige sebagai base
        view.setStyle("-fx-background-color: #F5E6A3;");

        // Background dengan dot pattern
        try {
            ImageView dotBackground = new ImageView(new Image(getClass().getResourceAsStream("/images/dot background.png")));
            dotBackground.setFitWidth(1280);
            dotBackground.setFitHeight(832);
            dotBackground.setPreserveRatio(false);

            // Container utama dengan background transparan
            BorderPane mainContainer = new BorderPane();
            mainContainer.setPrefSize(1280, 832);
            mainContainer.setStyle("-fx-background-color: transparent;");

            // Navbar di atas (tetap normal)
            HBox navbar = createNavbar();
            mainContainer.setTop(navbar);

            // Content area untuk overlay
            StackPane contentStack = new StackPane();
            contentStack.setPrefSize(1280, 752); // 832 - 80 (navbar height)
            contentStack.setStyle("-fx-background-color: transparent;");

            mainContainer.setCenter(contentStack);

            // Stack background, main container
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(dotBackground, mainContainer);

            view.setCenter(stackPane);

        } catch (Exception e) {
            // Fallback background beige
            view.setStyle("-fx-background-color: #F5E6A3;");

            BorderPane mainContainer = new BorderPane();
            HBox navbar = createNavbar();
            mainContainer.setTop(navbar);

            StackPane contentStack = new StackPane();
            contentStack.setPrefSize(1280, 752);
            mainContainer.setCenter(contentStack);

            view.setCenter(mainContainer);
        }
    }

    /**
     * Membuat navbar seperti halaman lain
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

    public void displayHasil(int skorBenar, int totalSoal) {
        // Akses content stack
        BorderPane mainContainer = (BorderPane) ((StackPane) view.getCenter()).getChildren().get(1);
        StackPane contentStack = (StackPane) mainContainer.getCenter();

        // Bersihkan content yang ada
        contentStack.getChildren().clear();

        // Overlay gelap tipis (sedikit hitam)
        VBox darkOverlay = new VBox();
        darkOverlay.setPrefSize(1280, 752);
        darkOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
        darkOverlay.setAlignment(Pos.CENTER);

        // Modal hasil - kotak putih kecil di tengah
        VBox modal = new VBox(25);
        modal.setAlignment(Pos.CENTER);
        modal.setPrefSize(450, 350);
        modal.setMaxSize(450, 350);
        modal.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);");
        modal.setPadding(new Insets(40));

        // Header dengan gambar "Skor Akhir"
        try {
            ImageView skorImage = new ImageView(new Image(getClass().getResourceAsStream("/images/Skor Akhir.png")));
            skorImage.setFitHeight(50);
            skorImage.setPreserveRatio(true);
            modal.getChildren().add(skorImage);
        } catch (Exception e) {
            Label headerLabel = new Label("Skor Akhir");
            headerLabel.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #516BB0;");
            modal.getChildren().add(headerLabel);
        }

        // Skor
        int persentase = (int) Math.round((double) skorBenar / totalSoal * 100);

        Label skorLabel = new Label("Skor Anda:");
        skorLabel.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 18px; -fx-font-weight: 600; -fx-text-fill: #1A1E2C;");

        Label nilaiLabel = new Label(skorBenar + " / " + totalSoal + " (" + persentase + "%)");
        nilaiLabel.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #516BB0;");

        // Pesan berdasarkan skor
        String pesanText;
        String pesanColor;
        if (persentase >= 80) {
            pesanText = "Excellent! Anda menguasai materi dengan baik!";
            pesanColor = "#2ecc71";
        } else if (persentase >= 60) {
            pesanText = "Good job! Terus tingkatkan kemampuan Anda!";
            pesanColor = "#f39c12";
        } else {
            pesanText = "Keep learning! Pelajari kembali materinya ya!";
            pesanColor = "#e74c3c";
        }

        Label pesanLabel = new Label(pesanText);
        pesanLabel.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: " + pesanColor + "; -fx-text-alignment: center;");
        pesanLabel.setWrapText(true);
        pesanLabel.setMaxWidth(350);

        modal.getChildren().addAll(skorLabel, nilaiLabel, pesanLabel);

        // Tombol aksi
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 0, 0));

        mainLagiButton = new Button("Main Lagi");
        mainLagiButton.setPrefSize(120, 45);
        mainLagiButton.setStyle("-fx-background-color: #516BB0; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 14px; -fx-font-weight: 600; -fx-background-radius: 10;");

        kembaliButton = new Button("Kembali");
        kembaliButton.setPrefSize(120, 45);
        kembaliButton.setStyle("-fx-background-color: #F5E6A3; -fx-text-fill: #1A1E2C; -fx-font-family: 'Poppins', Arial; -fx-font-size: 14px; -fx-font-weight: 600; -fx-background-radius: 10;");

        buttonContainer.getChildren().addAll(mainLagiButton, kembaliButton);
        modal.getChildren().add(buttonContainer);

        darkOverlay.getChildren().add(modal);
        contentStack.getChildren().add(darkOverlay);
    }

    // Getter methods
    public Parent getView() {
        return view;
    }

    public Button getMainLagiButton() {
        return mainLagiButton;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }
}

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
 * View untuk menampilkan skor akhir Tebak Gambar sebagai OVERLAY
 * Seperti konsep quiz-pilih mode: background beige + navbar + overlay tipis + modal kecil
 */
public class HasilTebakGambarView {

    private final BorderPane view;
    private Button mainLagiButton;
    private Button kembaliButton;

    // Navbar buttons
    public Button navHomeButton;
    public Button navMateriButton;
    public Button navQuizButton;

    public HasilTebakGambarView() {
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

        // Modal kecil di tengah
        VBox modal = new VBox(20);
        modal.setAlignment(Pos.CENTER);
        modal.setPadding(new Insets(30));
        modal.getStyleClass().add("result-modal");
        modal.setMaxWidth(400);  // Lebih kecil
        modal.setPrefWidth(400);

        // Header dengan gambar "Skor Akhir"
        try {
            ImageView skorAkhirImage = new ImageView(new Image(getClass().getResourceAsStream("/images/Skor Akhir.png")));
            skorAkhirImage.setFitHeight(35);
            skorAkhirImage.setPreserveRatio(true);
            modal.getChildren().add(skorAkhirImage);
        } catch (Exception e) {
            Label titleLabel = new Label("SKOR AKHIR");
            titleLabel.getStyleClass().add("result-title");
            titleLabel.setAlignment(Pos.CENTER);
            modal.getChildren().add(titleLabel);
        }

        // Skor besar
        Label skorLabel = new Label(skorBenar + " / " + totalSoal);
        skorLabel.getStyleClass().add("result-score");
        skorLabel.setAlignment(Pos.CENTER);

        // Keterangan
        Label keteranganLabel = new Label("Jawaban Benar");
        keteranganLabel.getStyleClass().add("result-message");
        keteranganLabel.setAlignment(Pos.CENTER);

        // Pesan motivasi
        String pesan;
        if (skorBenar == totalSoal) {
            pesan = "🎉 Sempurna! Kamu menguasai semua teknik!";
        } else if (skorBenar >= totalSoal * 0.7) {
            pesan = "👏 Bagus! Terus berlatih untuk hasil yang lebih baik!";
        } else {
            pesan = "💪 Jangan menyerah! Belajar lagi dan coba lagi!";
        }

        Label pesanLabel = new Label(pesan);
        pesanLabel.getStyleClass().add("result-message");
        pesanLabel.setWrapText(true);
        pesanLabel.setMaxWidth(350);
        pesanLabel.setAlignment(Pos.CENTER);

        // Tombol kembali dengan gambar
        Button kembaliKeMenuButton;
        try {
            ImageView kembaliIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/Kembali_kuning.png")));
            kembaliIcon.setFitHeight(75);  // Diperbesar dari 35 ke 50
            kembaliIcon.setPreserveRatio(true);

            kembaliKeMenuButton = new Button();
            kembaliKeMenuButton.setGraphic(kembaliIcon);
            kembaliKeMenuButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 12;");  // Padding juga diperbesar

            final Button finalButton = kembaliKeMenuButton;
            kembaliKeMenuButton.setOnMouseEntered(e -> finalButton.setStyle("-fx-background-color: rgba(245,230,163,0.2); -fx-border-width: 0; -fx-padding: 12; -fx-background-radius: 10;"));
            kembaliKeMenuButton.setOnMouseExited(e -> finalButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 12;"));
        } catch (Exception e) {
            kembaliKeMenuButton = UIFactory.createSecondaryActionButton("Kembali ke Menu");
        }

        // Set buttons untuk kompatibilitas
        mainLagiButton = kembaliKeMenuButton;
        kembaliButton = kembaliKeMenuButton;

        // Susun modal
        modal.getChildren().addAll(skorLabel, keteranganLabel, pesanLabel, kembaliKeMenuButton);

        // Tambahkan modal ke overlay
        darkOverlay.getChildren().add(modal);

        // Tambahkan overlay ke content stack
        contentStack.getChildren().add(darkOverlay);
    }

    /**
     * Getter untuk view utama
     */
    public Parent getView() {
        return view;
    }

    public Button getMainLagiButton() {
        return mainLagiButton;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }

    /**
     * Getter untuk navbar home button
     */
    public Button getNavHomeButton() {
        return navHomeButton;
    }

    /**
     * Getter untuk navbar materi button
     */
    public Button getNavMateriButton() {
        return navMateriButton;
    }

    /**
     * Getter untuk navbar quiz button
     */
    public Button getNavQuizButton() {
        return navQuizButton;
    }
}

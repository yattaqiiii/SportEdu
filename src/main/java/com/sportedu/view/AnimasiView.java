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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.PauseTransition;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.util.Duration;

/**
 * View untuk menampilkan animasi GIF teknik olahraga
 * Sesuai dengan desain "3.1.2 Animasi.png"
 */
public class AnimasiView {

    private VBox view;
    private Button backButton; // Tombol back ke halaman penjelasan
    private Button kembaliButton; // Tombol kembali kuning ke halaman teknik
    private ImageView gifImageView;
    private ImageView explanationImageView; // Gambar penjelasan teknik
    private Label instructionLabel;
    private ImageView titleImageView;

    public AnimasiView() {
        view = new VBox();
        // Remove fixed size to make it responsive

        // Initialize buttons with same sizes as PenjelasanView
        backButton = UIFactory.createNavButton("/images/Back.png", 100, 65); // Same as nextButton in PenjelasanView
        kembaliButton = UIFactory.createNavButton("/images/Kembali_kuning.png", 250, 70); // Same as kembaliButton in
                                                                                          // PenjelasanView

        setupLayout();
    }

    /**
     * Method untuk memaksa reset scale button ke ukuran normal
     * Digunakan untuk memastikan consistency setelah animasi atau hover
     */
    private void forceResetButtonScale(Node button) {
        button.setOpacity(1.0);
        button.setScaleX(1.0);
        button.setScaleY(1.0);
        // Tambahkan CSS class untuk memastikan scale tetap di 1.0
        if (!button.getStyleClass().contains("scale-reset")) {
            button.getStyleClass().add("scale-reset");
        }
    }

    /**
     * Membuat animasi pop out pop in yang berulang untuk kotak putih
     * Animasi lembut yang membuat kotak sedikit membesar dan mengecil berulang
     */
    private void createContinuousPopAnimation(Node element) {
        // Pastikan elemen dimulai dengan scale normal
        element.setScaleX(1.0);
        element.setScaleY(1.0);

        // Animasi scale up (sedikit membesar) - lebih ringan
        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(3.0), element); // Diperlambat untuk lebih smooth
        scaleUp.setFromX(1.0);
        scaleUp.setFromY(1.0);
        scaleUp.setToX(1.015); // Dikurangi dari 1.03 ke 1.015 untuk lebih halus
        scaleUp.setToY(1.015);
        scaleUp.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

        // Animasi scale down (kembali ke ukuran normal) - lebih ringan
        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(3.0), element); // Diperlambat untuk lebih smooth
        scaleDown.setFromX(1.015);
        scaleDown.setFromY(1.015);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        scaleDown.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

        // Gabungkan animasi secara berurutan
        SequentialTransition breathingAnimation = new SequentialTransition(scaleUp, scaleDown);
        breathingAnimation.setCycleCount(javafx.animation.Animation.INDEFINITE); // Berulang terus

        // Mulai animasi setelah delay kecil
        PauseTransition delay = new PauseTransition(Duration.seconds(1.5)); // Delay sedikit lebih lama untuk tidak mengganggu
        delay.setOnFinished(e -> breathingAnimation.play());
        delay.play();
    }

    /**
     * Membuat animasi pop out untuk elemen (logo/button)
     * Animasi: kosong -> membesar -> mengecil ke ukuran normal
     */
    private void createPopOutAnimation(Node element, double delaySeconds) {
        createPopOutAnimation(element, delaySeconds, null);
    }

    /**
     * Membuat animasi pop out untuk elemen dengan callback setelah selesai
     */
    private void createPopOutAnimation(Node element, double delaySeconds, Runnable onComplete) {
        // Set elemen benar-benar tidak terlihat di awal (opacity 0 + scale 0)
        element.setOpacity(0);
        element.setScaleX(0);
        element.setScaleY(0);

        // Disable mouse events selama animasi untuk mencegah hover interference
        element.setDisable(true);

        // Delay sebelum animasi dimulai
        PauseTransition delay = new PauseTransition(Duration.seconds(delaySeconds));

        // Animasi opacity dari 0 ke 1 (muncul) bersamaan dengan scale
        FadeTransition fadeIn = new FadeTransition(Duration.millis(100), element);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Animasi scale dari 0 ke 1.2 (membesar)
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), element);
        scaleUp.setFromX(0);
        scaleUp.setFromY(0);
        scaleUp.setToX(1.2);
        scaleUp.setToY(1.2);

        // Animasi scale dari 1.2 ke 1.0 (mengecil ke normal)
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), element);
        scaleDown.setFromX(1.2);
        scaleDown.setFromY(1.2);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Gabungkan fade in dengan scale up secara bersamaan
        ParallelTransition fadeAndScaleUp = new ParallelTransition(fadeIn, scaleUp);

        // Gabungkan animasi berurutan: delay -> (fade+scaleUp) -> scaleDown
        SequentialTransition animation = new SequentialTransition(delay, fadeAndScaleUp, scaleDown);

        // Setelah animasi selesai, pastikan scale kembali ke 1.0 dan siapkan untuk
        // hover
        animation.setOnFinished(e -> {
            // PENTING: Pastikan scale benar-benar 1.0 setelah animasi dengan force reset
            forceResetButtonScale(element);

            // Re-enable mouse events setelah animasi selesai
            element.setDisable(false);

            // Tambah delay kecil sebelum hover effects bisa bekerja dengan normal
            PauseTransition enableHoverDelay = new PauseTransition(Duration.millis(100));
            enableHoverDelay.setOnFinished(hoverEvent -> {
                // Execute callback if provided
                if (onComplete != null) {
                    onComplete.run();
                }
            });
            enableHoverDelay.play();
        });

        animation.play();
    }

    private void setupLayout() {
        view.getChildren().clear();

        try {
            // Background dengan dot pattern - make responsive
            ImageView dotBackground = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/dot background.png")));
            // Make background responsive like MainView
            dotBackground.fitWidthProperty().bind(view.widthProperty());
            dotBackground.fitHeightProperty().bind(view.heightProperty());
            dotBackground.setPreserveRatio(false);

            // Main container - make responsive
            StackPane mainContainer = new StackPane();
            mainContainer.prefWidthProperty().bind(view.widthProperty());
            mainContainer.prefHeightProperty().bind(view.heightProperty());

            // Overlay container untuk konten utama - make responsive
            VBox overlayContainer = new VBox();
            overlayContainer.prefWidthProperty().bind(view.widthProperty());
            overlayContainer.prefHeightProperty().bind(view.heightProperty());
            overlayContainer.setStyle("-fx-background-color: transparent;");

            // Logo SportEdu di kiri atas sebagai overlay
            ImageView logoOverlay = new ImageView();
            try {
                logoOverlay = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
                logoOverlay.setFitHeight(50);
                logoOverlay.setPreserveRatio(true);
                logoOverlay.setSmooth(true);
            } catch (Exception logoException) {
                // Jika logo tidak ditemukan, buat placeholder kosong
                logoOverlay = new ImageView();
            }

            // Content area - make responsive and center content
            VBox contentArea = new VBox(12); // Increased spacing for better layout
            contentArea.setAlignment(Pos.CENTER); // Changed from TOP_CENTER to CENTER
            contentArea.setPadding(new Insets(20, 40, 20, 40)); // Increased padding
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Title Image - slightly larger
            titleImageView = new ImageView();
            titleImageView.setFitHeight(35); // Increased from 25 to 35
            titleImageView.setPreserveRatio(true);

            // Main content area - increased spacing and centered
            HBox mainContentArea = new HBox(15); // Increased spacing from 8 to 15
            mainContentArea.setAlignment(Pos.CENTER);

            // Tombol Back di kiri - area yang disesuaikan untuk tombol lebih besar
            VBox leftButtonArea = new VBox();
            leftButtonArea.setAlignment(Pos.CENTER);
            leftButtonArea.setPrefWidth(80); // Increased from 70 to 80 to accommodate larger button
            leftButtonArea.getChildren().add(backButton);

            // White box container - dikecilkan dan lebih compact
            VBox whiteBox = new VBox(10); // Dikurangi spacing dari 12 ke 10
            whiteBox.setAlignment(Pos.CENTER);
            whiteBox.setPadding(new Insets(20, 25, 20, 25)); // Dikurangi padding dari 25,35 ke 20,25
            whiteBox.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"); // Dikurangi radius dan shadow
            whiteBox.setMaxWidth(480); // Dikurangi dari 600 ke 480
            whiteBox.setPrefWidth(480);
            whiteBox.setMaxHeight(420); // Dikurangi dari 550 ke 420

            // Container untuk GIF animasi - dikecilkan
            gifImageView = new ImageView();
            gifImageView.setFitWidth(300); // Dikurangi dari 380 ke 300
            gifImageView.setFitHeight(180); // Dikurangi dari 230 ke 180
            gifImageView.setPreserveRatio(true);

            // Gambar animasi teknik - dikecilkan
            explanationImageView = new ImageView();
            explanationImageView.setFitWidth(280); // Dikurangi dari 340 ke 280
            explanationImageView.setFitHeight(100); // Dikurangi dari 130 ke 100
            explanationImageView.setPreserveRatio(true);

            // Label instruksi - ukuran disesuaikan
            instructionLabel = new Label("Perhatikan gerakan teknik ini dengan seksama");
            instructionLabel.setStyle(
                    "-fx-font-family: 'Poppins', Arial; -fx-font-size: 13px; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;"); // Dikurangi dari 14px ke 13px
            instructionLabel.setWrapText(true);
            instructionLabel.setMaxWidth(440); // Dikurangi dari 540 ke 440
            instructionLabel.setAlignment(Pos.CENTER);
            instructionLabel.setMaxHeight(50); // Dikurangi dari 60 ke 50

            whiteBox.getChildren().addAll(gifImageView, explanationImageView, instructionLabel);

            // Tambahkan animasi pop-out untuk white box saat masuk halaman, kemudian
            // breathing
            createPopOutAnimation(whiteBox, 0.4, () -> {
                // Setelah pop-out selesai, mulai animasi breathing
                createContinuousPopAnimation(whiteBox);
            });

            // Area kanan kosong untuk balance - disesuaikan untuk tombol lebih besar
            VBox rightButtonArea = new VBox();
            rightButtonArea.setPrefWidth(80); // Increased from 70 to 80 to match left side

            mainContentArea.getChildren().addAll(leftButtonArea, whiteBox, rightButtonArea);

            // Animasi pop-out untuk backButton
            createPopOutAnimation(backButton, 0.8);

            // Tombol kembali kuning di bawah - larger area
            HBox bottomButtonArea = new HBox();
            bottomButtonArea.setAlignment(Pos.CENTER);
            bottomButtonArea.setPrefHeight(50); // Increased from 35 to 50
            bottomButtonArea.getChildren().add(kembaliButton);

            // Animasi pop-out untuk kembaliButton
            createPopOutAnimation(kembaliButton, 1.2);

            contentArea.getChildren().addAll(titleImageView, mainContentArea, bottomButtonArea);
            overlayContainer.getChildren().add(contentArea);

            // Stack background dan overlay - responsive
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(dotBackground, overlayContainer);

            // Tambahkan logo di kiri atas sebagai overlay terpisah
            StackPane finalStackPane = new StackPane();
            finalStackPane.getChildren().add(stackPane);

            // Posisikan logo di kiri atas
            finalStackPane.getChildren().add(logoOverlay);
            StackPane.setAlignment(logoOverlay, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlay, new Insets(20, 0, 0, 20));

            // Create ScrollPane like MainView for responsiveness
            javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(finalStackPane);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPannable(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

            view.getChildren().add(scrollPane);

        } catch (Exception e) {
            // Fallback responsive layout
            System.err.println("Background dot tidak ditemukan, menggunakan background warna solid: " + e.getMessage());

            // Main container - make responsive
            StackPane mainContainer = new StackPane();
            mainContainer.prefWidthProperty().bind(view.widthProperty());
            mainContainer.prefHeightProperty().bind(view.heightProperty());
            mainContainer.setStyle("-fx-background-color: #F5E6A3;");

            // Content area responsive - centered and larger spacing
            VBox contentArea = new VBox(12); // Increased spacing
            contentArea.setAlignment(Pos.CENTER); // Changed from TOP_CENTER to CENTER
            contentArea.setPadding(new Insets(20, 40, 20, 40)); // Increased padding
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            titleImageView = new ImageView();
            titleImageView.setFitHeight(35); // Increased size
            titleImageView.setPreserveRatio(true);

            HBox mainContentArea = new HBox(15); // Increased spacing
            mainContentArea.setAlignment(Pos.CENTER);

            VBox leftButtonArea = new VBox();
            leftButtonArea.setAlignment(Pos.CENTER);
            leftButtonArea.setPrefWidth(80); // Increased from 70 to 80 to accommodate larger button
            leftButtonArea.getChildren().add(backButton);

            VBox whiteBox = new VBox(12); // Increased spacing
            whiteBox.setAlignment(Pos.CENTER);
            whiteBox.setPadding(new Insets(25, 35, 25, 35)); // Increased padding
            whiteBox.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0, 0, 5);"); // No
                                                                                                                                                 // border,
                                                                                                                                                 // only
                                                                                                                                                 // shadow
            whiteBox.setMaxWidth(600); // Increased size
            whiteBox.setPrefWidth(600);
            whiteBox.setMaxHeight(550); // Increased size

            gifImageView = new ImageView();
            gifImageView.setFitWidth(380); // Increased size
            gifImageView.setFitHeight(230); // Increased size
            gifImageView.setPreserveRatio(true);

            explanationImageView = new ImageView();
            explanationImageView.setFitWidth(340); // Increased size
            explanationImageView.setFitHeight(130); // Increased size
            explanationImageView.setPreserveRatio(true);

            instructionLabel = new Label("Perhatikan gerakan teknik ini dengan seksama");
            instructionLabel.setStyle(
                    "-fx-font-family: 'Poppins', Arial; -fx-font-size: 14px; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;"); // Increased
                                                                                                                                    // font
                                                                                                                                    // size
            instructionLabel.setWrapText(true);
            instructionLabel.setMaxWidth(540); // Increased size
            instructionLabel.setAlignment(Pos.CENTER);
            instructionLabel.setMaxHeight(60); // Increased size

            whiteBox.getChildren().addAll(gifImageView, explanationImageView, instructionLabel);

            // Tambahkan animasi pop-out untuk white box fallback saat masuk halaman,
            // kemudian breathing
            createPopOutAnimation(whiteBox, 0.4, () -> {
                // Setelah pop-out selesai, mulai animasi breathing
                createContinuousPopAnimation(whiteBox);
            });

            VBox rightButtonArea = new VBox();
            rightButtonArea.setPrefWidth(80); // Increased from 70 to 80 to match left side

            mainContentArea.getChildren().addAll(leftButtonArea, whiteBox, rightButtonArea);

            // Animasi pop-out untuk backButton fallback
            createPopOutAnimation(backButton, 0.8);

            HBox bottomButtonArea = new HBox();
            bottomButtonArea.setAlignment(Pos.CENTER);
            bottomButtonArea.setPrefHeight(50); // Increased size
            bottomButtonArea.getChildren().add(kembaliButton);

            // Animasi pop-out untuk kembaliButton fallback
            createPopOutAnimation(kembaliButton, 1.2);

            contentArea.getChildren().addAll(titleImageView, mainContentArea, bottomButtonArea);

            VBox overlayContainer = new VBox();
            overlayContainer.getChildren().add(contentArea);

            // Logo SportEdu di kiri atas untuk fallback case
            ImageView logoOverlayFallback = new ImageView();
            try {
                logoOverlayFallback = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
                logoOverlayFallback.setFitHeight(50);
                logoOverlayFallback.setPreserveRatio(true);
                logoOverlayFallback.setSmooth(true);
            } catch (Exception logoException) {
                // Jika logo tidak ditemukan, buat placeholder kosong
                logoOverlayFallback = new ImageView();
            }

            mainContainer.getChildren().add(overlayContainer);
            mainContainer.getChildren().add(logoOverlayFallback);
            StackPane.setAlignment(logoOverlayFallback, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlayFallback, new Insets(20, 0, 0, 20));

            // Create ScrollPane for fallback too
            javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(mainContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPannable(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

            view.getChildren().add(scrollPane);
        }
    }

    public void displayAnimasi(Teknik teknik) {
        // Set title image berdasarkan teknik
        try {
            String titlePath = "/images/" + teknik.getNama() + "_title.png";
            try {
                Image titleImage = new Image(getClass().getResourceAsStream(titlePath));
                titleImageView.setImage(titleImage);
                // Animasi pop-out untuk title image
                createPopOutAnimation(titleImageView, 0.2);
            } catch (Exception titleEx) {
                // Fallback ke teks jika gambar title tidak ada
                titleImageView.setImage(null);
            }
        } catch (Exception e) {
            titleImageView.setImage(null);
        }

        // Set GIF animasi dengan debug logging yang lebih baik
        try {
            // Coba GIF dulu dengan nama teknik langsung
            String gifPath = "/images/" + teknik.getNama().toLowerCase() + ".gif";
            System.out.println("Attempting to load GIF: " + gifPath + " for technique: " + teknik.getNama());

            var gifUrl = getClass().getResource(gifPath);
            if (gifUrl != null) {
                System.out.println("GIF resource found: " + gifUrl.toString());
                Image gifImage = new Image(gifUrl.toExternalForm());
                if (!gifImage.isError()) {
                    gifImageView.setImage(gifImage);
                    System.out.println("SUCCESS: GIF loaded for " + teknik.getNama());
                } else {
                    System.err.println("ERROR: GIF image error for " + gifPath);
                    throw new Exception("GIF image error");
                }
            } else {
                System.err.println("ERROR: GIF resource not found: " + gifPath);
                throw new Exception("GIF resource not found");
            }
        } catch (Exception e) {
            System.err.println("FALLBACK: GIF loading failed for " + teknik.getNama() + ", trying PNG");
            try {
                // Fallback ke PNG animasi jika GIF tidak ada
                String pngPath = "/images/animasi_" + teknik.getNama().toLowerCase() + ".png";
                System.out.println("Trying PNG fallback: " + pngPath);
                var pngUrl = getClass().getResource(pngPath);
                if (pngUrl != null) {
                    Image staticImage = new Image(pngUrl.toExternalForm());
                    if (!staticImage.isError()) {
                        gifImageView.setImage(staticImage);
                        System.out.println("SUCCESS: PNG fallback loaded for " + teknik.getNama());
                    } else {
                        System.err.println("ERROR: PNG fallback image error for " + teknik.getNama());
                        gifImageView.setImage(null);
                    }
                } else {
                    System.err.println("ERROR: PNG fallback resource not found for " + teknik.getNama());
                    gifImageView.setImage(null);
                }
            } catch (Exception pngEx) {
                System.err.println("ERROR: PNG fallback also failed for " + teknik.getNama() + ": " + pngEx.getMessage());
                gifImageView.setImage(null);
            }
        }

        // Set gambar animasi teknik
        try {
            String animationPath = "/images/animasi_" + teknik.getNama().toLowerCase() + ".png";
            Image animationImage = new Image(getClass().getResourceAsStream(animationPath));
            explanationImageView.setImage(animationImage);
        } catch (Exception e) {
            System.err.println("Error loading animation image for " + teknik.getNama() + ": " + e.getMessage());
            // Hide animation image if not found
            explanationImageView.setImage(null);
        }

        // Update instruction text
        instructionLabel.setText("Perhatikan gerakan " + teknik.getNama() + " dengan seksama");
    }

    public Parent getView() {
        return view;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }
}

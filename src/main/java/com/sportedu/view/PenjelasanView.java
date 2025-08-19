package com.sportedu.view;

import com.sportedu.model.Teknik;
import javafx.application.Platform;
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
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.TextAlignment;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.PauseTransition;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.util.Duration;
import java.io.InputStream;

/**
 * View untuk menampilkan penjelasan detail tentang teknik olahraga
 * Sesuai dengan desain "3.1.1 Pengertian.png"
 */
public class PenjelasanView {

    private VBox view;
    private Button nextButton; // Tombol next untuk menampilkan overlay GIF
    private Button kembaliButton; // Tombol kembali kuning ke halaman teknik
    private Label descriptionLabel;
    private ImageView teknikImageView;
    private ImageView techniquePhotoView; // Gambar foto teknik (badminton_*.jpg, soccer_*.jpg)
    private ImageView titleImageView;

    // Overlay untuk menampilkan GIF animasi
    private StackPane animationOverlay;
    private ImageView overlayGifView;
    private Button closeOverlayButton;

    private MediaPlayer mediaPlayer; // MediaPlayer untuk audio
    private String currentTeknikName = ""; // Nama teknik saat ini untuk audio

    public PenjelasanView() {
        view = new VBox();
        // Remove fixed size to make it responsive

        // Initialize buttons with larger sizes
        nextButton = UIFactory.createNavButton("/images/Next.png", 100, 65); // Increased from 80,50
        kembaliButton = UIFactory.createNavButton("/images/Kembali_kuning.png", 250, 70); // Increased from 200,55

        // Set event handlers untuk menghentikan audio saat pindah page
        nextButton.setOnAction(e -> {
            // Jangan stop audio karena kita tetap di halaman yang sama
            // stopAudio();
            System.out.println("Next button clicked - showing animation overlay");
            // Event handler asli akan dipanggil oleh presenter untuk menampilkan overlay
        });
        kembaliButton.setOnAction(e -> {
            // Immediate stop untuk menghentikan audio segera
            stopAudio();
            System.out.println("IMMEDIATE audio stop called for KEMBALI navigation");
            // Event handler asli akan dipanggil oleh presenter
        });

        // Listener untuk mendeteksi ketika view tidak terlihat
        view.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                // View menjadi tidak terlihat, stop audio
                onViewDeactivated();
            } else {
                // View menjadi terlihat, reset state
                onViewActivated();
            }
        });

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

        // Animasi scale up (sedikit membesar)
        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(2.0), element);
        scaleUp.setFromX(1.0);
        scaleUp.setFromY(1.0);
        scaleUp.setToX(1.03); // Hanya sedikit membesar (3%)
        scaleUp.setToY(1.03);
        scaleUp.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

        // Animasi scale down (kembali ke ukuran normal)
        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(2.0), element);
        scaleDown.setFromX(1.03);
        scaleDown.setFromY(1.03);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        scaleDown.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

        // Gabungkan animasi secara berurutan
        SequentialTransition breathingAnimation = new SequentialTransition(scaleUp, scaleDown);
        breathingAnimation.setCycleCount(javafx.animation.Animation.INDEFINITE); // Berulang terus

        // Mulai animasi setelah delay kecil
        PauseTransition delay = new PauseTransition(Duration.seconds(1.0));
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

            // Gunakan BorderPane untuk layout responsive
            javafx.scene.layout.BorderPane borderPane = new javafx.scene.layout.BorderPane();
            // Make BorderPane responsive
            borderPane.prefWidthProperty().bind(view.widthProperty());
            borderPane.prefHeightProperty().bind(view.heightProperty());

            // Main container dengan StackPane untuk floating volume button dan background
            StackPane mainContainer = new StackPane();
            // Make mainContainer responsive
            mainContainer.prefWidthProperty().bind(view.widthProperty());
            mainContainer.prefHeightProperty().bind(view.heightProperty());

            // Overlay container untuk konten utama
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

            // Content area - make responsive with medium spacing (HAPUS NAVBAR)
            VBox contentArea = new VBox(15); // Increased from 8 to 15
            contentArea.setAlignment(Pos.TOP_CENTER);
            contentArea.setPadding(new Insets(15, 40, 12, 40)); // Increased padding
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Title Image - ukuran diperbesar
            titleImageView = new ImageView();
            titleImageView.setFitHeight(35); // Increased from 25 to 35
            titleImageView.setPreserveRatio(true);

            // Main content area dengan box putih dan tombol samping - medium spacing
            HBox mainContentArea = new HBox(15); // Increased from 8 to 15
            mainContentArea.setAlignment(Pos.CENTER);
            mainContentArea.setPrefHeight(580); // Increased from 520 to 580

            // Left spacer untuk menyeimbangkan posisi kotak putih
            Region leftSpacer = new Region();
            leftSpacer.setPrefWidth(80); // Increased from 70 to 80

            // White box container - medium size with scroll capability
            VBox whiteBox = new VBox(12); // Increased spacing from 8 to 12
            whiteBox.setAlignment(Pos.CENTER);
            whiteBox.setPadding(new Insets(18, 25, 18, 25)); // Increased padding
            whiteBox.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"); // Larger
                                                                                                                                                // radius
                                                                                                                                                // and
                                                                                                                                                // shadow
            whiteBox.setMaxWidth(550); // Increased from 480 to 550
            whiteBox.setPrefWidth(550);
            whiteBox.setMaxHeight(430); // Increased from 380 to 430

            // Container untuk gambar penjelasan - diperkecil agar tidak perlu scroll
            teknikImageView = new ImageView();
            teknikImageView.setFitWidth(280); // Dikurangi dari 320 ke 280
            teknikImageView.setFitHeight(90); // Dikurangi dari 110 ke 90
            teknikImageView.setPreserveRatio(true);

            // Container untuk gambar teknik - diperkecil agar tidak perlu scroll
            techniquePhotoView = new ImageView();
            techniquePhotoView.setFitWidth(320); // Dikurangi dari 400 ke 320
            techniquePhotoView.setFitHeight(160); // Dikurangi dari 210 ke 160
            techniquePhotoView.setPreserveRatio(true);

            // Container untuk deskripsi - tanpa scroll, teks disesuaikan
            descriptionLabel = new Label();
            descriptionLabel.setStyle(
                    "-fx-font-family: 'Poppins', Arial; -fx-font-size: 12px; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;"); // Font dikurangi ke 12px
            descriptionLabel.setWrapText(true);
            descriptionLabel.setMaxWidth(480);
            descriptionLabel.setMaxHeight(100); // Batasi tinggi untuk mencegah overflow
            descriptionLabel.setAlignment(Pos.CENTER);

            // Hapus scroll pane, langsung masukkan label
            whiteBox.getChildren().addAll(teknikImageView, techniquePhotoView, descriptionLabel);

            // Tambahkan animasi pop-out untuk white box saat masuk halaman, kemudian
            // breathing
            createPopOutAnimation(whiteBox, 0.4, () -> {
                // Setelah pop-out selesai, mulai animasi breathing
                createContinuousPopAnimation(whiteBox);
            });

            // Tombol area kanan dengan Next - medium area
            VBox rightButtonArea = new VBox();
            rightButtonArea.setAlignment(Pos.CENTER);
            rightButtonArea.setPrefWidth(80); // Increased from 70 to 80
            rightButtonArea.getChildren().add(nextButton);

            // Animasi pop-out untuk nextButton
            createPopOutAnimation(nextButton, 0.8);

            mainContentArea.getChildren().addAll(leftSpacer, whiteBox, rightButtonArea);

            // Tombol kembali kuning di bawah - medium area
            HBox bottomButtonArea = new HBox();
            bottomButtonArea.setAlignment(Pos.CENTER);
            bottomButtonArea.setPrefHeight(45); // Increased from 35 to 45
            bottomButtonArea.getChildren().add(kembaliButton);

            // Animasi pop-out untuk kembaliButton
            createPopOutAnimation(kembaliButton, 1.2);

            contentArea.getChildren().addAll(titleImageView, mainContentArea, bottomButtonArea);
            overlayContainer.getChildren().add(contentArea);

            // Floating volume button - remove speaker button from layout
            // StackPane.setAlignment(speakerButton, Pos.TOP_RIGHT);
            // StackPane.setMargin(speakerButton, new Insets(75, 35, 0, 0)); // Increased
            // margins

            // Posisikan logo di kiri atas
            StackPane.setAlignment(logoOverlay, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlay, new Insets(20, 0, 0, 20));

            // Stack background dan overlay - remove speaker button
            mainContainer.getChildren().addAll(dotBackground, overlayContainer, logoOverlay);

            // Create ScrollPane like MainView for responsiveness
            javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(mainContainer);
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

            // Main content container - make responsive
            StackPane mainContainer = new StackPane();
            mainContainer.prefWidthProperty().bind(view.widthProperty());
            mainContainer.prefHeightProperty().bind(view.heightProperty());
            mainContainer.setStyle("-fx-background-color: #F5E6A3;");

            // Content area responsive - medium spacing and padding (HAPUS NAVBAR)
            VBox contentArea = new VBox(15); // Increased from 8 to 15
            contentArea.setAlignment(Pos.TOP_CENTER);
            contentArea.setPadding(new Insets(15, 40, 12, 40)); // Increased padding
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            titleImageView = new ImageView();
            titleImageView.setFitHeight(35); // Increased from 25 to 35
            titleImageView.setPreserveRatio(true);

            HBox mainContentArea = new HBox(15); // Increased from 8 to 15
            mainContentArea.setAlignment(Pos.CENTER);

            // Left spacer untuk menyeimbangkan posisi kotak putih (fallback)
            Region leftSpacerFallback = new Region();
            leftSpacerFallback.setPrefWidth(80); // Increased from 70 to 80

            VBox whiteBox = new VBox(12); // Increased from 8 to 12
            whiteBox.setAlignment(Pos.CENTER);
            whiteBox.setPadding(new Insets(18, 25, 18, 25)); // Increased padding
            whiteBox.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"); // Larger
                                                                                                                                                // radius
                                                                                                                                                // and
                                                                                                                                                // shadow
            whiteBox.setMaxWidth(550); // Increased from 480 to 550
            whiteBox.setPrefWidth(550);
            whiteBox.setMaxHeight(430); // Increased from 380 to 430

            teknikImageView = new ImageView();
            teknikImageView.setFitWidth(320); // Increased from 280 to 320
            teknikImageView.setFitHeight(110); // Increased from 90 to 110
            teknikImageView.setPreserveRatio(true);

            techniquePhotoView = new ImageView();
            techniquePhotoView.setFitWidth(400); // Increased from 350 to 400
            techniquePhotoView.setFitHeight(210); // Increased from 180 to 210
            techniquePhotoView.setPreserveRatio(true);

            descriptionLabel = new Label();
            descriptionLabel.setStyle(
                    "-fx-font-family: 'Poppins', Arial; -fx-font-size: 14px; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;"); // Increased
                                                                                                                                    // font
                                                                                                                                    // size
                                                                                                                                    // from
                                                                                                                                    // 11px
                                                                                                                                    // to
                                                                                                                                    // 14px
            descriptionLabel.setWrapText(true);
            descriptionLabel.setMaxWidth(480); // Slightly smaller to fit in scroll pane
            descriptionLabel.setAlignment(Pos.CENTER);
            // Remove max height to allow full content display

            // Create scroll pane for description in fallback layout too
            javafx.scene.control.ScrollPane descriptionScrollPane = new javafx.scene.control.ScrollPane(
                    descriptionLabel);
            descriptionScrollPane.setFitToWidth(true);
            descriptionScrollPane.setPrefHeight(100); // Set preferred height for scroll area
            descriptionScrollPane.setMaxHeight(120); // Max height to limit scroll area
            descriptionScrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            descriptionScrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
            descriptionScrollPane.setPannable(true);
            descriptionScrollPane.setStyle(
                    "-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

            whiteBox.getChildren().addAll(teknikImageView, techniquePhotoView, descriptionScrollPane);

            // Tambahkan animasi pop-out untuk white box fallback saat masuk halaman,
            // kemudian breathing
            createPopOutAnimation(whiteBox, 0.4, () -> {
                // Setelah pop-out selesai, mulai animasi breathing
                createContinuousPopAnimation(whiteBox);
            });

            VBox rightButtonArea = new VBox();
            rightButtonArea.setAlignment(Pos.CENTER);
            rightButtonArea.setPrefWidth(80); // Increased from 70 to 80
            rightButtonArea.getChildren().add(nextButton);

            // Animasi pop-out untuk nextButton fallback
            createPopOutAnimation(nextButton, 0.8);

            mainContentArea.getChildren().addAll(leftSpacerFallback, whiteBox, rightButtonArea);

            HBox bottomButtonArea = new HBox();
            bottomButtonArea.setAlignment(Pos.CENTER);
            bottomButtonArea.setPrefHeight(45); // Increased from 35 to 45
            bottomButtonArea.getChildren().add(kembaliButton);

            // Animasi pop-out untuk kembaliButton fallback
            createPopOutAnimation(kembaliButton, 1.2);

            contentArea.getChildren().addAll(titleImageView, mainContentArea, bottomButtonArea);

            VBox overlayContainer = new VBox();
            overlayContainer.getChildren().add(contentArea);

            // Remove speaker button positioning code - no longer needed
            // StackPane.setAlignment(speakerButton, Pos.TOP_RIGHT);
            // StackPane.setMargin(speakerButton, new Insets(75, 35, 0, 0)); // Increased
            // margins

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

            // Remove speaker button from fallback layout too
            mainContainer.getChildren().addAll(overlayContainer);
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

    public void displayTeknik(Teknik teknik) {
        // Stop audio dari teknik sebelumnya jika ada
        stopAudio();

        // Simpan nama teknik saat ini untuk audio
        currentTeknikName = teknik.getNama().toLowerCase();

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

        // Set gambar pengertian (pengertian_*.png)
        try {
            String imagePath = "/images/pengertian_" + teknik.getNama().toLowerCase() + ".png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            teknikImageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading explanation image for " + teknik.getNama() + ": " + e.getMessage());
            // Fallback jika gambar tidak ditemukan
            teknikImageView.setImage(null);
        }

        // Set gambar teknik (badminton_*.jpg atau soccer_*.jpg) menggunakan class field
        try {
            String techniqueImagePath = "";
            String teknikName = teknik.getNama().toLowerCase();

            // Mapping untuk gambar teknik yang sesuai
            switch (teknikName) {
                // Badminton techniques
                case "smash":
                    techniqueImagePath = "/images/badminton_smash.jpg";
                    break;
                case "netting":
                    techniqueImagePath = "/images/badminton_netting.jpg";
                    break;
                case "servis":
                    techniqueImagePath = "/images/badminton_service.jpg";
                    break;
                case "footwork":
                    techniqueImagePath = "/images/badminton_footwork.jpg"; // Gambar baru
                    break;
                // Soccer techniques
                case "dribbling":
                    techniqueImagePath = "/images/soccer_dribble.jpg";
                    break;
                case "shooting":
                    techniqueImagePath = "/images/soccer_shoot.jpg";
                    break;
                case "heading":
                    techniqueImagePath = "/images/soccer_heading.jpg";
                    break;
                case "passing":
                    techniqueImagePath = "/images/soccer_passing.jpg"; // Gambar baru
                    break;
                default:
                    System.out.println("No specific technique image found for: " + teknikName);
                    break;
            }

            if (!techniqueImagePath.isEmpty()) {
                Image techniqueImage = new Image(getClass().getResourceAsStream(techniqueImagePath));
                techniquePhotoView.setImage(techniqueImage);
            } else {
                techniquePhotoView.setImage(null);
            }
        } catch (Exception e) {
            System.err.println("Error loading technique photo for " + teknik.getNama() + ": " + e.getMessage());
            techniquePhotoView.setImage(null);
        }

        // Set deskripsi
        descriptionLabel.setText(teknik.getDeskripsi());

        // Otomatis putar audio ketika teknik ditampilkan
        playAudioForCurrentTeknik();
    }

    private void playAudioForCurrentTeknik() {
        try {
            // Force stop dan cleanup audio sebelumnya dengan cara yang lebih agresif
            if (mediaPlayer != null) {
                try {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                } catch (Exception cleanupEx) {
                    System.err.println("Error during cleanup: " + cleanupEx.getMessage());
                }
                mediaPlayer = null;
            }

            // Gunakan audio sesuai dengan teknik saat ini
            String audioPath = "/audio/" + currentTeknikName + ".mp3";

            System.out.println("Looking for audio: " + audioPath + " (current technique: " + currentTeknikName + ")");

            // Coba beberapa cara untuk mendapatkan resource
            var audioURL = getClass().getResource(audioPath);

            if (audioURL == null) {
                // Coba cara lain dengan ClassLoader
                String altPath = "audio/" + currentTeknikName + ".mp3";
                audioURL = getClass().getClassLoader().getResource(altPath);
                System.out.println("Trying alternative path: " + altPath);
            }

            if (audioURL == null) {
                // Coba berbagai cara dinamis untuk menemukan audio file
                try {
                    // 1. Coba dari working directory saat ini (untuk portable distribution)
                    String workingDir = System.getProperty("user.dir");
                    System.out.println("DEBUG: Current working directory: " + workingDir);

                    String[] relativePaths = {
                            // Untuk development (project asli)
                            "src/main/resources/audio/" + currentTeknikName + ".mp3",
                            "target/classes/audio/" + currentTeknikName + ".mp3",

                            // Untuk portable distribution (running from sportedu folder)
                            "src/main/resources/audio/" + currentTeknikName + ".mp3",
                            "target/classes/audio/" + currentTeknikName + ".mp3",

                            // Fallback paths
                            "audio/" + currentTeknikName + ".mp3",
                            "../audio/" + currentTeknikName + ".mp3"
                    };

                    for (String relativePath : relativePaths) {
                        java.io.File audioFile = new java.io.File(workingDir, relativePath);
                        System.out.println("DEBUG: Checking path: " + audioFile.getAbsolutePath());
                        if (audioFile.exists()) {
                            audioURL = audioFile.toURI().toURL();
                            System.out.println("SUCCESS: Found audio at: " + audioFile.getAbsolutePath());
                            break;
                        }
                    }

                    // 2. Jika belum ketemu, compile/build dulu untuk generate target/classes
                    if (audioURL == null) {
                        System.out.println("DEBUG: Audio not found, checking if target/classes exists...");
                        java.io.File targetClasses = new java.io.File(workingDir, "target/classes/audio");
                        if (!targetClasses.exists()) {
                            System.out.println(
                                    "INFO: target/classes/audio not found. Application needs to be compiled first.");
                            System.out.println("INFO: Maven will compile automatically on first run.");
                        }

                        // Coba lagi setelah info
                        java.io.File audioFile = new java.io.File(workingDir,
                                "target/classes/audio/" + currentTeknikName + ".mp3");
                        if (audioFile.exists()) {
                            audioURL = audioFile.toURI().toURL();
                            System.out
                                    .println("SUCCESS: Found audio in target/classes: " + audioFile.getAbsolutePath());
                        }
                    }

                    // 3. Debug info - list available files
                    if (audioURL == null) {
                        System.out.println("DEBUG: Listing available directories and files...");

                        // Check src/main/resources/audio
                        java.io.File srcAudioDir = new java.io.File(workingDir, "src/main/resources/audio");
                        if (srcAudioDir.exists()) {
                            System.out.println("DEBUG: src/main/resources/audio exists");
                            java.io.File[] mp3Files = srcAudioDir.listFiles((dir, name) -> name.endsWith(".mp3"));
                            if (mp3Files != null && mp3Files.length > 0) {
                                System.out.println("DEBUG: Available MP3 files in src/main/resources/audio:");
                                for (java.io.File mp3 : mp3Files) {
                                    System.out.println("  - " + mp3.getName());
                                }
                            }
                        } else {
                            System.out.println("DEBUG: src/main/resources/audio does NOT exist");
                        }

                        // Check target/classes/audio
                        java.io.File targetAudioDir = new java.io.File(workingDir, "target/classes/audio");
                        if (targetAudioDir.exists()) {
                            System.out.println("DEBUG: target/classes/audio exists");
                            java.io.File[] mp3Files = targetAudioDir.listFiles((dir, name) -> name.endsWith(".mp3"));
                            if (mp3Files != null && mp3Files.length > 0) {
                                System.out.println("DEBUG: Available MP3 files in target/classes/audio:");
                                for (java.io.File mp3 : mp3Files) {
                                    System.out.println("  - " + mp3.getName());
                                }
                            }
                        } else {
                            System.out.println("DEBUG: target/classes/audio does NOT exist - Maven compilation needed");
                        }
                    }

                } catch (Exception fileEx) {
                    System.err.println("Dynamic file search failed: " + fileEx.getMessage());
                    fileEx.printStackTrace();
                }
            }

            if (audioURL != null) {
                System.out.println("SUCCESS: Audio file found at: " + audioURL.toExternalForm());
                Media media = new Media(audioURL.toExternalForm());
                mediaPlayer = new MediaPlayer(media);

                mediaPlayer.setOnReady(() -> {
                    System.out.println("MediaPlayer ready, starting automatic playback for: " + currentTeknikName);
                    mediaPlayer.play();
                });

                mediaPlayer.setOnEndOfMedia(() -> {
                    System.out.println("Audio finished playing: " + currentTeknikName);
                });

                mediaPlayer.setOnError(() -> {
                    System.err.println("Error playing audio: " + mediaPlayer.getError());
                });

            } else {
                System.err.println("FAILED: Audio file not found for technique: " + currentTeknikName);
                System.err.println("Expected file: " + audioPath);
                // List available audio files for debugging
                try {
                    var audioDir = getClass().getResource("/audio");
                    if (audioDir != null) {
                        System.out.println("Audio directory found: " + audioDir);
                    } else {
                        System.err.println("Audio directory not found: /audio/");
                    }
                } catch (Exception debugEx) {
                    System.err.println("Debug error: " + debugEx.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error playing audio for " + currentTeknikName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Parent getView() {
        return view;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }

    /**
     * Method untuk menghentikan audio yang sedang diputar
     * SUPER AGGRESSIVE AUDIO STOP - PAKSA STOP APAPUN YANG TERJADI
     */
    public void stopAudio() {
        try {
            if (mediaPlayer != null) {
                System.out.println("🔇 AGGRESSIVE AUDIO STOP starting...");

                // 1. Stop audio regardless of status
                try {
                    mediaPlayer.stop();
                    System.out.println("🔇 MediaPlayer.stop() called");
                } catch (Exception stopEx) {
                    System.err.println("Error calling stop(): " + stopEx.getMessage());
                }

                // 2. Set volume to 0 as backup
                try {
                    mediaPlayer.setVolume(0.0);
                    System.out.println("🔇 Volume set to 0");
                } catch (Exception volEx) {
                    System.err.println("Error setting volume: " + volEx.getMessage());
                }

                // 3. Force dispose immediately
                try {
                    mediaPlayer.dispose();
                    System.out.println("🔇 MediaPlayer.dispose() called");
                } catch (Exception disposeEx) {
                    System.err.println("Error disposing: " + disposeEx.getMessage());
                }

                // 4. Set to null
                mediaPlayer = null;
                System.out.println("🔇 MediaPlayer set to null");

                // 5. Force garbage collection
                System.gc();
                System.out.println("🔇 AGGRESSIVE AUDIO STOP completed");
            } else {
                System.out.println("🔇 No MediaPlayer to stop");
            }
        } catch (Exception e) {
            System.err.println("������� Error in aggressive audio stop: " + e.getMessage());
            // Force reset even with errors
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.dispose();
                }
            } catch (Exception finalEx) {
                // Ignore final exception
            }
            mediaPlayer = null;
            System.gc();
        }
    }

    /**
     * Method yang dipanggil ketika view akan disembunyikan atau tidak aktif
     */
    public void onViewDeactivated() {
        stopAudio();
        System.out.println("PenjelasanView deactivated - audio stopped");
    }

    /**
     * Method yang dipanggil ketika view akan ditampilkan atau aktif
     */
    public void onViewActivated() {
        // Reset audio state ketika view aktif kembali
        System.out.println("PenjelasanView activated");
    }

    /**
     * Method untuk cleanup MediaPlayer ketika view tidak digunakan lagi
     * Diperbaiki untuk cleanup yang lebih agresif
     */
    public void cleanup() {
        try {
            // Stop audio dulu sebelum cleanup dengan method yang sudah diperbaiki
            stopAudio();

            // Double check cleanup
            if (mediaPlayer != null) {
                try {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                } catch (Exception disposeEx) {
                    System.err.println("Error disposing during cleanup: " + disposeEx.getMessage());
                }
                mediaPlayer = null;
                System.out.println("MediaPlayer aggressively cleaned up");
            }
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
            // Force reset even with errors
            mediaPlayer = null;
        }
    }

    /**
     * Menampilkan overlay animasi di atas konten saat ini
     * Tidak pindah halaman, hanya overlay dengan efek game
     * Diperbaiki: lebih kecil, smooth, dan ringan
     */
    public void showAnimationOverlay(Teknik teknik) {
        try {
            // Buat overlay container yang menutupi seluruh view
            StackPane animationOverlay = new StackPane();
            animationOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);"); // Lebih transparan
            animationOverlay.setPrefSize(view.getWidth(), view.getHeight());
            animationOverlay.prefWidthProperty().bind(view.widthProperty());
            animationOverlay.prefHeightProperty().bind(view.heightProperty());

            // Container untuk konten animasi - DIKECILKAN
            VBox animationContent = new VBox(15); // Spacing dikurangi dari 20 ke 15
            animationContent.setAlignment(Pos.CENTER);
            animationContent.setPadding(new Insets(30)); // Padding dikurangi dari 50 ke 30
            animationContent.setStyle("-fx-background-color: #F5E6A3; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 6);"); // Shadow dikurangi
            animationContent.setMaxWidth(600); // Dikurangi dari 800 ke 600
            animationContent.setMaxHeight(450); // Dikurangi dari 600 ke 450

            // Title animasi - DIKECILKAN
            ImageView titleImageView = new ImageView();
            try {
                String titlePath = "/images/" + teknik.getNama() + "_title.png";
                Image titleImage = new Image(getClass().getResourceAsStream(titlePath));
                titleImageView.setImage(titleImage);
                titleImageView.setFitHeight(30); // Dikurangi dari 40 ke 30
                titleImageView.setPreserveRatio(true);
            } catch (Exception e) {
                Label titleLabel = new Label("Animasi " + teknik.getNama());
                titleLabel.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;"); // Font dikurangi dari 24px ke 20px
                animationContent.getChildren().add(titleLabel);
            }

            if (titleImageView.getImage() != null) {
                animationContent.getChildren().add(titleImageView);
            }

            // Container untuk GIF animasi - DIKECILKAN
            VBox gifContainer = new VBox();
            gifContainer.setAlignment(Pos.CENTER);
            gifContainer.setPadding(new Insets(15)); // Dikurangi dari 20 ke 15
            gifContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 3);"); // Shadow dikurangi
            gifContainer.setMaxWidth(450); // Dikurangi dari 600 ke 450

            // Load dan tampilkan GIF animasi - OPTIMIZED FOR REAL ANIMATION PLAYBACK
            ImageView gifImageView = new ImageView();
            boolean gifLoaded = false;
            String gifPath = "/images/" + teknik.getNama().toLowerCase() + ".gif";

            try {
                System.out.println("🎬 LOADING ANIMATED GIF: " + gifPath);
                System.out.println("🔍 DEBUG: Teknik nama: '" + teknik.getNama() + "'");
                System.out.println("🔍 DEBUG: Teknik nama lowercase: '" + teknik.getNama().toLowerCase() + "'");
                System.out.println("🔍 DEBUG: Full GIF path: '" + gifPath + "'");

                // DETAIL LOG: Cek resource availability
                var resourceCheck = getClass().getResource(gifPath);
                System.out.println("🔍 DEBUG: Resource check result: " + (resourceCheck != null ? resourceCheck.toString() : "NULL"));

                if (resourceCheck != null) {
                    System.out.println("🔍 DEBUG: Resource external form: " + resourceCheck.toExternalForm());
                } else {
                    System.out.println("❌ CRITICAL: Resource " + gifPath + " NOT FOUND in classpath!");

                    // Debug: List semua file GIF yang ada
                    System.out.println("🔍 DEBUG: Listing all available GIF files in /images/:");
                    try {
                        var imagesDir = getClass().getResource("/images/");
                        if (imagesDir != null) {
                            System.out.println("🔍 DEBUG: Images directory found: " + imagesDir);
                            // Try to list files if it's a directory
                            java.net.URI uri = imagesDir.toURI();
                            java.nio.file.Path dirPath = java.nio.file.Paths.get(uri);
                            if (java.nio.file.Files.exists(dirPath)) {
                                try (java.util.stream.Stream<java.nio.file.Path> files = java.nio.file.Files.list(dirPath)) {
                                    files.filter(path -> path.toString().toLowerCase().endsWith(".gif"))
                                         .forEach(path -> System.out.println("  🎬 Found GIF: " + path.getFileName()));
                                }
                            }
                        } else {
                            System.out.println("❌ CRITICAL: /images/ directory not found!");
                        }
                    } catch (Exception listEx) {
                        System.err.println("❌ Error listing GIF files: " + listEx.getMessage());
                    }
                }

                // METODE LOADING YANG DISEDERHANAKAN - HAPUS MEMORY OPTIMIZER
                Image gifImage = null;
                boolean loadSuccess = false;

                // METODE 1: Direct URL loading (paling sederhana dan stabil)
                var directURL = getClass().getResource(gifPath);
                if (directURL != null) {
                    try {
                        System.out.println("✅ Method 1: Direct URL loading starting...");
                        System.out.println("🔍 DEBUG: URL: " + directURL.toExternalForm());

                        gifImage = new Image(directURL.toExternalForm(),
                                           0, 0,         // original size
                                           true,         // preserveRatio
                                           false,        // smooth = false untuk animasi crisp
                                           false);       // backgroundLoading = false

                        if (gifImage != null) {
                            System.out.println("✅ Direct URL loading successful for " + teknik.getNama());
                            System.out.println("🔍 DEBUG: Image width: " + gifImage.getWidth());
                            System.out.println("🔍 DEBUG: Image height: " + gifImage.getHeight());
                            System.out.println("🔍 DEBUG: Image error: " + gifImage.isError());
                            System.out.println("🔍 DEBUG: Image progress: " + (gifImage.getProgress() * 100) + "%");
                            loadSuccess = true;
                        } else {
                            System.out.println("❌ Direct URL loading failed: gifImage is null");
                        }
                    } catch (Exception e) {
                        System.out.println("❌ Direct URL failed, trying InputStream: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("❌ Method 1 failed: directURL is null");
                }

                // METODE 2: InputStream sebagai backup (SKIP MEMORY OPTIMIZER)
                if (!loadSuccess) {
                    System.out.println("🔄 Method 2: InputStream loading starting...");
                    var gifStream = getClass().getResourceAsStream(gifPath);
                    if (gifStream != null) {
                        try {
                            System.out.println("✅ InputStream found, creating Image...");
                            gifImage = new Image(gifStream);
                            if (gifImage != null) {
                                System.out.println("✅ InputStream loading successful for " + teknik.getNama());
                                System.out.println("🔍 DEBUG: Stream Image width: " + gifImage.getWidth());
                                System.out.println("🔍 DEBUG: Stream Image height: " + gifImage.getHeight());
                                System.out.println("🔍 DEBUG: Stream Image error: " + gifImage.isError());
                                System.out.println("🔍 DEBUG: Stream Image progress: " + (gifImage.getProgress() * 100) + "%");
                                loadSuccess = true;
                            } else {
                                System.out.println("❌ InputStream loading failed: gifImage is null");
                            }
                        } catch (Exception e) {
                            System.err.println("❌ InputStream method failed for " + gifPath + ": " + e.getMessage());
                            e.printStackTrace();
                        } finally {
                            try { gifStream.close(); } catch (Exception e) { /* ignore */ }
                        }
                    } else {
                        System.out.println("❌ Method 2 failed: gifStream is null");
                    }
                }

                // METODE 3: Simple fallback tanpa optimization
                if (!loadSuccess) {
                    System.out.println("🔄 Method 3: Simple loading starting...");
                    try {
                        // Coba load simple tanpa parameter tambahan
                        var simpleStream = getClass().getResourceAsStream(gifPath);
                        if (simpleStream != null) {
                            gifImage = new Image(simpleStream);
                            if (gifImage != null) {
                                System.out.println("✅ Simple loading successful for " + teknik.getNama());
                                System.out.println("🔍 DEBUG: Simple Image width: " + gifImage.getWidth());
                                System.out.println("🔍 DEBUG: Simple Image height: " + gifImage.getHeight());
                                System.out.println("🔍 DEBUG: Simple Image error: " + gifImage.isError());
                                System.out.println("🔍 DEBUG: Simple Image progress: " + (gifImage.getProgress() * 100) + "%");
                                loadSuccess = true;
                            } else {
                                System.out.println("❌ Simple loading failed: gifImage is null");
                            }
                            simpleStream.close();
                        } else {
                            System.out.println("❌ Method 3 failed: simpleStream is null");
                        }
                    } catch (Exception e) {
                        System.err.println("❌ All loading methods failed for " + gifPath + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                if (loadSuccess && gifImage != null) {
                    // CHECK: Jika GIF berhasil dimuat tapi memiliki error atau dimensi 0
                    if (gifImage.isError() || gifImage.getWidth() == 0.0 || gifImage.getHeight() == 0.0) {
                        System.out.println("❌ GIF loaded but has error or zero dimensions - trying fallback");
                        System.out.println("  - Error: " + gifImage.isError());
                        System.out.println("  - Dimensions: " + gifImage.getWidth() + "x" + gifImage.getHeight());

                        // FALLBACK: Coba PNG animasi yang static sebagai pengganti
                        handleGifFallback(gifImageView, gifContainer, teknik, gifPath);
                        gifLoaded = true;

                    } else {
                        // GIF normal tanpa error - lanjutkan seperti biasa
                        System.out.println("🎉 SUCCESS: GIF loaded successfully!");
                        System.out.println("🔍 FINAL DEBUG: Final Image dimensions: " + gifImage.getWidth() + "x" + gifImage.getHeight());
                        System.out.println("🔍 FINAL DEBUG: Final Image error status: " + gifImage.isError());

                        // Configure ImageView untuk animasi optimal
                        gifImageView.setImage(gifImage);
                        gifImageView.setFitWidth(350);
                        gifImageView.setFitHeight(200);
                        gifImageView.setPreserveRatio(true);
                        gifImageView.setSmooth(false);     // PENTING: Disable smoothing
                        gifImageView.setCache(false);      // PENTING: Disable cache untuk looping
                        gifImageView.setViewport(null);    // Reset viewport

                        // PERBAIKAN SHOOTING: Perlambat animasi dengan metode yang lebih agresif
                        if (teknik.getNama().toLowerCase().equals("shooting")) {
                            System.out.println("🔧 SHOOTING: Applying AGGRESSIVE slower animation effect");

                            // Metode 1: CSS transform untuk memperlambat
                            gifImageView.setStyle("-fx-effect: none; -fx-opacity: 1.0;");

                            // Metode 2: Gunakan CSS animation untuk control timing (perbaiki untuk Java 11)
                            String slowAnimationCSS = "-fx-effect: none; -fx-opacity: 1.0;";
                            gifImageView.setStyle(slowAnimationCSS);

                            // Metode 3: Timeline dengan interval yang SANGAT lambat
                            javafx.animation.Timeline ultraSlowRefresh = new javafx.animation.Timeline(
                                new javafx.animation.KeyFrame(Duration.seconds(0.5), e -> {
                                    // Super gentle refresh untuk sangat memperlambat
                                    Platform.runLater(() -> {
                                        // Force re-render dengan perubahan minimal
                                        gifImageView.setScaleX(0.9999);
                                        gifImageView.setScaleX(1.0);
                                    });
                                })
                            );
                            ultraSlowRefresh.setCycleCount(javafx.animation.Timeline.INDEFINITE);
                            ultraSlowRefresh.play();

                            // Metode 4: Manipulasi frame rate dengan pause
                            javafx.animation.Timeline framePauser = new javafx.animation.Timeline(
                                new javafx.animation.KeyFrame(Duration.millis(100), e -> {
                                    Platform.runLater(() -> {
                                        // Pause animation briefly to slow it down
                                        gifImageView.setVisible(false);

                                        // Resume after very short pause
                                        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.millis(50));
                                        pause.setOnFinished(resumeEvent -> gifImageView.setVisible(true));
                                        pause.play();
                                    });
                                })
                            );
                            framePauser.setCycleCount(javafx.animation.Timeline.INDEFINITE);
                            framePauser.play();
                        }

                        // Monitor progress untuk GIF normal
                        if (!gifImage.isError()) {
                            final Image finalGifImage = gifImage;
                            final String finalTeknikName = teknik.getNama();

                            gifImage.progressProperty().addListener((obs, oldProgress, newProgress) -> {
                                System.out.println("🔍 PROGRESS UPDATE: " + finalTeknikName + " - " + (newProgress.doubleValue() * 100) + "%");
                                if (newProgress.doubleValue() >= 1.0) {
                                    Platform.runLater(() -> {
                                        System.out.println("✅ GIF fully loaded: " + finalGifImage.getWidth() + "x" + finalGifImage.getHeight() + " - " + finalTeknikName);
                                        gifImageView.setVisible(false);
                                        gifImageView.setVisible(true);
                                        System.out.println("🔄 FORCED REFRESH: ImageView visibility toggled");
                                    });
                                }
                            });
                        }

                        // Start natural monitoring untuk GIF yang berhasil
                        startNaturalGifMonitoring(gifImage, gifImageView, teknik.getNama());
                        gifLoaded = true;
                    }

                    // TAMBAHAN: Force visibility dan debug info
                    gifImageView.setVisible(true);
                    gifImageView.setOpacity(1.0);

                    System.out.println("🔍 IMAGEVIEW DEBUG: ImageView configured");
                    System.out.println("  - FitWidth: " + gifImageView.getFitWidth());
                    System.out.println("  - FitHeight: " + gifImageView.getFitHeight());
                    System.out.println("  - PreserveRatio: " + gifImageView.isPreserveRatio());
                    System.out.println("  - Visible: " + gifImageView.isVisible());
                    System.out.println("  - Opacity: " + gifImageView.getOpacity());

                    // PAKSA tambahkan ke container dengan debug
                    System.out.println("🔍 CONTAINER DEBUG: Adding ImageView to container...");
                    System.out.println("  - Container children before: " + gifContainer.getChildren().size());
                    gifContainer.getChildren().clear(); // Pastikan container kosong
                    gifContainer.getChildren().add(gifImageView);
                    System.out.println("  - Container children after: " + gifContainer.getChildren().size());
                    System.out.println("  - ImageView in container: " + gifContainer.getChildren().contains(gifImageView));

                    System.out.println("✅ SUCCESS: GIF displayed (animation should work) - " + teknik.getNama());

                    // Start monitoring untuk memastikan looping natural (skip jika PNG)
                    if (gifImage != null && !gifImage.isError()) {
                        startNaturalGifMonitoring(gifImage, gifImageView, teknik.getNama());
                    }
                    gifLoaded = true;
                } else {
                    // Jika benar-benar gagal, tampilkan pesan debug saja
                    System.err.println("❌ FAILED: Could not load GIF " + gifPath + " with any method");
                    System.err.println("❌ FINAL STATE: loadSuccess=" + loadSuccess + ", gifImage=" + (gifImage != null ? "not null" : "null"));

                    if (gifImage != null) {
                        System.err.println("❌ GIF IMAGE STATE:");
                        System.err.println("  - Width: " + gifImage.getWidth());
                        System.err.println("  - Height: " + gifImage.getHeight());
                        System.err.println("  - Error: " + gifImage.isError());
                        System.err.println("  - Progress: " + (gifImage.getProgress() * 100) + "%");
                    }

                    // TIDAK ADA FALLBACK PLACEHOLDER - biarkan container kosong
                    Label errorLabel = new Label("GIF " + teknik.getNama() + " tidak dapat dimuat");
                    errorLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-fill: #666666;");
                    gifContainer.getChildren().clear();
                    gifContainer.getChildren().add(errorLabel);
                    System.out.println("📝 ERROR LABEL: Added error message to container");
                    gifLoaded = true;
                }

            } catch (Exception e) {
                System.err.println("❌ EXCEPTION in GIF loading: " + e.getMessage());
                e.printStackTrace();

                // Print stack trace untuk debugging yang lebih detail
                System.err.println("❌ FULL STACK TRACE:");
                for (StackTraceElement element : e.getStackTrace()) {
                    System.err.println("  " + element.toString());
                }

                // TIDAK ADA FALLBACK - biarkan kosong atau pesan loading
                Label exceptionLabel = new Label("Error loading GIF: " + e.getMessage());
                exceptionLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-text-fill: #ff0000;");
                exceptionLabel.setWrapText(true);
                gifContainer.getChildren().clear();
                gifContainer.getChildren().add(exceptionLabel);
                System.out.println("📝 EXCEPTION LABEL: Added exception message to container");
                gifLoaded = true;
            }

            animationContent.getChildren().add(gifContainer);

            // Instruksi - DIKECILKAN
            Label instructionLabel = new Label("Perhatikan animasi di atas untuk memahami gerakan teknik " + teknik.getNama());
            instructionLabel.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 12px; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;"); // Font dikurangi dari 14px ke 12px
            instructionLabel.setWrapText(true);
            instructionLabel.setMaxWidth(400); // Dikurangi dari 500 ke 400
            instructionLabel.setAlignment(Pos.CENTER);
            animationContent.getChildren().add(instructionLabel);

            // Tombol kembali - DIKECILKAN
            Button closeButton = UIFactory.createNavButton("/images/Kembali_kuning.png", 150, 45); // Dikurangi dari 200x60 ke 150x45
            closeButton.setOnAction(e -> hideAnimationOverlay());
            animationContent.getChildren().add(closeButton);

            // Animasi entrance LEBIH SMOOTH dan RINGAN
            animationOverlay.setOpacity(0);
            animationContent.setScaleX(0.9); // Start scale lebih dekat ke 1.0 untuk smooth
            animationContent.setScaleY(0.9);

            // Fade in yang lebih smooth
            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), animationOverlay); // Lebih cepat dari 300ms
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            // Scale transition yang lebih smooth
            ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), animationContent); // Lebih cepat dari 400ms
            scaleIn.setFromX(0.9);
            scaleIn.setFromY(0.9);
            scaleIn.setToX(1.0);
            scaleIn.setToY(1.0);
            scaleIn.setInterpolator(javafx.animation.Interpolator.EASE_OUT); // Interpolator yang lebih smooth

            ParallelTransition entranceAnimation = new ParallelTransition(fadeIn, scaleIn);

            animationOverlay.getChildren().add(animationContent);

            // Tambahkan overlay ke view utama - perbaiki struktur layout
            // view.getChildren().get(0) adalah ScrollPane, bukan StackPane
            // Kita perlu menambahkan overlay ke VBox utama sebagai sibling
            if (view.getChildren().get(0) instanceof javafx.scene.control.ScrollPane) {
                javafx.scene.control.ScrollPane scrollPane = (javafx.scene.control.ScrollPane) view.getChildren().get(0);

                // Buat StackPane wrapper untuk menggabungkan konten scroll dengan overlay
                StackPane wrapperPane = new StackPane();
                wrapperPane.prefWidthProperty().bind(view.widthProperty());
                wrapperPane.prefHeightProperty().bind(view.heightProperty());

                // Pindahkan scrollPane ke wrapper
                view.getChildren().remove(scrollPane);
                wrapperPane.getChildren().add(scrollPane);
                wrapperPane.getChildren().add(animationOverlay);

                // Tambahkan wrapper ke view
                view.getChildren().add(wrapperPane);

                // Simpan referensi overlay untuk bisa dihapus nanti
                animationOverlay.setUserData("animationOverlay");
                wrapperPane.setUserData("wrapperPane");

                System.out.println("🎬 OVERLAY DEBUG: Animation overlay added to view structure");
                System.out.println("  - Wrapper children count: " + wrapperPane.getChildren().size());
                System.out.println("  - Main view children count: " + view.getChildren().size());

            } else {
                // Fallback jika struktur tidak sesuai ekspektasi
                System.err.println("❌ CRITICAL: Unexpected view structure in PenjelasanView");
                System.err.println("  - Expected: ScrollPane, Got: " + view.getChildren().get(0).getClass().getSimpleName());
                return;
            }

            entranceAnimation.play();
            System.out.println("🎬 ANIMATION: Entrance animation started for " + teknik.getNama() + " overlay");

        } catch (Exception e) {
            System.err.println("❌ CRITICAL ERROR showing animation overlay: " + e.getMessage());
            e.printStackTrace();

            // Print stack trace untuk debugging yang lebih detail
            System.err.println("❌ OVERLAY CREATION STACK TRACE:");
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("  " + element.toString());
            }
        }
    }

    /**
     * Menyembunyikan overlay animasi dengan efek keluar
     */
    public void hideAnimationOverlay() {
        try {
            System.out.println("🔄 HIDE OVERLAY: Starting to hide animation overlay...");

            // Cari wrapper pane yang berisi overlay
            StackPane wrapperPane = null;
            for (var child : view.getChildren()) {
                if (child.getUserData() != null && child.getUserData().equals("wrapperPane")) {
                    wrapperPane = (StackPane) child;
                    System.out.println("✅ HIDE OVERLAY: Found wrapper pane");
                    break;
                }
            }

            if (wrapperPane != null) {
                // Cari overlay dalam wrapper
                StackPane overlayToRemove = null;
                for (var child : wrapperPane.getChildren()) {
                    if (child.getUserData() != null && child.getUserData().equals("animationOverlay")) {
                        overlayToRemove = (StackPane) child;
                        System.out.println("✅ HIDE OVERLAY: Found overlay to remove");
                        break;
                    }
                }

                if (overlayToRemove != null) {
                    StackPane overlay = overlayToRemove;
                    VBox content = (VBox) overlay.getChildren().get(0);

                    System.out.println("🔄 HIDE OVERLAY: Starting exit animation...");

                    // Animasi keluar
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(300), overlay);
                    fadeOut.setFromValue(1);
                    fadeOut.setToValue(0);

                    ScaleTransition scaleOut = new ScaleTransition(Duration.millis(400), content);
                    scaleOut.setFromX(1.0);
                    scaleOut.setFromY(1.0);
                    scaleOut.setToX(0.8);
                    scaleOut.setToY(0.8);

                    ParallelTransition exitAnimation = new ParallelTransition(fadeOut, scaleOut);

                    final StackPane finalWrapperPane = wrapperPane;
                    exitAnimation.setOnFinished(e -> {
                        System.out.println("🔄 HIDE OVERLAY: Exit animation finished, cleaning up...");

                        // Hapus overlay dari wrapper
                        finalWrapperPane.getChildren().remove(overlay);

                        // Restore struktur asli: pindahkan scrollPane kembali ke view
                        javafx.scene.control.ScrollPane scrollPane = null;
                        for (var child : finalWrapperPane.getChildren()) {
                            if (child instanceof javafx.scene.control.ScrollPane) {
                                scrollPane = (javafx.scene.control.ScrollPane) child;
                                break;
                            }
                        }

                        if (scrollPane != null) {
                            finalWrapperPane.getChildren().remove(scrollPane);
                            view.getChildren().remove(finalWrapperPane);
                            view.getChildren().add(scrollPane);
                            System.out.println("✅ HIDE OVERLAY: Structure restored successfully");
                        } else {
                            System.err.println("❌ HIDE OVERLAY: Could not find scrollPane to restore");
                        }
                    });

                    exitAnimation.play();
                } else {
                    System.err.println("❌ HIDE OVERLAY: Could not find overlay to remove");
                }
            } else {
                System.err.println("❌ HIDE OVERLAY: Could not find wrapper pane");
            }

        } catch (Exception e) {
            System.err.println("❌ CRITICAL ERROR hiding animation overlay: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Start natural GIF monitoring dengan refresh rate yang lebih lambat
     * untuk memastikan looping yang smooth tanpa terlalu aggressive
     */
    private void startNaturalGifMonitoring(Image gifImage, ImageView gifImageView, String teknikName) {
        try {
            System.out.println("🔄 GIF MONITOR: Starting natural monitoring for " + teknikName);

            // Monitor status GIF dengan interval yang diperlambat
            javafx.animation.Timeline gifStatusMonitor = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(Duration.seconds(3.0), e -> { // Diperlambat ke 3 detik
                    if (gifImage != null && !gifImage.isError()) {
                        System.out.println("🎬 GIF " + teknikName + " - Status: " +
                                         "Dimensions=" + gifImage.getWidth() + "x" + gifImage.getHeight() +
                                         ", Progress=" + (gifImage.getProgress() * 100) + "%");
                    } else if (gifImage != null && gifImage.isError()) {
                        System.out.println("❌ GIF " + teknikName + " - Has error status");
                    } else {
                        System.out.println("❌ GIF " + teknikName + " - Image is null");
                    }
                })
            );

            // Hanya monitor 3 kali (total 9 detik) kemudian stop monitoring
            gifStatusMonitor.setCycleCount(3);
            gifStatusMonitor.play();

            // NATURAL looping refresh - sangat diperlambat untuk animasi yang natural
            javafx.animation.Timeline naturalRefresh = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(Duration.seconds(5.0), e -> { // Diperlambat ke 5 detik
                    // Refresh yang sangat halus untuk mempertahankan looping natural
                    if (gifImageView.getImage() == gifImage) {
                        Platform.runLater(() -> {
                            // Trigger refresh yang hampir tidak terlihat
                            double currentOpacity = gifImageView.getOpacity();
                            gifImageView.setOpacity(currentOpacity - 0.001); // Perubahan sangat kecil
                            gifImageView.setOpacity(currentOpacity);
                            System.out.println("🔄 GIF " + teknikName + " - Natural refresh applied");
                        });
                    }
                })
            );

            // Jalankan refresh hanya 6 kali (total 30 detik) kemudian biarkan natural
            naturalRefresh.setCycleCount(6);
            naturalRefresh.play();

            System.out.println("🎬 Natural GIF monitoring started for " + teknikName + " (very slow refresh for natural looping)");

        } catch (Exception e) {
            System.err.println("❌ Error setting up natural GIF monitoring: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Fallback handler untuk GIF loading errors
     * Mencoba berbagai cara untuk memuat GIF atau memberikan pengganti yang sesuai
     */
    private void handleGifFallback(ImageView gifImageView, VBox gifContainer, Teknik teknik, String originalGifPath) {
        try {
            System.out.println("🔄 GIF FALLBACK: Triggered for " + teknik.getNama() + " - trying alternative methods");

            // 1. PRIORITAS UTAMA: Gunakan file PNG animasi yang sudah ada
            String animasiPngPath = "/images/animasi_" + teknik.getNama().toLowerCase() + ".png";
            var animasiURL = getClass().getResource(animasiPngPath);
            System.out.println("🔍 FALLBACK: Checking PNG animasi at: " + animasiPngPath);

            if (animasiURL != null) {
                try {
                    System.out.println("✅ FALLBACK: PNG animasi found, loading...");
                    Image animasiImage = new Image(animasiURL.toExternalForm());
                    if (animasiImage != null && !animasiImage.isError() && animasiImage.getWidth() > 0 && animasiImage.getHeight() > 0) {
                        gifImageView.setImage(animasiImage);
                        gifImageView.setFitWidth(350);
                        gifImageView.setFitHeight(200);
                        gifImageView.setPreserveRatio(true);
                        gifImageView.setSmooth(true);

                        // Clear container first to avoid duplicates
                        gifContainer.getChildren().clear();
                        gifContainer.getChildren().add(gifImageView);

                        System.out.println("✅ SUCCESS: PNG animasi fallback loaded - " + animasiPngPath);
                        return;
                    } else {
                        System.out.println("❌ FALLBACK: PNG animasi has error or invalid dimensions");
                    }
                } catch (Exception pngEx) {
                    System.err.println("❌ FALLBACK: Failed to load PNG animasi: " + pngEx.getMessage());
                }
            } else {
                System.out.println("❌ FALLBACK: PNG animasi not found");
            }

            // 2. Coba GIF dengan nama atau lokasi alternatif
            System.out.println("🔄 FALLBACK: Trying alternative GIF paths...");
            String[] alternativeGifPaths = {
                "/images/gif/" + teknik.getNama().toLowerCase() + ".gif",
                "/images/gif" + (teknik.getNama().equals("Shooting") ? "1" :
                               teknik.getNama().equals("Passing") ? "2" :
                               teknik.getNama().equals("Dribbling") ? "3" :
                               teknik.getNama().equals("Heading") ? "4" : "1") + ".gif",
                "/images/loading.gif" // Generic loading GIF as last resort
            };

            for (String altPath : alternativeGifPaths) {
                try {
                    System.out.println("🔍 FALLBACK: Trying alternative GIF: " + altPath);
                    var altURL = getClass().getResource(altPath);
                    if (altURL != null) {
                        Image altGif = new Image(altURL.toExternalForm(), true);
                        if (altGif != null && !altGif.isError() && altGif.getWidth() > 0 && altGif.getHeight() > 0) {
                            gifImageView.setImage(altGif);
                            gifImageView.setFitWidth(350);
                            gifImageView.setFitHeight(200);
                            gifImageView.setPreserveRatio(true);
                            gifImageView.setSmooth(false);
                            gifImageView.setCache(false);

                            gifContainer.getChildren().clear();
                            gifContainer.getChildren().add(gifImageView);
                            System.out.println("✅ SUCCESS: Alternative GIF loaded - " + altPath);
                            return;
                        }
                    }
                } catch (Exception altEx) {
                    System.err.println("❌ FALLBACK: Alternative GIF failed: " + altEx.getMessage());
                    // Continue to next alternative
                }
            }

            // 3. Coba gambar teknik foto sebagai fallback terakhir
            System.out.println("🔄 FALLBACK: All GIF alternatives failed - trying technique photo fallback");

            String[] techniquePhotoPaths = {
                "/images/" + (teknik.getNama().toLowerCase().contains("smash") ||
                             teknik.getNama().toLowerCase().contains("netting") ||
                             teknik.getNama().toLowerCase().contains("servis") ||
                             teknik.getNama().toLowerCase().contains("footwork") ? "badminton_" : "soccer_")
                             + teknik.getNama().toLowerCase() + ".jpg",
                "/images/badminton_" + teknik.getNama().toLowerCase() + ".jpg",
                "/images/soccer_" + teknik.getNama().toLowerCase() + ".jpg"
            };

            for (String photoPath : techniquePhotoPaths) {
                try {
                    System.out.println("🔍 FALLBACK: Trying technique photo: " + photoPath);
                    var photoURL = getClass().getResource(photoPath);
                    if (photoURL != null) {
                        Image photoImage = new Image(photoURL.toExternalForm());
                        if (photoImage != null && !photoImage.isError() && photoImage.getWidth() > 0 && photoImage.getHeight() > 0) {
                            gifImageView.setImage(photoImage);
                            gifImageView.setFitWidth(350);
                            gifImageView.setFitHeight(200);
                            gifImageView.setPreserveRatio(true);
                            gifImageView.setSmooth(true);

                            gifContainer.getChildren().clear();
                            gifContainer.getChildren().add(gifImageView);

                            System.out.println("✅ SUCCESS: Technique photo fallback loaded - " + photoPath);
                            return;
                        }
                    }
                } catch (Exception photoEx) {
                    System.err.println("❌ FALLBACK: Technique photo failed: " + photoEx.getMessage());
                    // Continue to next photo
                }
            }

        } catch (Exception e) {
            System.err.println("❌ FALLBACK: All image loading failed for " + teknik.getNama() + ": " + e.getMessage());
            e.printStackTrace();
        }

        // Ultimate fallback: Show descriptive text
        try {
            System.out.println("🔄 FALLBACK: Using ultimate text fallback");
            gifContainer.getChildren().clear();

            VBox infoBox = new VBox(10);
            infoBox.setAlignment(Pos.CENTER);

            Label infoLabel = new Label("🎬 Animasi " + teknik.getNama());
            infoLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 18px; -fx-text-fill: #1A1E2C; -fx-font-weight: bold;");

            Label noteLabel = new Label("Animasi teknik " + teknik.getNama().toLowerCase() + " sedang dimuat...\nPerhatikan gerakan yang akan ditampilkan");
            noteLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-text-fill: #666; -fx-text-alignment: center;");
            noteLabel.setWrapText(true);
            noteLabel.setTextAlignment(TextAlignment.CENTER);

            infoBox.getChildren().addAll(infoLabel, noteLabel);
            gifContainer.getChildren().add(infoBox);

            System.out.println("✅ Descriptive text fallback displayed for " + teknik.getNama());
        } catch (Exception fallbackEx) {
            System.err.println("❌ Even text fallback failed: " + fallbackEx.getMessage());
        }
    }
}

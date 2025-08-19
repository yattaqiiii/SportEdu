package com.sportedu.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.PauseTransition;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
    public Button quizKembaliButton;
    public Button quizModeSelectionCancelButton;

    // Info button untuk popup informasi mahasiswa
    public Button infoButton;

    // MediaPlayer untuk audio
    private MediaPlayer currentAudioPlayer;

    // MediaPlayer untuk background music yang berulang
    private MediaPlayer bgmPlayer;

    public MainView(Stage stage) {
        this.stage = stage;
        this.root = new BorderPane();

        // Set ukuran window yang lebih kecil agar window controls bawaan Windows
        // terlihat
        stage.setMinWidth(800); // Minimum width
        stage.setMinHeight(600); // Minimum height
        stage.setWidth(1000); // Default width yang lebih kecil dari fullscreen
        stage.setHeight(700); // Default height yang lebih kecil dari fullscreen
        stage.setResizable(true); // Bisa di-resize dan maximize

        // Pastikan tidak fullscreen dan tidak maximized di awal
        stage.setMaximized(false);
        stage.setFullScreen(false);

        // Setup fullscreen toggle dengan F11
        // User juga bisa gunakan tombol maximize/fullscreen bawaan Windows di title bar
        setupFullscreenControls();

        // Inisialisasi dan putar background music berulang
        initializeBGM();

        showLandingPage(); // Tampilan awal

        // Putar audio startup SportEdu.mp3
        playAudio("SportEdu.mp3");
    }

    /**
     * Method untuk memutar audio
     */
    private void playAudio(String audioFileName) {
        try {
            // Stop audio yang sedang berjalan jika ada
            if (currentAudioPlayer != null) {
                currentAudioPlayer.stop();
                currentAudioPlayer.dispose();
            }

            // Load audio file dari resources/audio dengan path checking
            java.net.URL audioUrl = getClass().getResource("/audio/" + audioFileName);
            if (audioUrl == null) {
                System.err.println("Audio file not found: /audio/" + audioFileName);
                return;
            }

            String audioPath = audioUrl.toExternalForm();
            Media audio = new Media(audioPath);
            currentAudioPlayer = new MediaPlayer(audio);

            // Set volume dan play
            currentAudioPlayer.setVolume(0.5); // Volume 50%

            // Handle potential media errors
            currentAudioPlayer.setOnError(() -> {
                System.err.println("MediaPlayer error for " + audioFileName + ": " + currentAudioPlayer.getError());
            });

            currentAudioPlayer.play();

        } catch (Exception e) {
            System.err.println("Error playing audio " + audioFileName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Method untuk inisialisasi dan memutar background music berulang
     */
    private void initializeBGM() {
        try {
            // Stop BGM yang sedang berjalan jika ada
            if (bgmPlayer != null) {
                bgmPlayer.stop();
                bgmPlayer.dispose();
            }

            // Load BGM file dari resources/audio
            java.net.URL bgmUrl = getClass().getResource("/audio/bgm.mp3");
            if (bgmUrl == null) {
                System.err.println("BGM file not found: /audio/bgm.mp3");
                return;
            }

            String bgmPath = bgmUrl.toExternalForm();
            Media bgmMedia = new Media(bgmPath);
            bgmPlayer = new MediaPlayer(bgmMedia);

            // Set volume lebih kecil agar tidak bertabrakan dengan voice over
            bgmPlayer.setVolume(0.15); // Volume 15% (lebih kecil dari voice over)

            // Set untuk loop berulang tanpa batas
            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            // Handle potential media errors
            bgmPlayer.setOnError(() -> {
                System.err.println("BGM MediaPlayer error: " + bgmPlayer.getError());
            });

            // Auto-replay ketika selesai (backup untuk looping)
            bgmPlayer.setOnEndOfMedia(() -> {
                bgmPlayer.seek(Duration.ZERO);
                bgmPlayer.play();
            });

            // Putar BGM
            bgmPlayer.play();
            System.out.println("🎵 Background music started with looping");

        } catch (Exception e) {
            System.err.println("Error initializing BGM: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Method untuk menambahkan hover sound effect ke button dengan delay
     */
    private void addHoverSoundDelayed(Button button, String audioFileName) {
        // Beri delay kecil untuk memastikan button sudah dalam state normal
        javafx.animation.PauseTransition delayBeforeAddingSound = new javafx.animation.PauseTransition(
                Duration.millis(100));
        delayBeforeAddingSound.setOnFinished(e -> {
            // Simpan semua existing handlers setelah delay
            final var existingEnterHandler = button.getOnMouseEntered();
            final var existingExitHandler = button.getOnMouseExited();

            // Set new mouse entered handler dengan audio + existing handler
            button.setOnMouseEntered(event -> {
                playAudio(audioFileName);
                // Jalankan existing enter handler jika ada
                if (existingEnterHandler != null) {
                    existingEnterHandler.handle(event);
                }
            });

            // Preserve existing exit handler juga
            if (existingExitHandler != null) {
                button.setOnMouseExited(existingExitHandler);
            }
        });
        delayBeforeAddingSound.play();
    }

    /**
     * Method untuk menambahkan hover sound effect ke button
     */
    private void addHoverSound(Button button, String audioFileName) {
        addHoverSoundDelayed(button, audioFileName);
    }

    /**
     * Method ini harus dipanggil setelah Scene dibuat untuk mengaktifkan fullscreen
     * controls
     */
    public void initializeAfterSceneCreated() {
        setupSceneBasedControls();
    }

    public Parent getRoot() {
        return root;
    }

    public Stage getStage() {
        return stage;
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
                // Pastikan sekali lagi scale di 1.0
                forceResetButtonScale(element);

                // Jalankan callback setelah semua setup selesai
                if (onComplete != null) {
                    onComplete.run();
                }
            });
            enableHoverDelay.play();
        });

        animation.play();
    }

    /**
     * Menampilkan popup informasi mahasiswa sebagai overlay
     */
    private void showInfoPopup() {
        // Ambil root dari scene untuk memastikan overlay benar-benar di atas semua
        // konten
        Parent sceneRoot = stage.getScene().getRoot();
        StackPane overlayContainer;

        // Jika scene root adalah StackPane, gunakan itu. Jika tidak, buat wrapper
        if (sceneRoot instanceof StackPane) {
            overlayContainer = (StackPane) sceneRoot;
        } else {
            // Buat StackPane wrapper baru untuk overlay
            overlayContainer = new StackPane();
            overlayContainer.getChildren().add(sceneRoot);
            stage.getScene().setRoot(overlayContainer);
        }

        // Buat overlay container untuk popup yang memenuhi seluruh scene
        StackPane popupOverlay = new StackPane();
        popupOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // Background hitam transparan 50%

        // Pastikan overlay memenuhi seluruh scene
        popupOverlay.prefWidthProperty().bind(stage.getScene().widthProperty());
        popupOverlay.prefHeightProperty().bind(stage.getScene().heightProperty());
        popupOverlay.setMinWidth(0);
        popupOverlay.setMinHeight(0);
        popupOverlay.setMaxWidth(Double.MAX_VALUE);
        popupOverlay.setMaxHeight(Double.MAX_VALUE);

        // Kotak putih dengan informasi di tengah layar
        VBox infoBox = new VBox(15);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(30, 40, 30, 40));
        infoBox.setStyle(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        infoBox.setMaxWidth(350);
        infoBox.setMaxHeight(250);

        // Label informasi
        Label namaLabel = new Label("Nama: Asmaul Husna");
        namaLabel.setStyle(
                "-fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-text-fill: #1A1E2C;");

        Label nimLabel = new Label("NIM: 2022302004");
        nimLabel.setStyle(
                "-fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-text-fill: #1A1E2C;");

        Label universitasLabel = new Label("Universitas: Politeknik Aceh");
        universitasLabel.setStyle(
                "-fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-text-fill: #1A1E2C;");

        Label prodiLabel = new Label("Prodi: D3 Teknologi Informasi");
        prodiLabel.setStyle(
                "-fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-text-fill: #1A1E2C;");
        // Tombol tutup
        Button closeButton = new Button("Tutup");
        closeButton.setStyle(
                "-fx-background-color: #516BB0; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 14px; -fx-font-weight: 500; -fx-background-radius: 8; -fx-padding: 10 20;");
        closeButton.setOnMouseEntered(e -> closeButton.setStyle(
                "-fx-background-color: #3D5A9E; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 14px; -fx-font-weight: 500; -fx-background-radius: 8; -fx-padding: 10 20;"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle(
                "-fx-background-color: #516BB0; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 14px; -fx-font-weight: 500; -fx-background-radius: 8; -fx-padding: 10 20;"));
        closeButton.setOnAction(e -> {
            // Hapus popup overlay dari scene root dengan animasi fade out
            FadeTransition fadeOut = new FadeTransition(Duration.millis(150), popupOverlay);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(event -> overlayContainer.getChildren().remove(popupOverlay));
            fadeOut.play();
        });

        infoBox.getChildren().addAll(namaLabel, nimLabel, universitasLabel, prodiLabel, closeButton);

        // Posisikan infoBox DI TENGAH BENAR dengan StackPane
        popupOverlay.getChildren().add(infoBox);
        StackPane.setAlignment(infoBox, Pos.CENTER);

        // Tutup popup saat klik di area gelap (background overlay)
        popupOverlay.setOnMouseClicked(e -> {
            // Pastikan klik benar-benar di background, bukan di infoBox
            if (e.getTarget() == popupOverlay) {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(150), popupOverlay);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(event -> overlayContainer.getChildren().remove(popupOverlay));
                fadeOut.play();
            }
        });

        // Tambahkan popup ke scene root sebagai TOP LAYER overlay
        overlayContainer.getChildren().add(popupOverlay);

        // Animasi fade in untuk popup
        popupOverlay.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), popupOverlay);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    /**
     * Setup kontrol fullscreen dengan F11 dan gunakan window controls bawaan
     * Windows
     */
    private void setupFullscreenControls() {
        // F11 untuk toggle fullscreen (shortcut keyboard standar)
        KeyCodeCombination f11 = new KeyCodeCombination(KeyCode.F11);

        // Event handler untuk keyboard shortcut
        root.setOnKeyPressed(event -> {
            if (f11.match(event)) {
                toggleFullscreen();
                event.consume();
            }
        });

        // Listen untuk perubahan maximized state dari tombol Windows
        stage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
            // Hanya convert ke fullscreen jika user menggunakan double-click pada maximize
            // atau jika memang ingin behavior maximize = fullscreen
            System.out.println("Window maximized: " + newValue);
            // Uncomment baris berikut jika ingin maximize otomatis jadi fullscreen:
            // if (newValue && !stage.isFullScreen()) {
            // stage.setFullScreen(true);
            // }
        });

        // Make sure root dapat menerima focus untuk keyboard events
        root.setFocusTraversable(true);
        root.requestFocus();
    }

    /**
     * Setup controls yang memerlukan Scene (dipanggil setelah Scene dibuat)
     */
    private void setupSceneBasedControls() {
        if (stage.getScene() != null) {
            // Listen untuk double-click pada title bar untuk fullscreen
            stage.getScene().setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && event.getY() <= 30) { // Area title bar
                    toggleFullscreen();
                }
            });
        }
    }

    /**
     * Toggle fullscreen mode
     */
    private void toggleFullscreen() {
        boolean isCurrentlyFullscreen = stage.isFullScreen();
        stage.setFullScreen(!isCurrentlyFullscreen);

        // Reset maximized state ketika keluar dari fullscreen
        if (isCurrentlyFullscreen) {
            stage.setMaximized(false);
        }

        // Optional: Log status untuk debugging
        System.out.println("Fullscreen mode: " + (!isCurrentlyFullscreen ? "ON" : "OFF"));
    }

    /**
     * Metode untuk mengganti konten utama di tengah BorderPane.
     */
    public void setView(Node view) {
        // Langsung set view ke center tanpa ScrollPane - footer akan ditambahkan di
        // setiap halaman
        root.setCenter(view);
        // Hapus footer dari bottom region karena sekarang akan ada di dalam konten
        root.setBottom(null);
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
        navHomeButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;");
        navHomeButton.setOnMouseEntered(e -> navHomeButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));
        navHomeButton.setOnMouseExited(e -> navHomeButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));

        navMateriButton = new Button("Materi");
        navMateriButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;");
        navMateriButton.setOnMouseEntered(e -> navMateriButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));
        navMateriButton.setOnMouseExited(e -> navMateriButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));

        navQuizButton = new Button("Quiz");
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
                    navHomeButton.setStyle(
                            "-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;");
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
                    navMateriButton.setStyle(
                            "-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;");
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
                    navQuizButton.setStyle(
                            "-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;");
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
                if (navHomeButton != null)
                    navHomeButton.setStyle(activeStyle);
                break;
            case "materi":
                if (navMateriButton != null)
                    navMateriButton.setStyle(activeStyle);
                break;
            case "quiz":
                if (navQuizButton != null)
                    navQuizButton.setStyle(activeStyle);
                break;
        }
    }

    /**
     * Membuat dan menampilkan Landing Page sesuai desain "1. Landing Page.png"
     */
    public void showLandingPage() {
        // Container utama dengan background dot pattern
        VBox mainContainer = new VBox();
        mainContainer.prefWidthProperty().bind(stage.widthProperty());
        mainContainer.prefHeightProperty().bind(stage.heightProperty());

        // Background dengan dot pattern dari vektor
        try {
            ImageView dotBackground = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/dot background.png")));
            // Make background responsive to window size
            dotBackground.fitWidthProperty().bind(stage.widthProperty());
            dotBackground.fitHeightProperty().bind(stage.heightProperty());
            dotBackground.setPreserveRatio(false);

            // Overlay container untuk konten
            VBox overlayContainer = new VBox();
            overlayContainer.prefWidthProperty().bind(stage.widthProperty());
            overlayContainer.prefHeightProperty().bind(stage.heightProperty());
            overlayContainer.setStyle("-fx-background-color: transparent;");

            // Content area utama dengan background box - POSISI DIPERBAIKI
            VBox contentArea = new VBox(20);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(60, 80, 60, 80)); // Padding seimbang untuk positioning yang lebih baik
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Main content box dengan background putih dan rounded corners - POSISI TENGAH
            VBox contentBox = new VBox(15);
            contentBox.setAlignment(Pos.CENTER);
            contentBox.setPadding(new Insets(30, 40, 30, 40)); // Padding yang lebih besar
            contentBox.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
            contentBox.setMaxWidth(550);
            contentBox.setMaxHeight(400); // Tinggi yang lebih sesuai

            // Hapus margin negatif untuk posisi yang lebih natural
            // VBox.setMargin(contentBox, new Insets(0, 0, 0, 0)); // Tidak perlu margin

            // Tambahkan animasi pop out pop in berulang untuk contentBox
            createContinuousPopAnimation(contentBox);

            // Hero section dengan logo SportEdu sebagai judul - smaller spacing
            VBox heroSection = new VBox(15); // Reduced from 20 to 15
            heroSection.setAlignment(Pos.CENTER);

            // Gunakan gambar SportEdu sebagai judul - smaller
            try {
                ImageView sportEduLogo = new ImageView(
                        new Image(getClass().getResourceAsStream("/images/SportEdu.png")));
                sportEduLogo.setFitHeight(120); // Reduced from 80 to 60
                sportEduLogo.setPreserveRatio(true);
                sportEduLogo.setSmooth(true);
                heroSection.getChildren().add(sportEduLogo);

                // Animasi pop out untuk logo SportEdu
                createPopOutAnimation(sportEduLogo, 0.2);
            } catch (Exception logoException) {
                // Fallback jika logo SportEdu tidak ditemukan
                Label mainTitle = new Label("SportEdu");
                mainTitle.setStyle(
                        "-fx-font-family: 'Poppins', Arial; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;"); // Reduced
                                                                                                                                                               // from
                                                                                                                                                               // 48px
                                                                                                                                                               // to
                                                                                                                                                               // 36px
                heroSection.getChildren().add(mainTitle);

                // Animasi pop out untuk fallback title
                createPopOutAnimation(mainTitle, 0.2);
            }

            // Gunakan gambar subtitle sebagai pengganti teks - smaller
            try {
                ImageView subtitleImage = new ImageView(new Image(
                        getClass().getResourceAsStream("/images/Aplikasi Pembelajaran Olahraga Interaktif.png")));
                subtitleImage.setFitHeight(22); // Reduced from 30 to 22
                subtitleImage.setPreserveRatio(true);
                subtitleImage.setSmooth(true);
                heroSection.getChildren().add(subtitleImage);

                // Animasi pop out untuk subtitle
                createPopOutAnimation(subtitleImage, 0.4);
            } catch (Exception subtitleException) {
                // Fallback jika gambar subtitle tidak ditemukan
                Label subtitle = new Label("Platform pembelajaran olahraga interaktif untuk anak-anak");
                subtitle.setStyle(
                        "-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: 400; -fx-text-fill: #516BB0; -fx-text-alignment: center;"); // Reduced
                                                                                                                                                       // from
                                                                                                                                                       // 18px
                                                                                                                                                       // to
                                                                                                                                                       // 14px
                subtitle.setWrapText(true);
                heroSection.getChildren().add(subtitle);

                // Animasi pop out untuk fallback subtitle
                createPopOutAnimation(subtitle, 0.4);
            }

            // Action buttons container - smaller spacing and buttons
            VBox buttonContainer = new VBox(15); // Reduced from 20 to 15
            buttonContainer.setAlignment(Pos.CENTER);

            // Gunakan gambar tombol yang benar dari resources - standardized width
            materiButton = UIFactory.createMainButton("/images/Materi_butt.png", 250, 70); // Standardized to 250px
                                                                                           // width
            quizButton = UIFactory.createMainButton("/images/Quiz_butt.png", 250, 70); // Standardized to 250px width

            // Animasi pop out untuk tombol-tombol dengan hover sound setelah animasi
            // selesai
            createPopOutAnimation(materiButton, 0.6, () -> addHoverSound(materiButton, "Materi.mp3"));
            createPopOutAnimation(quizButton, 0.8, () -> addHoverSound(quizButton, "Quiz.mp3"));

            buttonContainer.getChildren().addAll(materiButton, quizButton);

            contentBox.getChildren().addAll(heroSection, buttonContainer);
            contentArea.getChildren().add(contentBox);
            overlayContainer.getChildren().add(contentArea);

            // Stack background dan overlay
            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
            stackPane.getChildren().addAll(dotBackground, overlayContainer);

            // Buat logo overlay di kiri atas
            ImageView logoOverlay = null;
            try {
                logoOverlay = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
                logoOverlay.setFitHeight(50);
                logoOverlay.setPreserveRatio(true);
                logoOverlay.setSmooth(true);
            } catch (Exception logoException) {
                System.err.println("Logo tidak ditemukan, membuat placeholder");
                logoOverlay = new ImageView(); // Placeholder kosong
            }

            // Buat tombol info di kanan atas menggunakan gambar
            infoButton = UIFactory.createNavButton("/images/info.png", 70, 70);
            infoButton.setOnAction(evt -> showInfoPopup());

            // Animasi pop out untuk tombol info
            createPopOutAnimation(infoButton, 1.0);

            // Final stack pane dengan overlay logo dan tombol info
            StackPane finalStackPane = new StackPane();
            finalStackPane.getChildren().add(stackPane);

            // Posisikan logo di kiri atas
            finalStackPane.getChildren().add(logoOverlay);
            StackPane.setAlignment(logoOverlay, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlay, new Insets(20, 0, 0, 20));

            // Posisikan tombol info di kanan atas
            finalStackPane.getChildren().add(infoButton);
            StackPane.setAlignment(infoButton, Pos.TOP_RIGHT);
            StackPane.setMargin(infoButton, new Insets(20, 20, 0, 0));

            // Langsung gunakan finalStackPane tanpa footer
            setView(finalStackPane);

        } catch (Exception e) {
            // Fallback jika background dot tidak ditemukan
            System.err.println("Background dot tidak ditemukan, menggunakan background warna solid");

            VBox fallbackContainer = new VBox();
            fallbackContainer.prefWidthProperty().bind(stage.widthProperty());
            fallbackContainer.prefHeightProperty().bind(stage.heightProperty());
            fallbackContainer.setStyle("-fx-background-color: #F5E6A3;");

            VBox contentArea = new VBox(20);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(60, 80, 60, 80)); // Padding seimbang untuk positioning yang lebih baik
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Main content box dengan background putih - POSISI TENGAH dengan animasi pop
            // out pop in
            VBox contentBox = new VBox(15);
            contentBox.setAlignment(Pos.CENTER);
            contentBox.setPadding(new Insets(30, 40, 30, 40)); // Padding yang lebih besar
            contentBox.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 15; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 4);");
            contentBox.setMaxWidth(550);
            contentBox.setMaxHeight(400); // Tinggi yang lebih sesuai

            // Hapus margin negatif untuk posisi yang lebih natural
            // VBox.setMargin(contentBox, new Insets(0, 0, 0, 0)); // Tidak perlu margin

            // Tambahkan animasi pop out pop in berulang untuk contentBox (fallback)
            createContinuousPopAnimation(contentBox);

            // Hero section dengan logo SportEdu - smaller spacing
            VBox heroSection = new VBox(15); // Reduced from 20 to 15
            heroSection.setAlignment(Pos.CENTER);

            try {
                ImageView sportEduLogo = new ImageView(
                        new Image(getClass().getResourceAsStream("/images/SportEdu.png")));
                sportEduLogo.setFitHeight(60); // Reduced from 80 to 60
                sportEduLogo.setPreserveRatio(true);
                sportEduLogo.setSmooth(true);
                heroSection.getChildren().add(sportEduLogo);

                // Animasi pop out untuk logo SportEdu (fallback)
                createPopOutAnimation(sportEduLogo, 0.2);
            } catch (Exception logoException) {
                Label mainTitle = new Label("SportEdu");
                mainTitle.setStyle(
                        "-fx-font-family: 'Poppins', Arial; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;"); // Reduced
                                                                                                                                   // from
                                                                                                                                   // 48px
                                                                                                                                   // to
                                                                                                                                   // 36px
                heroSection.getChildren().add(mainTitle);

                // Animasi pop out untuk fallback title (fallback)
                createPopOutAnimation(mainTitle, 0.2);
            }

            // Gunakan gambar subtitle sebagai pengganti teks - smaller
            try {
                ImageView subtitleImage = new ImageView(new Image(
                        getClass().getResourceAsStream("/images/Aplikasi Pembelajaran Olahraga Interaktif.png")));
                subtitleImage.setFitHeight(22); // Reduced from 30 to 22
                subtitleImage.setPreserveRatio(true);
                subtitleImage.setSmooth(true);
                heroSection.getChildren().add(subtitleImage);

                // Animasi pop out untuk subtitle (fallback)
                createPopOutAnimation(subtitleImage, 0.4);
            } catch (Exception subtitleException) {
                Label subtitle = new Label("Platform pembelajaran olahraga interaktif untuk anak-anak");
                subtitle.setStyle(
                        "-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: 400; -fx-text-fill: #516BB0;"); // Reduced
                                                                                                                           // from
                                                                                                                           // 18px
                                                                                                                           // to
                                                                                                                           // 14px
                subtitle.setWrapText(true);

                heroSection.getChildren().add(subtitle);

                // Animasi pop out untuk fallback subtitle (fallback)
                createPopOutAnimation(subtitle, 0.4);
            }

            VBox buttonContainer = new VBox(15); // Reduced from 20 to 15
            buttonContainer.setAlignment(Pos.CENTER);

            materiButton = UIFactory.createMainButton("/images/Materi_butt.png", 250, 70); // Standardized to 250px
                                                                                           // width
            quizButton = UIFactory.createMainButton("/images/Quiz_butt.png", 250, 70); // Standardized to 250px width

            // Animasi pop out untuk tombol-tombol dengan hover sound setelah animasi
            // selesai (fallback)
            createPopOutAnimation(materiButton, 0.6, () -> addHoverSound(materiButton, "Materi.mp3"));
            createPopOutAnimation(quizButton, 0.8, () -> addHoverSound(quizButton, "Quiz.mp3"));

            buttonContainer.getChildren().addAll(materiButton, quizButton);

            contentBox.getChildren().addAll(heroSection, buttonContainer);
            contentArea.getChildren().add(contentBox);
            fallbackContainer.getChildren().add(contentArea);

            // Buat logo overlay di kiri atas (fallback)
            ImageView logoOverlay = null;
            try {
                logoOverlay = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
                logoOverlay.setFitHeight(50);
                logoOverlay.setPreserveRatio(true);
                logoOverlay.setSmooth(true);
            } catch (Exception logoException) {
                System.err.println("Logo tidak ditemukan, membuat placeholder");
                logoOverlay = new ImageView(); // Placeholder kosong
            }

            // Buat tombol info di kanan atas (fallback) - gunakan referensi yang sama
            if (infoButton == null) {
                infoButton = UIFactory.createNavButton("/images/info.png", 40, 40);
                infoButton.setOnAction(evt -> showInfoPopup());

                // Animasi pop out untuk tombol info (fallback)
                createPopOutAnimation(infoButton, 1.0);
            }

            // Final stack pane dengan overlay logo dan tombol info (fallback)
            StackPane finalFallbackStackPane = new StackPane();
            finalFallbackStackPane.getChildren().add(fallbackContainer);

            // Posisikan logo di kiri atas (fallback)
            finalFallbackStackPane.getChildren().add(logoOverlay);
            StackPane.setAlignment(logoOverlay, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlay, new Insets(20, 0, 0, 20));

            // Posisikan tombol info di kanan atas (fallback)
            finalFallbackStackPane.getChildren().add(infoButton);
            StackPane.setAlignment(infoButton, Pos.TOP_RIGHT);
            StackPane.setMargin(infoButton, new Insets(20, 20, 0, 0));

            // Langsung gunakan finalFallbackStackPane tanpa footer
            setView(finalFallbackStackPane);
        }
    }

    /**
     * Membuat dan menampilkan Halaman Pilih Materi (Tampilan 3)
     */
    public void showMateriPilihanPage() {
        // Buat logo overlay
        ImageView logoOverlay;
        try {
            logoOverlay = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
            logoOverlay.setFitHeight(50);
            logoOverlay.setPreserveRatio(true);
            logoOverlay.setSmooth(true);
        } catch (Exception logoException) {
            System.err.println("Logo tidak ditemukan, membuat placeholder");
            logoOverlay = new ImageView(); // Placeholder kosong
        }

        // Container utama dengan background dot pattern seperti landing page
        VBox mainContainer = new VBox();
        mainContainer.prefWidthProperty().bind(stage.widthProperty());
        mainContainer.prefHeightProperty().bind(stage.heightProperty());

        // Background dengan dot pattern dari vektor
        try {
            ImageView dotBackground = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/dot background.png")));
            dotBackground.fitWidthProperty().bind(stage.widthProperty());
            dotBackground.fitHeightProperty().bind(stage.heightProperty());
            dotBackground.setPreserveRatio(false);

            // Overlay container untuk konten
            VBox overlayContainer = new VBox();
            overlayContainer.prefWidthProperty().bind(stage.widthProperty());
            overlayContainer.prefHeightProperty().bind(stage.heightProperty());
            overlayContainer.setStyle("-fx-background-color: transparent;");

            // Content area utama
            VBox contentArea = new VBox(40);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(80, 100, 80, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Judul halaman menggunakan gambar
            try {
                ImageView titleImage = new ImageView(new Image(getClass().getResourceAsStream("/images/Materi.png")));
                titleImage.setFitHeight(40);
                titleImage.setPreserveRatio(true);
                titleImage.setSmooth(true);

                VBox titleContainer = new VBox(titleImage);
                titleContainer.setAlignment(Pos.CENTER);
                titleContainer.setPadding(new Insets(0, 0, 20, 0));

                // Animasi pop out untuk title Materi
                createPopOutAnimation(titleImage, 0.2);

                contentArea.getChildren().add(titleContainer);
            } catch (Exception titleException) {
                // Fallback jika gambar tidak ditemukan
                Label title = new Label("Pilih Materi");
                title.setStyle(
                        "-fx-font-family: 'Poppins', Arial; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;");

                // Animasi pop out untuk fallback title
                createPopOutAnimation(title, 0.2);

                contentArea.getChildren().add(title);
            }

            // Container untuk kartu olahraga
            HBox pilihanBox = new HBox(60);
            pilihanBox.setAlignment(Pos.CENTER);

            // Gunakan UIFactory untuk kartu olahraga dengan gambar yang benar -
            // standardized width
            sepakBolaButton = UIFactory.createSportCard("/images/SepakBola_butt.png", 250, 250); // Standardized to
                                                                                                 // 250px
            badmintonButton = UIFactory.createSportCard("/images/Badminton_butt.png", 250, 250); // Standardized to
                                                                                                 // 250px
            sepakBolaButton.setOpacity(0.7);
            badmintonButton.setOpacity(0.7);

            // Animasi pop out untuk tombol olahraga dengan hover sound setelah animasi
            // selesai
            createPopOutAnimation(sepakBolaButton, 0.4, () -> {
                addHoverSound(sepakBolaButton, "Sepak Bola.mp3");
                // Tambahkan animasi breathing setelah pop out selesai
                createContinuousPopAnimation(sepakBolaButton);
            });
            createPopOutAnimation(badmintonButton, 0.6, () -> {
                addHoverSound(badmintonButton, "Badminton.mp3");
                // Tambahkan animasi breathing setelah pop out selesai
                createContinuousPopAnimation(badmintonButton);
            });

            pilihanBox.getChildren().addAll(sepakBolaButton, badmintonButton);

            // Tombol kembali kuning
            kembaliButton = UIFactory.createNavButton("/images/Kembali_kuning.png", 250, 70); // Sama besar dengan
                                                                                              // tombol materi

            // Animasi pop out untuk tombol kembali
            createPopOutAnimation(kembaliButton, 0.8);

            contentArea.getChildren().addAll(pilihanBox, kembaliButton);
            overlayContainer.getChildren().add(contentArea);

            // Stack background dan overlay
            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
            stackPane.getChildren().addAll(dotBackground, overlayContainer);

            // Tambahkan logo di kiri atas sebagai overlay terpisah
            StackPane finalStackPane = new StackPane();
            finalStackPane.getChildren().add(stackPane);

            // Posisikan logo di kiri atas
            finalStackPane.getChildren().add(logoOverlay);
            StackPane.setAlignment(logoOverlay, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlay, new Insets(20, 0, 0, 20));

            // Buat ScrollPane untuk seluruh konten (tanpa footer)
            javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(finalStackPane);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPannable(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

            setView(scrollPane);

        } catch (Exception e) {
            // Fallback jika background dot tidak ditemukan
            VBox fallbackContainer = new VBox();
            fallbackContainer.prefWidthProperty().bind(stage.widthProperty());
            fallbackContainer.prefHeightProperty().bind(stage.heightProperty());
            fallbackContainer.setStyle("-fx-background-color: #F5E6A3;");

            VBox contentArea = new VBox(40);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(100, 100, 100, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            Label title = new Label("Pilih Materi");
            title.setStyle(
                    "-fx-font-family: 'Poppins', Arial; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;");

            // Animasi pop out untuk title (fallback)
            createPopOutAnimation(title, 0.2);

            HBox pilihanBox = new HBox(60);
            pilihanBox.setAlignment(Pos.CENTER);

            sepakBolaButton = UIFactory.createSportCard("/images/SepakBola.png", 250, 250); // Standardized to 250px
                                                                                            // fallback
            badmintonButton = UIFactory.createSportCard("/images/Badminton.png", 250, 250); // Standardized to 250px
                                                                                            // fallback

            sepakBolaButton.setOpacity(0.7);
            badmintonButton.setOpacity(0.7);
            // Animasi pop out untuk tombol olahraga dengan hover sound setelah animasi
            // selesai (fallback)
            createPopOutAnimation(sepakBolaButton, 0.4, () -> {
                addHoverSound(sepakBolaButton, "Sepak Bola.mp3");
                // Tambahkan animasi breathing setelah pop out selesai (fallback)
                createContinuousPopAnimation(sepakBolaButton);
            });
            createPopOutAnimation(badmintonButton, 0.6, () -> {
                addHoverSound(badmintonButton, "Badminton.mp3");
                // Tambahkan animasi breathing setelah pop out selesai (fallback)
                createContinuousPopAnimation(badmintonButton);
            });

            pilihanBox.getChildren().addAll(sepakBolaButton, badmintonButton);

            kembaliButton = UIFactory.createNavButton("/images/Back.png", 120, 50);

            // Animasi pop out untuk tombol kembali (fallback)
            createPopOutAnimation(kembaliButton, 0.8);

            contentArea.getChildren().addAll(title, pilihanBox, kembaliButton);
            fallbackContainer.getChildren().add(contentArea);

            // Tambahkan logo di kiri atas sebagai overlay
            StackPane finalFallbackContainer = new StackPane();
            finalFallbackContainer.getChildren().add(fallbackContainer);
            finalFallbackContainer.getChildren().add(logoOverlay);
            StackPane.setAlignment(logoOverlay, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlay, new Insets(20, 0, 0, 20));

            // Buat ScrollPane untuk seluruh konten (tanpa footer)
            javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(finalFallbackContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPannable(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

            setView(scrollPane);
        }
    }

    /**
     * Membuat dan menampilkan Halaman Quiz Utama (Tampilan 2)
     */
    public void showQuizModePage() {
        // Buat logo overlay
        ImageView logoOverlay;
        try {
            logoOverlay = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
            logoOverlay.setFitHeight(50);
            logoOverlay.setPreserveRatio(true);
            logoOverlay.setSmooth(true);
        } catch (Exception logoException) {
            System.err.println("Logo tidak ditemukan, membuat placeholder");
            logoOverlay = new ImageView(); // Placeholder kosong
        }

        // Container utama dengan background dot pattern seperti landing page
        VBox mainContainer = new VBox();
        mainContainer.prefWidthProperty().bind(stage.widthProperty());
        mainContainer.prefHeightProperty().bind(stage.heightProperty());

        // Background dengan dot pattern dari vektor
        try {
            ImageView dotBackground = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/dot background.png")));
            dotBackground.fitWidthProperty().bind(stage.widthProperty());
            dotBackground.fitHeightProperty().bind(stage.heightProperty());
            dotBackground.setPreserveRatio(false);

            // Overlay container untuk konten
            VBox overlayContainer = new VBox();
            overlayContainer.prefWidthProperty().bind(stage.widthProperty());
            overlayContainer.prefHeightProperty().bind(stage.heightProperty());
            overlayContainer.setStyle("-fx-background-color: transparent;");

            // Content area utama TANPA background box putih
            VBox contentArea = new VBox(60);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(80, 100, 80, 100));
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

                // Animasi pop out untuk logo Quiz
                createPopOutAnimation(quizLogo, 0.2);
            } catch (Exception logoException) {
                // Fallback jika gambar Quiz tidak ditemukan
                Label title = new Label("Quiz");
                title.setStyle(
                        "-fx-font-family: 'Poppins', Arial; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;");
                heroSection.getChildren().add(title);

                // Animasi pop out untuk fallback title
                createPopOutAnimation(title, 0.2);
            }

            // Action buttons container untuk Mulai dan Petunjuk
            VBox buttonContainer = new VBox(30);
            buttonContainer.setAlignment(Pos.CENTER);

            // Gunakan gambar tombol Mulai dan Petunjuk_butt dari resources
            mulaiButton = UIFactory.createMainButton("/images/Mulai.png", 250, 80);
            petunjukButton = UIFactory.createMainButton("/images/Petunjuk_butt.png", 250, 80);

            // Animasi pop out untuk tombol-tombol Quiz dengan hover sound setelah animasi
            // selesai
            createPopOutAnimation(mulaiButton, 0.4, () -> {
                addHoverSound(mulaiButton, "Mulai.mp3");
                // Tambahkan animasi breathing setelah pop out selesai
                createContinuousPopAnimation(mulaiButton);
            });
            createPopOutAnimation(petunjukButton, 0.6, () -> {
                addHoverSound(petunjukButton, "Petunjuk.mp3");
                // Tambahkan animasi breathing setelah pop out selesai
                createContinuousPopAnimation(petunjukButton);
            });

            buttonContainer.getChildren().addAll(mulaiButton, petunjukButton);

            // Tombol kembali kuning
            quizKembaliButton = UIFactory.createNavButton("/images/Kembali_kuning.png", 250, 70);

            // Animasi pop out untuk tombol kembali
            createPopOutAnimation(quizKembaliButton, 0.8);

            contentArea.getChildren().addAll(heroSection, buttonContainer, quizKembaliButton);
            overlayContainer.getChildren().add(contentArea);

            // Stack background dan overlay
            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
            stackPane.getChildren().addAll(dotBackground, overlayContainer);

            // Tambahkan logo di kiri atas sebagai overlay terpisah
            StackPane finalStackPane = new StackPane();
            finalStackPane.getChildren().add(stackPane);

            // Posisikan logo di kiri atas
            finalStackPane.getChildren().add(logoOverlay);
            StackPane.setAlignment(logoOverlay, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlay, new Insets(20, 0, 0, 20));

            // Buat ScrollPane untuk seluruh konten (tanpa footer)
            javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(finalStackPane);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPannable(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

            setView(scrollPane);

        } catch (Exception e) {
            // Fallback jika background dot tidak ditemukan
            System.err.println("Background dot tidak ditemukan, menggunakan background warna solid");

            VBox fallbackContainer = new VBox();
            fallbackContainer.prefWidthProperty().bind(stage.widthProperty());
            fallbackContainer.prefHeightProperty().bind(stage.heightProperty());
            fallbackContainer.setStyle("-fx-background-color: #F5E6A3;");

            VBox contentArea = new VBox(60);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(80, 100, 80, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            VBox heroSection = new VBox(50);
            heroSection.setAlignment(Pos.CENTER);

            try {
                ImageView quizLogo = new ImageView(new Image(getClass().getResourceAsStream("/images/QUIZ.png")));
                quizLogo.setFitHeight(80); // Dikecilkan dari 120 ke 80
                quizLogo.setPreserveRatio(true);
                quizLogo.setSmooth(true);
                heroSection.getChildren().add(quizLogo);

                // Animasi pop out untuk logo Quiz (fallback)
                createPopOutAnimation(quizLogo, 0.2);
            } catch (Exception logoException) {
                Label title = new Label("Quiz");
                title.setStyle(
                        "-fx-font-family: 'Poppins', Arial; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;");
                heroSection.getChildren().add(title);

                // Animasi pop out untuk fallback title (fallback)
                createPopOutAnimation(title, 0.2);
            }

            VBox buttonContainer = new VBox(30);
            buttonContainer.setAlignment(Pos.CENTER);

            mulaiButton = UIFactory.createMainButton("/images/Mulai.png", 250, 80);
            petunjukButton = UIFactory.createMainButton("/images/Petunjuk_butt.png", 250, 80);

            // Animasi pop out untuk tombol-tombol Quiz dengan hover sound setelah animasi
            // selesai (fallback)
            createPopOutAnimation(mulaiButton, 0.4, () -> {
                addHoverSound(mulaiButton, "Mulai.mp3");
                // Tambahkan animasi breathing setelah pop out selesai (fallback)
                createContinuousPopAnimation(mulaiButton);
            });
            createPopOutAnimation(petunjukButton, 0.6, () -> {
                addHoverSound(petunjukButton, "Petunjuk.mp3");
                // Tambahkan animasi breathing setelah pop out selesai (fallback)
                createContinuousPopAnimation(petunjukButton);
            });

            buttonContainer.getChildren().addAll(mulaiButton, petunjukButton);

            // Tombol kembali kuning (fallback)
            quizKembaliButton = UIFactory.createNavButton("/images/Kembali_kuning.png", 250, 70);

            // Animasi pop out untuk tombol kembali (fallback)
            createPopOutAnimation(quizKembaliButton, 0.8);

            contentArea.getChildren().addAll(heroSection, buttonContainer, quizKembaliButton);
            fallbackContainer.getChildren().add(contentArea);

            // Tambahkan logo di kiri atas sebagai overlay
            StackPane finalFallbackContainer = new StackPane();
            finalFallbackContainer.getChildren().add(fallbackContainer);
            finalFallbackContainer.getChildren().add(logoOverlay);
            StackPane.setAlignment(logoOverlay, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlay, new Insets(20, 0, 0, 20));

            // Buat ScrollPane untuk seluruh konten (tanpa footer)
            javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(finalFallbackContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPannable(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

            setView(scrollPane);
        }
    }

    /**
     * Menampilkan halaman pemilihan mode quiz (Tebak Gambar vs Mencocokkan Gambar)
     */
    public void showQuizModeSelectionPage() {
        // Container utama dengan background dot pattern dan overlay gelap
        VBox mainContainer = new VBox();
        mainContainer.prefWidthProperty().bind(stage.widthProperty());
        mainContainer.prefHeightProperty().bind(stage.heightProperty());

        // Background dengan dot pattern dan overlay gelap
        try {
            ImageView dotBackground = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/dot background.png")));
            dotBackground.fitWidthProperty().bind(stage.widthProperty());
            dotBackground.fitHeightProperty().bind(stage.heightProperty());
            dotBackground.setPreserveRatio(false);

            VBox darkOverlay = new VBox();
            darkOverlay.prefWidthProperty().bind(stage.widthProperty());
            darkOverlay.prefHeightProperty().bind(stage.heightProperty());
            darkOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");

            VBox overlayContainer = new VBox();
            overlayContainer.prefWidthProperty().bind(stage.widthProperty());
            overlayContainer.prefHeightProperty().bind(stage.heightProperty());
            overlayContainer.setStyle("-fx-background-color: transparent;");

            VBox contentArea = new VBox();
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(0, 100, 0, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            VBox contentBox = new VBox(30);
            contentBox.setAlignment(Pos.CENTER);
            contentBox.setPadding(new Insets(40, 50, 40, 50));
            contentBox.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 15, 0, 0, 8);");
            contentBox.setMaxWidth(600);
            contentBox.setMaxHeight(400);

            // Top bar: Cancel button (X) at top right
            HBox topBar = new HBox();
            topBar.setAlignment(Pos.TOP_RIGHT);
            topBar.setPrefWidth(600);
            quizModeSelectionCancelButton = UIFactory.createNavButton("/images/Cancel_butt.png", 40, 40); // Smaller X
                                                                                                          // button
            // Event handler akan di-set oleh Presenter

            // Animasi pop out untuk cancel button
            createPopOutAnimation(quizModeSelectionCancelButton, 0.2);

            topBar.getChildren().add(quizModeSelectionCancelButton);

            // Header with smaller 'Pilih Mode' image
            VBox headerSection = new VBox(20);
            headerSection.setAlignment(Pos.CENTER);
            try {
                ImageView pilihModeImage = new ImageView(
                        new Image(getClass().getResourceAsStream("/images/Pilih Mode.png")));
                pilihModeImage.setFitHeight(40); // Smaller than before
                pilihModeImage.setPreserveRatio(true);
                pilihModeImage.setSmooth(true);
                headerSection.getChildren().add(pilihModeImage);

                // Animasi pop out untuk Pilih Mode image
                createPopOutAnimation(pilihModeImage, 0.4);
            } catch (Exception logoException) {
                Label pilihModeLabel = new Label("Pilih Mode Kuis");
                pilihModeLabel.setStyle(
                        "-fx-font-family: 'Poppins', Arial; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;");
                headerSection.getChildren().add(pilihModeLabel);

                // Animasi pop out untuk fallback label
                createPopOutAnimation(pilihModeLabel, 0.4);
            }

            // Standardized quiz mode buttons
            VBox buttonsContainer = new VBox(30);
            buttonsContainer.setAlignment(Pos.CENTER);
            tebakGambarButton = UIFactory.createMainButton("/images/Tebak gambar_butt.png", 250, 70); // Standardized
                                                                                                      // width
            cocokGambarButton = UIFactory.createMainButton("/images/Mencocokkan gambar_butt.png", 250, 70); // Standardized
                                                                                                            // width

            // Animasi pop out untuk quiz mode buttons
            createPopOutAnimation(tebakGambarButton, 0.6);
            createPopOutAnimation(cocokGambarButton, 0.8);

            buttonsContainer.getChildren().addAll(tebakGambarButton, cocokGambarButton);

            contentBox.getChildren().addAll(topBar, headerSection, buttonsContainer);
            contentArea.getChildren().add(contentBox);
            overlayContainer.getChildren().add(contentArea);
            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
            stackPane.getChildren().addAll(dotBackground, darkOverlay, overlayContainer);
            setView(stackPane);
        } catch (Exception e) {
            // Fallback jika background dot tidak ditemukan
            System.err.println("Background dot tidak ditemukan, menggunakan background warna solid");

            VBox fallbackContainer = new VBox();
            fallbackContainer.prefWidthProperty().bind(stage.widthProperty());
            fallbackContainer.prefHeightProperty().bind(stage.heightProperty());
            fallbackContainer.setStyle("-fx-background-color: rgba(245, 230, 163, 0.7);"); // Background kuning dengan
                                                                                           // transparansi

            VBox contentArea = new VBox();
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(0, 100, 0, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            VBox contentBox = new VBox(30);
            contentBox.setAlignment(Pos.CENTER);
            contentBox.setPadding(new Insets(40, 50, 40, 50));
            contentBox.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 15, 0, 0, 8);");
            contentBox.setMaxWidth(600);
            contentBox.setMaxHeight(400);

            HBox topBar = new HBox();
            topBar.setAlignment(Pos.TOP_RIGHT);
            topBar.setPrefWidth(600);
            quizModeSelectionCancelButton = UIFactory.createNavButton("/images/Cancel_butt.png", 40, 40);
            // Event handler akan di-set oleh Presenter

            // Animasi pop out untuk cancel button (fallback)
            createPopOutAnimation(quizModeSelectionCancelButton, 0.2);

            topBar.getChildren().add(quizModeSelectionCancelButton);

            VBox headerSection = new VBox(20);
            headerSection.setAlignment(Pos.CENTER);
            try {
                ImageView pilihModeImage = new ImageView(
                        new Image(getClass().getResourceAsStream("/images/Pilih Mode.png")));
                pilihModeImage.setFitHeight(40);
                pilihModeImage.setPreserveRatio(true);
                pilihModeImage.setSmooth(true);
                headerSection.getChildren().add(pilihModeImage);

                // Animasi pop out untuk Pilih Mode image (fallback)
                createPopOutAnimation(pilihModeImage, 0.4);
            } catch (Exception logoException) {
                Label pilihModeLabel = new Label("Pilih Mode Kuis");
                pilihModeLabel.setStyle(
                        "-fx-font-family: 'Poppins', Arial; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;");
                headerSection.getChildren().add(pilihModeLabel);

                // Animasi pop out untuk fallback label (fallback)
                createPopOutAnimation(pilihModeLabel, 0.4);
            }
            VBox buttonsContainer = new VBox(30);
            buttonsContainer.setAlignment(Pos.CENTER);
            tebakGambarButton = UIFactory.createMainButton("/images/Tebak gambar_butt.png", 250, 70); // Standardized
                                                                                                      // width fallback
            cocokGambarButton = UIFactory.createMainButton("/images/Mencocokkan gambar_butt.png", 250, 70); // Standardized
                                                                                                            // width
                                                                                                            // fallback

            // Animasi pop out untuk quiz mode buttons (fallback)
            createPopOutAnimation(tebakGambarButton, 0.6);
            createPopOutAnimation(cocokGambarButton, 0.8);

            buttonsContainer.getChildren().addAll(tebakGambarButton, cocokGambarButton);
            contentBox.getChildren().addAll(topBar, headerSection, buttonsContainer);
            contentArea.getChildren().add(contentBox);
            fallbackContainer.getChildren().add(contentArea);
            setView(fallbackContainer);
        }
    }

    /**
     * Membuat footer sebagai Node yang bisa ditambahkan ke konten
     */
    private HBox createFooter() {
        try {
            // Load footer image
            ImageView footerImage = new ImageView(new Image(getClass().getResourceAsStream("/images/Footer.png")));

            // Set footer to be responsive - bind to stage width
            footerImage.fitWidthProperty().bind(stage.widthProperty());
            footerImage.setPreserveRatio(true);
            footerImage.setSmooth(true);

            // Create container for footer
            HBox footerContainer = new HBox();
            footerContainer.setAlignment(Pos.CENTER);
            footerContainer.setStyle("-fx-background-color: transparent;");
            footerContainer.getChildren().add(footerImage);

            return footerContainer;

        } catch (Exception e) {
            System.err.println("Footer image not found: " + e.getMessage());
            // Create a simple text footer as fallback
            Label footerLabel = new Label("SportEdu - Platform Pembelajaran Olahraga Interaktif");
            footerLabel.setStyle(
                    "-fx-font-family: 'Poppins', Arial; -fx-font-size: 12px; -fx-text-fill: #666; -fx-padding: 10;");

            HBox footerContainer = new HBox();
            footerContainer.setAlignment(Pos.CENTER);
            footerContainer
                    .setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1 0 0 0;");
            footerContainer.getChildren().add(footerLabel);

            return footerContainer;
        }
    }

    /**
     * Method untuk menghentikan semua audio dan BGM
     * Dipanggil ketika aplikasi akan ditutup
     */
    public void cleanup() {
        try {
            // Stop dan dispose current audio player
            if (currentAudioPlayer != null) {
                currentAudioPlayer.stop();
                currentAudioPlayer.dispose();
                currentAudioPlayer = null;
            }

            // Stop dan dispose BGM player
            if (bgmPlayer != null) {
                bgmPlayer.stop();
                bgmPlayer.dispose();
                bgmPlayer = null;
            }

            System.out.println("🎵 Audio cleanup completed");
        } catch (Exception e) {
            System.err.println("Error during audio cleanup: " + e.getMessage());
        }
    }
}

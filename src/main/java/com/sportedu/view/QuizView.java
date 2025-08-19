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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.animation.*;
import javafx.util.Duration;

/**
 * View untuk menampilkan gameplay kuis Tebak Gambar dengan desain sesuai
 * tampilan 2.1
 * Menggunakan navbar, dot background, dan layout yang proper
 * Ditambahkan sound effect untuk jawaban benar/salah
 */
public class QuizView {

    private final BorderPane view;
    private final Stage stage;
    private final Button kembaliButton;
    public Button[] pilihanButtons;

    // Overlay untuk menampilkan gif feedback
    private StackPane feedbackOverlay;
    private ImageView feedbackGifView;

    // Sound effect players
    private MediaPlayer correctSoundPlayer;
    private MediaPlayer wrongSoundPlayer;

    public QuizView(Stage stage) {
        this.stage = stage;
        view = new BorderPane();
        view.prefWidthProperty().bind(stage.widthProperty());
        view.prefHeightProperty().bind(stage.heightProperty());

        kembaliButton = UIFactory.createNavButton("/images/Back.png", 80, 40); // Tombol kembali yang lebih kecil

        // Inisialisasi sound effects
        initializeSoundEffects();

        setupLayout();
    }

    /**
     * Inisialisasi sound effect untuk jawaban benar dan salah
     */
    private void initializeSoundEffects() {
        try {
            // Load sound untuk jawaban benar
            var correctSoundUrl = getClass().getResource("/audio/benar.mp3");
            if (correctSoundUrl != null) {
                Media correctMedia = new Media(correctSoundUrl.toExternalForm());
                correctSoundPlayer = new MediaPlayer(correctMedia);
                correctSoundPlayer.setVolume(0.7); // Volume 70%
            }

            // Load sound untuk jawaban salah
            var wrongSoundUrl = getClass().getResource("/audio/salah.mp3");
            if (wrongSoundUrl != null) {
                Media wrongMedia = new Media(wrongSoundUrl.toExternalForm());
                wrongSoundPlayer = new MediaPlayer(wrongMedia);
                wrongSoundPlayer.setVolume(0.7); // Volume 70%
            }
        } catch (Exception e) {
            System.err.println("Error loading sound effects: " + e.getMessage());
        }
    }

    /**
     * Putar sound effect untuk jawaban benar
     */
    public void playCorrectSound() {
        try {
            if (correctSoundPlayer != null) {
                correctSoundPlayer.stop(); // Stop jika sedang playing
                correctSoundPlayer.seek(javafx.util.Duration.ZERO); // Reset ke awal
                correctSoundPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Error playing correct sound: " + e.getMessage());
        }
    }

    /**
     * Putar sound effect untuk jawaban salah
     */
    public void playWrongSound() {
        try {
            if (wrongSoundPlayer != null) {
                wrongSoundPlayer.stop(); // Stop jika sedang playing
                wrongSoundPlayer.seek(javafx.util.Duration.ZERO); // Reset ke awal
                wrongSoundPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Error playing wrong sound: " + e.getMessage());
        }
    }

    /**
     * Cleanup sound effects
     */
    public void cleanup() {
        try {
            if (correctSoundPlayer != null) {
                correctSoundPlayer.stop();
                correctSoundPlayer.dispose();
                correctSoundPlayer = null;
            }
            if (wrongSoundPlayer != null) {
                wrongSoundPlayer.stop();
                wrongSoundPlayer.dispose();
                wrongSoundPlayer = null;
            }
        } catch (Exception e) {
            System.err.println("Error cleaning up sound effects: " + e.getMessage());
        }
    }

    private void setupLayout() {
        // Background dengan dot pattern sesuai desain yang responsif
        try {
            ImageView dotBackground = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/dot background.png")));
            dotBackground.fitWidthProperty().bind(stage.widthProperty());
            dotBackground.fitHeightProperty().bind(stage.heightProperty());
            dotBackground.setPreserveRatio(false);

            // Overlay container untuk konten
            BorderPane overlayContainer = new BorderPane();
            overlayContainer.prefWidthProperty().bind(stage.widthProperty());
            overlayContainer.prefHeightProperty().bind(stage.heightProperty());
            overlayContainer.setStyle("-fx-background-color: transparent;");

            // Stack background dan overlay (HAPUS NAVBAR)
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
    public void displaySoal(Soal soal) {
        // Hapus konten lama dari overlay
        BorderPane overlayContainer = (BorderPane) ((StackPane) view.getCenter()).getChildren().get(1);

        // Content area utama dengan padding yang lebih kecil
        VBox contentArea = new VBox(20);
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setPadding(new Insets(30, 50, 30, 50));
        VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

        // Header dengan logo "Tebak Gambar" yang lebih kecil
        VBox headerSection = new VBox(15);
        headerSection.setAlignment(Pos.CENTER);

        try {
            ImageView tebakGambarLogo = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/Tebak Gambar.png")));
            tebakGambarLogo.setFitHeight(35); // Dikurangi dari 45 ke 35
            tebakGambarLogo.setPreserveRatio(true);
            headerSection.getChildren().add(tebakGambarLogo);
        } catch (Exception e) {
            Label title = new Label("Tebak Gambar");
            title.setStyle(
                    "-fx-font-family: 'Poppins', Arial; -fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;");
            headerSection.getChildren().add(title);
        }

        // Gambar soal TERPISAH di atas - dengan background kuning (diperkecil lagi)
        VBox gambarContainer = new VBox();
        gambarContainer.setAlignment(Pos.CENTER);
        gambarContainer.setPadding(new Insets(20)); // Dikurangi dari 25 ke 20
        gambarContainer.setStyle(
                "-fx-background-color: #FFBC4C; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 4);");
        gambarContainer.setMaxWidth(350); // Dikurangi dari 450 ke 350

        ImageView gambarSoal = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream(soal.getPertanyaan()));
            if (image.isError()) {
                throw new Exception("Failed to load image: " + soal.getPertanyaan());
            }
            gambarSoal.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading quiz image: " + soal.getPertanyaan() + " - " + e.getMessage());

            // Fallback: buat placeholder image dengan teks
            gambarSoal.setImage(null);
            gambarSoal.setStyle(
                    "-fx-background-color: #E0E0E0; -fx-border-color: #BDBDBD; -fx-border-width: 2; -fx-border-radius: 12;");

            // Tambahkan label untuk menunjukkan gambar tidak ditemukan
            Label errorLabel = new Label("Gambar tidak\nditemukan");
            errorLabel.setStyle(
                    "-fx-font-family: 'Poppins', Arial; -fx-font-size: 12px; -fx-text-fill: #757575; -fx-text-alignment: center;");
            gambarContainer.getChildren().add(errorLabel);
        }
        gambarSoal.setFitHeight(150); // Dikurangi dari 200 ke 150
        gambarSoal.setFitWidth(220); // Dikurangi dari 280 ke 220
        gambarSoal.setPreserveRatio(true);
        gambarSoal.setStyle("-fx-background-color: white; -fx-border-radius: 12;");

        gambarContainer.getChildren().add(gambarSoal);

        // Container untuk pertanyaan dan jawaban - SATU KOTAK PUTIH yang lebih kecil
        VBox questionAnswerContainer = new VBox(20);
        questionAnswerContainer.setAlignment(Pos.CENTER);
        questionAnswerContainer.setPadding(new Insets(30));
        questionAnswerContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 4);");
        questionAnswerContainer.setMaxWidth(700); // Dikurangi dari 900 ke 700

        // Pertanyaan di dalam kotak putih dengan font biru yang lebih kecil
        Label pertanyaanLabel = new Label("Apakah nama teknik pada gambar ini?");
        pertanyaanLabel.setStyle(
                "-fx-font-family: 'Poppins', Arial; -fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: #516BB0; -fx-text-alignment: center;");
        pertanyaanLabel.setWrapText(true);

        // Grid untuk pilihan jawaban (2x2) - dengan styling yang sesuai referensi 2.1
        // (box kuning) tapi lebih kecil
        GridPane pilihanJawabanPane = new GridPane();
        pilihanJawabanPane.setAlignment(Pos.CENTER);
        pilihanJawabanPane.setHgap(20); // Dikurangi dari 30 ke 20
        pilihanJawabanPane.setVgap(15); // Dikurangi dari 25 ke 15

        pilihanButtons = new Button[4];
        String[] buttonLabels = { "A", "B", "C", "D" };

        for (int i = 0; i < soal.getPilihanJawaban().length; i++) {
            // Format: [A] Jawaban
            String buttonText = "[" + buttonLabels[i] + "] " + soal.getPilihanJawaban()[i];
            pilihanButtons[i] = new Button(buttonText);
            pilihanButtons[i].setPrefSize(250, 50); // Dikurangi dari 350x70 ke 250x50

            // Styling box kuning sesuai referensi 2.1 dengan ukuran font yang lebih kecil
            pilihanButtons[i].setStyle(
                    "-fx-background-color: #FFBC4C; " +
                            "-fx-border-color: #FF9800; " +
                            "-fx-border-width: 2; " +
                            "-fx-border-radius: 10; " +
                            "-fx-background-radius: 10; " +
                            "-fx-font-family: 'Poppins', Arial; " +
                            "-fx-font-size: 14px; " + // Dikurangi dari 16px ke 14px
                            "-fx-font-weight: 600; " +
                            "-fx-text-fill: #1A1E2C; " +
                            "-fx-text-alignment: center; " +
                            "-fx-effect: dropshadow(gaussian, rgba(255,156,0,0.4), 4, 0, 0, 2);");

            // Hover effects dengan warna kuning lebih gelap
            final Button btn = pilihanButtons[i];
            btn.setOnMouseEntered(e -> btn.setStyle(
                    "-fx-background-color: #FF9800; " +
                            "-fx-border-color: #F57C00; " +
                            "-fx-border-width: 2; " +
                            "-fx-border-radius: 10; " +
                            "-fx-background-radius: 10; " +
                            "-fx-font-family: 'Poppins', Arial; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: 700; " +
                            "-fx-text-fill: white; " +
                            "-fx-text-alignment: center; " +
                            "-fx-effect: dropshadow(gaussian, rgba(255,156,0,0.6), 6, 0, 0, 3);"));

            btn.setOnMouseExited(e -> btn.setStyle(
                    "-fx-background-color: #FFBC4C; " +
                            "-fx-border-color: #FF9800; " +
                            "-fx-border-width: 2; " +
                            "-fx-border-radius: 10; " +
                            "-fx-background-radius: 10; " +
                            "-fx-font-family: 'Poppins', Arial; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: 600; " +
                            "-fx-text-fill: #1A1E2C; " +
                            "-fx-text-alignment: center; " +
                            "-fx-effect: dropshadow(gaussian, rgba(255,156,0,0.4), 4, 0, 0, 2);"));

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

        // Content area berisi: header, gambar (kotak kuning), pertanyaan+jawaban (kotak
        // putih), tombol kembali
        contentArea.getChildren().addAll(headerSection, gambarContainer, questionAnswerContainer, bottomSection);

        overlayContainer.setCenter(contentArea);
    }

    /**
     * Menampilkan efek pada jawaban setelah user memilih
     * Dengan sound effect yang diperbaiki dan menampilkan GIF feedback
     *
     * @param userAnswerIndex    index jawaban yang dipilih user (0-3)
     * @param correctAnswerIndex index jawaban yang benar (0-3)
     * @param isCorrect         apakah jawaban user benar atau salah
     */
    public void showAnswerEffects(int userAnswerIndex, int correctAnswerIndex, boolean isCorrect) {
        if (pilihanButtons == null)
            return;

        // Putar sound effect sesuai hasil jawaban
        if (isCorrect) {
            playCorrectSound();
        } else {
            playWrongSound();
        }

        // Hapus semua event handler hover terlebih dahulu
        for (int i = 0; i < pilihanButtons.length; i++) {
            pilihanButtons[i].setOnMouseEntered(null);
            pilihanButtons[i].setOnMouseExited(null);
        }

        // Warnai semua tombol merah terlebih dahulu
        for (int i = 0; i < pilihanButtons.length; i++) {
            pilihanButtons[i].setStyle(
                    "-fx-background-color: #F44336; " +
                            "-fx-border-color: #D32F2F; " +
                            "-fx-border-width: 3; " +
                            "-fx-border-radius: 10; " +
                            "-fx-background-radius: 10; " +
                            "-fx-font-family: 'Poppins', Arial; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: 700; " +
                            "-fx-text-fill: white; " +
                            "-fx-text-alignment: center; " +
                            "-fx-effect: dropshadow(gaussian, rgba(244,67,54,0.6), 8, 0, 0, 3);");
        }

        // Kemudian warnai jawaban benar dengan hijau
        if (correctAnswerIndex >= 0 && correctAnswerIndex < pilihanButtons.length) {
            pilihanButtons[correctAnswerIndex].setStyle(
                    "-fx-background-color: #4CAF50; " +
                            "-fx-border-color: #388E3C; " +
                            "-fx-border-width: 3; " +
                            "-fx-border-radius: 10; " +
                            "-fx-background-radius: 10; " +
                            "-fx-font-family: 'Poppins', Arial; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: 700; " +
                            "-fx-text-fill: white; " +
                            "-fx-text-alignment: center; " +
                            "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.6), 8, 0, 0, 3);");
        }

        // Disable semua tombol setelah jawaban ditampilkan
        for (Button btn : pilihanButtons) {
            btn.setDisable(true);
        }

        // Tampilkan GIF feedback setelah delay singkat untuk melihat perubahan warna tombol
        PauseTransition showGifDelay = new PauseTransition(Duration.millis(500));
        showGifDelay.setOnFinished(e -> {
            showResultOverlay(isCorrect, null); // Tampilkan gif feedback
        });
        showGifDelay.play();
    }

    /**
     * Method overload untuk kompatibilitas dengan kode lama
     */
    public void showAnswerEffects(int userAnswerIndex, int correctAnswerIndex) {
        // Tentukan apakah jawaban benar berdasarkan index
        boolean isCorrect = (userAnswerIndex == correctAnswerIndex);
        showAnswerEffects(userAnswerIndex, correctAnswerIndex, isCorrect);
    }

    /**
     * Reset tombol jawaban untuk soal baru
     */
    public void resetAnswerButtons() {
        if (pilihanButtons == null)
            return;

        for (Button btn : pilihanButtons) {
            btn.setDisable(false);
            btn.setStyle(
                    "-fx-background-color: #FFBC4C; " +
                            "-fx-border-color: #FF9800; " +
                            "-fx-border-width: 2; " +
                            "-fx-border-radius: 10; " +
                            "-fx-background-radius: 10; " +
                            "-fx-font-family: 'Poppins', Arial; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: 600; " +
                            "-fx-text-fill: #1A1E2C; " +
                            "-fx-text-alignment: center; " +
                            "-fx-effect: dropshadow(gaussian, rgba(255,156,0,0.4), 4, 0, 0, 2);");
        }
    }

    /**
     * Menampilkan loading overlay dengan checklist/cross GIF berdasarkan hasil jawaban
     * Loading selama 0.75 detik seperti pada mencocokkan gambar
     */
    public void showResultOverlay(boolean isCorrect, Runnable onComplete) {
        try {
            // Buat overlay container
            StackPane resultOverlay = new StackPane();
            resultOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
            resultOverlay.setPrefSize(stage.getWidth(), stage.getHeight());
            resultOverlay.prefWidthProperty().bind(stage.widthProperty());
            resultOverlay.prefHeightProperty().bind(stage.heightProperty());

            // Container untuk GIF result
            VBox resultContent = new VBox();
            resultContent.setAlignment(Pos.CENTER);
            resultContent.setPadding(new Insets(30));
            resultContent.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0, 0, 5);");
            resultContent.setMaxWidth(300);
            resultContent.setMaxHeight(300);

            // Load GIF berdasarkan hasil
            ImageView resultGif = new ImageView();
            boolean gifLoaded = false;

            try {
                // Gunakan GIF benar/salah yang sudah ditambahkan
                String gifPath = isCorrect ? "/images/benar.gif" : "/images/salah.gif";
                var gifUrl = getClass().getResource(gifPath);

                if (gifUrl != null) {
                    Image gifImage = new Image(gifUrl.toExternalForm());
                    if (!gifImage.isError()) {
                        resultGif.setImage(gifImage);
                        resultGif.setFitWidth(150); // Dikecilkan dari 200
                        resultGif.setFitHeight(150); // Dikecilkan dari 200
                        resultGif.setPreserveRatio(true);
                        gifLoaded = true;
                    }
                }

                // Fallback ke loading.gif jika GIF benar/salah tidak ditemukan
                if (!gifLoaded) {
                    var loadingUrl = getClass().getResource("/images/loading.gif");
                    if (loadingUrl != null) {
                        Image loadingImage = new Image(loadingUrl.toExternalForm());
                        if (!loadingImage.isError()) {
                            resultGif.setImage(loadingImage);
                            resultGif.setFitWidth(120);
                            resultGif.setFitHeight(120);
                            resultGif.setPreserveRatio(true);
                            gifLoaded = true;
                        }
                    }
                }

            } catch (Exception e) {
                System.err.println("Error loading GIF: " + e.getMessage());
            }

            // Tambahkan GIF jika berhasil dimuat
            if (gifLoaded && resultGif.getImage() != null) {
                resultContent.getChildren().add(resultGif);
            }

            // Fallback: Gunakan simbol Unicode yang besar
            if (!gifLoaded) {
                Label symbolLabel = new Label(isCorrect ? "✅" : "❌");
                symbolLabel.setStyle(
                    "-fx-font-size: 100px; " + // Dikecilkan dari 120px
                    "-fx-text-alignment: center;"
                );
                resultContent.getChildren().add(symbolLabel);
            }

            // Text hasil
            Label resultText = new Label(isCorrect ? "JAWABAN BENAR!" : "JAWABAN SALAH!");
            resultText.setStyle(
                "-fx-font-family: 'Poppins', Arial; " +
                "-fx-font-size: 24px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: " + (isCorrect ? "#4CAF50" : "#F44336") + ";" +
                "-fx-text-alignment: center;"
            );
            resultContent.getChildren().add(resultText);

            // Animasi entrance
            resultOverlay.setOpacity(0);
            resultContent.setScaleX(0.8);
            resultContent.setScaleY(0.8);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), resultOverlay);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), resultContent);
            scaleIn.setFromX(0.8);
            scaleIn.setFromY(0.8);
            scaleIn.setToX(1.0);
            scaleIn.setToY(1.0);

            ParallelTransition entranceAnimation = new ParallelTransition(fadeIn, scaleIn);

            resultOverlay.getChildren().add(resultContent);

            // Tambahkan ke view
            StackPane mainStackPane = (StackPane) view.getCenter();
            mainStackPane.getChildren().add(resultOverlay);

            // Simpan referensi
            resultOverlay.setUserData("resultOverlay");

            // Auto close setelah 2.5 detik untuk memastikan GIF berputar penuh beberapa kali
            PauseTransition autoClose = new PauseTransition(Duration.seconds(2.5));
            autoClose.setOnFinished(e -> {
                hideResultOverlay();
                if (onComplete != null) {
                    onComplete.run();
                }
            });

            entranceAnimation.setOnFinished(e -> autoClose.play());
            entranceAnimation.play();

        } catch (Exception e) {
            System.err.println("Error showing result overlay: " + e.getMessage());
            // Jika ada error, langsung jalankan callback
            if (onComplete != null) {
                onComplete.run();
            }
        }
    }

    /**
     * Menyembunyikan result overlay
     */
    private void hideResultOverlay() {
        try {
            StackPane mainStackPane = (StackPane) view.getCenter();

            // Cari overlay yang aktif
            StackPane overlayToRemove = null;
            for (var child : mainStackPane.getChildren()) {
                if (child.getUserData() != null && child.getUserData().equals("resultOverlay")) {
                    overlayToRemove = (StackPane) child;
                    break;
                }
            }

            if (overlayToRemove != null) {
                StackPane overlay = overlayToRemove;
                VBox content = (VBox) overlay.getChildren().get(0);

                // Animasi keluar
                FadeTransition fadeOut = new FadeTransition(Duration.millis(200), overlay);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);

                ScaleTransition scaleOut = new ScaleTransition(Duration.millis(300), content);
                scaleOut.setFromX(1.0);
                scaleOut.setFromY(1.0);
                scaleOut.setToX(0.8);
                scaleOut.setToY(0.8);

                ParallelTransition exitAnimation = new ParallelTransition(fadeOut, scaleOut);
                exitAnimation.setOnFinished(e -> mainStackPane.getChildren().remove(overlay));
                exitAnimation.play();
            }

        } catch (Exception e) {
            System.err.println("Error hiding result overlay: " + e.getMessage());
        }
    }

    // --- GETTER METHODS ---

    public Parent getView() {
        return view;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }
}

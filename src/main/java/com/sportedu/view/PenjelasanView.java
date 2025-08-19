package com.sportedu.view;

import com.sportedu.model.Teknik;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.PauseTransition;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.util.Duration;

/**
 * View untuk menampilkan penjelasan detail tentang teknik olahraga.
 * Sesuai dengan desain "3.1.1 Pengertian.png".
 */
public class PenjelasanView {

    private VBox view;
    private Button kembaliButton;
    private Label descriptionLabel;
    private ImageView teknikImageView;
    private ImageView techniquePhotoView;
    private ImageView animationGifView;
    private MediaView animationVideoView;
    private ImageView titleImageView;
    private ImageView loadingIndicator;
    private MediaPlayer mediaPlayer;
    private String currentTeknikName = "";

    public PenjelasanView() {
        view = new VBox();
        kembaliButton = UIFactory.createNavButton("/images/Kembali_kuning.png", 250, 70);

        kembaliButton.setOnAction(e -> {
            stopAudio();
            System.out.println("Audio stopped for KEMBALI navigation.");
        });

        view.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                onViewDeactivated();
            } else {
                onViewActivated();
            }
        });

        setupLayout();
    }

    private void forceResetButtonScale(Node button) {
        button.setOpacity(1.0);
        button.setScaleX(1.0);
        button.setScaleY(1.0);
        if (!button.getStyleClass().contains("scale-reset")) {
            button.getStyleClass().add("scale-reset");
        }
    }

    private void createContinuousPopAnimation(Node element) {
        element.setScaleX(1.0);
        element.setScaleY(1.0);

        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(2.0), element);
        scaleUp.setToX(1.03);
        scaleUp.setToY(1.03);
        scaleUp.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(2.0), element);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        scaleDown.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

        SequentialTransition breathingAnimation = new SequentialTransition(scaleUp, scaleDown);
        breathingAnimation.setCycleCount(javafx.animation.Animation.INDEFINITE);

        PauseTransition delay = new PauseTransition(Duration.seconds(1.0));
        delay.setOnFinished(e -> breathingAnimation.play());
        delay.play();
    }

    private void createPopOutAnimation(Node element, double delaySeconds, Runnable onComplete) {
        element.setOpacity(0);
        element.setScaleX(0);
        element.setScaleY(0);
        element.setDisable(true);

        PauseTransition delay = new PauseTransition(Duration.seconds(delaySeconds));
        FadeTransition fadeIn = new FadeTransition(Duration.millis(100), element);
        fadeIn.setToValue(1);
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), element);
        scaleUp.setToX(1.2);
        scaleUp.setToY(1.2);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), element);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        ParallelTransition fadeAndScaleUp = new ParallelTransition(fadeIn, scaleUp);
        SequentialTransition animation = new SequentialTransition(delay, fadeAndScaleUp, scaleDown);

        animation.setOnFinished(e -> {
            forceResetButtonScale(element);
            element.setDisable(false);
            if (onComplete != null) {
                onComplete.run();
            }
        });
        animation.play();
    }

    private void setupLayout() {
        view.getChildren().clear();
        try {
            ImageView dotBackground = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/dot background.png")));
            dotBackground.fitWidthProperty().bind(view.widthProperty());
            dotBackground.fitHeightProperty().bind(view.heightProperty());
            dotBackground.setPreserveRatio(false);

            StackPane mainContainer = new StackPane();
            mainContainer.prefWidthProperty().bind(view.widthProperty());
            mainContainer.prefHeightProperty().bind(view.heightProperty());

            VBox overlayContainer = new VBox();
            overlayContainer.prefWidthProperty().bind(view.widthProperty());
            overlayContainer.prefHeightProperty().bind(view.heightProperty());
            overlayContainer.setStyle("-fx-background-color: transparent;");

            ImageView logoOverlay = new ImageView();
            try {
                logoOverlay = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
                logoOverlay.setFitHeight(50);
                logoOverlay.setPreserveRatio(true);
                logoOverlay.setSmooth(true);
            } catch (Exception logoException) {
                System.err.println("Logo not found, using placeholder.");
            }

            VBox contentArea = createContentArea();
            overlayContainer.getChildren().add(contentArea);

            StackPane.setAlignment(logoOverlay, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlay, new Insets(20, 0, 0, 20));

            mainContainer.getChildren().addAll(dotBackground, overlayContainer, logoOverlay);

            ScrollPane scrollPane = new ScrollPane(mainContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

            view.getChildren().add(scrollPane);

        } catch (Exception e) {
            System.err.println("Error setting up main layout with dot background. Using fallback: " + e.getMessage());
            setupFallbackLayout();
        }
    }

    private void setupFallbackLayout() {
        view.getChildren().clear();
        StackPane mainContainer = new StackPane();
        mainContainer.prefWidthProperty().bind(view.widthProperty());
        mainContainer.prefHeightProperty().bind(view.heightProperty());
        mainContainer.setStyle("-fx-background-color: #F5E6A3;");

        VBox contentArea = createContentArea();
        mainContainer.getChildren().add(contentArea);

        ImageView logoOverlayFallback = new ImageView();
        try {
            logoOverlayFallback = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
            logoOverlayFallback.setFitHeight(50);
            logoOverlayFallback.setPreserveRatio(true);
            logoOverlayFallback.setSmooth(true);
            StackPane.setAlignment(logoOverlayFallback, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlayFallback, new Insets(20, 0, 0, 20));
            mainContainer.getChildren().add(logoOverlayFallback);
        } catch (Exception logoException) {
            System.err.println("Logo not found in fallback setup.");
        }

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        view.getChildren().add(scrollPane);
    }

    private VBox createContentArea() {
        VBox contentArea = new VBox(15);
        contentArea.setAlignment(Pos.TOP_CENTER);
        contentArea.setPadding(new Insets(15, 40, 12, 40));
        VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

        titleImageView = new ImageView();
        titleImageView.setFitHeight(35);
        titleImageView.setPreserveRatio(true);

        HBox mainContentArea = new HBox(15);
        mainContentArea.setAlignment(Pos.CENTER);
        mainContentArea.setPrefHeight(580);

        VBox whiteBox = new VBox(12);
        whiteBox.setAlignment(Pos.CENTER);
        whiteBox.setPadding(new Insets(18, 25, 18, 25));
        whiteBox.setStyle(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        whiteBox.setMaxWidth(550);
        whiteBox.setPrefWidth(550);
        whiteBox.setMaxHeight(430);

        teknikImageView = new ImageView();
        teknikImageView.setFitWidth(280);
        teknikImageView.setFitHeight(90);
        teknikImageView.setPreserveRatio(true);

        techniquePhotoView = new ImageView();
        techniquePhotoView.setFitWidth(240);
        techniquePhotoView.setFitHeight(120);
        techniquePhotoView.setPreserveRatio(true);

        animationGifView = new ImageView();
        animationGifView.setFitWidth(240);
        animationGifView.setFitHeight(120);
        animationGifView.setPreserveRatio(true);

        // MediaView untuk MP4 tidak perlu lagi digunakan
        animationVideoView = new MediaView();
        animationVideoView.setFitWidth(240);
        animationVideoView.setFitHeight(120);
        animationVideoView.setPreserveRatio(true);
        animationVideoView.setVisible(false);

        loadingIndicator = new ImageView(new Image(getClass().getResourceAsStream("/images/loading.gif")));
        loadingIndicator.setFitHeight(50);
        loadingIndicator.setFitWidth(50);
        loadingIndicator.setVisible(false);

        HBox techniqueContainer = new HBox(15);
        techniqueContainer.setAlignment(Pos.CENTER);
        StackPane animationStack = new StackPane(animationGifView, animationVideoView, loadingIndicator);
        techniqueContainer.getChildren().addAll(techniquePhotoView, animationStack);

        descriptionLabel = new Label();
        descriptionLabel.setStyle(
                "-fx-font-family: 'Poppins', Arial; -fx-font-size: 12px; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(480);
        descriptionLabel.setMaxHeight(100);
        descriptionLabel.setAlignment(Pos.CENTER);

        whiteBox.getChildren().addAll(teknikImageView, techniqueContainer, descriptionLabel);

        createPopOutAnimation(whiteBox, 0.4, () -> createContinuousPopAnimation(whiteBox));
        mainContentArea.getChildren().add(whiteBox);

        HBox bottomButtonArea = new HBox();
        bottomButtonArea.setAlignment(Pos.CENTER);
        bottomButtonArea.setPrefHeight(45);
        bottomButtonArea.getChildren().add(kembaliButton);
        createPopOutAnimation(kembaliButton, 1.2, null);

        contentArea.getChildren().addAll(titleImageView, mainContentArea, bottomButtonArea);
        return contentArea;
    }

    public void displayTeknik(Teknik teknik) {
        stopAudio();
        currentTeknikName = teknik.getNama().toLowerCase();

        try {
            String titlePath = "/images/" + teknik.getNama() + "_title.png";
            Image titleImage = new Image(getClass().getResourceAsStream(titlePath));
            if (!titleImage.isError()) {
                titleImageView.setImage(titleImage);
                createPopOutAnimation(titleImageView, 0.2, null);
            } else {
                titleImageView.setImage(null);
                System.err.println("Title image not found for " + teknik.getNama());
            }
        } catch (Exception e) {
            titleImageView.setImage(null);
        }

        try {
            String imagePath = "/images/pengertian_" + currentTeknikName + ".png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            teknikImageView.setImage(image);
        } catch (Exception e) {
            teknikImageView.setImage(null);
            System.err.println("Error loading explanation image: " + e.getMessage());
        }

        try {
            String techniqueImagePath = "";
            switch (currentTeknikName) {
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
                    techniqueImagePath = "/images/badminton_footwork.jpg";
                    break;
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
                    techniqueImagePath = "/images/soccer_passing.jpg";
                    break;
            }
            if (!techniqueImagePath.isEmpty()) {
                Image techniqueImage = new Image(getClass().getResourceAsStream(techniqueImagePath));
                techniquePhotoView.setImage(techniqueImage);
            } else {
                techniquePhotoView.setImage(null);
            }
        } catch (Exception e) {
            techniquePhotoView.setImage(null);
            System.err.println("Error loading technique photo: " + e.getMessage());
        }

        setAnimatedMedia();
        descriptionLabel.setText(teknik.getDeskripsi());
        playAudioForCurrentTeknik();
    }

    private void setAnimatedMedia() {
        // Tampilkan loading indicator di awal
        loadingIndicator.setVisible(true);

        // Gunakan Delayed Task untuk mensimulasikan waktu pemuatan
        PauseTransition delay = new PauseTransition(Duration.seconds(1)); // Tambahkan jeda 1 detik untuk loading
        delay.setOnFinished(e -> {
            // Coba muat GIF
            try {
                String gifPath = "/images/" + currentTeknikName + ".gif";
                Image gif = new Image(getClass().getResourceAsStream(gifPath));

                // Periksa apakah gambar berhasil dimuat
                if (!gif.isError() && gif.getRequestedWidth() > 0) {
                    animationGifView.setImage(gif);
                    animationGifView.setVisible(true);
                    animationVideoView.setVisible(false);
                    loadingIndicator.setVisible(false);
                    System.out.println("SUCCESS: GIF animation loaded for " + currentTeknikName);
                } else {
                    // Fallback ke PNG jika GIF tidak ditemukan atau tidak valid
                    System.err.println("GIF not found or not valid, trying PNG static image.");
                    try {
                        String pngPath = "/images/animasi_" + currentTeknikName + ".png";
                        Image staticImage = new Image(getClass().getResourceAsStream(pngPath));
                        if (!staticImage.isError()) {
                            animationGifView.setImage(staticImage);
                            animationGifView.setVisible(true);
                            animationVideoView.setVisible(false);
                            loadingIndicator.setVisible(false);
                            System.out.println("SUCCESS: PNG fallback loaded for " + currentTeknikName);
                        } else {
                            animationGifView.setImage(null);
                            loadingIndicator.setVisible(false);
                            System.err.println("PNG fallback image not found.");
                        }
                    } catch (Exception pngEx) {
                        animationGifView.setImage(null);
                        loadingIndicator.setVisible(false);
                        System.err.println("PNG fallback also failed: " + pngEx.getMessage());
                    }
                }
            } catch (Exception gifEx) {
                // Fallback jika terjadi error saat memuat GIF
                System.err.println("Error loading GIF: " + gifEx.getMessage());
                loadingIndicator.setVisible(false);
            }
        });
        delay.play();
    }

    private void playAudioForCurrentTeknik() {
        stopAudio();

        try {
            String audioPath = "/audio/" + currentTeknikName + ".mp3";
            var audioURL = getClass().getResource(audioPath);

            if (audioURL != null) {
                Media media = new Media(audioURL.toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnReady(() -> mediaPlayer.play());
                mediaPlayer.setOnError(() -> System.err.println("Error playing audio: " + mediaPlayer.getError()));
            } else {
                System.err.println("Audio file not found for: " + currentTeknikName);
            }
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    public void stopAudio() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.dispose();
                System.out.println("Audio stopped and disposed for: " + currentTeknikName);
            } catch (Exception e) {
                System.err.println("Error during audio cleanup: " + e.getMessage());
            } finally {
                mediaPlayer = null;
            }
        }
    }

    public void onViewDeactivated() {
        stopAudio();
    }

    public void onViewActivated() {
    }

    public void cleanup() {
        stopAudio();
    }

    public Parent getView() {
        return view;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }
}
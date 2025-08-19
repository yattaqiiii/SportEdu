package com.sportedu.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View untuk kuis mencocokkan gambar dengan navbar, scrolling, dan responsive
 * sizing
 * Responsive design yang mengikuti pola MainView
 */
public class MatchingQuizFXView {

    // Interface untuk callback ketika game selesai
    public interface OnGameCompleteListener {
        void onGameComplete(int correctAnswers, int totalQuestions);
    }

    // Interface untuk callback ketika match attempt
    public interface OnMatchAttemptListener {
        void onMatchAttempt(boolean isCorrect);
    }

    // Interface untuk callback ketika next round
    public interface OnNextRoundListener {
        void onNextRound();
    }

    // Interface untuk callback navigasi
    public interface OnNavigationListener {
        void onNavigateToHome();

        void onNavigateToMateri();

        void onNavigateToQuiz();

        void onNavigateBack();
    }

    private final BorderPane view;
    private final Stage stage;
    private Button kembaliButton;

    // Container untuk lines
    private Pane lineContainer;

    // Data untuk matching
    private List<String> imagePaths;
    private List<String> answerTexts;
    private List<String> imageIds;
    private List<String> correctAnswerIds;

    // Tracking connections
    private final Map<String, String> connections = new HashMap<>();
    private final Map<String, Button> imageButtons = new HashMap<>();
    private final Map<String, Button> answerButtons = new HashMap<>();
    private final Map<String, Line> connectionLines = new HashMap<>();

    // Line drawing
    private String selectedImageId = null;

    // Sound effect players
    private MediaPlayer correctSoundPlayer;
    private MediaPlayer wrongSoundPlayer;

    // Game completion callbacks
    private OnGameCompleteListener onGameComplete;
    private OnMatchAttemptListener onMatchAttempt;
    private OnNextRoundListener onNextRound;
    private OnNavigationListener onNavigation;

    public MatchingQuizFXView(Stage stage) {
        this.stage = stage;
        view = new BorderPane();

        // Remove fixed size constraints to allow responsive design
        // view.setPrefSize(1280, 832);
        // view.setMaxSize(1280, 832);

        // Initialize kembali button even though it won't be displayed
        kembaliButton = new Button("Kembali");
        kembaliButton.setVisible(false); // Hide it but keep it initialized

        // Inisialisasi sound effects
        initializeSoundEffects();

        // Tambahkan stylesheet
        try {
            view.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            view.getStyleClass().add("root");
        } catch (Exception e) {
            System.err.println("Could not load stylesheet: " + e.getMessage());
        }

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
        // Set background beige
        view.setStyle("-fx-background-color: #F5E6A3;");

        // Background dengan dot pattern - make responsive
        try {
            ImageView dotBackground = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/dot background.png")));
            // Make background responsive to window size like MainView
            dotBackground.fitWidthProperty().bind(stage.widthProperty());
            dotBackground.fitHeightProperty().bind(stage.heightProperty());
            dotBackground.setPreserveRatio(false);

            // Container utama - make responsive
            BorderPane mainContainer = new BorderPane();
            mainContainer.prefWidthProperty().bind(stage.widthProperty());
            mainContainer.prefHeightProperty().bind(stage.heightProperty());
            mainContainer.setStyle("-fx-background-color: transparent;");

            // Content area dengan scroll - make responsive dan center content
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.prefWidthProperty().bind(stage.widthProperty());
            scrollPane.prefHeightProperty().bind(stage.heightProperty()); // Remove navbar height subtraction
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true); // Add this to make content fit height
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            // Wrapper untuk center content vertikal
            StackPane contentWrapper = new StackPane();
            contentWrapper.setAlignment(Pos.CENTER);
            contentWrapper.setMinHeight(600);

            // Content container
            VBox contentContainer = createContentContainer();
            contentWrapper.getChildren().add(contentContainer);
            scrollPane.setContent(contentWrapper);

            mainContainer.setCenter(scrollPane);

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

            // Stack background dan main container
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(dotBackground, mainContainer);

            // Tambahkan logo di kiri atas sebagai overlay terpisah
            StackPane finalStackPane = new StackPane();
            finalStackPane.getChildren().add(stackPane);

            // Posisikan logo di kiri atas
            finalStackPane.getChildren().add(logoOverlay);
            StackPane.setAlignment(logoOverlay, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlay, new Insets(20, 0, 0, 20));

            view.setCenter(finalStackPane);

        } catch (Exception e) {
            // Fallback background beige dengan responsive design
            view.setStyle("-fx-background-color: #F5E6A3;");
            System.err.println("Gagal memuat dot background: " + e.getMessage());

            // Simple layout without background image - but still responsive (HAPUS NAVBAR)
            BorderPane mainContainer = new BorderPane();
            mainContainer.prefWidthProperty().bind(stage.widthProperty());
            mainContainer.prefHeightProperty().bind(stage.heightProperty());

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.prefWidthProperty().bind(stage.widthProperty());
            scrollPane.prefHeightProperty().bind(stage.heightProperty()); // Remove navbar height subtraction
            scrollPane.setStyle("-fx-background-color: transparent;");
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true); // Add this to make content fit height
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            // Wrapper untuk center content vertikal
            StackPane contentWrapper = new StackPane();
            contentWrapper.setAlignment(Pos.CENTER);
            contentWrapper.setMinHeight(600);

            VBox contentContainer = createContentContainer();
            contentWrapper.getChildren().add(contentContainer);
            scrollPane.setContent(contentWrapper);
            mainContainer.setCenter(scrollPane);

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

            // Stack main container dengan logo overlay
            StackPane fallbackStackPane = new StackPane();
            fallbackStackPane.getChildren().add(mainContainer);
            fallbackStackPane.getChildren().add(logoOverlayFallback);
            StackPane.setAlignment(logoOverlayFallback, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlayFallback, new Insets(20, 0, 0, 20));

            view.setCenter(fallbackStackPane);
        }
    }

    private VBox createContentContainer() {
        VBox container = new VBox(15); // Reduced spacing from 30 to 15
        container.setAlignment(Pos.CENTER); // Changed from TOP_CENTER to CENTER
        container.setPadding(new Insets(40, 30, 40, 30)); // Balanced padding
        container.setStyle("-fx-background-color: transparent;");
        container.setMinHeight(600); // Increased back to 600 to allow centering

        // Header dengan gambar "Cocokkan Gambar" - smaller size
        VBox headerSection = new VBox(10); // Reduced spacing from 20 to 10
        headerSection.setAlignment(Pos.CENTER);

        try {
            ImageView headerImage = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/Cocokkan Gambar.png")));
            headerImage.setFitHeight(35); // Reduced from 45 to 35
            headerImage.setPreserveRatio(true);
            headerSection.getChildren().add(headerImage);
        } catch (Exception e) {
            Label headerLabel = new Label("Mencocokkan Gambar");
            headerLabel.setStyle(
                    "-fx-font-family: 'Poppins', Arial; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #516BB0;"); // Reduced
            // from
            // 24px
            // to
            // 20px
            headerSection.getChildren().add(headerLabel);
        }

        // Instruksi dengan font yang lebih kecil
        Label instruksiLabel = new Label(
                "Hubungkan gambar dengan jawaban yang tepat dengan menarik garis dari titik ke titik");
        instruksiLabel.setStyle(
                "-fx-font-family: 'Poppins', Arial; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #516BB0; -fx-text-alignment: center;"); // Reduced
        // from
        // 14px
        // to
        // 12px
        instruksiLabel.setWrapText(true);
        instruksiLabel.setMaxWidth(400); // Reduced from 500 to 400
        headerSection.getChildren().add(instruksiLabel);

        container.getChildren().add(headerSection);
        return container;
    }

    /**
     * Setup matching game dengan data gambar dan jawaban
     */
    public void setupMatchingGame(List<String> imagePaths, List<String> answerTexts, List<String> imageIds,
            List<String> correctAnswerIds) {
        this.imagePaths = imagePaths;
        this.answerTexts = answerTexts;
        this.imageIds = imageIds;
        this.correctAnswerIds = correctAnswerIds;

        // Clear previous connections
        connections.clear();
        imageButtons.clear();
        answerButtons.clear();
        connectionLines.clear();

        // Get content container through the new wrapper structure
        // Structure: view.getCenter() -> finalStackPane -> stackPane -> mainContainer
        // (BorderPane) -> scrollPane -> contentWrapper -> contentContainer
        StackPane finalStackPane = (StackPane) view.getCenter();
        StackPane stackPane = (StackPane) finalStackPane.getChildren().get(0); // First child is the inner stackPane
        BorderPane mainContainer = (BorderPane) stackPane.getChildren().get(1); // Second child of inner stackPane is
                                                                                // mainContainer
        ScrollPane scrollPane = (ScrollPane) mainContainer.getCenter();
        StackPane contentWrapper = (StackPane) scrollPane.getContent(); // Now we have contentWrapper
        VBox contentContainer = (VBox) contentWrapper.getChildren().get(0); // Get the VBox from contentWrapper

        // Clear previous game content (keep header)
        if (contentContainer.getChildren().size() > 1) {
            contentContainer.getChildren().subList(1, contentContainer.getChildren().size()).clear();
        }

        // Game area dengan line container - smaller size
        StackPane gameArea = new StackPane();
        gameArea.setPrefHeight(300); // Reduced from 450 to 300
        gameArea.setMaxHeight(300);

        // Line container untuk menggambar garis (di layer paling atas) - smaller
        lineContainer = new Pane();
        lineContainer.setPrefSize(700, 300); // Reduced from 900,450 to 700,300
        lineContainer.setStyle("-fx-background-color: transparent;");
        lineContainer.setMouseTransparent(true); // Allow clicks to pass through

        // Main game layout - reduced spacing and padding
        HBox gameLayout = new HBox(60); // Reduced space between columns from 100 to 60
        gameLayout.setAlignment(Pos.CENTER);
        gameLayout.setPadding(new Insets(20)); // Reduced padding from 30 to 20

        // Left side - Images
        VBox imageColumn = createImageColumn();

        // Right side - Answers
        VBox answerColumn = createAnswerColumn();

        gameLayout.getChildren().addAll(imageColumn, answerColumn);

        // Add game layout first, then line container on top
        gameArea.getChildren().addAll(gameLayout, lineContainer);

        contentContainer.getChildren().add(gameArea);

        // Add Kembali button at the bottom - smaller size
        HBox buttonSection = new HBox();
        buttonSection.setAlignment(Pos.CENTER);
        buttonSection.setPadding(new Insets(10, 0, 10, 0)); // Reduced padding

        // Create visible and functional Kembali button with image
        kembaliButton = new Button();
        kembaliButton.setVisible(true);

        // Set up image for the button
        try {
            ImageView kembaliImage = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/Kembali_merah.png")));
            kembaliImage.setFitHeight(30); // Adjust height as needed
            kembaliImage.setPreserveRatio(true);
            kembaliImage.setSmooth(true);
            kembaliButton.setGraphic(kembaliImage);
        } catch (Exception e) {
            // Fallback to text if image not found
            kembaliButton.setText("Kembali");
            System.err.println("Could not load Kembali_merah.png: " + e.getMessage());
        }

        kembaliButton.setPrefSize(80, 35); // Slightly taller for image
        kembaliButton.setStyle(
                "-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 5;");

        // Add hover effects - simple scaling
        kembaliButton.setOnMouseEntered(e -> {
            kembaliButton.setScaleX(1.1);
            kembaliButton.setScaleY(1.1);
        });

        kembaliButton.setOnMouseExited(e -> {
            kembaliButton.setScaleX(1.0);
            kembaliButton.setScaleY(1.0);
        });

        // Set navigation action
        kembaliButton.setOnAction(e -> {
            if (onNavigation != null) {
                onNavigation.onNavigateBack();
            }
        });

        buttonSection.getChildren().add(kembaliButton);
        contentContainer.getChildren().add(buttonSection);
    }

    private VBox createImageColumn() {
        VBox imageColumn = new VBox(20); // Reduced spacing from 30 to 20
        imageColumn.setAlignment(Pos.CENTER_LEFT);
        imageColumn.setPrefWidth(280); // Reduced from 350 to 280

        // Remove subtitle - no "Gambar" label

        for (int i = 0; i < imagePaths.size(); i++) {
            HBox imageRow = createImageRow(imagePaths.get(i), imageIds.get(i), i);
            imageColumn.getChildren().add(imageRow);
        }

        return imageColumn;
    }

    private HBox createImageRow(String imagePath, String imageId, int index) {
        HBox row = new HBox(10); // Reduced spacing from 15 to 10
        row.setAlignment(Pos.CENTER_LEFT);

        // Image container with white background - smaller size
        VBox imageContainer = new VBox();
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setStyle(
                "-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0, 0, 2); -fx-padding: 8;"); // Reduced
        // radius
        // and
        // padding

        try {
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
            // Smaller image constraints
            imageView.setFitWidth(100); // Reduced from 140 to 100
            imageView.setFitHeight(60); // Reduced from 85 to 60
            imageView.setPreserveRatio(true);

            // Container adapts to smaller image size
            imageContainer.getChildren().add(imageView);
            imageContainer.setPrefSize(116, 76); // Reduced from 160,105 to 116,76
        } catch (Exception e) {
            Label placeholder = new Label("Gambar " + (index + 1));
            placeholder.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;"); // Reduced font size
            imageContainer.getChildren().add(placeholder);
            imageContainer.setPrefSize(116, 76);
        }

        // Smaller connection point (dot)
        Button connectPoint = new Button();
        connectPoint.setPrefSize(22, 22); // Reduced from 28,28 to 22,22
        connectPoint.setStyle(
                "-fx-background-color: white; -fx-border-width: 2; -fx-border-color: #ccc; -fx-background-radius: 11; -fx-padding: 0;"); // Updated
        // radius

        connectPoint.setOnAction(e -> startConnection(imageId, connectPoint));

        imageButtons.put(imageId, connectPoint);
        row.getChildren().addAll(imageContainer, connectPoint);
        return row;
    }

    private VBox createAnswerColumn() {
        VBox answerColumn = new VBox(20); // Reduced spacing from 30 to 20
        answerColumn.setAlignment(Pos.CENTER_RIGHT);
        answerColumn.setPrefWidth(280); // Reduced from 350 to 280

        // Remove subtitle - no "Jawaban" label

        for (int i = 0; i < answerTexts.size(); i++) {
            HBox answerRow = createAnswerRow(answerTexts.get(i), imageIds.get(i));
            answerColumn.getChildren().add(answerRow);
        }

        return answerColumn;
    }

    private HBox createAnswerRow(String answerText, String answerId) {
        HBox row = new HBox(10); // Reduced spacing from 15 to 10
        row.setAlignment(Pos.CENTER_RIGHT);

        // Smaller connection point (dot)
        Button connectPoint = new Button();
        connectPoint.setPrefSize(22, 22); // Reduced from 28,28 to 22,22
        connectPoint.setStyle(
                "-fx-background-color: white; -fx-border-width: 2; -fx-border-color: #ccc; -fx-background-radius: 11; -fx-padding: 0;"); // Updated
        // radius

        connectPoint.setOnAction(e -> completeConnection(answerId, connectPoint));

        // Answer container with white background - smaller size
        VBox answerContainer = new VBox();
        answerContainer.setAlignment(Pos.CENTER);
        answerContainer.setPrefSize(116, 70); // Reduced from 160,100 to 116,70
        answerContainer.setStyle(
                "-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0, 0, 2);"); // Reduced
        // radius
        // and
        // shadow

        // Answer label with blue text, smaller and centered
        Label answerLabel = new Label(answerText);
        answerLabel.setStyle(
                "-fx-font-family: 'Poppins', Arial; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #516BB0; -fx-text-alignment: center;"); // Reduced
        // from
        // 14px
        // to
        // 11px
        answerLabel.setWrapText(true);
        answerLabel.setMaxWidth(100); // Reduced from 140 to 100
        answerLabel.setAlignment(Pos.CENTER);
        answerContainer.getChildren().add(answerLabel);

        answerButtons.put(answerId, connectPoint);
        row.getChildren().addAll(connectPoint, answerContainer);
        return row;
    }

    private void startConnection(String imageId, Button imageButton) {
        // Clear previous selection
        resetAllButtonStyles();

        selectedImageId = imageId;

        // Visual feedback - highlight selected image point with smaller white circle
        // and blue effect
        imageButton.setGraphic(null); // Remove any image
        imageButton.setStyle(
                "-fx-background-color: white; -fx-background-radius: 11; -fx-border-width: 2; -fx-border-color: #516BB0; -fx-effect: dropshadow(gaussian, #516BB0, 6, 0, 0, 0);"); // Updated
        // for
        // smaller
        // size

        // If there was a previous connection from this image, show the line as blue
        // (being drawn)
        String connectedAnswerId = connections.get(imageId);
        if (connectedAnswerId != null) {
            Button answerButton = answerButtons.get(connectedAnswerId);
            answerButton.setGraphic(null); // Remove any image
            answerButton.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 11; -fx-border-width: 2; -fx-border-color: #516BB0; -fx-effect: dropshadow(gaussian, #516BB0, 6, 0, 0, 0);"); // Updated
            // for
            // smaller
            // size

            // Change existing line to blue (being dragged)
            Line existingLine = connectionLines.get(imageId);
            if (existingLine != null) {
                existingLine.setStroke(Color.web("#516BB0")); // Blue when being dragged
            }
        }
    }

    private void completeConnection(String answerId, Button answerButton) {
        if (selectedImageId != null) {
            // Remove old connection if exists
            String oldAnswer = connections.get(selectedImageId);
            if (oldAnswer != null) {
                Line oldLine = connectionLines.get(selectedImageId);
                if (oldLine != null) {
                    lineContainer.getChildren().remove(oldLine);
                }
            }

            // Create new connection
            Button imageButton = imageButtons.get(selectedImageId);
            Line line = createConnectionLine(imageButton, answerButton);

            // Check if answer is correct based on image filename
            boolean isCorrect = false;

            // Get the image path for the selected image
            int imageIndex = imageIds.indexOf(selectedImageId);
            if (imageIndex >= 0 && imageIndex < imagePaths.size()) {
                String imagePath = imagePaths.get(imageIndex);
                String correctAnswer = extractAnswerFromImagePath(imagePath);

                // Get the answer text that user is connecting to
                int answerIndex = imageIds.indexOf(answerId);
                if (answerIndex >= 0 && answerIndex < answerTexts.size()) {
                    String userAnswerText = answerTexts.get(answerIndex);

                    // Check if user's answer matches the correct answer (case insensitive)
                    isCorrect = correctAnswer.equalsIgnoreCase(userAnswerText);
                }
            }

            // Set line color based on correctness and play sound effect
            if (isCorrect) {
                line.setStroke(Color.GREEN); // Hijau jika benar
                playCorrectSound(); // Putar sound effect benar
            } else {
                line.setStroke(Color.RED); // Merah jika salah
                playWrongSound(); // Putar sound effect salah
            }

            lineContainer.getChildren().add(line);
            connections.put(selectedImageId, answerId);
            connectionLines.put(selectedImageId, line);

            // Notify match attempt
            if (onMatchAttempt != null) {
                onMatchAttempt.onMatchAttempt(isCorrect);
            }

            // Reset visual feedback
            resetAllButtonStyles();
            selectedImageId = null;

            // Check if all connections are made
            checkAllConnectionsComplete();
        }
    }

    private Line createConnectionLine(Button startButton, Button endButton) {
        Line line = new Line();

        // Delay yang lebih lama untuk memastikan layout benar-benar selesai
        javafx.animation.PauseTransition layoutDelay = new javafx.animation.PauseTransition(
                javafx.util.Duration.millis(100)); // 100ms delay untuk layout

        layoutDelay.setOnFinished(e -> {
            // Use layout bounds in scene for more accurate positioning
            javafx.geometry.Bounds startBounds = startButton.localToScene(startButton.getBoundsInLocal());
            javafx.geometry.Bounds endBounds = endButton.localToScene(endButton.getBoundsInLocal());
            javafx.geometry.Bounds containerBounds = lineContainer.localToScene(lineContainer.getBoundsInLocal());

            // Calculate center points relative to line container dengan safety check
            double startX = startBounds.getCenterX() - containerBounds.getMinX();
            double startY = startBounds.getCenterY() - containerBounds.getMinY();
            double endX = endBounds.getCenterX() - containerBounds.getMinX();
            double endY = endBounds.getCenterY() - containerBounds.getMinY();

            // Safety check untuk koordinat yang valid
            if (!Double.isNaN(startX) && !Double.isNaN(startY) &&
                    !Double.isNaN(endX) && !Double.isNaN(endY)) {

                line.setStartX(startX);
                line.setStartY(startY);
                line.setEndX(endX);
                line.setEndY(endY);

                // Force refresh container setelah set koordinat
                lineContainer.requestLayout();
                lineContainer.autosize();
            } else {
                // Fallback dengan koordinat relatif sederhana
                line.setStartX(50);
                line.setStartY(50);
                line.setEndX(650);
                line.setEndY(50);

                // Force refresh container
                lineContainer.requestLayout();
                lineContainer.autosize();
            }
        });

        layoutDelay.play();

        line.setStrokeWidth(4);
        line.setOpacity(0.9);

        return line;
    }

    private void checkAllConnectionsComplete() {
        // Check if all images have connections
        if (connections.size() == imageIds.size()) {
            showLoadingOverlay();

            javafx.animation.PauseTransition showResultsDelay = new javafx.animation.PauseTransition(
                    javafx.util.Duration.seconds(0.75));

            showResultsDelay.setOnFinished(e -> {
                hideLoadingOverlay();

                if (onNextRound != null) {
                    onNextRound.onNextRound();
                } else {
                    checkAnswers();
                }
            });

            showResultsDelay.play();
        }
    }

    /**
     * Show loading overlay with animated GIF to prevent user interaction
     */
    private void showLoadingOverlay() {
        try {
            // Create loading overlay
            StackPane loadingOverlay = new StackPane();
            loadingOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
            loadingOverlay.setPrefSize(stage.getWidth(), stage.getHeight());

            // Create loading GIF
            ImageView loadingGif = new ImageView();
            try {
                Image loadingImage = new Image(getClass().getResourceAsStream("/images/loading.gif"));
                loadingGif.setImage(loadingImage);
                loadingGif.setFitWidth(100);
                loadingGif.setFitHeight(100);
                loadingGif.setPreserveRatio(true);
            } catch (Exception e) {
                // Fallback jika loading.gif tidak ditemukan
                Label loadingLabel = new Label("Loading...");
                loadingLabel.setStyle(
                        "-fx-font-family: 'Poppins', Arial; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
                loadingOverlay.getChildren().add(loadingLabel);
            }

            if (loadingGif.getImage() != null) {
                loadingOverlay.getChildren().add(loadingGif);
            }

            // Add loading overlay to main view structure
            StackPane finalStackPane = (StackPane) view.getCenter();
            finalStackPane.getChildren().add(loadingOverlay);

            // Disable all user interactions
            loadingOverlay.setMouseTransparent(false);

        } catch (Exception e) {
            System.err.println("Error showing loading overlay: " + e.getMessage());
        }
    }

    /**
     * Hide loading overlay
     */
    private void hideLoadingOverlay() {
        try {
            StackPane finalStackPane = (StackPane) view.getCenter();

            // Remove loading overlay (should be the last child)
            if (finalStackPane.getChildren().size() > 1) {
                finalStackPane.getChildren().remove(finalStackPane.getChildren().size() - 1);
            }

        } catch (Exception e) {
            System.err.println("Error hiding loading overlay: " + e.getMessage());
        }
    }

    private void resetAllButtonStyles() {
        // Reset image buttons to smaller white circles
        for (Button button : imageButtons.values()) {
            button.setGraphic(null); // Remove any image
            button.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 11; -fx-border-width: 2; -fx-border-color: #ccc;"); // Updated
            // for
            // smaller
            // size
        }

        // Reset answer buttons to smaller white circles
        for (Button button : answerButtons.values()) {
            button.setGraphic(null); // Remove any image
            button.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 11; -fx-border-width: 2; -fx-border-color: #ccc;"); // Updated
            // for
            // smaller
            // size
        }
    }

    private void checkAnswers() {
        int correctAnswers = 0;

        // Count correct answers based on image filename matching
        for (int i = 0; i < imageIds.size(); i++) {
            String imageId = imageIds.get(i);
            String userAnswerId = connections.get(imageId);

            if (userAnswerId != null) {
                // Get the image path for this imageId
                String imagePath = imagePaths.get(i);
                String correctAnswer = extractAnswerFromImagePath(imagePath);

                // Get the answer text that user connected to
                int userAnswerIndex = imageIds.indexOf(userAnswerId);
                if (userAnswerIndex >= 0 && userAnswerIndex < answerTexts.size()) {
                    String userAnswerText = answerTexts.get(userAnswerIndex);

                    // Check if user's answer matches the correct answer (case insensitive)
                    if (correctAnswer.equalsIgnoreCase(userAnswerText)) {
                        correctAnswers++;
                    }
                }
            }
        }

        // Trigger callback with results
        if (onGameComplete != null) {
            onGameComplete.onGameComplete(correctAnswers, imageIds.size());
        }
    }

    /**
     * Extract the answer from image file path by taking the last word after
     * underscore
     * Example: "badminton_netting.jpg" -> "netting"
     */
    private String extractAnswerFromImagePath(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return "";
        }

        // Get filename without path
        String filename = imagePath;
        if (imagePath.contains("/")) {
            filename = imagePath.substring(imagePath.lastIndexOf("/") + 1);
        }
        if (imagePath.contains("\\")) {
            filename = filename.substring(filename.lastIndexOf("\\") + 1);
        }

        // Remove file extension
        if (filename.contains(".")) {
            filename = filename.substring(0, filename.lastIndexOf("."));
        }

        // Get the last word after underscore
        if (filename.contains("_")) {
            String[] parts = filename.split("_");
            return parts[parts.length - 1].toLowerCase();
        }

        // If no underscore, return the whole filename
        return filename.toLowerCase();
    }

    // Getter methods
    public Parent getView() {
        return view;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }

    public void setOnGameComplete(OnGameCompleteListener listener) {
        this.onGameComplete = listener;
    }

    // Methods expected by MainPresenter
    public void setRound(int round, int totalRounds, List<String> imagePaths, List<String> answerTexts,
            List<String> imageIds) {
        // No longer use the same order - the new system will handle shuffled answers
        // correctly
        // because it extracts the correct answer from the image filename
        setupMatchingGame(imagePaths, answerTexts, imageIds, new ArrayList<>());
    }

    public void setOnMatchAttempt(OnMatchAttemptListener listener) {
        this.onMatchAttempt = listener;
    }

    public void setOnNextRound(OnNextRoundListener listener) {
        this.onNextRound = listener;
    }

    public void setOnNavigation(OnNavigationListener listener) {
        this.onNavigation = listener;
    }
}

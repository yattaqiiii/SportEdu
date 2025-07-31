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
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View untuk kuis mencocokkan gambar dengan navbar, scrolling, dan connection dots
 * Fix size 1280x832 dengan scroll jika konten lebih panjang
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

    private final BorderPane view;
    private Button kembaliButton;

    // Navbar buttons
    public Button navHomeButton;
    public Button navMateriButton;
    public Button navQuizButton;

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

    // Game completion callbacks
    private OnGameCompleteListener onGameComplete;
    private OnMatchAttemptListener onMatchAttempt;
    private OnNextRoundListener onNextRound;

    // Round data
    private int currentRound;
    private int totalRounds;

    public MatchingQuizFXView() {
        view = new BorderPane();
        view.setPrefSize(1280, 832);
        view.setMaxSize(1280, 832);

        // Initialize kembali button even though it won't be displayed
        kembaliButton = new Button("Kembali");
        kembaliButton.setVisible(false); // Hide it but keep it initialized

        // Tambahkan stylesheet
        try {
            view.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            view.getStyleClass().add("root");
        } catch (Exception e) {
            System.err.println("Could not load stylesheet: " + e.getMessage());
        }

        setupLayout();
    }

    private void setupLayout() {
        // Set background beige
        view.setStyle("-fx-background-color: #F5E6A3;");

        // Background dengan dot pattern
        try {
            ImageView dotBackground = new ImageView(new Image(getClass().getResourceAsStream("/images/dot background.png")));
            dotBackground.setFitWidth(1280);
            dotBackground.setFitHeight(832);
            dotBackground.setPreserveRatio(false);

            // Container utama
            BorderPane mainContainer = new BorderPane();
            mainContainer.setPrefSize(1280, 832);
            mainContainer.setStyle("-fx-background-color: transparent;");

            // Navbar di atas
            HBox navbar = createNavbar();
            mainContainer.setTop(navbar);

            // Content area dengan scroll
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setPrefSize(1280, 752); // 832 - 80 (navbar)
            scrollPane.setMaxSize(1280, 752);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
            scrollPane.setFitToWidth(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            // Content container
            VBox contentContainer = createContentContainer();
            scrollPane.setContent(contentContainer);

            mainContainer.setCenter(scrollPane);

            // Stack background dan main container
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(dotBackground, mainContainer);

            view.setCenter(stackPane);

        } catch (Exception e) {
            // Fallback background beige
            view.setStyle("-fx-background-color: #F5E6A3;");
            System.err.println("Gagal memuat dot background: " + e.getMessage());

            // Simple layout without background image
            BorderPane mainContainer = new BorderPane();
            HBox navbar = createNavbar();
            mainContainer.setTop(navbar);

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setPrefSize(1280, 752);
            scrollPane.setStyle("-fx-background-color: transparent;");
            scrollPane.setFitToWidth(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            VBox contentContainer = createContentContainer();
            scrollPane.setContent(contentContainer);
            mainContainer.setCenter(scrollPane);

            view.setCenter(mainContainer);
        }
    }

    private VBox createContentContainer() {
        VBox container = new VBox(40);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(40, 50, 40, 50));
        container.setStyle("-fx-background-color: transparent;");
        container.setMinHeight(800); // Minimum height untuk memastikan scroll berfungsi

        // Header dengan gambar "Cocokkan Gambar"
        VBox headerSection = new VBox(30);
        headerSection.setAlignment(Pos.CENTER);

        try {
            ImageView headerImage = new ImageView(new Image(getClass().getResourceAsStream("/images/Cocokkan Gambar.png")));
            headerImage.setFitHeight(80);
            headerImage.setPreserveRatio(true);
            headerSection.getChildren().add(headerImage);
        } catch (Exception e) {
            Label headerLabel = new Label("Mencocokkan Gambar");
            headerLabel.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #516BB0;");
            headerSection.getChildren().add(headerLabel);
        }

        // Instruksi dengan warna biru dan tebal
        Label instruksiLabel = new Label("Hubungkan gambar dengan jawaban yang tepat dengan menarik garis dari titik ke titik");
        instruksiLabel.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #516BB0; -fx-text-alignment: center;");
        instruksiLabel.setWrapText(true);
        instruksiLabel.setMaxWidth(600);
        headerSection.getChildren().add(instruksiLabel);

        container.getChildren().add(headerSection);
        return container;
    }

    /**
     * Setup matching game dengan data gambar dan jawaban
     */
    public void setupMatchingGame(List<String> imagePaths, List<String> answerTexts, List<String> imageIds, List<String> correctAnswerIds) {
        this.imagePaths = imagePaths;
        this.answerTexts = answerTexts;
        this.imageIds = imageIds;
        this.correctAnswerIds = correctAnswerIds;

        // Clear previous connections
        connections.clear();
        imageButtons.clear();
        answerButtons.clear();
        connectionLines.clear();

        // Get content container
        VBox contentContainer = (VBox) ((ScrollPane) ((BorderPane) ((StackPane) view.getCenter()).getChildren().get(1)).getCenter()).getContent();

        // Clear previous game content (keep header)
        if (contentContainer.getChildren().size() > 1) {
            contentContainer.getChildren().subList(1, contentContainer.getChildren().size()).clear();
        }

        // Game area dengan line container
        StackPane gameArea = new StackPane();
        gameArea.setPrefHeight(600);
        gameArea.setMaxHeight(600);

        // Line container untuk menggambar garis (di layer paling atas)
        lineContainer = new Pane();
        lineContainer.setPrefSize(1180, 600);
        lineContainer.setStyle("-fx-background-color: transparent;");
        lineContainer.setMouseTransparent(true); // Allow clicks to pass through

        // Main game layout
        HBox gameLayout = new HBox(150); // Space between columns
        gameLayout.setAlignment(Pos.CENTER);
        gameLayout.setPadding(new Insets(50));

        // Left side - Images
        VBox imageColumn = createImageColumn();

        // Right side - Answers
        VBox answerColumn = createAnswerColumn();

        gameLayout.getChildren().addAll(imageColumn, answerColumn);

        // Add game layout first, then line container on top
        gameArea.getChildren().addAll(gameLayout, lineContainer);

        contentContainer.getChildren().add(gameArea);

        // No buttons section - removed completely
    }

    private VBox createImageColumn() {
        VBox imageColumn = new VBox(40);
        imageColumn.setAlignment(Pos.CENTER_LEFT);
        imageColumn.setPrefWidth(450);

        // Remove subtitle - no "Gambar" label

        for (int i = 0; i < imagePaths.size(); i++) {
            HBox imageRow = createImageRow(imagePaths.get(i), imageIds.get(i), i);
            imageColumn.getChildren().add(imageRow);
        }

        return imageColumn;
    }

    private HBox createImageRow(String imagePath, String imageId, int index) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);

        // Image container with light beige background that matches the dot color scheme
        VBox imageContainer = new VBox();
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setStyle("-fx-background-color: #F5E6A3; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 4); -fx-padding: 8;");

        try {
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
            // Let image maintain its natural proportions
            imageView.setPreserveRatio(true);

            // Get image natural size and adapt container
            Image img = imageView.getImage();
            double imgWidth = img.getWidth();
            double imgHeight = img.getHeight();

            // Scale to fit within max bounds while preserving ratio
            double maxWidth = 180;
            double maxHeight = 110;
            double scaleX = maxWidth / imgWidth;
            double scaleY = maxHeight / imgHeight;
            double scale = Math.min(scaleX, scaleY);

            double finalWidth = imgWidth * scale;
            double finalHeight = imgHeight * scale;

            imageView.setFitWidth(finalWidth);
            imageView.setFitHeight(finalHeight);

            // Container adapts to actual image size plus padding (stroke effect)
            imageContainer.getChildren().add(imageView);
            imageContainer.setPrefSize(finalWidth + 16, finalHeight + 16); // 8px padding on each side

        } catch (Exception e) {
            Label placeholder = new Label("Gambar " + (index + 1));
            placeholder.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
            imageContainer.getChildren().add(placeholder);
            imageContainer.setPrefSize(196, 126); // Default size with padding
        }

        // Larger connection point (dot)
        Button connectPoint = new Button();
        connectPoint.setPrefSize(35, 35);
        connectPoint.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 0;");

        try {
            ImageView dotIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/Cocokkan gambar titik.png")));
            dotIcon.setFitWidth(30);
            dotIcon.setFitHeight(30);
            dotIcon.setPreserveRatio(true);
            connectPoint.setGraphic(dotIcon);
        } catch (Exception e) {
            // Fallback: larger colored circle
            connectPoint.setStyle("-fx-background-color: #516BB0; -fx-background-radius: 17; -fx-border-width: 0;");
        }

        connectPoint.setOnAction(e -> startConnection(imageId, connectPoint));

        imageButtons.put(imageId, connectPoint);
        row.getChildren().addAll(imageContainer, connectPoint);
        return row;
    }

    private VBox createAnswerColumn() {
        VBox answerColumn = new VBox(40);
        answerColumn.setAlignment(Pos.CENTER_RIGHT);
        answerColumn.setPrefWidth(450);

        // Remove subtitle - no "Jawaban" label

        for (int i = 0; i < answerTexts.size(); i++) {
            HBox answerRow = createAnswerRow(answerTexts.get(i), imageIds.get(i));
            answerColumn.getChildren().add(answerRow);
        }

        return answerColumn;
    }

    private HBox createAnswerRow(String answerText, String answerId) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_RIGHT);

        // Larger connection point (dot)
        Button connectPoint = new Button();
        connectPoint.setPrefSize(35, 35);
        connectPoint.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 0;");

        try {
            ImageView dotIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/Cocokkan gambar titik.png")));
            dotIcon.setFitWidth(30);
            dotIcon.setFitHeight(30);
            dotIcon.setPreserveRatio(true);
            connectPoint.setGraphic(dotIcon);
        } catch (Exception e) {
            // Fallback: larger colored circle
            connectPoint.setStyle("-fx-background-color: #F5E6A3; -fx-background-radius: 17; -fx-border-width: 0;");
        }

        connectPoint.setOnAction(e -> completeConnection(answerId, connectPoint));

        // Answer container with light beige background matching the dots
        VBox answerContainer = new VBox();
        answerContainer.setAlignment(Pos.CENTER);
        answerContainer.setPrefSize(200, 130);
        answerContainer.setStyle("-fx-background-color: #F5E6A3; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 4); -fx-padding: 10;");

        // Answer label with blue text, bolder and perfectly centered
        Label answerLabel = new Label(answerText);
        answerLabel.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 900; -fx-text-fill: #516BB0; -fx-text-alignment: center;");
        answerLabel.setWrapText(true);
        answerLabel.setMaxWidth(180);
        answerLabel.setAlignment(Pos.CENTER);
        VBox.setVgrow(answerLabel, Priority.ALWAYS);
        answerContainer.getChildren().add(answerLabel);

        answerButtons.put(answerId, connectPoint);
        row.getChildren().addAll(connectPoint, answerContainer);
        return row;
    }

    private void startConnection(String imageId, Button imageButton) {
        // Clear previous selection
        resetAllButtonStyles();

        selectedImageId = imageId;

        // Visual feedback - highlight selected image point
        try {
            ImageView selectedDot = new ImageView(new Image(getClass().getResourceAsStream("/images/Cocokkan gambar titik.png")));
            selectedDot.setFitWidth(20);
            selectedDot.setFitHeight(20);
            selectedDot.setPreserveRatio(true);
            selectedDot.setStyle("-fx-effect: dropshadow(gaussian, #2ecc71, 10, 0, 0, 0);");
            imageButton.setGraphic(selectedDot);
        } catch (Exception e) {
            imageButton.setStyle("-fx-background-color: #2ecc71; -fx-background-radius: 12; -fx-border-width: 0;");
        }

        // If there was a previous connection from this image, highlight the connected answer
        String connectedAnswerId = connections.get(imageId);
        if (connectedAnswerId != null) {
            Button answerButton = answerButtons.get(connectedAnswerId);
            try {
                ImageView connectedDot = new ImageView(new Image(getClass().getResourceAsStream("/images/Cocokkan gambar titik.png")));
                connectedDot.setFitWidth(20);
                connectedDot.setFitHeight(20);
                connectedDot.setPreserveRatio(true);
                connectedDot.setStyle("-fx-effect: dropshadow(gaussian, #e74c3c, 10, 0, 0, 0);");
                answerButton.setGraphic(connectedDot);
            } catch (Exception e) {
                answerButton.setStyle("-fx-background-color: #e74c3c; -fx-background-radius: 12; -fx-border-width: 0;");
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

            // All lines are blue regardless of correctness
            line.setStroke(Color.web("#516BB0")); // Blue for all connections

            lineContainer.getChildren().add(line);
            connections.put(selectedImageId, answerId);
            connectionLines.put(selectedImageId, line);

            // Check if answer is correct for scoring
            boolean isCorrect = false;
            for (int i = 0; i < imageIds.size(); i++) {
                if (imageIds.get(i).equals(selectedImageId) && correctAnswerIds.get(i).equals(answerId)) {
                    isCorrect = true;
                    break;
                }
            }

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

        // Use layout bounds in scene for more accurate positioning
        javafx.geometry.Bounds startBounds = startButton.localToScene(startButton.getBoundsInLocal());
        javafx.geometry.Bounds endBounds = endButton.localToScene(endButton.getBoundsInLocal());
        javafx.geometry.Bounds containerBounds = lineContainer.localToScene(lineContainer.getBoundsInLocal());

        // Calculate center points relative to line container
        double startX = startBounds.getCenterX() - containerBounds.getMinX();
        double startY = startBounds.getCenterY() - containerBounds.getMinY();
        double endX = endBounds.getCenterX() - containerBounds.getMinX();
        double endY = endBounds.getCenterY() - containerBounds.getMinY();

        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);

        line.setStrokeWidth(4);
        line.setOpacity(0.9);

        return line;
    }

    private void checkAllConnectionsComplete() {
        // Check if all images have connections
        if (connections.size() == imageIds.size()) {
            // All connections made - always show results first
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(1000); // Brief pause to show all lines
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Always show results when all connections are complete
                checkAnswers();
            });
        }
    }

    private void resetAllButtonStyles() {
        // Reset image buttons
        for (Button button : imageButtons.values()) {
            try {
                ImageView normalDot = new ImageView(new Image(getClass().getResourceAsStream("/images/Cocokkan gambar titik.png")));
                normalDot.setFitWidth(30);
                normalDot.setFitHeight(30);
                normalDot.setPreserveRatio(true);
                button.setGraphic(normalDot);
            } catch (Exception e) {
                button.setStyle("-fx-background-color: #516BB0; -fx-background-radius: 17; -fx-border-width: 0;");
            }
        }

        // Reset answer buttons
        for (Button button : answerButtons.values()) {
            try {
                ImageView normalDot = new ImageView(new Image(getClass().getResourceAsStream("/images/Cocokkan gambar titik.png")));
                normalDot.setFitWidth(30);
                normalDot.setFitHeight(30);
                normalDot.setPreserveRatio(true);
                button.setGraphic(normalDot);
            } catch (Exception e) {
                button.setStyle("-fx-background-color: #F5E6A3; -fx-background-radius: 17; -fx-border-width: 0;");
            }
        }
    }

    private void checkAnswers() {
        // Calculate score
        int correctCount = 0;
        for (int i = 0; i < imageIds.size(); i++) {
            String imageId = imageIds.get(i);
            String userAnswer = connections.get(imageId);
            String correctAnswer = correctAnswerIds.get(i);

            if (userAnswer != null && userAnswer.equals(correctAnswer)) {
                correctCount++;
            }
        }

        // Show compact result display
        showCompactResults(correctCount, imageIds.size());
    }

    /**
     * Display compact results following guess image quiz concept
     */
    public void showCompactResults(int correctAnswers, int totalQuestions) {
        // Create semi-transparent overlay
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        overlay.setPrefSize(1280, 832);

        // Very compact result container matching the reference design
        VBox resultContainer = new VBox(15);
        resultContainer.setAlignment(Pos.CENTER);
        resultContainer.setPrefSize(300, 240);  // Sedikit diperbesar untuk tombol yang lebih besar
        resultContainer.setMaxSize(300, 240);
        resultContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2); -fx-padding: 20;");

        // Skor Akhir image header - ukuran tetap kecil
        try {
            ImageView scoreImage = new ImageView(new Image(getClass().getResourceAsStream("/images/Skor Akhir.png")));
            scoreImage.setFitHeight(35);
            scoreImage.setPreserveRatio(true);
            resultContainer.getChildren().add(scoreImage);
        } catch (Exception e) {
            Label scoreLabel = new Label("Skor Akhir");
            scoreLabel.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #516BB0;");
            resultContainer.getChildren().add(scoreLabel);
        }

        // Score display
        Label scoreText = new Label(correctAnswers + "/" + totalQuestions);
        scoreText.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2C5F2D;");
        resultContainer.getChildren().add(scoreText);

        // Percentage
        int percentage = (int) Math.round((double) correctAnswers / totalQuestions * 100);
        Label percentageLabel = new Label(percentage + "%");
        percentageLabel.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2C5F2D;");
        resultContainer.getChildren().add(percentageLabel);

        // Single yellow back button using Kembali_kuning.png - ukuran diperbesar lagi
        Button backButton = new Button();
        backButton.setPrefSize(180, 60);  // Diperbesar lagi dari 140x50 menjadi 180x60
        backButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 0; -fx-cursor: hand;");

        try {
            ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/Kembali_kuning.png")));
            backIcon.setFitWidth(180);  // Sesuaikan dengan ukuran button
            backIcon.setFitHeight(60);
            backIcon.setPreserveRatio(true);
            backButton.setGraphic(backIcon);
        } catch (Exception e) {
            // Fallback button with yellow styling
            backButton.setText("Kembali");
            backButton.setStyle("-fx-background-color: #F5E6A3; -fx-background-radius: 25; -fx-text-fill: #516BB0; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;");
        }

        // Perbaiki action handler untuk memastikan tombol bekerja
        backButton.setOnAction(e -> {
            System.out.println("Back button clicked!"); // Debug log

            // Remove overlay first
            StackPane mainStack = (StackPane) view.getCenter();
            mainStack.getChildren().remove(overlay);

            // Then call the completion handler
            if (onGameComplete != null) {
                onGameComplete.onGameComplete(correctAnswers, totalQuestions);
            } else {
                System.out.println("onGameComplete is null!"); // Debug log
            }
        });

        // Tambahkan hover effect untuk memastikan tombol responsif
        backButton.setOnMouseEntered(e -> {
            backButton.setStyle(backButton.getStyle() + "; -fx-opacity: 0.8;");
        });

        backButton.setOnMouseExited(e -> {
            backButton.setStyle(backButton.getStyle().replace("; -fx-opacity: 0.8;", ""));
        });

        resultContainer.getChildren().add(backButton);

        overlay.getChildren().add(resultContainer);

        // Add overlay to main view
        StackPane mainStack = (StackPane) view.getCenter();
        mainStack.getChildren().add(overlay);
    }

    /**
     * Membuat navbar dengan logo dan tombol navigasi
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
            Label logoText = new Label("SportEdu");
            logoText.setStyle("-fx-font-family: 'Poppins', Arial; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
            logoContainer.getChildren().add(logoText);
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
        navHomeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16; -fx-cursor: hand;");
        navHomeButton.setOnMouseEntered(e -> navHomeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16; -fx-cursor: hand;"));
        navHomeButton.setOnMouseExited(e -> navHomeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16; -fx-cursor: hand;"));
        // Perbaiki action handler untuk tombol Home dengan debug
        navHomeButton.setOnAction(e -> {
            System.out.println("🏠 NAVBAR HOME CLICKED in MatchingQuizFXView!");
            System.out.println("onGameComplete is null? " + (onGameComplete == null));
            if (onGameComplete != null) {
                System.out.println("Calling onGameComplete(0, 0) for Home navigation");
                onGameComplete.onGameComplete(0, 0); // Dummy values for navigation
            } else {
                System.out.println("❌ ERROR: onGameComplete is NULL! Cannot navigate to Home!");
            }
        });

        navMateriButton = new Button("Materi");
        navMateriButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16; -fx-cursor: hand;");
        navMateriButton.setOnMouseEntered(e -> navMateriButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16; -fx-cursor: hand;"));
        navMateriButton.setOnMouseExited(e -> navMateriButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16; -fx-cursor: hand;"));
        // Perbaiki action handler untuk tombol Materi dengan debug
        navMateriButton.setOnAction(e -> {
            System.out.println("📚 NAVBAR MATERI CLICKED in MatchingQuizFXView!");
            System.out.println("onGameComplete is null? " + (onGameComplete == null));
            if (onGameComplete != null) {
                System.out.println("Calling onGameComplete(-1, -1) for Materi navigation");
                onGameComplete.onGameComplete(-1, -1); // Special values to indicate Materi navigation
            } else {
                System.out.println("❌ ERROR: onGameComplete is NULL! Cannot navigate to Materi!");
            }
        });

        navQuizButton = new Button("Quiz");
        navQuizButton.setStyle("-fx-background-color: rgba(245, 230, 163, 0.2); -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 600; -fx-border-width: 0; -fx-padding: 8 16; -fx-background-radius: 5; -fx-cursor: hand;");
        // Quiz button is current page, so no action needed but add hover for consistency
        navQuizButton.setOnMouseEntered(e -> navQuizButton.setStyle("-fx-background-color: rgba(245, 230, 163, 0.3); -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 600; -fx-border-width: 0; -fx-padding: 8 16; -fx-background-radius: 5; -fx-cursor: hand;"));
        navQuizButton.setOnMouseExited(e -> navQuizButton.setStyle("-fx-background-color: rgba(245, 230, 163, 0.2); -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 600; -fx-border-width: 0; -fx-padding: 8 16; -fx-background-radius: 5; -fx-cursor: hand;"));
        // Quiz button action with debug
        navQuizButton.setOnAction(e -> {
            System.out.println("🎮 NAVBAR QUIZ CLICKED in MatchingQuizFXView!");
            System.out.println("Already on Quiz page - no navigation needed");
        });

        navButtons.getChildren().addAll(navHomeButton, navMateriButton, navQuizButton);
        navbar.getChildren().add(navButtons);

        return navbar;
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
    public void setRound(int round, int totalRounds, List<String> imagePaths, List<String> answerTexts, List<String> imageIds) {
        this.currentRound = round;
        this.totalRounds = totalRounds;

        // Use the first 4 images as correct answers (same order)
        List<String> correctIds = new ArrayList<>(imageIds);

        setupMatchingGame(imagePaths, answerTexts, imageIds, correctIds);
    }

    public void setOnMatchAttempt(OnMatchAttemptListener listener) {
        this.onMatchAttempt = listener;
    }

    public void setOnNextRound(OnNextRoundListener listener) {
        this.onNextRound = listener;
    }
}

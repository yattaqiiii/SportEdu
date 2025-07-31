package com.sportedu.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * View untuk kuis mencocokkan gambar sesuai desain "2.2 Mencocokkan Gambar.png"
 */
public class MatchingQuizFXView {

    private final BorderPane view;
    private final Label titleLabel;
    private final Label progressLabel;
    private final Label instructionLabel;
    private final GridPane gameGrid;
    private final Button kembaliButton;
    private final Button nextButton;

    private final List<Button> imageButtons = new ArrayList<>();
    private final List<Button> nameButtons = new ArrayList<>();

    private Button selectedImageButton = null;
    private Button selectedNameButton = null;
    private int correctMatches = 0;
    private Consumer<Boolean> onMatchAttempt;
    private Runnable onNextRound;

    public MatchingQuizFXView() {
        view = new BorderPane();
        view.getStyleClass().add("landing-background");
        view.setPadding(new Insets(30));

        // Header dengan progress
        VBox headerBox = new VBox(15);
        headerBox.setAlignment(Pos.CENTER);

        titleLabel = new Label("Mencocokkan Gambar");
        titleLabel.getStyleClass().add("page-title");

        progressLabel = new Label("Set 1 / 3");
        progressLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #516BB0; -fx-font-weight: 600;");

        instructionLabel = new Label("Cocokkan gambar dengan nama yang sesuai!");
        instructionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1A1E2C; -fx-font-weight: 500;");

        headerBox.getChildren().addAll(titleLabel, progressLabel, instructionLabel);

        // Container utama dengan background putih
        VBox contentContainer = new VBox(30);
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.setPadding(new Insets(40));
        contentContainer.getStyleClass().add("content-background");

        // Grid untuk game area
        gameGrid = new GridPane();
        gameGrid.setAlignment(Pos.CENTER);
        gameGrid.setHgap(50);
        gameGrid.setVgap(20);

        // Label untuk kolom
        Label imageColumnLabel = new Label("GAMBAR");
        imageColumnLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #516BB0;");

        Label nameColumnLabel = new Label("NAMA");
        nameColumnLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #516BB0;");

        gameGrid.add(imageColumnLabel, 0, 0);
        gameGrid.add(nameColumnLabel, 1, 0);

        // Initialize buttons untuk 4 pasangan
        for (int i = 0; i < 4; i++) {
            // Image buttons (kolom kiri)
            Button imgButton = new Button();
            imgButton.setPrefSize(180, 140);
            imgButton.getStyleClass().add("technique-card");
            imgButton.setStyle("-fx-background-color: white; -fx-border-color: #FFBC4C; -fx-border-width: 3; -fx-border-radius: 10;");
            imageButtons.add(imgButton);
            gameGrid.add(imgButton, 0, i + 1);

            // Name buttons (kolom kanan)
            Button nameButton = new Button();
            nameButton.setPrefSize(250, 60);
            nameButton.getStyleClass().add("quiz-option-button");
            nameButton.setStyle("-fx-background-color: #FEFFC4; -fx-border-color: #FFBC4C; -fx-border-width: 2; -fx-border-radius: 10; -fx-font-size: 16px; -fx-font-weight: 600;");
            nameButtons.add(nameButton);
            gameGrid.add(nameButton, 1, i + 1);
        }

        contentContainer.getChildren().add(gameGrid);

        // Control buttons
        HBox controlBox = new HBox(30);
        controlBox.setAlignment(Pos.CENTER);

        kembaliButton = UIFactory.createNavButton("/images/Back.png", 120, 50);
        nextButton = UIFactory.createPrimaryActionButton("Lanjut");
        nextButton.setDisable(true);
        nextButton.setPrefWidth(150);

        controlBox.getChildren().addAll(kembaliButton, nextButton);

        view.setTop(headerBox);
        view.setCenter(contentContainer);
        view.setBottom(controlBox);

        BorderPane.setMargin(headerBox, new Insets(0, 0, 20, 0));
        BorderPane.setMargin(controlBox, new Insets(20, 0, 0, 0));

        setupButtonEvents();
    }

    private void setupButtonEvents() {
        // Setup click events for image buttons
        for (int i = 0; i < imageButtons.size(); i++) {
            final int index = i;
            imageButtons.get(i).setOnAction(e -> selectImageButton(imageButtons.get(index)));
        }

        // Setup click events for name buttons
        for (int i = 0; i < nameButtons.size(); i++) {
            final int index = i;
            nameButtons.get(i).setOnAction(e -> selectNameButton(nameButtons.get(index)));
        }

        nextButton.setOnAction(e -> {
            if (onNextRound != null) {
                onNextRound.run();
            }
        });
    }

    private void selectImageButton(Button button) {
        if (!button.isDisabled()) {
            clearImageSelections();
            selectedImageButton = button;
            button.setStyle(button.getStyle() + "; -fx-border-color: #516BB0; -fx-border-width: 4;");
            checkMatch();
        }
    }

    private void selectNameButton(Button button) {
        if (!button.isDisabled()) {
            clearNameSelections();
            selectedNameButton = button;
            button.setStyle(button.getStyle() + "; -fx-border-color: #516BB0; -fx-border-width: 4;");
            checkMatch();
        }
    }

    private void clearImageSelections() {
        for (Button btn : imageButtons) {
            if (!btn.isDisabled()) {
                btn.setStyle("-fx-background-color: white; -fx-border-color: #FFBC4C; -fx-border-width: 3; -fx-border-radius: 10;");
            }
        }
        selectedImageButton = null;
    }

    private void clearNameSelections() {
        for (Button btn : nameButtons) {
            if (!btn.isDisabled()) {
                btn.setStyle("-fx-background-color: #FEFFC4; -fx-border-color: #FFBC4C; -fx-border-width: 2; -fx-border-radius: 10; -fx-font-size: 16px; -fx-font-weight: 600;");
            }
        }
        selectedNameButton = null;
    }

    private void checkMatch() {
        if (selectedImageButton != null && selectedNameButton != null) {
            String imageId = selectedImageButton.getId();
            String nameId = selectedNameButton.getId();

            if (imageId != null && imageId.equals(nameId)) {
                // Correct match
                markCorrect(selectedImageButton, selectedNameButton);
                correctMatches++;

                if (onMatchAttempt != null) {
                    onMatchAttempt.accept(true);
                }

                if (correctMatches >= 4) {
                    nextButton.setDisable(false);
                }
            } else {
                // Incorrect match
                markIncorrect(selectedImageButton, selectedNameButton);

                if (onMatchAttempt != null) {
                    onMatchAttempt.accept(false);
                }
            }

            selectedImageButton = null;
            selectedNameButton = null;
        }
    }

    private void markCorrect(Button imageBtn, Button nameBtn) {
        imageBtn.setDisable(true);
        nameBtn.setDisable(true);
        imageBtn.setStyle("-fx-background-color: #2ecc71; -fx-border-color: #27ae60; -fx-border-width: 4; -fx-border-radius: 10;");
        nameBtn.setStyle("-fx-background-color: #2ecc71; -fx-border-color: #27ae60; -fx-border-width: 4; -fx-border-radius: 10; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
    }

    private void markIncorrect(Button imageBtn, Button nameBtn) {
        imageBtn.setStyle("-fx-background-color: #e74c3c; -fx-border-color: #c0392b; -fx-border-width: 4; -fx-border-radius: 10;");
        nameBtn.setStyle("-fx-background-color: #e74c3c; -fx-border-color: #c0392b; -fx-border-width: 4; -fx-border-radius: 10; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");

        // Reset after 1.5 seconds
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
        pause.setOnFinished(e -> {
            imageBtn.setStyle("-fx-background-color: white; -fx-border-color: #FFBC4C; -fx-border-width: 3; -fx-border-radius: 10;");
            nameBtn.setStyle("-fx-background-color: #FEFFC4; -fx-border-color: #FFBC4C; -fx-border-width: 2; -fx-border-radius: 10; -fx-font-size: 16px; -fx-font-weight: 600; -fx-text-fill: black;");
        });
        pause.play();
    }

    public void setRound(int round, int totalRounds, List<String> imagePaths, List<String> names, List<String> ids) {
        progressLabel.setText("Set " + round + " / " + totalRounds);
        resetButtons();

        for (int i = 0; i < 4; i++) {
            // Set images
            try {
                Image image = new Image(getClass().getResourceAsStream(imagePaths.get(i)));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(160);
                imageView.setFitHeight(120);
                imageView.setPreserveRatio(true);
                imageButtons.get(i).setGraphic(imageView);
                imageButtons.get(i).setText("");
                imageButtons.get(i).setId(ids.get(i));
            } catch (Exception e) {
                imageButtons.get(i).setText("Gambar\nTidak Ada");
                imageButtons.get(i).setGraphic(null);
                imageButtons.get(i).setId(ids.get(i));
            }

            // Set names
            nameButtons.get(i).setText(names.get(i));
            nameButtons.get(i).setGraphic(null);
            nameButtons.get(i).setId(ids.get(i));
        }
    }

    private void resetButtons() {
        correctMatches = 0;
        nextButton.setDisable(true);

        for (int i = 0; i < 4; i++) {
            imageButtons.get(i).setDisable(false);
            nameButtons.get(i).setDisable(false);
            imageButtons.get(i).setStyle("-fx-background-color: white; -fx-border-color: #FFBC4C; -fx-border-width: 3; -fx-border-radius: 10;");
            nameButtons.get(i).setStyle("-fx-background-color: #FEFFC4; -fx-border-color: #FFBC4C; -fx-border-width: 2; -fx-border-radius: 10; -fx-font-size: 16px; -fx-font-weight: 600;");
        }

        selectedImageButton = null;
        selectedNameButton = null;
    }

    public void setOnMatchAttempt(Consumer<Boolean> onMatchAttempt) {
        this.onMatchAttempt = onMatchAttempt;
    }

    public void setOnNextRound(Runnable onNextRound) {
        this.onNextRound = onNextRound;
    }

    public Parent getView() {
        return view;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }
}


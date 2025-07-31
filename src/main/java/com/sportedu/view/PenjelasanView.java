package com.sportedu.view;

import com.sportedu.model.Teknik;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * View untuk menampilkan penjelasan detail tentang teknik olahraga
 * Sesuai dengan desain "3.1.1 Pengertian.png"
 */
public class PenjelasanView {

    private BorderPane view;
    private BorderPane headerPane; // Container untuk header (judul dan tombol back)
    private Button backToTeknikButton; // Tombol kembali ke pilih teknik
    private Button kembaliButton; // Tombol kembali ke animasi
    private Button nextButton; // Tombol next ke animasi
    private Button volumeButton;
    private Label titleLabel;
    private Label descriptionLabel;
    private ImageView teknikImageView;

    private boolean isSoundOn = false;

    private HBox createNavbar() {
        HBox navbar = new HBox();
        navbar.setPrefHeight(80);
        navbar.setPrefWidth(1280);
        navbar.setPadding(new Insets(15, 40, 15, 40));
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setStyle("-fx-background-color: #516BB0;");

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

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        navbar.getChildren().add(spacer);

        HBox navButtons = new HBox(30);
        navButtons.setAlignment(Pos.CENTER_RIGHT);

        Button navHomeButton = new Button("Halaman Utama");
        navHomeButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;");
        navHomeButton.setOnMouseEntered(e -> navHomeButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));
        navHomeButton.setOnMouseExited(e -> navHomeButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 500; -fx-border-width: 0; -fx-padding: 8 16;"));

        Button navMateriButton = new Button("Materi");
        navMateriButton.setStyle(
                "-fx-background-color: rgba(245, 230, 163, 0.2); -fx-text-fill: #F5E6A3; -fx-font-family: 'Poppins', Arial; -fx-font-size: 16px; -fx-font-weight: 600; -fx-border-width: 0; -fx-padding: 8 16; -fx-background-radius: 5;");

        Button navQuizButton = new Button("Quiz");
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

    public PenjelasanView() {
        view = new BorderPane();
        view.getStyleClass().add("explanation-container");
        view.setPrefSize(1280, 832);

        // Header dengan judul dan tombol kembali
        headerPane = new BorderPane();

        // Tombol back di kiri
        backToTeknikButton = UIFactory.createNavButton("/images/Back.png", 50, 50);
        backToTeknikButton.setId("backToTeknik");
        headerPane.setLeft(backToTeknikButton);

        // Title di tengah menggunakan ImageView
        titleLabel = new Label();
        ImageView titleImageView = new ImageView();
        titleImageView.setFitHeight(40);
        titleImageView.setPreserveRatio(true);
        BorderPane.setAlignment(titleImageView, Pos.CENTER);
        headerPane.setCenter(titleImageView);

        // Dummy node di kanan untuk balance
        HBox dummyRight = new HBox();
        dummyRight.setMinWidth(50);
        headerPane.setRight(dummyRight);

        // Konten utama dengan background putih
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20));
        contentBox.getStyleClass().add("content-background");
        contentBox.setMaxWidth(800); // Kurangi lebar maksimal
        contentBox.setMinHeight(500); // Tambah minimum height

        // Container untuk gambar
        teknikImageView = new ImageView();
        teknikImageView.setFitWidth(450);
        teknikImageView.setFitHeight(350);
        teknikImageView.setPreserveRatio(true);
        teknikImageView.getStyleClass().add("technique-image");

        // Container untuk deskripsi
        descriptionLabel = new Label();
        descriptionLabel.getStyleClass().add("explanation-text");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(600);
        descriptionLabel.setAlignment(Pos.CENTER);

        contentBox.getChildren().addAll(teknikImageView, descriptionLabel);

        // Tombol navigasi
        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);

        kembaliButton = UIFactory.createNavButton("/images/Back.png", 120, 50);
        nextButton = UIFactory.createNavButton("/images/Next.png", 120, 50);
        volumeButton = UIFactory.createNavButton("/images/volume off.png", 80, 80);
        volumeButton.getStyleClass().add("volume-button");
        volumeButton.setOnAction(e -> toggleSound());

        buttonBox.getChildren().addAll(kembaliButton, volumeButton, nextButton);

        try {
            // Background dengan dot pattern
            ImageView dotBackground = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/dot background.png")));
            dotBackground.setFitWidth(1280);
            dotBackground.setFitHeight(832);
            dotBackground.setPreserveRatio(false);

            // Overlay container - tanpa spacing
            VBox overlayContainer = new VBox(0);
            overlayContainer.setPrefSize(1280, 832);
            overlayContainer.setStyle("-fx-background-color: transparent;");

            // Navbar di atas
            HBox navbar = createNavbar();

            // Content area - hilangkan semua padding
            VBox contentArea = new VBox();
            contentArea.setAlignment(Pos.TOP_CENTER);
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);
            contentArea.setSpacing(20);

            // Tambahkan komponen ke content area dengan spacing yang tepat
            contentArea.getChildren().addAll(headerPane, contentBox, buttonBox);
            VBox.setMargin(contentBox, new Insets(20, 0, 20, 0));

            // Susun layout dengan overlay
            overlayContainer.getChildren().addAll(navbar, contentArea);

            // Stack background dan overlay - pastikan tidak ada spacing
            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
            stackPane.setAlignment(Pos.TOP_CENTER);
            stackPane.getChildren().addAll(dotBackground, overlayContainer);

            view.setCenter(stackPane);
        } catch (Exception e) {
            System.err.println("Error loading background: " + e.getMessage());
            // Fallback jika background tidak bisa dimuat
            VBox fallbackContainer = new VBox();
            fallbackContainer.setPrefSize(1280, 832);

            HBox navbar = createNavbar();

            VBox contentArea = new VBox(40);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(80, 100, 80, 100));
            contentArea.getChildren().addAll(headerPane, contentBox, buttonBox);

            fallbackContainer.getChildren().addAll(navbar, contentArea);
            view.setCenter(fallbackContainer);
        }
    }

    public void displayTeknik(Teknik teknik) {
        // Set title image berdasarkan nama teknik
        String titleImagePath = "/images/pengertian_" + teknik.getNama().toLowerCase() + ".png";
        try {
            Image titleImage = new Image(getClass().getResourceAsStream(titleImagePath));
            if (!titleImage.isError()) {
                ((ImageView) headerPane.getCenter()).setImage(titleImage);
            } else {
                System.err.println("Cannot load title image: " + titleImagePath);
                titleLabel.setText("Teknik " + teknik.getNama()); // fallback ke text
                headerPane.setCenter(titleLabel);
            }
        } catch (Exception e) {
            System.err.println("Error loading title image: " + e.getMessage());
            titleLabel.setText("Teknik " + teknik.getNama()); // fallback ke text
            headerPane.setCenter(titleLabel);
        }

        // Set description
        descriptionLabel.setText(teknik.getDeskripsi());

        // Set main image
        try {
            Image image = new Image(getClass().getResourceAsStream(teknik.getImagePath()));
            if (!image.isError()) {
                teknikImageView.setImage(image);
            } else {
                teknikImageView.setImage(null);
                System.err.println("Cannot load image: " + teknik.getImagePath());
            }
        } catch (Exception e) {
            System.err.println("Error loading image for " + teknik.getNama() + ": " + e.getMessage());
            teknikImageView.setImage(null);
        }
    }

    public Parent getView() {
        return view;
    }

    public Button getBackToTeknikButton() {
        return backToTeknikButton;
    }

    public Button getToAnimasiPrevButton() {
        return kembaliButton;
    }

    public Button getToAnimasiNextButton() {
        return nextButton;
    }

    public Button getVolumeButton() {
        return volumeButton;
    }

    public boolean isSoundOn() {
        return isSoundOn;
    }

    private void toggleSound() {
        isSoundOn = !isSoundOn;

        try {
            if (isSoundOn) {
                // Update tombol ke volume on
                Image volumeOnImage = new Image(getClass().getResourceAsStream("/images/volume on.png"));
                ImageView volumeOnView = new ImageView(volumeOnImage);
                volumeOnView.setFitWidth(50);
                volumeOnView.setFitHeight(50);
                volumeOnView.setPreserveRatio(true);
                volumeButton.setGraphic(volumeOnView);

                // TODO: Implementasi text-to-speech nanti
                System.out.println("Sound ON - Text-to-speech akan diimplementasikan nanti");
            } else {
                // Update tombol ke volume off
                Image volumeOffImage = new Image(getClass().getResourceAsStream("/images/volume off.png"));
                ImageView volumeOffView = new ImageView(volumeOffImage);
                volumeOffView.setFitWidth(50);
                volumeOffView.setFitHeight(50);
                volumeOffView.setPreserveRatio(true);
                volumeButton.setGraphic(volumeOffView);

                System.out.println("Sound OFF");
            }
        } catch (Exception e) {
            System.err.println("Error updating volume button: " + e.getMessage());
        }
    }
}

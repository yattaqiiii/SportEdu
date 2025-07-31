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
 * View untuk menampilkan animasi GIF teknik olahraga
 * Sesuai dengan desain "3.1.2 Animasi.png" dan "3.1.2 Animasi - volume on.png"
 */
public class AnimasiView {

    private final BorderPane view;
    private final Button kembaliButton;
    private final Button volumeButton;
    private final Label titleLabel;
    private final ImageView gifImageView;
    private final Label instructionLabel;

    private boolean isSoundOn = false;

    public AnimasiView() {
        view = new BorderPane();
        view.getStyleClass().add("animation-container");
        view.setPadding(new Insets(50));

        // Header dengan judul
        titleLabel = new Label();
        titleLabel.getStyleClass().add("page-title");
        titleLabel.setAlignment(Pos.CENTER);

        // Konten utama dengan background putih
        VBox contentBox = new VBox(30);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(50));
        contentBox.getStyleClass().add("content-background");

        // Container untuk GIF animasi
        gifImageView = new ImageView();
        gifImageView.setFitWidth(550);
        gifImageView.setFitHeight(450);
        gifImageView.setPreserveRatio(true);
        gifImageView.getStyleClass().add("technique-image");

        // Label instruksi
        instructionLabel = new Label("Perhatikan gerakan dalam animasi di atas");
        instructionLabel.getStyleClass().add("explanation-text");
        instructionLabel.setAlignment(Pos.CENTER);

        contentBox.getChildren().addAll(gifImageView, instructionLabel);

        // Tombol navigasi dan kontrol
        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);

        kembaliButton = UIFactory.createNavButton("/images/Back.png", 120, 50);
        volumeButton = UIFactory.createNavButton("/images/volume off.png", 80, 80);
        volumeButton.getStyleClass().add("volume-button");

        // Event handler untuk tombol volume
        volumeButton.setOnAction(e -> toggleSound());

        buttonBox.getChildren().addAll(kembaliButton, volumeButton);

        view.setTop(titleLabel);
        view.setCenter(contentBox);
        view.setBottom(buttonBox);

        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setAlignment(buttonBox, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 30, 0));
        BorderPane.setMargin(buttonBox, new Insets(30, 0, 0, 0));
    }

    public void displayAnimasi(Teknik teknik) {
        titleLabel.setText("Animasi " + teknik.getNama());

        try {
            // Load GIF animasi
            Image gifImage = new Image(getClass().getResourceAsStream(teknik.getGifPath()));
            if (!gifImage.isError()) {
                gifImageView.setImage(gifImage);
            } else {
                // Fallback jika GIF tidak ditemukan
                gifImageView.setImage(null);
                System.err.println("Cannot load GIF: " + teknik.getGifPath());
                instructionLabel.setText("Animasi tidak tersedia untuk teknik " + teknik.getNama());
            }
        } catch (Exception e) {
            System.err.println("Error loading GIF for " + teknik.getNama() + ": " + e.getMessage());
            gifImageView.setImage(null);
            instructionLabel.setText("Animasi tidak tersedia untuk teknik " + teknik.getNama());
        }
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

    public Parent getView() {
        return view;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }

    public Button getVolumeButton() {
        return volumeButton;
    }

    public boolean isSoundOn() {
        return isSoundOn;
    }
}

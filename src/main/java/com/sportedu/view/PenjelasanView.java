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

    private final BorderPane view;
    private final Button kembaliButton;
    private final Button nextButton;
    private final Label titleLabel;
    private final Label descriptionLabel;
    private final ImageView teknikImageView;

    public PenjelasanView() {
        view = new BorderPane();
        view.getStyleClass().add("explanation-container");
        view.setPadding(new Insets(50));

        // Header dengan judul
        titleLabel = new Label();
        titleLabel.getStyleClass().add("page-title");
        titleLabel.setAlignment(Pos.CENTER);

        // Konten utama dengan background putih
        VBox contentBox = new VBox(40);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(50));
        contentBox.getStyleClass().add("content-background");

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

        buttonBox.getChildren().addAll(kembaliButton, nextButton);

        view.setTop(titleLabel);
        view.setCenter(contentBox);
        view.setBottom(buttonBox);

        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setAlignment(buttonBox, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 30, 0));
        BorderPane.setMargin(buttonBox, new Insets(30, 0, 0, 0));
    }

    public void displayTeknik(Teknik teknik) {
        titleLabel.setText("Teknik " + teknik.getNama());
        descriptionLabel.setText(teknik.getDeskripsi());

        try {
            Image image = new Image(getClass().getResourceAsStream(teknik.getImagePath()));
            if (!image.isError()) {
                teknikImageView.setImage(image);
            } else {
                // Fallback jika gambar tidak ditemukan
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

    public Button getKembaliButton() {
        return kembaliButton;
    }

    public Button getNextButton() {
        return nextButton;
    }
}

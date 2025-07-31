package com.sportedu.view;

import com.sportedu.model.Soal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * View untuk menampilkan gameplay kuis (satu soal per layar).
 * File ini diperbaiki dengan menambahkan semua getter yang dibutuhkan.
 */
public class QuizView {

    private final VBox view;
    private final Button kembaliButton;
    public Button[] pilihanButtons;

    public QuizView() {
        view = new VBox(40);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(50));
        view.getStyleClass().add("landing-background");

        kembaliButton = UIFactory.createNavButton("/images/Back.png", 120, 50);
    }

    public void displaySoal(Soal soal) {
        view.getChildren().clear();

        // Judul quiz
        Label title = new Label("Tebak Gambar Ini!");
        title.getStyleClass().add("page-title");

        // Container untuk konten quiz dengan background putih
        VBox contentContainer = new VBox(30);
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.setPadding(new Insets(40));
        contentContainer.getStyleClass().add("content-background");

        // Gambar soal
        ImageView gambarSoal = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream(soal.getPertanyaan()));
            gambarSoal.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading quiz image: " + soal.getPertanyaan());
        }
        gambarSoal.setFitHeight(300);
        gambarSoal.setFitWidth(400);
        gambarSoal.setPreserveRatio(true);
        gambarSoal.getStyleClass().add("technique-image");

        // Grid untuk pilihan jawaban (2x2)
        GridPane pilihanJawabanPane = new GridPane();
        pilihanJawabanPane.setAlignment(Pos.CENTER);
        pilihanJawabanPane.setHgap(20);
        pilihanJawabanPane.setVgap(20);

        pilihanButtons = new Button[4];
        for (int i = 0; i < soal.getPilihanJawaban().length; i++) {
            pilihanButtons[i] = UIFactory.createQuizOptionButton(soal.getPilihanJawaban()[i], i);
            int row = i / 2;
            int col = i % 2;
            pilihanJawabanPane.add(pilihanButtons[i], col, row);
        }

        contentContainer.getChildren().addAll(gambarSoal, pilihanJawabanPane);
        view.getChildren().addAll(title, contentContainer, kembaliButton);
    }

    // --- GETTER METHODS (YANG SEBELUMNYA HILANG) ---

    public Parent getView() {
        return view;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }
}

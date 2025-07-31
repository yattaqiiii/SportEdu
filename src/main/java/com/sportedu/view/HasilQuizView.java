package com.sportedu.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

/**
 * View untuk menampilkan hasil quiz dengan pop-up
 * Sesuai dengan desain "2.1 Tebak Gambar - skor akhir.png"
 */
public class HasilQuizView {

    private final VBox view;
    private final Label titleLabel;
    private final Label skorLabel;
    private final Label pesanLabel;
    private final Button kembaliButton;
    private final Button mainLagiButton;

    public HasilQuizView() {
        view = new VBox(40);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(50));
        view.getStyleClass().add("result-modal");
        view.setPrefWidth(600);
        view.setPrefHeight(500);

        // Judul
        titleLabel = new Label("QUIZ BERHASIL!");
        titleLabel.getStyleClass().add("result-title");
        titleLabel.setAlignment(Pos.CENTER);

        // Skor
        skorLabel = new Label();
        skorLabel.getStyleClass().add("result-score");
        skorLabel.setAlignment(Pos.CENTER);

        // Pesan
        pesanLabel = new Label();
        pesanLabel.getStyleClass().add("result-message");
        pesanLabel.setWrapText(true);
        pesanLabel.setMaxWidth(500);
        pesanLabel.setAlignment(Pos.CENTER);

        // Tombol
        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);

        kembaliButton = UIFactory.createDangerActionButton("Kembali ke Menu");
        mainLagiButton = UIFactory.createPrimaryActionButton("Main Lagi");

        buttonBox.getChildren().addAll(kembaliButton, mainLagiButton);

        view.getChildren().addAll(titleLabel, skorLabel, pesanLabel, buttonBox);
    }

    public void displayHasil(int skor, int totalSoal) {
        skorLabel.setText(skor + " / " + totalSoal);

        // Menentukan pesan berdasarkan skor
        double persentase = (double) skor / totalSoal * 100;
        String pesan;

        if (persentase >= 90) {
            pesan = "Luar biasa! Kamu sangat memahami teknik olahraga!";
            titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #2ecc71;");
        } else if (persentase >= 70) {
            pesan = "Bagus! Kamu cukup memahami teknik olahraga!";
            titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #f39c12;");
        } else if (persentase >= 50) {
            pesan = "Cukup baik! Terus belajar untuk hasil yang lebih baik!";
            titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #e67e22;");
        } else {
            pesan = "Jangan menyerah! Coba pelajari materi lagi dan ulangi quiz!";
            titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");
        }

        pesanLabel.setText(pesan);
    }

    public Parent getView() {
        return view;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }

    public Button getMainLagiButton() {
        return mainLagiButton;
    }
}

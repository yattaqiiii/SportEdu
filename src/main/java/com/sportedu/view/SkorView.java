package com.sportedu.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * View untuk menampilkan halaman skor akhir setelah kuis selesai.
 * File ini dibutuhkan oleh MainPresenter.
 */
public class SkorView {

    private final VBox view;
    private final Button kembaliKeMenuButton;

    public SkorView(int skor, int totalSoal) {
        view = new VBox(25);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(20));

        Label title = new Label("Kuis Selesai!");
        title.getStyleClass().add("title-label");

        // Menghitung skor dalam format 0-100
        int skorPersen = (totalSoal > 0) ? (skor * 100 / totalSoal) : 0;
        Label skorLabel = new Label(String.valueOf(skorPersen));
        skorLabel.getStyleClass().add("score-label");

        Label detailLabel = new Label("Kamu berhasil menjawab " + skor + " dari " + totalSoal + " pertanyaan!");
        detailLabel.getStyleClass().add("final-score-text");

        kembaliKeMenuButton = UIFactory.createStyledButton("Kembali ke Menu Utama");

        view.getChildren().addAll(title, skorLabel, detailLabel, kembaliKeMenuButton);
    }

    public Parent getView() {
        return view;
    }

    public Button getKembaliKeMenuButton() {
        return kembaliKeMenuButton;
    }
}

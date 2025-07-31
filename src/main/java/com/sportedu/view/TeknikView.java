package com.sportedu.view;

import com.sportedu.model.Teknik;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

/**
 * View untuk menampilkan daftar teknik dari sebuah cabang olahraga.
 */
public class TeknikView {

    private final VBox view;
    private final Button kembaliButton;
    private Consumer<Teknik> onTeknikSelected;

    public TeknikView() {
        view = new VBox(30);
        view.setAlignment(Pos.TOP_CENTER);
        view.setPadding(new Insets(50));
        view.getStyleClass().add("landing-background");

        kembaliButton = UIFactory.createNavButton("/images/Back.png", 120, 50);
    }

    /**
     * Menampilkan daftar teknik ke dalam view.
     * @param teknikList Daftar objek Teknik yang akan ditampilkan.
     * @param olahraga Judul cabang olahraga.
     */
    public void displayTeknik(List<Teknik> teknikList, String olahraga) {
        view.getChildren().clear();

        // Judul halaman
        Label title = new Label("Teknik Dasar " + olahraga);
        title.getStyleClass().add("page-title");

        // Container untuk grid teknik dengan background putih
        VBox contentContainer = new VBox(30);
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.setPadding(new Insets(40));
        contentContainer.getStyleClass().add("content-background");

        // Grid untuk teknik (2x2)
        GridPane teknikGrid = new GridPane();
        teknikGrid.setAlignment(Pos.CENTER);
        teknikGrid.setHgap(30);
        teknikGrid.setVgap(30);

        // Debug logging
        System.out.println("Menampilkan " + teknikList.size() + " teknik untuk " + olahraga);

        for (int i = 0; i < teknikList.size() && i < 4; i++) {
            Teknik teknik = teknikList.get(i);

            VBox teknikContainer = new VBox(15);
            teknikContainer.setAlignment(Pos.CENTER);

            // Debug info
            System.out.println("- Teknik: " + teknik.getNama() + ", Path: " + teknik.getImagePath());

            // Gunakan UIFactory baru untuk kartu teknik
            Button teknikButton = UIFactory.createTechniqueCard(teknik.getImagePath(), teknik.getNama(), 200, 150);

            teknikButton.setOnAction(e -> {
                if (onTeknikSelected != null) {
                    onTeknikSelected.accept(teknik);
                }
            });

            // Label nama teknik
            Label teknikLabel = new Label(teknik.getNama());
            teknikLabel.getStyleClass().add("technique-label");
            teknikLabel.setWrapText(true);
            teknikLabel.setMaxWidth(200);
            teknikLabel.setAlignment(Pos.CENTER);

            teknikContainer.getChildren().addAll(teknikButton, teknikLabel);

            // Posisi dalam grid 2x2
            int row = i / 2;
            int col = i % 2;
            teknikGrid.add(teknikContainer, col, row);
        }

        // Jika tidak ada teknik
        if (teknikList.isEmpty()) {
            Label noDataLabel = new Label("Tidak ada data teknik untuk " + olahraga);
            noDataLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #e74c3c;");
            contentContainer.getChildren().add(noDataLabel);
        } else {
            contentContainer.getChildren().add(teknikGrid);
        }

        view.getChildren().addAll(title, contentContainer, kembaliButton);
    }

    public void setOnTeknikSelected(Consumer<Teknik> onTeknikSelected) {
        this.onTeknikSelected = onTeknikSelected;
    }

    public Parent getView() {
        return view;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }
}

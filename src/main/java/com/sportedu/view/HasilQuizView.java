package com.sportedu.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.Node;

/**
 * View untuk menampilkan hasil quiz sebagai OVERLAY
 * Menggunakan UI yang sama dengan HasilTebakGambarView
 */
public class HasilQuizView {

    private final BorderPane view;
    private Button mainLagiButton;
    private Button kembaliButton;

    public HasilQuizView() {
        view = new BorderPane();
        // Remove fixed size to make it responsive

        // Tambahkan stylesheet
        view.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        view.getStyleClass().add("root");

        setupOverlayLayout();
    }

    private void setupOverlayLayout() {
        // Container utama dengan background dot pattern - make responsive like MainView
        VBox mainContainer = new VBox();
        mainContainer.prefWidthProperty().bind(view.widthProperty());
        mainContainer.prefHeightProperty().bind(view.heightProperty());

        // Background dengan dot pattern dari vektor - same as MainView
        try {
            ImageView dotBackground = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/dot background.png")));
            // Make background responsive like MainView
            dotBackground.fitWidthProperty().bind(mainContainer.widthProperty());
            dotBackground.fitHeightProperty().bind(mainContainer.heightProperty());
            dotBackground.setPreserveRatio(false);

            // Content area untuk overlay (tanpa navbar) - centered like MainView
            VBox contentArea = new VBox();
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setSpacing(20);
            contentArea.setPadding(new Insets(40, 50, 40, 50));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Container untuk memusatkan modal
            StackPane centerContainer = new StackPane();
            centerContainer.setAlignment(Pos.CENTER);

            // Placeholder untuk modal yang akan ditambahkan di displayHasil
            centerContainer.setId("result-container");

            contentArea.getChildren().add(centerContainer);

            // Stack background dan content seperti MainView
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(dotBackground, contentArea);

            // Create ScrollPane like MainView for responsiveness
            javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(stackPane);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPannable(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

            mainContainer.getChildren().add(scrollPane);
            view.setCenter(mainContainer);

        } catch (Exception e) {
            // Fallback responsive layout like MainView
            System.err.println("Background dot tidak ditemukan, menggunakan background warna solid: " + e.getMessage());

            VBox mainContainer2 = new VBox();
            mainContainer2.prefWidthProperty().bind(view.widthProperty());
            mainContainer2.prefHeightProperty().bind(view.heightProperty());
            mainContainer2.setStyle("-fx-background-color: #F5E6A3;");

            VBox contentArea = new VBox();
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setSpacing(20);
            contentArea.setPadding(new Insets(40, 50, 40, 50));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            StackPane centerContainer = new StackPane();
            centerContainer.setAlignment(Pos.CENTER);
            centerContainer.setId("result-container");

            contentArea.getChildren().add(centerContainer);

            // Create ScrollPane for fallback too
            javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(contentArea);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPannable(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

            mainContainer2.getChildren().add(scrollPane);
            view.setCenter(mainContainer2);
        }
    }

    public void displayHasil(int skor, int totalSoal) {
        // Cari result container menggunakan pattern yang sama seperti
        // HasilTebakGambarView
        StackPane resultContainer = null;

        // Traverse dari view untuk mencari result-container
        if (view.getCenter() instanceof VBox) {
            VBox mainContainer = (VBox) view.getCenter();
            for (Node child : mainContainer.getChildren()) {
                if (child instanceof javafx.scene.control.ScrollPane) {
                    javafx.scene.control.ScrollPane scrollPane = (javafx.scene.control.ScrollPane) child;
                    if (scrollPane.getContent() instanceof StackPane) {
                        StackPane stackPane = (StackPane) scrollPane.getContent();
                        for (Node stackChild : stackPane.getChildren()) {
                            if (stackChild instanceof VBox) {
                                VBox contentArea = (VBox) stackChild;
                                for (Node contentChild : contentArea.getChildren()) {
                                    if (contentChild instanceof StackPane &&
                                            "result-container".equals(contentChild.getId())) {
                                        resultContainer = (StackPane) contentChild;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (resultContainer == null) {
            System.err.println("Result container tidak ditemukan!");
            return;
        }

        // Bersihkan content yang ada
        resultContainer.getChildren().clear();

        // Modal kecil di tengah - smaller and responsive like MainView
        VBox modal = new VBox(15);
        modal.setAlignment(Pos.CENTER);
        modal.setPadding(new Insets(20));
        modal.getStyleClass().add("result-modal");
        modal.setMaxWidth(320);
        modal.setPrefWidth(320);

        // Background untuk modal - putih bersih tanpa stroke dan shadow
        Rectangle modalBackground = new Rectangle(320, 380);
        modalBackground.setFill(Color.WHITE);
        modalBackground.setArcWidth(20);
        modalBackground.setArcHeight(20);

        // Header dengan gambar "Skor Akhir" - smaller size
        try {
            ImageView skorAkhirImage = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/Skor Akhir.png")));
            skorAkhirImage.setFitHeight(25);
            skorAkhirImage.setPreserveRatio(true);
            modal.getChildren().add(skorAkhirImage);
        } catch (Exception e) {
            Label titleLabel = new Label("SKOR AKHIR");
            titleLabel.getStyleClass().add("result-title");
            titleLabel.setAlignment(Pos.CENTER);
            modal.getChildren().add(titleLabel);
        }

        // Skor besar
        Label skorLabel = new Label(skor + " / " + totalSoal);
        skorLabel.getStyleClass().add("result-score");
        skorLabel.setAlignment(Pos.CENTER);

        // Keterangan
        Label keteranganLabel = new Label("Jawaban Benar");
        keteranganLabel.getStyleClass().add("result-message");
        keteranganLabel.setAlignment(Pos.CENTER);

        // Pesan motivasi berdasarkan persentase
        double persentase = (double) skor / totalSoal * 100;
        String pesan;

        if (persentase >= 90) {
            pesan = "🎉 Luar biasa! Kamu sangat memahami teknik olahraga!";
        } else if (persentase >= 70) {
            pesan = "👏 Bagus! Kamu cukup memahami teknik olahraga!";
        } else if (persentase >= 50) {
            pesan = "💪 Cukup baik! Terus belajar untuk hasil yang lebih baik!";
        } else {
            pesan = "📚 Jangan menyerah! Coba pelajari materi lagi dan ulangi quiz!";
        }

        Label pesanLabel = new Label(pesan);
        pesanLabel.getStyleClass().add("result-message");
        pesanLabel.setWrapText(true);
        pesanLabel.setMaxWidth(280);
        pesanLabel.setAlignment(Pos.CENTER);

        // Tombol kembali dengan gambar - smaller size
        Button kembaliKeMenuButton;
        try {
            ImageView kembaliIcon = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/Kembali_kuning.png")));
            kembaliIcon.setFitHeight(50);
            kembaliIcon.setPreserveRatio(true);

            kembaliKeMenuButton = new Button();
            kembaliKeMenuButton.setGraphic(kembaliIcon);
            kembaliKeMenuButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 8;");

            final Button finalButton = kembaliKeMenuButton;
            kembaliKeMenuButton.setOnMouseEntered(e -> finalButton.setStyle(
                    "-fx-background-color: rgba(245,230,163,0.2); -fx-border-width: 0; -fx-padding: 8; -fx-background-radius: 10;"));
            kembaliKeMenuButton.setOnMouseExited(e -> finalButton
                    .setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 8;"));
        } catch (Exception e) {
            kembaliKeMenuButton = UIFactory.createSecondaryActionButton("Kembali ke Menu");
        }

        // Set buttons untuk kompatibilitas
        mainLagiButton = kembaliKeMenuButton;
        kembaliButton = kembaliKeMenuButton;

        // Susun modal
        modal.getChildren().addAll(skorLabel, keteranganLabel, pesanLabel, kembaliKeMenuButton);

        // Stack modal background dengan content - centered properly like MainView
        StackPane modalPane = new StackPane();
        modalPane.getChildren().addAll(modalBackground, modal);
        modalPane.setAlignment(Pos.CENTER);

        // Add to result container
        resultContainer.getChildren().add(modalPane);
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

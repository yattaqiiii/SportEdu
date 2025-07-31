package com.sportedu.view;

import com.sportedu.model.Teknik;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
    private ImageView titleImage;

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

    public TeknikView() {
        view = new VBox();
        view.setPrefSize(1280, 832);
        kembaliButton = UIFactory.createNavButton("/images/Back.png", 120, 50);
    }

    /**
     * Menampilkan daftar teknik ke dalam view.
     * 
     * @param teknikList Daftar objek Teknik yang akan ditampilkan.
     * @param olahraga   Judul cabang olahraga.
     */
    public void displayTeknik(List<Teknik> teknikList, String olahraga) {
        view.getChildren().clear();
        view.setPrefSize(1280, 832);

        // Container untuk grid teknik dengan background putih
        VBox contentContainer = new VBox(30);
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.setPadding(new Insets(40));
        contentContainer.getStyleClass().add("content-background");

        try {
            // Background dengan dot pattern
            ImageView dotBackground;
            try {
                dotBackground = new ImageView(new Image(getClass().getResourceAsStream("/images/dot background.png")));
            } catch (Exception e) {
                // Coba alternatif nama file
                System.out.println("Mencoba load background dengan nama alternatif");
                dotBackground = new ImageView(new Image(getClass().getResourceAsStream("/images/dot_background.png")));
            }
            dotBackground.setFitWidth(1280);
            dotBackground.setFitHeight(832);
            dotBackground.setPreserveRatio(false);

            // Overlay container untuk konten
            VBox overlayContainer = new VBox();
            overlayContainer.setPrefSize(1280, 832);
            overlayContainer.setStyle("-fx-background-color: transparent;");

            // Navbar di atas
            HBox navbar = createNavbar();

            // Di method displayTeknik()
            VBox contentArea = new VBox(40);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(80, 100, 80, 100)); // Diubah dari 100 ke 80
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Judul halaman menggunakan gambar
            try {
                String imagePath = olahraga.equalsIgnoreCase("Badminton") ? "/images/Teknik Bulu Tangkis.png"
                        : "/images/Teknik Sepak Bola.png";

                titleImage = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
                titleImage.setFitHeight(60);
                titleImage.setPreserveRatio(true);
                titleImage.setSmooth(true);
            } catch (Exception e) {
                // Fallback ke text jika gambar tidak ditemukan
                titleImage = null;
                System.err.println("Tidak dapat memuat gambar judul: " + e.getMessage());
            }

            // Grid untuk teknik (2x2)
            GridPane teknikGrid = new GridPane();
            teknikGrid.setAlignment(Pos.CENTER);
            teknikGrid.setHgap(40); // Gap horizontal diperbesar
            teknikGrid.setVgap(40); // Gap vertical diperbesar

            // Debug logging
            System.out.println("Menampilkan " + teknikList.size() + " teknik untuk " + olahraga);

            for (int i = 0; i < teknikList.size() && i < 4; i++) {
                Teknik teknik = teknikList.get(i);

                VBox teknikContainer = new VBox(15);
                teknikContainer.setAlignment(Pos.CENTER);

                // Debug info
                System.out.println("- Teknik: " + teknik.getNama() + ", Path: " + teknik.getImagePath());

                // Gunakan gambar button yang sesuai berdasarkan nama teknik
                String buttonImagePath;
                switch (teknik.getNama().toLowerCase()) {
                    // Teknik Sepak Bola
                    case "passing":
                        buttonImagePath = "/images/passing_butt.png";
                        break;
                    case "shooting":
                        buttonImagePath = "/images/shooting_butt.png";
                        break;
                    case "dribbling":
                        buttonImagePath = "/images/dribbling_butt.png";
                        break;
                    case "heading":
                        buttonImagePath = "/images/heading_butt.png";
                        break;
                    // Teknik Bulu Tangkis
                    case "netting":
                        buttonImagePath = "/images/netting_butt.png";
                        break;
                    case "smash":
                        buttonImagePath = "/images/smash_butt.png";
                        break;
                    case "footwork":
                        buttonImagePath = "/images/footwork_butt.png";
                        break;
                    case "servis":
                        buttonImagePath = "/images/servis_butt.png";
                        break;
                    default:
                        buttonImagePath = teknik.getImagePath();
                }

                Button teknikButton = UIFactory.createNavButton(buttonImagePath, 300, 230); // Ukuran dibesarkan
                teknikButton.setOnAction(e -> {
                    if (onTeknikSelected != null) {
                        onTeknikSelected.accept(teknik);
                    }
                });

                teknikContainer.getChildren().add(teknikButton);

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

            // Tambahkan komponen ke area konten
            // Isi content container dengan grid atau pesan no data
            if (teknikList.isEmpty()) {
                contentContainer.getChildren().add(new Label("Tidak ada data teknik untuk " + olahraga));
            } else {
                contentContainer.getChildren().add(teknikGrid);
            }

            if (titleImage != null) {
                contentArea.getChildren().addAll(titleImage, contentContainer, kembaliButton);
            } else {
                // Fallback ke text title jika gambar tidak ada
                Label textTitle = new Label("Teknik Dasar " + olahraga);
                textTitle.getStyleClass().add("page-title");
                contentArea.getChildren().addAll(textTitle, contentContainer, kembaliButton);
            }

            overlayContainer.getChildren().addAll(navbar, contentArea);

            // Stack background dan overlay
            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
            stackPane.getChildren().addAll(dotBackground, overlayContainer);

            view.getChildren().add(stackPane);

        } catch (Exception e) {
            // Fallback jika background dot tidak ditemukan
            System.err.println("Background dot tidak ditemukan, menggunakan background warna solid");
            view.setStyle("-fx-background-color: #F5E6A3;");

            VBox fallbackContainer = new VBox();
            fallbackContainer.setPrefSize(1280, 832);

            HBox navbar = createNavbar();

            VBox contentArea = new VBox(40);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(100, 100, 100, 100));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Tambahkan komponen ke area konten
            if (titleImage != null) {
                contentArea.getChildren().addAll(titleImage, contentContainer, kembaliButton);
            } else {
                Label textTitle = new Label("Teknik Dasar " + olahraga);
                textTitle.getStyleClass().add("page-title");
                contentArea.getChildren().addAll(textTitle, contentContainer, kembaliButton);
            }

            fallbackContainer.getChildren().addAll(navbar, contentArea);
            view.getChildren().add(fallbackContainer);
        }
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

package com.sportedu.view;

import com.sportedu.model.Teknik;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.PauseTransition;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.util.Duration;

import java.util.List;
import java.util.function.Consumer;

/**
 * View untuk menampilkan daftar teknik dari sebuah cabang olahraga.
 */
public class TeknikView {

    private final VBox view;
    private final Stage stage;
    private final Button kembaliButton;
    private Consumer<Teknik> onTeknikSelected;
    private ImageView titleImage;

    public TeknikView(Stage stage) {
        this.stage = stage;
        view = new VBox();
        view.prefWidthProperty().bind(stage.widthProperty());
        view.prefHeightProperty().bind(stage.heightProperty());
        kembaliButton = UIFactory.createNavButton("/images/Kembali_kuning.png", 200, 60); // Standardized size
                                                                                          // responsif
    }

    /**
     * Method untuk memaksa reset scale button ke ukuran normal
     * Digunakan untuk memastikan consistency setelah animasi atau hover
     */
    private void forceResetButtonScale(Node button) {
        button.setOpacity(1.0);
        button.setScaleX(1.0);
        button.setScaleY(1.0);
        // Tambahkan CSS class untuk memastikan scale tetap di 1.0
        if (!button.getStyleClass().contains("scale-reset")) {
            button.getStyleClass().add("scale-reset");
        }
    }

    /**
     * Membuat animasi pop out pop in yang berulang untuk kotak putih
     * Animasi lembut yang membuat kotak sedikit membesar dan mengecil berulang
     */
    private void createContinuousPopAnimation(Node element) {
        // Pastikan elemen dimulai dengan scale normal
        element.setScaleX(1.0);
        element.setScaleY(1.0);

        // Animasi scale up (sedikit membesar)
        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(2.0), element);
        scaleUp.setFromX(1.0);
        scaleUp.setFromY(1.0);
        scaleUp.setToX(1.03); // Hanya sedikit membesar (3%)
        scaleUp.setToY(1.03);
        scaleUp.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

        // Animasi scale down (kembali ke ukuran normal)
        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(2.0), element);
        scaleDown.setFromX(1.03);
        scaleDown.setFromY(1.03);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        scaleDown.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

        // Gabungkan animasi secara berurutan
        SequentialTransition breathingAnimation = new SequentialTransition(scaleUp, scaleDown);
        breathingAnimation.setCycleCount(javafx.animation.Animation.INDEFINITE); // Berulang terus

        // Mulai animasi setelah delay kecil
        PauseTransition delay = new PauseTransition(Duration.seconds(1.0));
        delay.setOnFinished(e -> breathingAnimation.play());
        delay.play();
    }

    /**
     * Membuat animasi pop out untuk elemen (logo/button)
     * Animasi: kosong -> membesar -> mengecil ke ukuran normal
     */
    private void createPopOutAnimation(Node element, double delaySeconds) {
        createPopOutAnimation(element, delaySeconds, null);
    }

    /**
     * Membuat animasi pop out untuk elemen dengan callback setelah selesai
     */
    private void createPopOutAnimation(Node element, double delaySeconds, Runnable onComplete) {
        // Set elemen benar-benar tidak terlihat di awal (opacity 0 + scale 0)
        element.setOpacity(0);
        element.setScaleX(0);
        element.setScaleY(0);

        // Disable mouse events selama animasi untuk mencegah hover interference
        element.setDisable(true);

        // Delay sebelum animasi dimulai
        PauseTransition delay = new PauseTransition(Duration.seconds(delaySeconds));

        // Animasi opacity dari 0 ke 1 (muncul) bersamaan dengan scale
        FadeTransition fadeIn = new FadeTransition(Duration.millis(100), element);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Animasi scale dari 0 ke 1.2 (membesar)
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), element);
        scaleUp.setFromX(0);
        scaleUp.setFromY(0);
        scaleUp.setToX(1.2);
        scaleUp.setToY(1.2);

        // Animasi scale dari 1.2 ke 1.0 (mengecil ke normal)
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), element);
        scaleDown.setFromX(1.2);
        scaleDown.setFromY(1.2);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Gabungkan fade in dengan scale up secara bersamaan
        ParallelTransition fadeAndScaleUp = new ParallelTransition(fadeIn, scaleUp);

        // Gabungkan animasi berurutan: delay -> (fade+scaleUp) -> scaleDown
        SequentialTransition animation = new SequentialTransition(delay, fadeAndScaleUp, scaleDown);

        // Setelah animasi selesai, pastikan scale kembali ke 1.0 dan siapkan untuk
        // hover
        animation.setOnFinished(e -> {
            // PENTING: Pastikan scale benar-benar 1.0 setelah animasi dengan force reset
            forceResetButtonScale(element);

            // Re-enable mouse events setelah animasi selesai
            element.setDisable(false);

            // Tambah delay kecil sebelum hover effects bisa bekerja dengan normal
            PauseTransition enableHoverDelay = new PauseTransition(Duration.millis(100));
            enableHoverDelay.setOnFinished(hoverEvent -> {
                // Execute callback if provided
                if (onComplete != null) {
                    onComplete.run();
                }
            });
            enableHoverDelay.play();
        });

        animation.play();
    }

    /**
     * Menampilkan daftar teknik ke dalam view.
     * 
     * @param teknikList Daftar objek Teknik yang akan ditampilkan.
     * @param olahraga   Judul cabang olahraga.
     */
    public void displayTeknik(List<Teknik> teknikList, String olahraga) {
        view.getChildren().clear();
        view.prefWidthProperty().bind(stage.widthProperty());
        view.prefHeightProperty().bind(stage.heightProperty());

        try {
            // Background dengan dot pattern yang responsif
            ImageView dotBackground = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/dot background.png")));
            dotBackground.fitWidthProperty().bind(stage.widthProperty());
            dotBackground.fitHeightProperty().bind(stage.heightProperty());
            dotBackground.setPreserveRatio(false);

            // Overlay container untuk konten
            VBox overlayContainer = new VBox();
            overlayContainer.prefWidthProperty().bind(stage.widthProperty());
            overlayContainer.prefHeightProperty().bind(stage.heightProperty());
            overlayContainer.setStyle("-fx-background-color: transparent;");

            // Logo SportEdu di kiri atas sebagai overlay
            ImageView logoOverlay = new ImageView();
            try {
                logoOverlay = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
                logoOverlay.setFitHeight(50);
                logoOverlay.setPreserveRatio(true);
                logoOverlay.setSmooth(true);
            } catch (Exception logoException) {
                // Jika logo tidak ditemukan, buat placeholder kosong
                logoOverlay = new ImageView();
            }

            // Content area utama (HAPUS NAVBAR)
            VBox contentArea = new VBox(30);
            contentArea.setAlignment(Pos.CENTER);
            contentArea.setPadding(new Insets(40, 50, 40, 50));
            VBox.setVgrow(contentArea, javafx.scene.layout.Priority.ALWAYS);

            // Judul halaman menggunakan gambar dengan ukuran responsif
            try {
                String imagePath = olahraga.equalsIgnoreCase("Badminton") ? "/images/Teknik Bulu Tangkis.png"
                        : "/images/Teknik Sepak Bola.png";

                titleImage = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
                titleImage.setFitHeight(50);
                titleImage.setPreserveRatio(true);
                titleImage.setSmooth(true);

                // Animasi pop out untuk title image
                createPopOutAnimation(titleImage, 0.2);
            } catch (Exception e) {
                // Fallback ke text jika gambar tidak ditemukan
                titleImage = null;
                System.err.println("Tidak dapat memuat gambar judul: " + e.getMessage());
            }

            // Container untuk grid teknik dengan white box yang responsif - proper sizing
            VBox contentContainer = new VBox(20);
            contentContainer.setAlignment(Pos.CENTER);
            contentContainer.setPadding(new Insets(20, 30, 20, 30)); // Proper padding
            contentContainer.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
            contentContainer.setMaxWidth(600); // Reduced from 700 to 600
            contentContainer.setMaxHeight(400); // Proper height to contain all buttons

            // Tambahkan animasi breathing untuk white box
            createContinuousPopAnimation(contentContainer);

            // Grid untuk teknik (2x2) dengan ukuran button yang standardized
            GridPane teknikGrid = new GridPane();
            teknikGrid.setAlignment(Pos.CENTER);
            teknikGrid.setHgap(20); // Proper horizontal gap
            teknikGrid.setVgap(20); // Proper vertical gap instead of negative

            // Debug logging
            System.out.println("Menampilkan " + teknikList.size() + " teknik untuk " + olahraga);

            for (int i = 0; i < teknikList.size() && i < 4; i++) {
                Teknik teknik = teknikList.get(i);

                VBox teknikContainer = new VBox(0); // Reduced spacing from 10 to 0
                teknikContainer.setAlignment(Pos.CENTER);

                // Debug info
                System.out.println("- Teknik: " + teknik.getNama() + ", Path: " + teknik.getImagePath());

                // Gunakan gambar button yang sesuai berdasarkan nama teknik dengan ukuran lebih
                // kecil
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

                // Button dengan ukuran standardized dan proper spacing
                Button teknikButton = UIFactory.createNavButton(buttonImagePath, 200, 120); // Smaller and standardized
                                                                                            // size
                teknikButton.setOnAction(event -> {
                    if (onTeknikSelected != null) {
                        onTeknikSelected.accept(teknik);
                    }
                });

                // Animasi pop out untuk technique button dengan delay berdasarkan posisi
                createPopOutAnimation(teknikButton, 0.4 + (i * 0.2));

                teknikContainer.getChildren().add(teknikButton);

                // Posisi dalam grid 2x2
                int row = i / 2;
                int col = i % 2;
                teknikGrid.add(teknikContainer, col, row);
            }

            // Jika tidak ada teknik
            if (teknikList.isEmpty()) {
                Label noDataLabel = new Label("Tidak ada data teknik untuk " + olahraga);
                noDataLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #e74c3c;");
                contentContainer.getChildren().add(noDataLabel);
            } else {
                // Add ScrollPane inside white box if content exceeds height
                javafx.scene.control.ScrollPane innerScrollPane = new javafx.scene.control.ScrollPane();
                innerScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
                innerScrollPane.setFitToWidth(true);
                innerScrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
                innerScrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
                innerScrollPane.setPannable(true);
                innerScrollPane.setContent(teknikGrid);

                contentContainer.getChildren().add(innerScrollPane);
            }

            // Tambahkan komponen ke area konten
            if (titleImage != null) {
                // Animasi pop out untuk kembali button
                createPopOutAnimation(kembaliButton, 1.2);
                contentArea.getChildren().addAll(titleImage, contentContainer, kembaliButton);
            } else {
                // Fallback ke text title jika gambar tidak ada
                Label textTitle = new Label("Teknik Dasar " + olahraga);
                textTitle.setStyle(
                        "-fx-font-family: 'Poppins', Arial; -fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;");

                // Animasi pop out untuk text title fallback dan kembali button
                createPopOutAnimation(textTitle, 0.2);
                createPopOutAnimation(kembaliButton, 1.2);
                contentArea.getChildren().addAll(textTitle, contentContainer, kembaliButton);
            }

            overlayContainer.getChildren().add(contentArea);

            // Stack background dan overlay
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(dotBackground, overlayContainer);

            // Tambahkan logo di kiri atas sebagai overlay terpisah
            StackPane finalStackPane = new StackPane();
            finalStackPane.getChildren().add(stackPane);

            // Posisikan logo di kiri atas
            finalStackPane.getChildren().add(logoOverlay);
            StackPane.setAlignment(logoOverlay, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlay, new Insets(20, 0, 0, 20));

            // Buat ScrollPane untuk seluruh konten
            javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(finalStackPane);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPannable(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

            view.getChildren().add(scrollPane);

        } catch (Exception e) {
            // Fallback jika background dot tidak ditemukan
            System.err.println("Background dot tidak ditemukan, menggunakan background warna solid: " + e.getMessage());

            view.setStyle("-fx-background-color: #F5E6A3;");

            // Judul (HAPUS NAVBAR)
            Label title = new Label("Teknik Dasar " + olahraga);
            title.setStyle(
                    "-fx-font-family: 'Poppins', Arial; -fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;");

            // Animasi pop out untuk title fallback
            createPopOutAnimation(title, 0.2);

            // Grid untuk teknik dengan ukuran button yang standardized
            GridPane teknikGrid = new GridPane();
            teknikGrid.setAlignment(Pos.CENTER);
            teknikGrid.setHgap(20); // Proper horizontal gap
            teknikGrid.setVgap(20); // Proper vertical gap instead of negative

            for (int i = 0; i < teknikList.size() && i < 4; i++) {
                Teknik teknik = teknikList.get(i);

                String buttonImagePath;
                switch (teknik.getNama().toLowerCase()) {
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

                // Button dengan ukuran standardized dan proper spacing - fallback
                Button teknikButton = UIFactory.createNavButton(buttonImagePath, 200, 120); // Smaller and standardized
                                                                                            // size
                teknikButton.setOnAction(event -> {
                    if (onTeknikSelected != null) {
                        onTeknikSelected.accept(teknik);
                    }
                });

                // Animasi pop out untuk technique button fallback dengan delay berdasarkan
                // posisi
                createPopOutAnimation(teknikButton, 0.4 + (i * 0.2));

                // Add button directly to grid without extra container to reduce spacing
                int row = i / 2;
                int col = i % 2;
                teknikGrid.add(teknikButton, col, row);
            }

            // Container dengan ScrollPane dan white box constraint - proper sizing
            VBox whiteBoxContainer = new VBox(20);
            whiteBoxContainer.setAlignment(Pos.CENTER);
            whiteBoxContainer.setPadding(new Insets(20, 30, 20, 30)); // Proper padding
            whiteBoxContainer.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
            whiteBoxContainer.setMaxWidth(600);
            whiteBoxContainer.setMaxHeight(400); // Proper height to contain all buttons

            // Tambahkan animasi breathing untuk white box fallback
            createContinuousPopAnimation(whiteBoxContainer);

            javafx.scene.control.ScrollPane innerScrollPane = new javafx.scene.control.ScrollPane();
            innerScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
            innerScrollPane.setFitToWidth(true);
            innerScrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
            innerScrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            innerScrollPane.setPannable(true);
            innerScrollPane.setContent(teknikGrid);

            whiteBoxContainer.getChildren().add(innerScrollPane);

            VBox finalContentArea = new VBox(30);
            finalContentArea.setAlignment(Pos.CENTER);
            finalContentArea.setPadding(new Insets(40, 50, 40, 50));
            VBox.setVgrow(finalContentArea, javafx.scene.layout.Priority.ALWAYS);

            // Animasi pop out untuk kembali button fallback
            createPopOutAnimation(kembaliButton, 1.2);
            finalContentArea.getChildren().addAll(title, whiteBoxContainer, kembaliButton);

            // Container dengan ScrollPane (HAPUS NAVBAR)
            VBox mainContainer = new VBox();
            mainContainer.getChildren().add(finalContentArea);

            // Logo SportEdu di kiri atas untuk fallback case
            ImageView logoOverlayFallback = new ImageView();
            try {
                logoOverlayFallback = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
                logoOverlayFallback.setFitHeight(50);
                logoOverlayFallback.setPreserveRatio(true);
                logoOverlayFallback.setSmooth(true);
            } catch (Exception logoException) {
                // Jika logo tidak ditemukan, buat placeholder kosong
                logoOverlayFallback = new ImageView();
            }

            // Stack main container dengan logo overlay
            StackPane fallbackStackPane = new StackPane();
            fallbackStackPane.getChildren().add(mainContainer);
            fallbackStackPane.getChildren().add(logoOverlayFallback);
            StackPane.setAlignment(logoOverlayFallback, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlayFallback, new Insets(20, 0, 0, 20));

            javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(fallbackStackPane);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPannable(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

            view.getChildren().add(scrollPane);
        }
    }

    public Parent getView() {
        return view;
    }

    public void setOnTeknikSelected(Consumer<Teknik> callback) {
        this.onTeknikSelected = callback;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }
}

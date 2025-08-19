package com.sportedu.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * View untuk menampilkan halaman petunjuk quiz
 * Menjelaskan cara bermain Tebak Gambar dan Mencocokkan Gambar
 */
public class InstructionsView {

    private final BorderPane view;
    private final Button kembaliButton;
    private final Stage stage;

    // Interface untuk callback navigasi
    public interface OnNavigationListener {
        void onNavigateToHome();

        void onNavigateToMateri();

        void onNavigateToQuiz();

        void onNavigateBack();
    }

    private OnNavigationListener onNavigation;

    public InstructionsView(Stage stage) {
        this.stage = stage;
        view = new BorderPane();
        // Remove fixed size to make it responsive
        // view.setPrefSize(1280, 832);
        // view.setMaxSize(1280, 832);

        // Tombol kembali menggunakan Kembali_kuning.png dengan ukuran yang lebih kecil
        kembaliButton = UIFactory.createNavButton("/images/Kembali_kuning.png", 200, 55); // Reduced from 300,80 to
                                                                                          // 200,55

        setupLayout();
    }

    private void setupLayout() {
        // Container utama dengan background dot pattern - make responsive
        VBox mainContainer = new VBox();
        mainContainer.prefWidthProperty().bind(stage.widthProperty());
        mainContainer.prefHeightProperty().bind(stage.heightProperty());

        // Background dengan dot pattern dari vektor
        try {
            ImageView dotBackground = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/dot background.png")));
            // Make background responsive to window size
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

            // Content area dengan scroll - less aggressive since white box has internal
            // scroll (HAPUS NAVBAR)
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Disable horizontal scroll
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPannable(true);

            // Content container - smaller and more compact
            VBox contentContainer = createContentContainer();
            scrollPane.setContent(contentContainer);

            overlayContainer.getChildren().add(scrollPane);

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

            view.setCenter(finalStackPane);

        } catch (Exception e) {
            // Fallback background beige - also responsive
            VBox fallbackContainer = new VBox();
            fallbackContainer.prefWidthProperty().bind(stage.widthProperty());
            fallbackContainer.prefHeightProperty().bind(stage.heightProperty());
            fallbackContainer.setStyle("-fx-background-color: #F5E6A3;");

            // Content area dengan scroll (HAPUS NAVBAR)
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setStyle("-fx-background-color: transparent;");
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Disable horizontal scroll
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPannable(true);

            VBox contentContainer = createContentContainer();
            scrollPane.setContent(contentContainer);

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

            // Stack fallback container dengan logo overlay
            StackPane fallbackStackPane = new StackPane();
            fallbackContainer.getChildren().add(scrollPane);
            fallbackStackPane.getChildren().add(fallbackContainer);
            fallbackStackPane.getChildren().add(logoOverlayFallback);
            StackPane.setAlignment(logoOverlayFallback, Pos.TOP_LEFT);
            StackPane.setMargin(logoOverlayFallback, new Insets(20, 0, 0, 20));

            view.setCenter(fallbackStackPane);

            System.err.println("Gagal memuat dot background: " + e.getMessage());
        }
    }

    private VBox createContentContainer() {
        VBox container = new VBox(25); // Reduced from 40 to 25
        container.setAlignment(Pos.CENTER); // Changed from TOP_CENTER to CENTER
        container.setPadding(new Insets(40, 40, 40, 40)); // Increased padding for better centering
        container.setStyle("-fx-background-color: transparent;");
        container.setMinHeight(700); // Increased from 600 to 700 for better spacing

        // Add top spacer to push content towards center
        Region topSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);

        // Header dengan gambar "Petunjuk" - smaller
        VBox headerSection = new VBox(20); // Reduced from 30 to 20
        headerSection.setAlignment(Pos.CENTER);

        try {
            ImageView headerImage = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/Petunjuk.png")));
            headerImage.setFitHeight(60); // Reduced from 80 to 60
            headerImage.setPreserveRatio(true);
            headerSection.getChildren().add(headerImage);
        } catch (Exception e) {
            Label headerLabel = new Label("Petunjuk");
            headerLabel.setStyle(
                    "-fx-font-family: 'Poppins', Arial; -fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #516BB0;"); // Reduced
                                                                                                                               // from
                                                                                                                               // 36px
                                                                                                                               // to
                                                                                                                               // 28px
            headerSection.getChildren().add(headerLabel);
        }

        // Main content box (white background) - with internal scrolling
        VBox contentBox = new VBox();
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 4);");
        contentBox.setMaxWidth(700);
        contentBox.setMaxHeight(400); // Set max height to enable scrolling

        // Create scrollable content inside the white box
        ScrollPane innerScrollPane = new ScrollPane();
        innerScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        innerScrollPane.setFitToWidth(true);
        innerScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        innerScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        innerScrollPane.setPannable(true);

        // Content container for scrollable content
        VBox scrollableContent = new VBox(20);
        scrollableContent.setAlignment(Pos.TOP_CENTER);
        scrollableContent.setPadding(new Insets(25, 30, 25, 30));

        // Instruksi untuk Tebak Gambar
        VBox tebakGambarSection = createInstructionSection(
                "1. Tebak Gambar",
                "Lihat gambar yang ditampilkan di layar, ya! Gambar ini menunjukkan aktivitas, gerakan, atau alat dalam olahraga. "
                        +
                        "Tugas kamu adalah menebak apa yang sedang dilakukan atau nama gerakan/alat diarannya. Perhatikan baik-baik setiap petunjuk pada gambar, "
                        +
                        "lalu pilih jawabanmu dengan tepat!");

        // Instruksi untuk Mencocokkan Gambar
        VBox mencocokkanGambarSection = createInstructionSection(
                "2. Cocokkan Gambar",
                "Pada bagian ini, kamu akan melihat dua sisi gambar. Di sisi kiri, ada gambar yang menunjukkan aktivitas atau teknik dalam olahraga. "
                        +
                        "Di sisi kanan, ada pilihan jawaban berupa nama gerakan, alat, atau jenis olahraga yang sesuai. "
                        +
                        "Tugas kamu adalah menghubungkan setiap gambar di sebelah kiri dengan jawaban yang paling tepat di sebelah kanan. Perhatikan baik-baik "
                        +
                        "setiap gambar, ya! Lihat aktivitas yang dilakukan, lalu cocokkan dengan jawabannya. Jumlahnya ada 3 sesi soal, jadi pastikan kamu menjawab semuanya dengan benar!");

        scrollableContent.getChildren().addAll(tebakGambarSection, mencocokkanGambarSection);
        innerScrollPane.setContent(scrollableContent);
        contentBox.getChildren().add(innerScrollPane);

        // Tombol kembali di bawah - smaller spacing
        HBox buttonSection = new HBox();
        buttonSection.setAlignment(Pos.CENTER);
        buttonSection.setPadding(new Insets(20, 0, 15, 0)); // Reduced from 30,0,20,0 to 20,0,15,0

        // Set navigation action untuk kembali button
        kembaliButton.setOnAction(e -> {
            if (onNavigation != null) {
                onNavigation.onNavigateBack();
            }
        });

        buttonSection.getChildren().add(kembaliButton);

        // Add bottom spacer to center content vertically
        Region bottomSpacer = new Region();
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        container.getChildren().addAll(topSpacer, headerSection, contentBox, buttonSection, bottomSpacer);
        return container;
    }

    private VBox createInstructionSection(String title, String content) {
        VBox section = new VBox(10); // Reduced from 15 to 10
        section.setAlignment(Pos.TOP_LEFT);

        // Title dengan warna biru dan bold - smaller font
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-family: 'Poppins', Arial; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #516BB0;"); // Reduced
                                                                                                                           // from
                                                                                                                           // 22px
                                                                                                                           // to
                                                                                                                           // 18px

        // Content dengan text yang mudah dibaca - smaller font
        Label contentLabel = new Label(content);
        contentLabel.setStyle(
                "-fx-font-family: 'Poppins', Arial; -fx-font-size: 14px; -fx-font-weight: normal; -fx-text-fill: #333333; -fx-line-spacing: 1.5px;"); // Reduced
                                                                                                                                                      // from
                                                                                                                                                      // 16px
                                                                                                                                                      // to
                                                                                                                                                      // 14px,
                                                                                                                                                      // line-spacing
                                                                                                                                                      // from
                                                                                                                                                      // 2px
                                                                                                                                                      // to
                                                                                                                                                      // 1.5px
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(620); // Reduced from 820 to 620

        section.getChildren().addAll(titleLabel, contentLabel);
        return section;
    }

    /**
     * Membuat navbar dengan logo dan tombol navigasi - smaller and responsive like
     * MainView
     */
    // Getter methods
    public Parent getView() {
        return view;
    }

    public Button getKembaliButton() {
        return kembaliButton;
    }

    public void setOnNavigation(OnNavigationListener listener) {
        this.onNavigation = listener;
    }
}

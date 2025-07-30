// MainView.java
package com.sportedu.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainView {
    private Scene scene;
    private StackPane root;
    private VBox currentPage;

    // Interface untuk komunikasi dengan presenter
    public interface MainViewListener {
        void onMateriClicked();
        void onQuizClicked();
        void onSepakBolaClicked();
        void onBadmintonClicked();
        void onTeknikClicked(String teknik);
        void onKembaliClicked();
        void onMulaiQuizClicked();
        void onPetunjukClicked();
        void onTebakGambarClicked();
        void onCocokkanGambarClicked();
    }

    private MainViewListener listener;

    public MainView() {
        initializeView();
    }

    private void initializeView() {
        root = new StackPane();
        root.setPrefSize(1280, 832);

        // Background dengan pattern dots
        createBackground();

        scene = new Scene(root, 1280, 832);

        // Load CSS
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Tidak dapat menemukan styles.css. Pastikan file tersebut ada di folder resources.");
        }
    }

    private void createBackground() {
        // Background krem dengan pattern dots
        Region background = new Region();
        background.setStyle(
                "-fx-background-color: #FFF5E1; " +
                "-fx-background-image: radial-gradient(circle at 15px 15px, #E0D8C4 1px, transparent 0); " +
                "-fx-background-size: 30px 30px;"
        );
        background.setPrefSize(1280, 832);
        root.getChildren().add(background);
    }

    public void setListener(MainViewListener listener) {
        this.listener = listener;
    }

    // Halaman Welcome/Splash
    public void showWelcomePage() {
        clearPage();

        VBox welcomePage = new VBox(20); // Mengurangi spasi
        welcomePage.setAlignment(Pos.TOP_CENTER); // Mulai dari atas

        // Header dengan logo
        HBox header = createHeader();

        // Spacer untuk mendorong konten ke tengah secara vertikal
        Region topSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.SOMETIMES);

        // Content area
        VBox contentArea = new VBox(25); // Mengurangi spasi
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5);");
        contentArea.setPadding(new Insets(50, 80, 50, 80)); // Menyesuaikan padding
        contentArea.setMaxWidth(700); // Sedikit lebih lebar

        Label titleLabel = new Label("SPORTEDU");
        titleLabel.setStyle("-fx-font-family: 'Arial Black'; -fx-font-size: 64px; -fx-font-weight: 900; " +
                "-fx-text-fill: #2A3A75; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 2, 2);");

        Label subtitleLabel = new Label("Aplikasi Pembelajaran Olahraga Interaktif untuk Anak SD");
        subtitleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #F4A261; -fx-font-weight: bold;");

        HBox buttonContainer = new HBox(30); // Mengurangi spasi
        buttonContainer.setAlignment(Pos.CENTER);

        // Warna tombol disesuaikan dengan desain
        Button materiButton = createPrimaryButton("Materi", "#E74C3C");
        Button quizButton = createPrimaryButton("Quiz", "#3498DB");

        materiButton.setOnAction(e -> {
            if (listener != null) listener.onMateriClicked();
        });

        quizButton.setOnAction(e -> {
            if (listener != null) listener.onQuizClicked();
        });

        buttonContainer.getChildren().addAll(materiButton, quizButton);
        contentArea.getChildren().addAll(titleLabel, subtitleLabel, new VBox(20), buttonContainer); // Menambah spasi kecil

        // Spacer untuk mendorong konten ke tengah
        Region bottomSpacer = new Region();
        VBox.setVgrow(bottomSpacer, Priority.SOMETIMES);

        welcomePage.getChildren().addAll(header, topSpacer, contentArea, bottomSpacer);

        // Menambahkan karakter di kanan bawah
        try {
            Image characterImage = new Image(getClass().getResourceAsStream("/tampilan/1. Landing Page.png"));
            ImageView characterView = new ImageView(characterImage);
            characterView.setFitWidth(300); // Sesuaikan ukuran
            characterView.setPreserveRatio(true);

            StackPane.setAlignment(characterView, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(characterView, new Insets(0, 50, 0, 0)); // Margin dari kanan bawah
            root.getChildren().add(characterView);
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar karakter: " + e.getMessage());
        }


        root.getChildren().add(welcomePage);
        currentPage = welcomePage;
    }

    // Halaman Materi (pilih olahraga)
    public void showMateriPage() {
        clearPage();

        VBox materiPage = new VBox(30);
        materiPage.setAlignment(Pos.CENTER);

        HBox header = createHeader();

        VBox contentArea = new VBox(40);
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setPadding(new Insets(40));

        Label titleLabel = new Label("Materi");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; " +
                "-fx-text-fill: #2E86AB; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 2, 2);");

        VBox sportsContainer = new VBox(30);
        sportsContainer.setAlignment(Pos.CENTER);

        Button sepakBolaButton = createSportButton("⚽ Sepak Bola", "#E63946");
        Button badmintonButton = createSportButton("🏸 Badminton", "#F4A261");

        sepakBolaButton.setOnAction(e -> {
            if (listener != null) listener.onSepakBolaClicked();
        });

        badmintonButton.setOnAction(e -> {
            if (listener != null) listener.onBadmintonClicked();
        });

        sportsContainer.getChildren().addAll(sepakBolaButton, badmintonButton);

        Button kembaliButton = createSecondaryButton("Kembali", "#6C757D");
        kembaliButton.setOnAction(e -> {
            if (listener != null) listener.onKembaliClicked();
        });

        contentArea.getChildren().addAll(titleLabel, sportsContainer, kembaliButton);
        materiPage.getChildren().addAll(header, contentArea);

        ScrollPane scrollPane = new ScrollPane(materiPage);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        root.getChildren().add(scrollPane);
        currentPage = materiPage;
    }

    // Halaman Teknik Sepak Bola
    public void showSepakBolaPage() {
        clearPage();

        VBox sepakBolaPage = new VBox(30);
        sepakBolaPage.setAlignment(Pos.CENTER);

        HBox header = createHeader();

        // Navigasi dengan panah
        HBox navContainer = new HBox();
        navContainer.setAlignment(Pos.CENTER);
        navContainer.setPrefWidth(1280);

        Button leftArrow = createArrowButton("←");
        Button rightArrow = createArrowButton("→");

        VBox contentArea = new VBox(30);
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        contentArea.setPadding(new Insets(40, 60, 40, 60));
        contentArea.setMaxWidth(800);
        contentArea.setMinHeight(400);

        Label titleLabel = new Label("Teknik Sepak Bola");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; " +
                "-fx-text-fill: #2E86AB; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 2, 2);");

        Label instructionLabel = new Label("Cari tahu teknik sepak bola, yuk!");
        instructionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");

        // Grid teknik
        GridPane teknikGrid = new GridPane();
        teknikGrid.setAlignment(Pos.CENTER);
        teknikGrid.setHgap(20);
        teknikGrid.setVgap(20);

        Button passingButton = createTeknikButton("Passing");
        Button dribblingButton = createTeknikButton("Dribbling");
        Button shootingButton = createTeknikButton("Shooting");
        Button headingButton = createTeknikButton("Heading");

        teknikGrid.add(passingButton, 0, 0);
        teknikGrid.add(dribblingButton, 1, 0);
        teknikGrid.add(shootingButton, 0, 1);
        teknikGrid.add(headingButton, 1, 1);

        // Event handlers
        passingButton.setOnAction(e -> {
            if (listener != null) listener.onTeknikClicked("Passing");
        });
        dribblingButton.setOnAction(e -> {
            if (listener != null) listener.onTeknikClicked("Dribbling");
        });
        shootingButton.setOnAction(e -> {
            if (listener != null) listener.onTeknikClicked("Shooting");
        });
        headingButton.setOnAction(e -> {
            if (listener != null) listener.onTeknikClicked("Heading");
        });

        Button kembaliButton = createSecondaryButton("Kembali", "#E63946");
        kembaliButton.setOnAction(e -> {
            if (listener != null) listener.onKembaliClicked();
        });

        contentArea.getChildren().addAll(titleLabel, instructionLabel, teknikGrid);

        // Layout dengan arrow navigation
        HBox mainContent = new HBox();
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setSpacing(50);

        leftArrow.setStyle("-fx-background-color: #E63946; -fx-text-fill: white; " +
                "-fx-font-size: 24px; -fx-pref-width: 60; -fx-pref-height: 60; " +
                "-fx-background-radius: 30;");
        rightArrow.setStyle("-fx-background-color: #E63946; -fx-text-fill: white; " +
                "-fx-font-size: 24px; -fx-pref-width: 60; -fx-pref-height: 60; " +
                "-fx-background-radius: 30;");

        mainContent.getChildren().addAll(leftArrow, contentArea, rightArrow);

        sepakBolaPage.getChildren().addAll(header, mainContent, kembaliButton);

        ScrollPane scrollPane = new ScrollPane(sepakBolaPage);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        root.getChildren().add(scrollPane);
        currentPage = sepakBolaPage;
    }

    // Halaman Detail Teknik (contoh: Passing)
    public void showTeknikDetailPage(String teknikName) {
        clearPage();

        VBox detailPage = new VBox(30);
        detailPage.setAlignment(Pos.CENTER);

        HBox header = createHeader();

        // Navigation arrows
        HBox navContainer = new HBox();
        navContainer.setAlignment(Pos.CENTER);
        navContainer.setPrefWidth(1280);

        Button leftArrow = createArrowButton("←");
        Button rightArrow = createArrowButton("→");

        VBox contentArea = new VBox(30);
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        contentArea.setPadding(new Insets(40, 60, 40, 60));
        contentArea.setMaxWidth(800);
        contentArea.setMinHeight(400);

        if (teknikName.equals("Passing")) {
            showPassingContent(contentArea);
        } else if (teknikName.equals("Animasi Passing")) {
            showAnimasiPassingContent(contentArea);
        } else {
            showDefaultTeknikContent(contentArea, teknikName);
        }

        leftArrow.setStyle("-fx-background-color: #E63946; -fx-text-fill: white; " +
                "-fx-font-size: 24px; -fx-pref-width: 60; -fx-pref-height: 60; " +
                "-fx-background-radius: 30;");
        rightArrow.setStyle("-fx-background-color: #E63946; -fx-text-fill: white; " +
                "-fx-font-size: 24px; -fx-pref-width: 60; -fx-pref-height: 60; " +
                "-fx-background-radius: 30;");

        HBox mainContent = new HBox();
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setSpacing(50);
        mainContent.getChildren().addAll(leftArrow, contentArea, rightArrow);

        Button kembaliButton = createSecondaryButton("Kembali", "#F4A261");
        kembaliButton.setOnAction(e -> {
            if (listener != null) listener.onKembaliClicked();
        });

        detailPage.getChildren().addAll(header, mainContent, kembaliButton);

        ScrollPane scrollPane = new ScrollPane(detailPage);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        root.getChildren().add(scrollPane);
        currentPage = detailPage;
    }

    private void showPassingContent(VBox contentArea) {
        Label titleLabel = new Label("Peralatan");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; " +
                "-fx-text-fill: #2E86AB; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 2, 2);");

        contentArea.getChildren().add(titleLabel);
    }

    private void showAnimasiPassingContent(VBox contentArea) {
        Label titleLabel = new Label("Animasi Passing");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; " +
                "-fx-text-fill: #2E86AB; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 2, 2);");

        contentArea.getChildren().add(titleLabel);
    }

    private void showPassingDefinitionContent(VBox contentArea) {
        Label titleLabel = new Label("Pengertian Passing");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; " +
                "-fx-text-fill: #2E86AB; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 2, 2);");

        Label definitionText = new Label("Passing adalah keterampilan memindahkan bola dari satu pemain ke\n" +
                "pemain lain, yang dapat dilakukan menggunakan kaki atau bagian\n" +
                "tubuh lain kecuali tangan.");
        definitionText.setStyle("-fx-font-size: 16px; -fx-text-fill: #333; -fx-text-alignment: center;");
        definitionText.setWrapText(true);

        contentArea.getChildren().addAll(titleLabel, definitionText);
    }

    private void showDefaultTeknikContent(VBox contentArea, String teknikName) {
        Label titleLabel = new Label(teknikName);
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; " +
                "-fx-text-fill: #2E86AB; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 2, 2);");

        Label contentLabel = new Label("Konten untuk " + teknikName + " akan ditampilkan di sini.");
        contentLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");

        contentArea.getChildren().addAll(titleLabel, contentLabel);
    }

    // Halaman Petunjuk Quiz
    public void showPetunjukPage() {
        clearPage();

        VBox petunjukPage = new VBox(30);
        petunjukPage.setAlignment(Pos.CENTER);

        HBox header = createHeader();

        VBox contentArea = new VBox(30);
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        contentArea.setPadding(new Insets(40, 60, 40, 60));
        contentArea.setMaxWidth(900);

        Label titleLabel = new Label("Petunjuk");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; " +
                "-fx-text-fill: #2E86AB; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 2, 2);");

        VBox instructionsBox = new VBox(20);
        instructionsBox.setAlignment(Pos.CENTER_LEFT);

        Label instruction1 = new Label("1. Tebak Gambar");
        instruction1.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label detail1 = new Label("   Lihat gambar yang ditampilkan di layar, ya! Gambar ini menunjukkan\n" +
                "   aktivitas, gerakan, atau alat dalam olahraga.\n" +
                "   Tugas kamu adalah menebak apa yang sedang dilakukan atau nama\n" +
                "   gerakan/alat olahraganya. Perhatikan baik-baik petunjuk pada gambar,\n" +
                "   lalu pilih jawabanmu dengan tepat!");
        detail1.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        Label instruction2 = new Label("2. Cocokkan Gambar");
        instruction2.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label detail2 = new Label("   Pada bagian ini, kamu akan melihat dua sisi gambar. Di sisi kiri, ada\n" +
                "   gambar-gambar yang menunjukkan aktivitas atau teknik dalam\n" +
                "   olahraga. Di sisi kanan, ada pilihan jawaban berupa nama gerakan, alat,\n" +
                "   atau jenis olahraga yang sesuai.\n" +
                "   Tugas kamu adalah menghubungkan setiap gambar di sebelah kiri\n" +
                "   dengan jawaban yang paling tepat di sebelah kanan. Perhatikan baik-\n" +
                "   baik setiap gambar, ya! Lihat posisi tubuh, alat yang digunakan, atau\n" +
                "   aktivitas yang dilakukan, lalu cocokkan dengan jawabannya.");
        detail2.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        instructionsBox.getChildren().addAll(instruction1, detail1, instruction2, detail2);

        Button kembaliButton = createSecondaryButton("Kembali", "#F4A261");
        kembaliButton.setOnAction(e -> {
            if (listener != null) listener.onKembaliClicked();
        });

        contentArea.getChildren().addAll(titleLabel, instructionsBox);
        petunjukPage.getChildren().addAll(header, contentArea, kembaliButton);

        ScrollPane scrollPane = new ScrollPane(petunjukPage);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        root.getChildren().add(scrollPane);
        currentPage = petunjukPage;
    }

    // Halaman Quiz Menu
    public void showQuizPage() {
        clearPage();

        VBox quizPage = new VBox(30);
        quizPage.setAlignment(Pos.CENTER);

        HBox header = createHeader();

        VBox contentArea = new VBox(40);
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setPadding(new Insets(40));

        Label titleLabel = new Label("QUIZ");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; " +
                "-fx-text-fill: #2E86AB; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 2, 2);");

        VBox buttonContainer = new VBox(20);
        buttonContainer.setAlignment(Pos.CENTER);

        Button mulaiButton = createPrimaryButton("▶ Mulai", "#F4A261");
        Button petunjukButton = createSecondaryButton("💡 Petunjuk", "#E63946");

        mulaiButton.setOnAction(e -> {
            if (listener != null) listener.onMulaiQuizClicked();
        });

        petunjukButton.setOnAction(e -> {
            if (listener != null) listener.onPetunjukClicked();
        });

        buttonContainer.getChildren().addAll(mulaiButton, petunjukButton);

        contentArea.getChildren().addAll(titleLabel, buttonContainer);
        quizPage.getChildren().addAll(header, contentArea);

        ScrollPane scrollPane = new ScrollPane(quizPage);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        root.getChildren().add(scrollPane);
        currentPage = quizPage;
    }

    // Helper methods untuk membuat komponen UI
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 50, 10, 50)); // Padding disesuaikan
        header.setStyle("-fx-background-color: #2A3A75;"); // Warna biru tua dari desain
        header.setPrefWidth(1280);
        header.setPrefHeight(70); // Tinggi disesuaikan

        // Logo placeholder
        Label logoText = new Label("SPORTEDU");
        logoText.setStyle("-fx-font-family: 'Arial Black'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button halamanUtamaBtn = createNavButton("Halaman Utama");
        Button materiBtn = createNavButton("Materi");
        Button quizBtn = createNavButton("Quiz");

        HBox menuContainer = new HBox(20); // Spasi antar menu
        menuContainer.setAlignment(Pos.CENTER_RIGHT);
        menuContainer.getChildren().addAll(halamanUtamaBtn, materiBtn, quizBtn);

        header.getChildren().addAll(logoText, spacer, menuContainer);

        return header;
    }

    private Button createNavButton(String text) {
        Button button = new Button(text);
        String baseStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 8 15; -fx-border-color: transparent; -fx-border-width: 0 0 2 0;";
        String hoverStyle = "-fx-background-color: transparent; -fx-text-fill: #F4A261; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 8 15; -fx-border-color: #F4A261; -fx-border-width: 0 0 2 0;";

        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));

        return button;
    }

    private Button createPrimaryButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-font-size: 18px; -fx-font-weight: bold; " +
                "-fx-pref-width: 200; -fx-pref-height: 50; " +
                "-fx-background-radius: 25; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 3);");

        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: derive(" + color + ", -10%); -fx-text-fill: white; " +
                        "-fx-font-size: 18px; -fx-font-weight: bold; " +
                        "-fx-pref-width: 200; -fx-pref-height: 50; " +
                        "-fx-background-radius: 25; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 5);")
        );

        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                        "-fx-font-size: 18px; -fx-font-weight: bold; " +
                        "-fx-pref-width: 200; -fx-pref-height: 50; " +
                        "-fx-background-radius: 25; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 3);")
        );

        return button;
    }

    private Button createSecondaryButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                "-fx-pref-width: 150; -fx-pref-height: 40; " +
                "-fx-background-radius: 20;");
        return button;
    }

    private Button createSportButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-font-size: 20px; -fx-font-weight: bold; " +
                "-fx-pref-width: 300; -fx-pref-height: 80; " +
                "-fx-background-radius: 40; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 3);");
        return button;
    }

    private Button createTeknikButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #F4A261; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                "-fx-pref-width: 150; -fx-pref-height: 50; " +
                "-fx-background-radius: 25;");
        return button;
    }

    private Button createArrowButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #E63946; -fx-text-fill: white; " +
                "-fx-font-size: 24px; -fx-pref-width: 60; -fx-pref-height: 60; " +
                "-fx-background-radius: 30;");
        return button;
    }

    private void clearPage() {
        if (currentPage != null) {
            root.getChildren().remove(currentPage);
        }
        // Hapus juga elemen lain seperti karakter agar tidak menumpuk
        root.getChildren().removeIf(node -> node instanceof ImageView);
    }

    public Scene getScene() {
        return scene;
    }
}


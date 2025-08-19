package com.sportedu.view;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * UIFactory untuk membuat komponen UI sesuai dengan desain folder tampilan
 * Menggunakan skema warna dan styling yang konsisten
 */
public class UIFactory {

    /**
     * Membuat tombol dengan gambar untuk navigasi utama (Landing Page)
     */
    public static Button createMainButton(String imagePath, double width, double height) {
        try {
            if (UIFactory.class.getResourceAsStream(imagePath) == null) {
                System.err.println("Warning: Gambar tidak ditemukan di path: " + imagePath);
                return createFallbackMainButton("Button", width, height);
            }

            Image image = new Image(UIFactory.class.getResourceAsStream(imagePath));
            if (image.isError()) {
                System.err.println("Error: Gagal memload gambar dari path: " + imagePath);
                return createFallbackMainButton("Error", width, height);
            }

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            Button button = new Button();
            button.setGraphic(imageView);
            button.setPrefSize(width, height);
            button.getStyleClass().add("main-button");

            return button;
        } catch (Exception e) {
            System.err.println("Error: Tidak dapat membuat main button: " + imagePath + " - " + e.getMessage());
            return createFallbackMainButton("Error", width, height);
        }
    }

    /**
     * Membuat tombol untuk kartu olahraga (Sepak Bola/Badminton)
     */
    public static Button createSportCard(String imagePath, double width, double height) {
        try {
            if (UIFactory.class.getResourceAsStream(imagePath) == null) {
                return createFallbackSportCard("Sport", width, height);
            }

            Image image = new Image(UIFactory.class.getResourceAsStream(imagePath));
            if (image.isError()) {
                return createFallbackSportCard("Error", width, height);
            }

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width * 0.8); // 80% dari ukuran card
            imageView.setFitHeight(height * 0.8);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            Button button = new Button();
            button.setGraphic(imageView);
            button.setPrefSize(width, height);
            button.getStyleClass().add("sport-card");

            return button;
        } catch (Exception e) {
            return createFallbackSportCard("Error", width, height);
        }
    }

    /**
     * Membuat tombol untuk kartu teknik
     */
    public static Button createTechniqueCard(String imagePath, String teknikName, double width, double height) {
        try {
            Button button = new Button();
            button.setPrefSize(width, height);
            button.getStyleClass().add("technique-card");

            if (UIFactory.class.getResourceAsStream(imagePath) != null) {
                Image image = new Image(UIFactory.class.getResourceAsStream(imagePath));
                if (!image.isError()) {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(width * 0.7);
                    imageView.setFitHeight(height * 0.6);
                    imageView.setPreserveRatio(true);
                    imageView.setSmooth(true);
                    button.setGraphic(imageView);
                }
            }

            // Jika gambar tidak ada, gunakan teks sebagai fallback
            if (button.getGraphic() == null) {
                button.setText(teknikName);
                button.setStyle("-fx-font-size: 14px; -fx-font-weight: 600;");
            }

            return button;
        } catch (Exception e) {
            return createFallbackTechniqueCard(teknikName, width, height);
        }
    }

    /**
     * Membuat tombol navigasi (Back, Next, dll)
     */
    public static Button createNavButton(String imagePath, double width, double height) {
        try {
            if (UIFactory.class.getResourceAsStream(imagePath) == null) {
                return createFallbackNavButton("Nav", width, height);
            }

            Image image = new Image(UIFactory.class.getResourceAsStream(imagePath));
            if (image.isError()) {
                return createFallbackNavButton("Error", width, height);
            }

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width * 0.8);
            imageView.setFitHeight(height * 0.8);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            Button button = new Button();
            button.setGraphic(imageView);
            button.setPrefSize(width, height);
            button.getStyleClass().add("nav-button");

            return button;
        } catch (Exception e) {
            return createFallbackNavButton("Error", width, height);
        }
    }

    /**
     * Membuat tombol aksi primer (untuk quiz, dll)
     */
    public static Button createPrimaryActionButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("primary-action-button");
        button.setPrefWidth(200);
        button.setPrefHeight(50);
        return button;
    }

    /**
     * Membuat tombol aksi sekunder
     */
    public static Button createSecondaryActionButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("secondary-action-button");
        button.setPrefWidth(180);
        button.setPrefHeight(45);
        return button;
    }

    /**
     * Membuat tombol bahaya (untuk keluar, reset, dll)
     */
    public static Button createDangerActionButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("danger-action-button");
        button.setPrefWidth(180);
        button.setPrefHeight(45);
        return button;
    }

    /**
     * Membuat tombol untuk opsi quiz
     */
    public static Button createQuizOptionButton(String text, int index) {
        Button button = new Button(text);
        button.getStyleClass().add("quiz-option-button");
        button.setPrefWidth(350);
        button.setWrapText(true);
        return button;
    }

    // === FALLBACK METHODS ===

    private static Button createFallbackMainButton(String text, double width, double height) {
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.setStyle("-fx-background-color: #516BB0; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 15;");
        return button;
    }

    private static Button createFallbackSportCard(String text, double width, double height) {
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.getStyleClass().add("sport-card");
        button.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;");
        return button;
    }

    private static Button createFallbackTechniqueCard(String text, double width, double height) {
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.getStyleClass().add("technique-card");
        button.setStyle("-fx-font-size: 14px; -fx-font-weight: 600; -fx-text-fill: #1A1E2C;");
        return button;
    }

    private static Button createFallbackNavButton(String text, double width, double height) {
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.setStyle("-fx-background-color: #516BB0; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-size: 12px;");
        return button;
    }

    // === DEPRECATED METHODS (untuk backward compatibility) ===

    /**
     * @deprecated Gunakan createMainButton, createSportCard, atau createNavButton
     */
    @Deprecated
    public static Button createButtonWithImage(String imagePath, double width, double height) {
        return createNavButton(imagePath, width, height);
    }

    /**
     * @deprecated Gunakan createPrimaryActionButton atau createSecondaryActionButton
     */
    @Deprecated
    public static Button createStyledButton(String text) {
        return createSecondaryActionButton(text);
    }
}

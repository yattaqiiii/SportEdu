package com.sportedu;

import com.sportedu.model.SportEduModel;
import com.sportedu.presenter.MainPresenter;
import com.sportedu.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Auto minimize terminal for distribution
        minimizeConsoleWindow();

        // Load custom font
        Font.loadFont(getClass().getResourceAsStream("/Poppins/Poppins-Regular.ttf"), 10);
        Font.loadFont(getClass().getResourceAsStream("/Poppins/Poppins-Bold.ttf"), 10);

        MainView mainView = new MainView(primaryStage);
        SportEduModel model = new SportEduModel(); // Model dibuat sekali
        new MainPresenter(mainView, model); // Presenter utama mengontrol alur

        Scene scene = new Scene(mainView.getRoot(), 1280, 832);

        // Add stylesheet with error handling
        try {
            String stylesheet = getClass().getResource("/styles.css").toExternalForm();
            scene.getStylesheets().add(stylesheet);
        } catch (Exception e) {
            System.err.println("Could not load stylesheet: " + e.getMessage());
        }

        primaryStage.setTitle("SportEdu - Media Pembelajaran Olahraga");
        primaryStage.setScene(scene);

        // Aktifkan window controls bawaan Windows untuk fullscreen/maximize
        primaryStage.setResizable(true); // Harus true agar tombol Windows berfungsi
        primaryStage.setFullScreen(false);
        primaryStage.setFullScreenExitHint("");

        // Initialize fullscreen controls setelah Scene dibuat
        mainView.initializeAfterSceneCreated();

        // Setup cleanup handler untuk audio ketika aplikasi ditutup
        primaryStage.setOnCloseRequest(event -> {
            mainView.cleanup();
        });

        primaryStage.show();
    }

    /**
     * Minimize console window for distribution builds
     */
    private void minimizeConsoleWindow() {
        try {
            // Only try to minimize if running on Windows
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                // This will minimize the console window that appears when running the JAR
                Runtime.getRuntime().exec("cmd /c start /min cmd /c echo.");
            }
        } catch (Exception e) {
            // Silently ignore if console minimization fails
            // This is not critical functionality
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

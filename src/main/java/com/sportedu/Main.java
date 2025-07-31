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
        // Load custom font
        Font.loadFont(getClass().getResourceAsStream("/Poppins/Poppins-Regular.ttf"), 10);
        Font.loadFont(getClass().getResourceAsStream("/Poppins/Poppins-Bold.ttf"), 10);

        MainView mainView = new MainView(primaryStage);
        SportEduModel model = new SportEduModel(); // Model dibuat sekali
        new MainPresenter(mainView, model); // Presenter utama mengontrol alur

        Scene scene = new Scene(mainView.getRoot(), 1024, 768);

        // Add stylesheet with error handling
        try {
            String stylesheet = getClass().getResource("/styles.css").toExternalForm();
            scene.getStylesheets().add(stylesheet);
        } catch (Exception e) {
            System.err.println("Could not load stylesheet: " + e.getMessage());
        }

        primaryStage.setTitle("SportEdu - Media Pembelajaran Olahraga");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
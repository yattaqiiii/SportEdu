// Main.java
package com.sportedu;

import javafx.application.Application;
import javafx.stage.Stage;
import com.sportedu.presenter.MainPresenter;
import com.sportedu.view.MainView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainView mainView = new MainView();
        MainPresenter mainPresenter = new MainPresenter(mainView);

        primaryStage.setTitle("SportEdu - Aplikasi Pembelajaran Olahraga Interaktif");
        primaryStage.setScene(mainView.getScene());
        primaryStage.setResizable(false);
        primaryStage.show();

        mainPresenter.showWelcomePage();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
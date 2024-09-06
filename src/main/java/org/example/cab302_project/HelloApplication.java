package org.example.cab302_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
//test
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!"); //comment ??????
        stage.setScene(scene);
        stage.show(); //Tappei
        stage.show();
        //rhodes changes
        //kyle Reeves test
        //Vai test
        // Inkwang test
        //changes
        //hahs
        //new test this time awesome
        //pusfg
    }

    public static void main(String[] args) {
        launch();
    }
}
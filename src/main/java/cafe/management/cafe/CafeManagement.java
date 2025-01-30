package cafe.management.cafe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CafeManagement extends Application {

    private static Stage Samestage;

    @Override
    public  void start(Stage stage) throws IOException {
        Samestage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(CafeManagement.class.getResource("LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Java OOP Project");
        stage.setScene(scene);
        stage.show();
    }

    public static void switchScene(String fxmlFile,int width) throws IOException {
        FXMLLoader loader = new FXMLLoader(CafeManagement.class.getResource(fxmlFile));
        Scene scene = new Scene(loader.load(), width, 600);
        Samestage.setScene(scene);
        Samestage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
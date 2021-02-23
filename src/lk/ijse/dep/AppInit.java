package lk.ijse.dep;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInit extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/lk/ijse/dep/view/LoginForm.fxml"))));
        primaryStage.show();
        primaryStage.setResizable(false);
    }
}

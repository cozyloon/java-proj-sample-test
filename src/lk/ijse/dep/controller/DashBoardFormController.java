package lk.ijse.dep.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashBoardFormController {
    public AnchorPane root;

    public void imgAddCustomer_OnAction(MouseEvent mouseEvent) throws IOException {
        navigation("/lk/ijse/dep/view/");
    }

    public void imgSearch_OnAction(MouseEvent mouseEvent) throws IOException {
        navigation("/lk/ijse/dep/view/");
    }

    private void navigation(String s) throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource(s))));
        stage.centerOnScreen();
        stage.setResizable(false);
    }
}

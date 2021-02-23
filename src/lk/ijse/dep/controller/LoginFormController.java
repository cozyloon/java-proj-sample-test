package lk.ijse.dep.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep.db.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginFormController {
    public AnchorPane root;
    public TextField txtUser;
    public TextField txtPass;
    public Label lblMsg;

    public void initialize() {
        lblMsg.setVisible(false);
    }

    public void btnLogin_OnAction(ActionEvent actionEvent) throws IOException, SQLException {
        loginViaStm();
    }


    public void btnRegister_OnAction(ActionEvent actionEvent) throws IOException {
        navigation("/lk/ijse/dep/view/RegisterForm.fxml");
    }


    public void lblForgotPass_OnAction(MouseEvent mouseEvent) {
    }


    private void loginViaStm() throws IOException, SQLException {
        String user = txtUser.getText().trim();
        String pass = txtPass.getText().trim();

        boolean validUser = false;
        boolean validPass = false;


        if (pass.isEmpty()) {
            txtPass.requestFocus();
            txtPass.clear();
            lblMsg.setVisible(true);
        } else {
            validPass = true;
        }

        if (user.isEmpty()) {
            txtUser.requestFocus();
            txtUser.clear();
            lblMsg.setVisible(true);
        } else {
            validUser = true;
        }

        if (validUser && validPass){
            Connection con= DBConnection.getInstance().getConnection();
            PreparedStatement stm=con.prepareStatement("Select username,password from register where username=? and password=?");
            stm.setObject(1,user);
            stm.setObject(2,pass);

            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()){
                new Alert(Alert.AlertType.CONFIRMATION,"Login Successful !").show();
                navigation("/lk/ijse/dep/view/DashBoardForm.fxml");
                lblMsg.setVisible(false);
            }else {
                lblMsg.setVisible(true);
            }
        }
    }

    private void navigation(String s) throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource(s))));
        stage.centerOnScreen();
        stage.setResizable(false);
    }

}

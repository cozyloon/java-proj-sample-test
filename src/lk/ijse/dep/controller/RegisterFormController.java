package lk.ijse.dep.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep.db.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class RegisterFormController {
    public AnchorPane root;
    public TextField txtFullName;
    public TextField txtUser;
    public TextField txtPass;
    public TextField txtConPass;
    public TextField txtEmail;
    public TextField txtAcc;
    public Label lblFullName;
    public Label lblUser;
    public Label lblEmail;
    public Label lblPass;
    public Label lblConPass;
    public Button btnRegister;

    public void initialize() {
        lblFullName.setVisible(false);
        lblUser.setVisible(false);
        lblEmail.setVisible(false);
        lblPass.setVisible(false);
        lblConPass.setVisible(false);

        txtUser.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    Connection con = DBConnection.getInstance().getConnection();
                    PreparedStatement stm = con.prepareStatement("select  username from register where username=?");

                    stm.setObject(1, newValue);
                    ResultSet resultSet = stm.executeQuery();

                    if (resultSet.next()) {
                        lblUser.setVisible(true);
                        lblUser.setText("User already exist!");
                    } else {
                        lblUser.setText("");
                    }

                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void btnRegister_OnAction(ActionEvent actionEvent) {
        registerViaStm();
    }

    public void lblPrivacy_OnAction(MouseEvent mouseEvent) {
    }

    public void lblTerms_OnAction(MouseEvent mouseEvent) {
    }

    public void lblLogin_OnAction(MouseEvent mouseEvent) throws IOException {
        navigation("/lk/ijse/dep/view/LoginForm.fxml");
    }


    private void registerViaStm() {
        String fullName = txtFullName.getText().trim();
        String user = txtUser.getText().trim();
        String email = txtEmail.getText().trim();
        String pass = txtPass.getText().trim();
        String conPass = txtConPass.getText().trim();

        boolean validFull = false;
        boolean validUser = false;
        boolean validEmail = false;
        boolean validPass = false;
        boolean validConPass = false;

        if (conPass.isEmpty()) {
            txtConPass.requestFocus();
            lblConPass.setVisible(true);
        } else if (!conPass.equals(pass)) {
            txtConPass.requestFocus();
            txtConPass.clear();
            lblConPass.setText("Password is not matched");
        } else {
            validConPass = true;
            lblConPass.setVisible(false);
        }

        if (pass.isEmpty()) {
            txtPass.requestFocus();
            lblPass.setVisible(true);
        } else if ((pass.length()) < 8) {
            txtPass.requestFocus();
            txtPass.clear();
            lblPass.setText("Enter 8 digits ");
        } else {
            validPass = true;
            lblPass.setVisible(false);
        }

        if (email.isEmpty()) {
            txtEmail.requestFocus();
            lblEmail.setVisible(true);
        } else if (!email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            txtEmail.requestFocus();
            txtEmail.clear();
            lblEmail.setText("Enter valid email :example@gmail.com");
        } else {
            validEmail = true;
            lblEmail.setVisible(false);
        }

        if (user.isEmpty()) {
            txtUser.requestFocus();
            lblUser.setVisible(true);
        } else {
            validUser = true;
            lblUser.setVisible(false);
        }

        if (fullName.isEmpty()) {
            txtFullName.requestFocus();
            lblFullName.setVisible(true);
        } else {
            validFull = true;
            lblFullName.setVisible(false);
        }

        if (validFull && validUser && validEmail && validPass && validConPass) {
            try {
                Connection con = DBConnection.getInstance().getConnection();
                PreparedStatement stm = con.prepareStatement("Insert into register (fullname,username,email,password) values (?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);
                stm.setObject(1, fullName);
                stm.setObject(2, user);
                stm.setObject(3, email);
                stm.setObject(4, pass);


                if (stm.executeUpdate() > 0) {

                    txtFullName.setDisable(true);
                    txtUser.setDisable(true);
                    txtEmail.setDisable(true);
                    txtPass.setDisable(true);
                    txtConPass.setDisable(true);
                    btnRegister.setDisable(true);

                    ResultSet generatedKeys = stm.getGeneratedKeys();

                    if (generatedKeys.next()) {
                        txtAcc.setText(generateRegNumber(generatedKeys.getInt(1)));
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Something Went Wrong !").show();
                    }

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Registered Successfully !", ButtonType.OK);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        navigation("/lk/ijse/dep/view/LoginForm.fxml");
                    }

                } else {
                    new Alert(Alert.AlertType.ERROR, "Something Went Wrong !").show();
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong !").show();
            }
        }
    }

    private String generateRegNumber(int reg) {
        return String.format("Reg-%1$04d", reg);
    }

    private void navigation(String s) throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource(s))));
        stage.centerOnScreen();
        stage.setResizable(false);
    }
}

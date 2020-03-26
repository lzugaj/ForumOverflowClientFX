package sample.controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController {

    @FXML
    private void showRegistrationFormHandler(ActionEvent actionEvent) {
        try {
            loadRegistrationWindow();
        } catch (Exception e) {
            System.out.println("Cannot load registration window");
            e.printStackTrace();
        }
    }

    private void loadRegistrationWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/registration.fxml"));
        Parent parent = fxmlLoader.load();
        createStageContainer(parent);
    }

    private void createStageContainer(Parent parent) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Registration");
        stage.setScene(new Scene(parent));
        stage.show();
    }
}

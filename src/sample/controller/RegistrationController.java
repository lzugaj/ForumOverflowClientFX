package sample.controller;

import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.config.RestTemplateConfig;
import sample.dialog.ErrorDialog;
import sample.dialog.WarningDialog;
import sample.model.User;
import sample.utils.constants.AppConstants;
import sample.utils.constants.ViewConstants;
import sample.utils.message.ErrorMessage;
import sample.utils.message.WarningMessage;

/**
 * Created by lzugaj on Thursday, March 2020
 */

public class RegistrationController {

    private static final String SAVE_USER_URL = "http://localhost:8080/forum-overflow/api/user";

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void signUpActionHandler() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        User newUser = new User(firstName, lastName, username, email, password);
        if (isNotEmpty(firstName) && isNotEmpty(lastName) && isNotEmpty(email) && isNotEmpty(username) && isNotEmpty(password)) {
            signUpProcess(newUser);
        } else {
            showWarningDialog();
        }
    }

    private void signUpProcess(User newUser) {
        RestTemplate restTemplate = restTemplateConfig();
        try {
            User user = saveUser(restTemplate, newUser);
            showHomePageActionHandler(user);
        } catch (HttpClientErrorException.BadRequest | IOException e) {
            if (e instanceof HttpClientErrorException.BadRequest) {
                ErrorDialog.showErrorDialog(ErrorMessage.SIGN_IN_PASSWORD_INCORRECT);
            } else {
                ErrorDialog.showErrorDialog(ErrorMessage.SOMETHING_WENT_WRONG);
            }
        }
    }

    private boolean isNotEmpty(String value) {
        return value.length() > 0;
    }

    private RestTemplate restTemplateConfig() {
        return RestTemplateConfig.config();
    }

    private User saveUser(RestTemplate restTemplate, User newUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE));
        HttpEntity<?> request = new HttpEntity<Object>(newUser, headers);
        ResponseEntity<User> responseEntity = restTemplate.exchange(SAVE_USER_URL, HttpMethod.POST, request, User.class);
        return responseEntity.getBody();
    }

    private void showHomePageActionHandler(User searchedUser) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewConstants.HOME_VIEW));
        Parent parent = fxmlLoader.load();
        createStageContainer(parent);
        transferUserToHomeController(fxmlLoader, searchedUser);
    }

    private void createStageContainer(Parent parent) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle(AppConstants.FORUM_OVERFLOW);
        stage.setScene(new Scene(parent));
        stage.show();
    }

    private void transferUserToHomeController(FXMLLoader fxmlLoader, User searchedUser) {
        HomeController homeController = fxmlLoader.getController();
        homeController.transferUser(searchedUser);
    }

    private void showWarningDialog() {
        WarningDialog.showWarningDialog(WarningMessage.SIGN_UP_MISSING_VALUES);
    }
}

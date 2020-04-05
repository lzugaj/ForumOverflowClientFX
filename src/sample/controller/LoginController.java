package sample.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.config.RestTemplateConfig;
import sample.controller.container.StageContainer;
import sample.dialog.Dialog;
import sample.dialog.DialogFactory;
import sample.model.User;
import sample.utils.constants.AppConstants;
import sample.utils.constants.ViewConstants;
import sample.utils.message.ErrorMessage;
import sample.utils.message.WarningMessage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private DialogFactory dialogFactory = new DialogFactory();

    @FXML
    private void signInActionHandler() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (isNotEmpty(username) && isNotEmpty(password)) {
            signInProcess(username, password);
        } else {
            showWarningDialog();
        }
    }

    private void signInProcess(String username, String password) {
        RestTemplate restTemplate = restTemplateConfig();
        try {
            String searchedUserURL = "http://localhost:8090/forum-overflow/api/user/search/username/" + username + "/password/" + password;
            User searchedUser = findUserByCredentials(restTemplate, searchedUserURL);
            showHomePageActionHandler(searchedUser);
        } catch (IOException | HttpClientErrorException.BadRequest | HttpClientErrorException.NotFound e) {
            if (e instanceof HttpClientErrorException.BadRequest) {
                Dialog errorDialog = dialogFactory.getAlertType(AppConstants.ERROR_DIALOG);
                errorDialog.show(ErrorMessage.SIGN_IN_PASSWORD_INCORRECT);
            } else if (e instanceof HttpClientErrorException.NotFound) {
                Dialog warningDialog = dialogFactory.getAlertType(AppConstants.WARNING_DIALOG);
                warningDialog.show(WarningMessage.SIGN_IN_USER_NOT_FOUND);
            } else {
                Dialog errorDialog = dialogFactory.getAlertType(AppConstants.ERROR_DIALOG);
                errorDialog.show(ErrorMessage.SOMETHING_WENT_WRONG);
            }
        }
    }

    private RestTemplate restTemplateConfig() {
        return RestTemplateConfig.config();
    }

    private User findUserByCredentials(RestTemplate restTemplate, String searchedUserURL) {
        ResponseEntity<User> response = restTemplate.getForEntity(searchedUserURL, User.class);
        return response.getBody();
    }

    private void showHomePageActionHandler(User searchedUser) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewConstants.HOME_VIEW));
        Parent parent = fxmlLoader.load();
        StageContainer.create(parent);
        transferUserToHomeController(fxmlLoader, searchedUser);
    }

    private void transferUserToHomeController(FXMLLoader fxmlLoader, User searchedUser) {
        HomeController homeController = fxmlLoader.getController();
        homeController.transferUser(searchedUser);
    }

    private void showWarningDialog() {
        Dialog warningDialog = dialogFactory.getAlertType(AppConstants.WARNING_DIALOG);
        warningDialog.show(WarningMessage.MISSING_VALUES);
    }

    private boolean isNotEmpty(String value) {
        return value.length() > 0;
    }

    @FXML
    private void showRegistrationPageActionHandler() {
        try {
            loadRegistrationWindow();
        } catch (Exception e) {
            Dialog errorDialog = dialogFactory.getAlertType(AppConstants.ERROR_DIALOG);
            errorDialog.show(ErrorMessage.SOMETHING_WENT_WRONG);
        }
    }

    private void loadRegistrationWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewConstants.REGISTRATION_VIEW));
        Parent parent = fxmlLoader.load();
        StageContainer.create(parent);
    }
}
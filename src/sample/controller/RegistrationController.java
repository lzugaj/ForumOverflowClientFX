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

/**
 * Created by lzugaj on Thursday, March 2020
 */

public class RegistrationController {

    private static final String SAVE_USER_URL = "http://localhost:8090/forum-overflow/api/user";

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

    private DialogFactory dialogFactory = new DialogFactory();

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
            Dialog errorDialog = dialogFactory.getAlertType(AppConstants.ERROR_DIALOG);
            if (e instanceof HttpClientErrorException.BadRequest) {
                errorDialog.show(ErrorMessage.SIGN_IN_PASSWORD_INCORRECT);
            } else {
                errorDialog.show(ErrorMessage.SOMETHING_WENT_WRONG);
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
}

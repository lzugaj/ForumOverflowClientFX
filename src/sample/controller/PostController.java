package sample.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import sample.config.RestTemplateConfig;
import sample.controller.container.StageContainer;
import sample.dialog.Dialog;
import sample.dialog.DialogFactory;
import sample.model.Category;
import sample.model.Post;
import sample.model.User;
import sample.utils.constants.AppConstants;
import sample.utils.constants.ViewConstants;
import sample.utils.message.ErrorMessage;
import sample.utils.message.SuccessMessage;
import sample.utils.message.WarningMessage;

/**
 * Created by lzugaj on Wednesday, April 2020
 */

public class PostController {

    public static final String GET_ALL_CATEGORIES_URL = "http://localhost:8090/forum-overflow/api/v1/category";

    private static final String SAVE_POST_URL = "http://localhost:8090/forum-overflow/api/v1/post";

    @FXML
    private TextArea titleTextArea;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    private User currentUser = new User();

    private DialogFactory dialogFactory = new DialogFactory();

    public void transferUser(User loggedInUser) {
        currentUser = loggedInUser;

        List<Category> categories = fetchAllCategories();
        List<String> categoryNames = mapCategoryNames(categories);
        categoryChoiceBox.setItems(FXCollections.observableArrayList(categoryNames));
    }

    private List<Category> fetchAllCategories() {
        RestTemplate restTemplate = restTemplateConfig();
        return findAllCategories(restTemplate);
    }

    private RestTemplate restTemplateConfig() {
        return RestTemplateConfig.config();
    }

    private List<Category> findAllCategories(RestTemplate restTemplate) {
        ResponseEntity<List<Category>> responseEntity = restTemplate.exchange(GET_ALL_CATEGORIES_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Category>>() {});
        return responseEntity.getBody();
    }
    private List<String> mapCategoryNames(List<Category> categories) {
        return categories.stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    @FXML
    private void savePostActionHandler() {
        String title = titleTextArea.getText();
        String description = descriptionTextArea.getText();
        String category = categoryChoiceBox.getValue();
        Category searchedCategory = fetchSearchedCategory(category);
        if (isNotEmpty(title) && isNotEmpty(description) && isNotNull(category)) {
            Post newPost = new Post(currentUser, searchedCategory, title, description);
            savingPostProcess(newPost);
        } else {
            showWarningDialog();
        }
    }

    private Category fetchSearchedCategory(String searchedCategory) {
        List<Category> categories = fetchAllCategories();
        return categories.stream()
                .filter(category -> category.getName().equals(searchedCategory))
                .findFirst()
                .orElse(null);
    }

    private boolean isNotEmpty(String value) {
        return value.length() > 0;
    }

    private boolean isNotNull(String value) {
        return value != null;
    }

    private void savingPostProcess(Post post) {
        RestTemplate restTemplate = restTemplateConfig();
        try {
            savePost(restTemplate, post);
            showSuccessDialog();
        } catch (HttpClientErrorException.BadRequest e) {
            Dialog errorDialog = dialogFactory.getAlertType(AppConstants.ERROR_DIALOG);
            errorDialog.show(ErrorMessage.SIGN_IN_PASSWORD_INCORRECT);
        }
    }

    private void showSuccessDialog() {
        Dialog successDialog = dialogFactory.getAlertType(AppConstants.SUCCESS_DIALOG);
        successDialog.show(SuccessMessage.SUCCESSFULLY_CREATED_POST);
    }

    private void savePost(RestTemplate restTemplate, Post post) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE));
        HttpEntity<?> request = new HttpEntity<>(post, headers);
        restTemplate.exchange(SAVE_POST_URL, HttpMethod.POST, request, Post.class);
    }

    private void showWarningDialog() {
        Dialog warningDialog = dialogFactory.getAlertType(AppConstants.WARNING_DIALOG);
        warningDialog.show(WarningMessage.MISSING_VALUES);
    }

    @FXML
    private void loadHomePageActionHandler() {
        try {
            User user = currentUser;
            loadHomePageWindow(user);
        } catch (Exception e) {
            Dialog errorDialog = dialogFactory.getAlertType(AppConstants.ERROR_DIALOG);
            errorDialog.show(ErrorMessage.SOMETHING_WENT_WRONG);
        }
    }

    private void loadHomePageWindow(User user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewConstants.HOME_VIEW));
        Parent parent = fxmlLoader.load();
        StageContainer.create(parent);
        transferInfoToHomeController(fxmlLoader, user);
    }

    private void transferInfoToHomeController(FXMLLoader fxmlLoader, User user) {
        HomeController homeController = fxmlLoader.getController();
        homeController.transferUser(user);
    }
}

package sample.controller;

import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import sample.config.RestTemplateConfig;
import sample.dialog.Dialog;
import sample.dialog.DialogFactory;
import sample.model.Category;
import sample.model.Post;
import sample.model.User;
import sample.utils.constants.AppConstants;
import sample.utils.message.ErrorMessage;
import sample.utils.message.SuccessMessage;
import sample.utils.message.WarningMessage;

/**
 * Created by lzugaj on Wednesday, April 2020
 */

public class PostController {

    public static final String BASE_CATEGORY_URL = "http://localhost:8090/forum-overflow/api/category";

    private static final String SAVE_POST_URL = "http://localhost:8090/forum-overflow/api/post";

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
        ResponseEntity<List<Category>> responseEntity = restTemplate.exchange(BASE_CATEGORY_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Category>>() {});
        return responseEntity.getBody();
    }
    private List<String> mapCategoryNames(List<Category> categories) {
        List<String> names = new ArrayList<>();
        for (Category category : categories) {
            names.add(category.getName());
        }

        return names;
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
        for (Category category : categories) {
            if (category.getName().equals(searchedCategory)) {
                return category;
            }
        }

        return null;
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
}

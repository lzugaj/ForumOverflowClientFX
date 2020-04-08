package sample.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import sample.config.RestTemplateConfig;
import sample.controller.container.StageContainer;
import sample.dialog.Dialog;
import sample.dialog.DialogFactory;
import sample.dialog.type.SuccessDialog;
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

public class UserProfilePostDetailsController {

    private static final String DELETE_POST_BY_ID_URL = "http://localhost:8090/forum-overflow/api/v1/post/";

    private static final String GET_ALL_CATEGORIES_URL = "http://localhost:8090/forum-overflow/api/v1/category";

    private static final String UPDATE_POST_URL = "http://localhost:8090/forum-overflow/api/v1/post/{id}";

    @FXML
    private Label postCreatorLabel;

    @FXML
    private Label postDateLabel;

    @FXML
    private Label postTimeLabel;

    @FXML
    private Label postTitleLabel;

    @FXML
    private Label postDescriptionLabel;

    @FXML
    private Label updatePostTitleLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private TextArea titleTextArea;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Label categoryLabel;

    @FXML
    private Button savePostButton;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    private User user = new User();

    private Post selectedPost = new Post();

    private DialogFactory dialogFactory = new DialogFactory();

    private boolean isShown = true;

    public void transferPost(Post post) {
        user = post.getUser();
        selectedPost = post;

        postCreatorLabel.setText(userCredentialsFormatter(post.getUser()));
        postDateLabel.setText(postDateFormatter(post.getCreatedDate()));
        postTimeLabel.setText(postTimeFormatter(post.getCreatedDate()));
        postTitleLabel.setText(post.getTitle());
        postDescriptionLabel.setText(post.getDescription());

        setVariablesVisibility(false);
    }

    private String userCredentialsFormatter(User user) {
        return user.getFirstName() + " " + user.getLastName() + "(" + user.getUsername() + ")";
    }

    private String postDateFormatter(Date postCreatedDate) {
        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(postCreatedDate);
    }

    private String postTimeFormatter(Date postCreatedTime) {
        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(postCreatedTime);
    }

    @FXML
    private void deletePostActionHandler() {
        Long postId = selectedPost.getId();
        deletePostProcess(postId);
    }

    private void deletePostProcess(Long postId) {
        RestTemplate restTemplate = restTemplateConfig();
        try {
            showAlertAreYouReallyWantToDeleteThisPost(restTemplate, postId);
        } catch (Exception e) {
            Dialog errorDialog = dialogFactory.getAlertType(AppConstants.ERROR_DIALOG);
            errorDialog.show(ErrorMessage.SOMETHING_WENT_WRONG);
        }
    }

    private void showAlertAreYouReallyWantToDeleteThisPost(RestTemplate restTemplate, Long postId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(WarningMessage.WARNING);
        alert.setHeaderText("Are you sure you want to delete Post with id: " + postId);
        alert.setResizable(false);
        alert.showAndWait();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            deletePostById(restTemplate, postId);
            loadUserProfileAndMessageActionHandler();
        }
    }

    private RestTemplate restTemplateConfig() {
        return RestTemplateConfig.config();
    }

    private void deletePostById(RestTemplate restTemplate, Long postId) {
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(postId));
        restTemplate.delete (DELETE_POST_BY_ID_URL + postId, params);
    }

    private void loadUserProfileAndMessageActionHandler() {
        try {
            loadUserProfileAndMessageWindow(user, selectedPost);
        } catch (Exception e) {
            Dialog errorDialog = dialogFactory.getAlertType(AppConstants.ERROR_DIALOG);
            errorDialog.show(ErrorMessage.SOMETHING_WENT_WRONG);
        }
    }

    private void loadUserProfileAndMessageWindow(User user, Post selectedPost) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewConstants.USER_PROFILE_VIEW));
        Parent parent = fxmlLoader.load();
        StageContainer.create(parent);
        transferInfoToUserProfileController(fxmlLoader, user, selectedPost);
    }

    private void transferInfoToUserProfileController(FXMLLoader fxmlLoader, User user, Post selectedPost) {
        UserProfileController userProfileController = fxmlLoader.getController();
        userProfileController.transferUserWithMessage(user, selectedPost);
    }

    @FXML
    private void showPostUpdateFormActionHandler() {
        if (isShown) {
            setVariablesVisibility(true);

            List<Category> categories = fetchAllCategories();
            List<String> categoryNames = mapCategoryNames(categories);
            categoryChoiceBox.setItems(FXCollections.observableArrayList(categoryNames));
            isShown = false;
        } else {
            setVariablesVisibility(false);
            isShown = true;
        }
    }

    private void setVariablesVisibility(boolean show) {
        updatePostTitleLabel.setVisible(show);
        titleLabel.setVisible(show);
        descriptionLabel.setVisible(show);
        categoryLabel.setVisible(show);
        savePostButton.setVisible(show);
        titleTextArea.setVisible(show);
        descriptionTextArea.setVisible(show);
        categoryChoiceBox.setVisible(show);
    }

    @FXML
    private void updatePostActionHandler() {
        String title = titleTextArea.getText();
        String description = descriptionTextArea.getText();
        String category = categoryChoiceBox.getValue();
        Category searchedCategory = fetchSearchedCategory(category);
        if (isNotEmpty(title) && isNotEmpty(description) && isNotNull(category)) {
            RestTemplate restTemplate = restTemplateConfig();
            Post newPost = new Post(user, searchedCategory, title, description);
            HttpStatus httpStatus = updatingPostProcess(restTemplate, newPost, selectedPost.getId());
            if (httpStatus.is2xxSuccessful()) {
                postDateLabel.setText(postDateFormatter(new Date()));
                postTimeLabel.setText(postTimeFormatter(new Date()));
                postTitleLabel.setText(newPost.getTitle());
                postDescriptionLabel.setText(newPost.getDescription());

                showSuccessMessage();
            } else {
                showErrorDialog();
            }
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

    private List<Category> fetchAllCategories() {
        RestTemplate restTemplate = restTemplateConfig();
        return findAllCategories(restTemplate);
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

    private HttpStatus updatingPostProcess(RestTemplate restTemplate, Post post, Long postId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(post, headers);
        ResponseEntity<Post> response = restTemplate.exchange(UPDATE_POST_URL, HttpMethod.PUT, entity, Post.class, postId);
        return response.getStatusCode();
    }

    private void showSuccessMessage() {
        Dialog successDialog = dialogFactory.getAlertType(AppConstants.SUCCESS_DIALOG);
        successDialog.show(SuccessMessage.SUCCESSFULLY_UPDATED_POST);
    }

    private void showWarningDialog() {
        Dialog warningDialog = dialogFactory.getAlertType(AppConstants.WARNING_DIALOG);
        warningDialog.show(WarningMessage.MISSING_VALUES);
    }

    private void showErrorDialog() {
        Dialog errorDialog = dialogFactory.getAlertType(AppConstants.ERROR_DIALOG);
        errorDialog.show(ErrorMessage.SOMETHING_WENT_WRONG);
    }

    @FXML
    private void loadUserProfileActionHandler() {
        try {
            loadUserProfileWindow(user);
        } catch (Exception e) {
            Dialog errorDialog = dialogFactory.getAlertType(AppConstants.ERROR_DIALOG);
            errorDialog.show(ErrorMessage.SOMETHING_WENT_WRONG);
        }
    }

    private void loadUserProfileWindow(User user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewConstants.USER_PROFILE_VIEW));
        Parent parent = fxmlLoader.load();
        StageContainer.create(parent);
        transferInfoToHomeController(fxmlLoader, user);
    }

    private void transferInfoToHomeController(FXMLLoader fxmlLoader, User user) {
        UserProfileController userProfileController = fxmlLoader.getController();
        userProfileController.transferUser(user);
    }
}

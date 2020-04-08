package sample.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import sample.config.RestTemplateConfig;
import sample.controller.container.StageContainer;
import sample.dialog.Dialog;
import sample.dialog.DialogFactory;
import sample.model.Post;
import sample.model.User;
import sample.utils.constants.AppConstants;
import sample.utils.constants.ViewConstants;
import sample.utils.message.ErrorMessage;

/**
 * Created by lzugaj on Sunday, April 2020
 */

public class UserProfileController {

    public static final String GET_ALL_POSTS_URL = "http://localhost:8090/forum-overflow/api/v1/post";

    @FXML
    private Label userProfileNameLabel;

    @FXML
    private Label userEmailLabel;

    @FXML
    private ListView<Post> userPostsListView;

    private User currentUser = new User();

    private DialogFactory dialogFactory = new DialogFactory();

    public void transferUser(User user) {
        currentUser = user;

        userProfileNameLabel.setText(userCredentialsFormatter(user));
        userEmailLabel.setText(user.getEmail());
        fetchAllUserPosts(user);
    }

    private String userCredentialsFormatter(User user) {
        return user.getFirstName() + " " + user.getLastName() + "(" + user.getUsername() + ")";
    }

    private void fetchAllUserPosts(User user) {
        RestTemplate restTemplate = restTemplateConfig();
        try {
            List<Post> posts = findAllPosts(restTemplate);
            fillPostsToListView(posts, user);
        } catch (Exception e) {
            Dialog errorDialog = dialogFactory.getAlertType(AppConstants.ERROR_DIALOG);
            errorDialog.show(ErrorMessage.SOMETHING_WENT_WRONG);
        }
    }

    private RestTemplate restTemplateConfig() {
        return RestTemplateConfig.config();
    }

    private List<Post> findAllPosts(RestTemplate restTemplate) {
        ResponseEntity<List<Post>> responseEntity = restTemplate.exchange(GET_ALL_POSTS_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Post>>() {});
        return responseEntity.getBody();
    }

    private void fillPostsToListView(List<Post> posts, User user) {
        List<Post> userOnlyPosts = findUserPosts(posts, user);
        Collections.sort(userOnlyPosts);
        ObservableList<Post> data = FXCollections.observableArrayList();
        data.addAll(userOnlyPosts);
        userPostsListView.setItems(data);
    }

    private List<Post> findUserPosts(List<Post> posts, User user) {
        return posts.stream()
                .filter(post -> post.getUser().getUsername().equals(user.getUsername()))
                .collect(Collectors.toList());
    }

    @FXML
    private void loadHomePageActionHandler() {
        try {
            loadHomePageWindow(currentUser);
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

package sample.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
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
 * Created by lzugaj on Friday, March 2020
 */

public class HomeController {

    public static final String BASE_URL = "http://localhost:8090/forum-overflow/api/post";

    @FXML
    private Menu menu;

    @FXML
    private ListView<Post> postsListView;

    private User user = new User();

    private DialogFactory dialogFactory = new DialogFactory();

    @FXML
    private void refreshPostListActionHandler() {
        fetchAllPosts();
    }

    public void transferUser(User searchedUser) {
        menu.setText(getUserName(searchedUser));
        user = searchedUser;

        fetchAllPosts();
    }

    private String getUserName(User searchedUser) {
        return searchedUser.getFirstName() + " " + searchedUser.getLastName();
    }

    private void fetchAllPosts() {
        RestTemplate restTemplate = restTemplateConfig();
        try {
            List<Post> posts = findAllPosts(restTemplate);
            fillPostsToListView(posts);
        } catch (Exception e) {
            Dialog errorDialog = dialogFactory.getAlertType(AppConstants.ERROR_DIALOG);
            errorDialog.show(ErrorMessage.SOMETHING_WENT_WRONG);
        }
    }

    private RestTemplate restTemplateConfig() {
        return RestTemplateConfig.config();
    }

    private List<Post> findAllPosts(RestTemplate restTemplate) {
        ResponseEntity<List<Post>> responseEntity = restTemplate.exchange(BASE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Post>>() {});
        return responseEntity.getBody();
    }

    private void fillPostsToListView(List<Post> posts) {
        ObservableList<Post> data = FXCollections.observableArrayList();
        data.addAll(posts);
        postsListView.setItems(data);
    }

    @FXML
    private void showAddingPostPageActionHandler() {
        try {
             loadAddingPostWindow(user);
        } catch (Exception e) {
            Dialog errorDialog = dialogFactory.getAlertType(AppConstants.ERROR_DIALOG);
            errorDialog.show(ErrorMessage.SOMETHING_WENT_WRONG);
        }
    }

    private void loadAddingPostWindow(User user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewConstants.POST_VIEW));
        Parent parent = fxmlLoader.load();
        StageContainer.create(parent);
        transferUserToPostController(fxmlLoader, user);
    }

    private void transferUserToPostController(FXMLLoader fxmlLoader, User loggedInUser) {
        PostController postController = fxmlLoader.getController();
        postController.transferUser(loggedInUser);
    }
}

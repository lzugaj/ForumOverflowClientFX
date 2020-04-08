package sample.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
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

public class PostDetailsController {

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

    private User user = new User();

    private DialogFactory dialogFactory = new DialogFactory();

    public void transferPost(Post post) {
        user = post.getUser();

        postCreatorLabel.setText(userCredentialsFormatter(post.getUser()));
        postDateLabel.setText(postDateFormatter(post.getCreatedDate()));
        postTimeLabel.setText(postTimeFormatter(post.getCreatedDate()));
        postTitleLabel.setText(post.getTitle());
        postDescriptionLabel.setText(post.getDescription());
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
    private void loadHomePageActionHandler() {
        try {
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

package sample.controller.cell;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import sample.model.Post;
import sample.model.User;
import sample.utils.constants.ViewConstants;

/**
 * Created by lzugaj on Wednesday, April 2020
 */

public class PostCell extends ListCell<Post> {

    @FXML
    private Label postCreatorLabel;

    @FXML
    private Label postCreatedDateLabel;

    @FXML
    private Label postTitleLabel;

    @FXML
    private Label postDescriptionLabel;

    public PostCell() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewConstants.POST_CELL_VIEW));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Post item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            postCreatorLabel.setText(formatUserCredentials(item.getUser()));
            postCreatedDateLabel.setText(formatDate(item.getCreatedDate()));
            postTitleLabel.setText(item.getTitle());
            postDescriptionLabel.setText(item.getDescription());

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    private String formatUserCredentials(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    private String formatDate(Date createdDate) {
        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(createdDate);
    }
}

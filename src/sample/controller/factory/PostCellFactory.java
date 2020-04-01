package sample.controller.factory;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import sample.controller.cell.PostCell;
import sample.model.Post;

/**
 * Created by lzugaj on Wednesday, April 2020
 */

public class PostCellFactory implements Callback<ListView<Post>, ListCell<Post>> {

    @Override
    public ListCell<Post> call(ListView<Post> param) {
        return new PostCell();
    }
}

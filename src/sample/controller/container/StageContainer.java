package sample.controller.container;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.utils.constants.AppConstants;

/**
 * Created by lzugaj on Friday, April 2020
 */

public abstract class StageContainer {


    private StageContainer() {
        // Private constructor
    }

    public static void create(Parent parent) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle(AppConstants.FORUM_OVERFLOW);
        stage.setScene(new Scene(parent));
        stage.show();
    }
}

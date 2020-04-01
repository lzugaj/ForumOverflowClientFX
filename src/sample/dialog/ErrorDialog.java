package sample.dialog;

import javafx.scene.control.Alert;
import sample.utils.message.ErrorMessage;

/**
 * Created by lzugaj on Friday, March 2020
 */

public abstract class ErrorDialog {

    private ErrorDialog() {
        // Private constructor
    }

    public static void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ErrorMessage.ERROR);
        alert.setHeaderText(errorMessage);
        alert.show();
    }
}

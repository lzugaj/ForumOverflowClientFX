package sample.dialog.type;

import javafx.scene.control.Alert;
import sample.dialog.Dialog;
import sample.utils.message.ErrorMessage;

/**
 * Created by lzugaj on Friday, March 2020
 */

public class ErrorDialog implements Dialog {

    @Override
    public void show(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ErrorMessage.ERROR);
        alert.setHeaderText(message);
        alert.show();
    }
}

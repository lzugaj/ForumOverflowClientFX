package sample.dialog.type;

import javafx.scene.control.Alert;
import sample.dialog.Dialog;
import sample.utils.message.SuccessMessage;

/**
 * Created by lzugaj on Friday, April 2020
 */

public class SuccessDialog implements Dialog {

    public void show(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(SuccessMessage.SUCCESS);
        alert.setHeaderText(message);
        alert.show();
    }
}

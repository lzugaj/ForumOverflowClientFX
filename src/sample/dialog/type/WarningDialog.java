package sample.dialog.type;

import javafx.scene.control.Alert;
import sample.dialog.Dialog;
import sample.utils.message.WarningMessage;

/**
 * Created by lzugaj on Friday, March 2020
 */

public class WarningDialog implements Dialog {

    @Override
    public void show(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(WarningMessage.WARNING);
        alert.setHeaderText(message);
        alert.show();
    }
}

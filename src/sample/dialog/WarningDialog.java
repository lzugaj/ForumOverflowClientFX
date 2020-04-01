package sample.dialog;

import javafx.scene.control.Alert;
import sample.utils.message.WarningMessage;

/**
 * Created by lzugaj on Friday, March 2020
 */

public abstract class WarningDialog {

    private WarningDialog() {
        // Private constructor
    }

    public static void showWarningDialog(String warningMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(WarningMessage.WARNING);
        alert.setHeaderText(warningMessage);
        alert.show();
    }
}

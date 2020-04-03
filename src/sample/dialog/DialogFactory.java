package sample.dialog;

import sample.dialog.type.ErrorDialog;
import sample.dialog.type.SuccessDialog;
import sample.dialog.type.WarningDialog;
import sample.utils.constants.AppConstants;

/**
 * Created by lzugaj on Friday, April 2020
 */

public class DialogFactory {

    public Dialog getAlertType(String dialogType) {
        switch (dialogType) {
            case AppConstants.WARNING_DIALOG:
                return new WarningDialog();
            case AppConstants.SUCCESS_DIALOG:
                return new SuccessDialog();
            case AppConstants.ERROR_DIALOG:
                return new ErrorDialog();
            default:
                return null;
        }
    }
}

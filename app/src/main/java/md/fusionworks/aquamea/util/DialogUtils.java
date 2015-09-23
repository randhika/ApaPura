package md.fusionworks.aquamea.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by ungvas on 9/23/15.
 */
public class DialogUtils {

    public static void showAlertDialog(Context context, String title, String message) {

        new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText("OK")
                .show();
    }
}

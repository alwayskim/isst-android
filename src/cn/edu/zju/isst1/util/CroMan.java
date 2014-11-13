/**
 *
 */
package cn.edu.zju.isst1.util;

import android.app.Activity;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Crouton管理类
 *
 * @author theasir
 */
public class CroMan {

    public static void showAlert(final Activity activity, final String text) {
        Crouton.showText(activity, text, Style.ALERT);
    }

    public static void showAlert(final Activity activity,
            final int textResourceId) {
        Crouton.showText(activity, textResourceId, Style.ALERT);
    }

    public static void showAlert(final Activity activity, final String text,
            final int duration) {
        Crouton.makeText(activity, text, Style.ALERT)
                .setConfiguration(
                        new Configuration.Builder().setDuration(duration)
                                .build()
                ).show();
    }

    public static void showConfirm(final Activity activity, final String text) {
        Crouton.showText(activity, text, Style.CONFIRM);
    }

    public static void showConfirm(final Activity activity,
            final int textResourceId) {
        Crouton.showText(activity, textResourceId, Style.CONFIRM);
    }

    public static void showConfirm(final Activity activity, final String text,
            final int duration) {
        Crouton.makeText(activity, text, Style.CONFIRM)
                .setConfiguration(
                        new Configuration.Builder().setDuration(duration)
                                .build()
                ).show();
    }

    public static void showInfo(final Activity activity, final String text) {
        Crouton.showText(activity, text, Style.INFO);
    }

    public static void showInfo(final Activity activity,
            final int textResourceId) {
        Crouton.showText(activity, textResourceId, Style.INFO);
    }

    public static void showInfo(final Activity activity, final String text,
            final int duration) {
        Crouton.makeText(activity, text, Style.INFO)
                .setConfiguration(
                        new Configuration.Builder().setDuration(duration)
                                .build()
                ).show();
    }

}

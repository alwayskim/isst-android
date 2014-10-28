/**
 *
 */
package cn.edu.zju.isst.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import cn.edu.zju.isst.util.Lgr;

/**
 * 设置管理类
 *
 * @author theasir
 *         <p/>
 *         TODO WIP
 */
public class CSTSettings {

    private static final String SP_NAME = "ISST";

    private static final String FOR_THE_FIRST_TIME = "for_the_first_time";

    private static final String IS_AUTO_LOGIN = "is_auto_login";

    public static void setForTheFirstTime(boolean isForTheFirstTime,
            Activity activity) {
        writeValueForKey(FOR_THE_FIRST_TIME, isForTheFirstTime, activity);
    }

    public static boolean isForTheFirstTime(Activity activity) {
        return activity.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE)
                .getBoolean(FOR_THE_FIRST_TIME, true);
    }

    public static void setAutoLogin(boolean isAutoLogin, Activity activity) {
        Lgr.i("CSTSettings setAutoLogin: " + isAutoLogin);
        writeValueForKey(IS_AUTO_LOGIN, isAutoLogin, activity);
    }

    public static boolean isAutoLogin(Activity activity) {
        return activity.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE)
                .getBoolean(IS_AUTO_LOGIN, false);
    }

    public static void cleanAllSettings(Activity activity) {
        SharedPreferences sp = activity.getSharedPreferences(SP_NAME,
                Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    private static void writeValueForKey(String key, Object value,
            Activity activity) {
        SharedPreferences sp = activity.getSharedPreferences(SP_NAME,
                Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        }
        editor.commit();
    }

}

/**
 *
 */
package cn.edu.zju.isst1.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author yyy
 */
public class TSUtil {

    private TSUtil() {
    }

    public static String toYMD(long time) {
        if (time > 0) {
            Date date = new Date(time);
            DateFormat df = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
            return df.format(date);
        }
        return "";
    }

    public static String toFull(long time) {
        if (time > 0) {
            Date date = new Date(time);
            DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm",
                    Locale.CHINA);
            return df.format(date);
        }
        return "";
    }

    public static String toMD(long time) {
        if (time > 0) {
            Date date = new Date(time);
            DateFormat df = new SimpleDateFormat("MM.dd", Locale.CHINA);
            return df.format(date);
        }
        return "";
    }

    public static String toHM(long time) {
        if (time > 0) {
            Date date = new Date(time);
            DateFormat df = new SimpleDateFormat("HH:mm", Locale.CHINA);
            return df.format(date);
        }
        return "";
    }

    public static String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }
}

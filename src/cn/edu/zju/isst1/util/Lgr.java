package cn.edu.zju.isst1.util;

import android.util.Log;

/**
 * Log统一管理类
 *
 * @author theasir
 */
public class Lgr {

    private static final String TAG = "CarpeDiem";

    private static boolean isDebuggable;// 是否需要打印log，可以在application的onCreate函数里面初始化

    public static boolean isDebuggable() {
        return isDebuggable;
    }

    public static void setDebuggable(boolean isDebuggable) {
        Lgr.isDebuggable = isDebuggable;
    }

    // 下面四个是默认tag的函数
    public static void v(String msg) {
        if (isDebuggable) {
            Log.v(TAG, "VERBOSE----" + msg);
        }
    }

    public static void d(String msg) {
        if (isDebuggable) {
            Log.d(TAG, "DEBUG----" + msg);
        }
    }

    public static void i(String msg) {
        if (isDebuggable) {
            Log.i(TAG, "INFO----" + msg);
        }
    }

    public static void w(String msg) {
        if (isDebuggable) {
            Log.w(TAG, "WARNING----" + msg);
        }
    }

    public static void e(String msg) {
        if (isDebuggable) {
            Log.e(TAG, "ERROR----" + msg);
        }
    }

    // 下面是传入自定义tag的函数
    public static void v(String tag, String msg) {
        if (isDebuggable) {
            Log.v(tag, "VERBOSE----" + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebuggable) {
            Log.d(tag, "DEBUG----" + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebuggable) {
            Log.i(tag, "INFO----" + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebuggable) {
            Log.w(tag, "WARNING----" + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebuggable) {
            Log.e(tag, "ERROR----" + msg);
        }
    }
}

/**
 *
 */
package cn.edu.zju.isst1.exception;

import android.os.Message;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;

import cn.edu.zju.isst1.util.Lgr;

import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_CLASSCAST;
import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_IO;
import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_JSON;
import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_SOCKETTIMEOUT;
import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_UNKNOWN;

/**
 * @deprecated
 * 异常清除机
 *
 * @author theasir
 */
public class ExceptionWeeder {

    /**
     * 处理异常，目的是传递消息
     *
     * @param e   异常
     * @param msg 消息
     */
    public static void fckException(Exception e, Message msg) {
        Lgr.i("ExceptionWeeder FUCK U Exception type of " + e.getClass());

        if (Lgr.isDebuggable()) {
            e.printStackTrace();
        }

        if (e instanceof SocketTimeoutException) {
            msg.what = EXCEPTION_SOCKETTIMEOUT;
        } else if (e instanceof IOException) {
            msg.what = EXCEPTION_IO;
        } else if (e instanceof JSONException) {
            msg.what = EXCEPTION_JSON;
        } else if (e instanceof ClassCastException) {
            msg.what = EXCEPTION_CLASSCAST;
        } else {
            msg.what = EXCEPTION_UNKNOWN;
        }
    }
}

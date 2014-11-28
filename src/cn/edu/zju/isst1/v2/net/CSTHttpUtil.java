package cn.edu.zju.isst1.v2.net;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.webkit.CookieManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;

import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_CLASSCAST;
import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_IO;
import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_JSON;
import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_SOCKETTIMEOUT;
import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_UNKNOWN;
import static cn.edu.zju.isst1.constant.Constants.HTTPERROR_CLIENTERROR;
import static cn.edu.zju.isst1.constant.Constants.HTTPERROR_SERVERERROR;
import static cn.edu.zju.isst1.constant.Constants.HTTPERROR_UNKNOWN;
import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;

/**
 * Created by i308844 on 7/28/14.
 */
public class CSTHttpUtil {

    public static Map<String, String> getCookiesHeaders(String url) {
        Map<String, String> headers = new HashMap<String, String>();
        CookieManager.getInstance().removeExpiredCookie();
        String cookieString = CookieManager.getInstance().getCookie(url);
        if (!Judge.isNullOrEmpty(cookieString)) {
            headers.put("Cookie", cookieString);
        }
        return headers;
    }

    public static void refreshCookies(String url, Map<String, String> headers) {
        String cookie = headers.get("Set-Cookie");
        if (!Judge.isNullOrEmpty(cookie)) {
            CookieManager.getInstance().setCookie(url, cookie);
        }
    }

    /**
     * 将参数转化为字节流（用于POST请求）
     *
     * @param params 参数
     * @return 字节流
     * @throws java.io.UnsupportedEncodingException 未处理异常
     */
    public static String paramsToString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder sbParams = new StringBuilder();
        if (!Judge.isNullOrEmpty(params)) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!Judge.isNullOrEmpty(entry.getValue())) {
                    sbParams.append(entry.getKey()).append('=');
                    sbParams.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                    sbParams.append('&');
                }
            }
            sbParams.deleteCharAt(sbParams.length() - 1);
        }

        Lgr.i("Params", sbParams.toString());

        return sbParams.toString();
    }


    /**
     * 处理请求异常
     * @param status 请求返回的状态码
     * @param context 所在activity的上下文
     * @return
     */
    public static int dispose(int status, Context context) {
        switch (status) {
            case NETWORK_NOT_CONNECTED:
                CroMan.showAlert((Activity) context, R.string.network_not_connected);
                break;

            case HTTPERROR_UNKNOWN:
            case HTTPERROR_CLIENTERROR:
            case HTTPERROR_SERVERERROR:
                CroMan.showAlert((Activity) context, R.string.http_error);
                break;

            case EXCEPTION_SOCKETTIMEOUT:
                CroMan.showAlert((Activity) context, R.string.exception_socket_timeout);
                break;

            case EXCEPTION_UNKNOWN:
            case EXCEPTION_IO:
            case EXCEPTION_JSON:
            case EXCEPTION_CLASSCAST:
                CroMan.showAlert((Activity) context, R.string.exception);
                break;

            default:
                break;
        }
        return 0;
    }
}

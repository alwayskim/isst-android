package cn.edu.zju.isst.v2.net;

import android.webkit.CookieManager;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst.util.Judge;

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
}

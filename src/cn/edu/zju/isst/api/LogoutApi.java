/**
 *
 */
package cn.edu.zju.isst.api;

import cn.edu.zju.isst.net.RequestListener;

/**
 * @deprecated
 * @author theasir
 */
public class LogoutApi extends CSTApi {

    /**
     * 接口子网址
     */
    private static final String SUB_URL = "/api/logout";

    public static void logout(RequestListener listener) {
        request("GET", SUB_URL, null, listener);
    }
}

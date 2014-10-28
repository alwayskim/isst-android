/**
 *
 */
package cn.edu.zju.isst.api;

import cn.edu.zju.isst.net.RequestListener;

/**
 * @deprecated
 * @author theasir
 */
public class VersionApi extends CSTApi {

    public static final String SUB_URL = "/api/android/version";

    public static void getVersionInfo(RequestListener listener) {
        request("GET", SUB_URL, null, listener);
    }
}

/**
 *
 */
package cn.edu.zju.isst.api;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst.net.RequestListener;

/**
 * @deprecated
 * @author theasir
 */
public class PushMessageApi extends CSTApi {

    private static final String SUB_URL = "/api/messages";

    public static void getMsgList(int page, int pageSize,
            RequestListener listener) {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", "" + page);
        paramsMap.put("pageSize", "" + pageSize);

        request("GET", SUB_URL, paramsMap, listener);
    }
}

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
public class CampusActivityApi extends CSTApi {

    /**
     * 接口子网址
     */
    private static final String SUB_URL = "/api/campus/activities";

    public static void getCampusActivityList(int page, int pageSize,
            String keywords, RequestListener listener) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", "" + page);
        paramsMap.put("pageSize", "" + pageSize);
        paramsMap.put("keywords", keywords);

        request("GET", SUB_URL, paramsMap, listener);
    }

    public static void getCampusActivityDetail(int id, RequestListener listener) {
        request("GET", SUB_URL + "/" + id, null, listener);
    }
}

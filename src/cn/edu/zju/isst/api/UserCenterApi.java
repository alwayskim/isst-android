package cn.edu.zju.isst.api;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst.net.RequestListener;

/**
 * @deprecated
 * @author xyj
 */

public class UserCenterApi extends CSTApi {

    private static final String SUB_URL = "/api/users";

    // api在users里，界面在job
    // 发布内推
    public static void publishRecommend(int id, String title, String content,
            String company, String position, int cityId,
            RequestListener listener) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("id", "" + id);
        paramsMap.put("title", title);
        paramsMap.put("content", content);
        paramsMap.put("company", company);
        paramsMap.put("position", position);
        paramsMap.put("cityId", "" + cityId);

        request("POST", SUB_URL + "/jobs/recommend", paramsMap, listener);
    }

    // 发布的内推,经验列表
    public static void getUserCenterList(UserCenterCategory userCenterCategory,
            int page, int pageSize, RequestListener listener) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", "" + page);
        paramsMap.put("pageSize", "" + pageSize);

        request("GET", SUB_URL + "/" + userCenterCategory.getSubUrl(),
                paramsMap, listener);
    }

    // 发布的活动列表
    public static void getMyPublicActivities(int page, int pageSize,
            RequestListener listener) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", "" + page);
        paramsMap.put("pageSize", "" + pageSize);

        request("GET", SUB_URL + "/activities", paramsMap, listener);
    }

    // 参加的活动列表
    public static void getMyActivities(int page, int pageSize,
            RequestListener listener) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", "" + page);
        paramsMap.put("pageSize", "" + pageSize);

        request("GET", SUB_URL + "/activities/participated", paramsMap, listener);
    }

}

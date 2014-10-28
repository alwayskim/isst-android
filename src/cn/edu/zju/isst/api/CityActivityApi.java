/**
 *
 */
package cn.edu.zju.isst.api;

import android.R.integer;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst.net.RequestListener;

/**
 * @deprecated
 * @author theasir
 */
public class CityActivityApi extends CSTApi {

    private static final String SUB_URL = "/api/cities";

    public static void getCityActivityList(int cityId, int page, int pageSize,
            String keywords, RequestListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_URL).append("/" + cityId).append("/activities");

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", "" + page);
        paramsMap.put("pageSize", "" + pageSize);
        paramsMap.put("keywords", keywords);

        request("GET", sb.toString(), paramsMap, listener);
    }

    public static void getAuditingActivityList(int cityId, int page,
            integer pageSize, RequestListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_URL).append("/" + cityId).append("/activities/auditing");

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", "" + page);
        paramsMap.put("pageSize", "" + pageSize);

        request("GET", sb.toString(), paramsMap, listener);
    }

    public static void getCityActivityDetail(int cityId, int activityId,
            RequestListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_URL).append("/" + cityId).append("/activities")
                .append("/" + activityId);

        request("GET", sb.toString(), null, listener);
    }

    public static void getCityActivityParList(int cityId, int activityId,
            int page, int pageSize, RequestListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_URL).append("/" + cityId).append("/activities")
                .append("/" + activityId).append("/participants");

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", "" + page);
        paramsMap.put("pageSize", "" + pageSize);

        request("GET", sb.toString(), paramsMap, listener);
    }

    public static void auditCityActivity(int cityId, int activityId, RequestListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_URL).append("/" + cityId).append("/activities")
                .append("/" + activityId).append("/audit");

        request("POST", sb.toString(), null, listener);
    }

    public static void participate(int cityId, int activityId, RequestListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_URL).append("/" + cityId).append("/activities")
                .append("/" + activityId).append("/participate");

        request("POST", sb.toString(), null, listener);
    }

    public static void unparticipate(int cityId, int activityId, RequestListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_URL).append("/" + cityId).append("/activities")
                .append("/" + activityId).append("/unparticipate");

        request("POST", sb.toString(), null, listener);
    }
}

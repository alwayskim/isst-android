/**
 *
 */
package cn.edu.zju.isst.api;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst.net.RequestListener;
import cn.edu.zju.isst.util.Lgr;

/**
 *
 * @deprecated
 * 归档接口
 *
 * @author theasir
 *         <p/>
 *         TODO WIP
 */
public class AlumniApi extends CSTApi {

    private static final String SUB_URL = "/api";

    public static void getUserList(int id, String name, int gender, int grade,
            int classId, String major, int cityId, String company,
            RequestListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_URL).append("/alumni");

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("id", "" + id);
        paramsMap.put("name", name);
        paramsMap.put("gender", "" + gender);
        paramsMap.put("grade", "" + grade);
        paramsMap.put("classId", "" + classId);
        paramsMap.put("major", "" + major);
        paramsMap.put("cityId", "" + cityId);
        paramsMap.put("company", company);

        request("GET", sb.toString(), paramsMap, listener);
    }

    public static void getClassesList(RequestListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_URL).append("/classes");

        Lgr.i("yyy --- getClassesList:" + sb.toString());

        request("GET", sb.toString(), null, listener);
    }

    public static void getMajorList(RequestListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_URL).append("/majors");

        Lgr.i("yyy --- getMajorList:" + sb.toString());

        request("GET", sb.toString(), null, listener);
    }

    public static void getCityList(RequestListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_URL).append("/cities");

        Lgr.i("yyy --- getCityList:" + sb.toString());

        request("GET", sb.toString(), null, listener);
    }

    public static void getUser(int id, RequestListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_URL).append("/alumni").append("/" + id);

        Lgr.i("yyy --- getUser:" + sb.toString());

        request("GET", sb.toString(), null, listener);
    }
}

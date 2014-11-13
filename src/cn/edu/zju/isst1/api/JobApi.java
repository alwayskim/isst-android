/**
 *
 */
package cn.edu.zju.isst1.api;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst1.net.RequestListener;

/**
 * @deprecated
 * 归档接口
 *
 * @author theasir
 *         <p/>
 *         TODO WIP
 */
public class JobApi extends CSTApi {

    private static final String SUB_URL = "/api/jobs/";

    /**
     * 获取归档列表
     *
     * @param category 类别
     * @param page     页数
     * @param pageSize 页面大小
     * @param keywords 关键字
     * @param listener 回调对象
     */
    public static void getJobList(JobCategory category, int page, int pageSize,
            String keywords, RequestListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_URL).append("categories/").append(category.getSubUrl());

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", "" + page);
        paramsMap.put("pageSize", "" + pageSize);
        paramsMap.put("keywords", keywords);

        request("GET", sb.toString(), paramsMap, listener);
    }

    /**
     * 获取归档详情
     *
     * @param id       id
     * @param listener 回调对象
     */
    public static void getJobDetail(int id, RequestListener listener) {
        request("GET", SUB_URL + id, null, listener);
    }
}

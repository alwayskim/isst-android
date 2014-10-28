/**
 *
 */
package cn.edu.zju.isst.api;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst.net.RequestListener;

/**
 * @deprecated
 * 归档接口
 *
 * @author xyj
 *         <p/>
 *         TODO WIP
 */
public class JobCommentApi extends CSTApi {

    private static final String SUB_URL = "/api/jobs/";

    /**
     * @param page     页数
     * @param pageSize 页面大小
     */
    public static void getJobCommentList(Integer page, int jobId, Integer pageSize,
            RequestListener listener) {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", page.toString());
        paramsMap.put("pageSize", pageSize.toString());
        request("GET", SUB_URL + jobId + "/comments", paramsMap, listener);
    }

    /**
     * @param content 内容
     */
    public static void sendJobComment(String content, int jobId, RequestListener listener) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("content", content.toString());
        request("POST", SUB_URL + jobId + "/comments", paramsMap, listener);
    }

}

/**
 *
 */
package cn.edu.zju.isst1.v2.archive.net;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst1.api.ArchiveCategory;
import cn.edu.zju.isst1.api.CSTApi;
import cn.edu.zju.isst1.net.RequestListener;
import cn.edu.zju.isst1.util.Lgr;

/**
 * @author theasir
 *         <p/>
 *         TODO WIP
 * @deprecated 归档接口
 */
public class ArchiveApi extends CSTApi {

    private static final String SUB_URL = "/api/archives/";

    /**
     * 获取百科列表
     *
     * @param page     页数
     * @param pageSize 页面大小
     * @param keywords 关键字
     * @param listener 回调对象
     */
    public static void getWikiList(int page, int pageSize,
            String keywords, RequestListener listener) {
        getArchiveList(ArchiveCategory.ENCYCLOPEDIA, page, pageSize, keywords,
                listener);
    }

    /**
     * 获取归档列表
     *
     * @param category 类别
     * @param page     页数
     * @param pageSize 页面大小
     * @param keywords 关键字
     * @param listener 回调对象
     */
    public static void getArchiveList(ArchiveCategory category, int page,
            int pageSize, String keywords, RequestListener listener) {
        Lgr.i("API---getArchiveList----enter");
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
    public static void getArchiveDetail(int id, RequestListener listener) {
        request("GET", SUB_URL + id, null, listener);
    }
}

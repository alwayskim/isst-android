package cn.edu.zju.isst1.v2.event.base;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTResponse;

/**
 * Created by i308844 on 8/25/14.
 */
public class EventRequest extends CSTJsonRequest {

    private String subUrl;

    private int page;

    private int pageSize;

    private String keywords;

    private boolean hasParams = false;

    public EventRequest(int method, String subUrl,
            Map<String, String> params,
            CSTResponse<JSONObject> response) {
        super(method, subUrl, params, response);
        this.subUrl = subUrl;
    }

    @Override
    public String getUrl() {
        if (hasParams) {
            StringBuilder sb = new StringBuilder();
            sb.append("?");
            try {
                if (page > 0) {
                    sb.append("page=").append(URLEncoder.encode("" + page, "UTF-8")).append("&");
                }
                if (pageSize > 0) {
                    sb.append("pageSize=").append(URLEncoder.encode("" + pageSize, "UTF-8"))
                            .append("&");
                }
                if (keywords != null) {
                    sb.append("keywords=").append(URLEncoder.encode(keywords, "UTF-8")).append("&");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.deleteCharAt(sb.length() - 1);

            return super.getUrl() + sb.toString();
        }
        return super.getUrl();
    }

    public EventRequest setPage(int page) {
        this.page = page;
        hasParams = true;
        return this;
    }

    public EventRequest setPageSize(int pageSize) {
        this.pageSize = pageSize;
        hasParams = true;
        return this;
    }

    public EventRequest setKeywords(String keywords) {
        this.keywords = keywords;
        hasParams = true;
        return this;
    }
}

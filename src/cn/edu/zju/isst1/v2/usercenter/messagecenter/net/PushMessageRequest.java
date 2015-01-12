package cn.edu.zju.isst1.v2.usercenter.messagecenter.net;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTResponse;

/**
 * Created by lqynydyxf on 15/1/11.
 */
public class PushMessageRequest extends CSTJsonRequest{
    private String subUrl;

    private int page;

    private int pageSize;

    private String keywords;

    private boolean hasParams = false;

    public PushMessageRequest(int method, String subUrl,
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

    public PushMessageRequest setPage(int page) {
        this.page = page;
        hasParams = true;
        return this;
    }

    public PushMessageRequest setPageSize(int pageSize) {
        this.pageSize = pageSize;
        hasParams = true;
        return this;
    }

    public PushMessageRequest setKeywords(String keywords) {
        this.keywords = keywords;
        hasParams = true;
        return this;
    }
}

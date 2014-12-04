package cn.edu.zju.isst1.v2.net;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import cn.edu.zju.isst1.util.Lgr;

/**
 * Created by i308844 on 8/18/14.
 */
public class CSTJsonRequest extends CSTRequest<JSONObject> {

    private static final String BASE_URL = "http://www.cst.zju.edu.cn/isst";
//    private static final String BASE_URL = "http://10.82.60.35:8080/isst";

    public CSTJsonRequest(int method, String subUrl, Map<String, String> params,
                          CSTResponse<JSONObject> response) {
        super(method, BASE_URL + subUrl, params, response);
        setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            CSTHttpUtil.refreshCookies(BASE_URL, response.headers);
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}

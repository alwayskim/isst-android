package cn.edu.zju.isst1.v2.net;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import cn.edu.zju.isst1.util.Lgr;

/**
 * Created by alwayking on 14/12/2.
 */
public class CSTJsonCommonRequest extends CSTRequest<JSONObject>{
    public CSTJsonCommonRequest(int method, String subUrl, Map<String, String> params,
                          CSTResponse<JSONObject> response) {
        super(method,subUrl, params, response);
        setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Lgr.i(jsonString.toString());
//            CSTHttpUtil.refreshCookies(subU, response.headers);
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}

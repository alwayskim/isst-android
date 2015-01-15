package cn.edu.zju.isst1.v2.net;

import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.JsonRequest;

import org.json.JSONObject;

/**
 * Created by i308844 on 2014/7/22.
 */
public class VolleyImpl {

    private static String TAG_JSON = "json_request";

    public static void requestJsonObject(CSTRequest<JSONObject> jsonRequest, String tag) {

        VolleyRequestManager.getInstance().addToRequestQueue(jsonRequest);
    }

    public static void requestCommonObject(JsonRequest<JSONObject> request, String tag) {

        VolleyRequestManager.getInstance().addToRequestQueue(request);
    }

    public static void imageRequst(String path, ImageView iv) {

        VolleyRequestManager.getInstance().addLoadImageRequest(iv,path);
    }

}

package cn.edu.zju.isst1.v2.splash.net;

import android.content.Context;

import org.json.JSONObject;

import cn.edu.zju.isst1.v2.net.CSTJsonResponse;

/**
 * Created by i308844 on 8/18/14.
 */
public class VersionResponse extends CSTJsonResponse {

    protected VersionResponse(Context context) {
        super(context);
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
    }
}

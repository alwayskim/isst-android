package cn.edu.zju.isst.v2.splash.net;

import org.json.JSONObject;

import android.content.Context;

import cn.edu.zju.isst.v2.net.CSTJsonResponse;

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

package cn.edu.zju.isst1.v2.net;

import android.content.Context;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import cn.edu.zju.isst1.util.Lgr;

/**
 * Created by i308844 on 8/18/14.
 */
public class CSTJsonResponse extends CSTResponse<JSONObject> implements CSTResponseStatusListener{

    protected Context mContext;

    protected CSTJsonResponse(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void onSuccessStatus() {

    }

    @Override
    public Object onErrorStatus(CSTStatusInfo statusInfo) {
        return null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Lgr.i(error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Lgr.i("aaaa");
    }
}

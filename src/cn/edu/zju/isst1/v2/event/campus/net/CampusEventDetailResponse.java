package cn.edu.zju.isst1.v2.event.campus.net;

import android.content.Context;

import org.json.JSONObject;

import cn.edu.zju.isst1.v2.net.CSTJsonResponse;

/**
 * Created by always on 28/08/2014.
 */
public class CampusEventDetailResponse extends CSTJsonResponse{

    protected CampusEventDetailResponse(Context context) {
        super(context);
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
    }
}

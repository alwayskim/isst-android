package cn.edu.zju.isst.v2.event.city.net;

import org.json.JSONObject;

import android.content.Context;

import cn.edu.zju.isst.v2.net.CSTJsonResponse;

/**
 * Created by always on 28/08/2014.
 */
public class CityEventParticipateResponse extends CSTJsonResponse{

    protected CityEventParticipateResponse(Context context) {
        super(context);
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
    }
}

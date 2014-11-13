package cn.edu.zju.isst1.v2.event.city.net;

import android.content.Context;

import org.json.JSONObject;

import cn.edu.zju.isst1.v2.net.CSTJsonResponse;

/**
 * Created by always on 02/09/2014.
 */
public class CityEventParticipantsListResponse extends CSTJsonResponse {

    protected CityEventParticipantsListResponse(Context context) {
        super(context);
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
    }
}

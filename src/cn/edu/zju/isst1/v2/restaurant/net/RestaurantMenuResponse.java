package cn.edu.zju.isst1.v2.restaurant.net;

import android.content.Context;

import org.json.JSONObject;

import cn.edu.zju.isst1.v2.net.CSTJsonResponse;

/**
 * Created by lqynydyxf on 14/10/26.
 */
public class RestaurantMenuResponse extends CSTJsonResponse{

    protected RestaurantMenuResponse(Context context) {
        super(context);
    }
    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
    }
}

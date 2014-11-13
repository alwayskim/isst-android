package cn.edu.zju.isst1.v2.restaurant.net;

import android.content.Context;

import org.json.JSONObject;

import cn.edu.zju.isst1.v2.net.CSTJsonResponse;

/**
 * Created by lqynydyxf on 2014/8/21.
 */
public class RestaurantResponse extends CSTJsonResponse{
    protected RestaurantResponse(Context context) {
        super(context);
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
    }
}

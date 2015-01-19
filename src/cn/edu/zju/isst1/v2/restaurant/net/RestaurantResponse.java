package cn.edu.zju.isst1.v2.restaurant.net;

import android.content.Context;

import org.json.JSONObject;

import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.data.CSTRestaurant;
import cn.edu.zju.isst1.v2.net.CSTJsonResponse;
import cn.edu.zju.isst1.v2.restaurant.data.CSTRestaurantDataDelegate;

/**
 * Created by lqynydyxf on 2014/8/21.
 */
public class RestaurantResponse extends CSTJsonResponse {

    private boolean clearDatabase;

    protected RestaurantResponse(Context context, boolean clearDatabase) {
        super(context);
        this.clearDatabase = clearDatabase;
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
        if (clearDatabase) {
            Lgr.i(response.toString());
            CSTRestaurantDataDelegate.deleteAllRestaurent(mContext);

        }
        CSTRestaurant restaurant = (CSTRestaurant) CSTJsonParser.parseJson(response, new CSTRestaurant());
        for (CSTRestaurant restaurant_demo : restaurant.itemList) {
            CSTRestaurantDataDelegate.saveRestaurant(mContext, restaurant_demo);
        }
    }
}

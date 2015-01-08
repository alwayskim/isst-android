package cn.edu.zju.isst1.v2.event.city.net;

import android.content.Context;

import org.json.JSONObject;

import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.event.city.data.CSTCityEvent;
import cn.edu.zju.isst1.v2.event.city.data.CSTCityEventDataDelegate;
import cn.edu.zju.isst1.v2.net.CSTJsonResponse;

/**
 * Created by always on 25/08/2014.
 */
public class CityEventResponse extends CSTJsonResponse {

    private boolean clearDatabase;

    public CityEventResponse(Context context, boolean clearDatabase) {
        super(context);
        this.clearDatabase = clearDatabase;
    }

    @Override
    public void onResponse(JSONObject response) {
        CSTCityEvent event = (CSTCityEvent) CSTJsonParser
                .parseJson(response, new CSTCityEvent());
        if (clearDatabase) {
            CSTCityEventDataDelegate.deleteAllCityEvent(mContext);
        }
        CSTCityEventDataDelegate.saveCityEventListValues(mContext, event);
    }
}

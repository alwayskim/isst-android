package cn.edu.zju.isst.v2.event.campus.net;

import org.json.JSONObject;

import android.content.Context;

import cn.edu.zju.isst.v2.event.campus.data.CSTCampusEvent;
import cn.edu.zju.isst.v2.event.campus.data.CSTCampusEventDataDelegate;
import cn.edu.zju.isst.v2.data.CSTJsonParser;
import cn.edu.zju.isst.v2.net.CSTJsonResponse;

/**
 * Created by always on 22/08/2014.
 */
public class CampusEventResponse extends CSTJsonResponse {

    private boolean clearDatabase;

    public CampusEventResponse(Context context, boolean clearDatabase) {
        super(context);
        this.clearDatabase = clearDatabase;
    }

    @Override
    public void onResponse(JSONObject response) {
        CSTCampusEvent event = (CSTCampusEvent) CSTJsonParser.parseJson(response, new CSTCampusEvent());
        if (clearDatabase) {
            CSTCampusEventDataDelegate.deleteAllCampusEvent(mContext);
        }
        CSTCampusEventDataDelegate.saveCampusEventList(mContext, event);
    }
}

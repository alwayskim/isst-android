package cn.edu.zju.isst1.v2.usercenter.messagecenter.net;

import android.content.Context;

import org.json.JSONObject;

import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.event.campus.data.CSTCampusEvent;
import cn.edu.zju.isst1.v2.event.campus.data.CSTCampusEventDataDelegate;
import cn.edu.zju.isst1.v2.net.CSTJsonResponse;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.CSTMessage;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.CSTMessageDataDelegate;

/**
 * Created by alwayking on 14/11/30.
 */
public class PushMessageResponse extends CSTJsonResponse{
    private boolean isClearDatabase;

    protected PushMessageResponse(Context context,boolean isClearDatabase) {
        super(context);
        this.isClearDatabase = isClearDatabase;
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
        CSTMessage message = (CSTMessage) CSTJsonParser.parseJson(response, new CSTMessage());
        if (isClearDatabase) {
            CSTMessageDataDelegate.deleteAllMessage(mContext);
        }
        CSTMessageDataDelegate.saveMessageList(mContext, message);
    }
}

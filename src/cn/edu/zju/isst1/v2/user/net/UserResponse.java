package cn.edu.zju.isst1.v2.user.net;

import android.content.Context;

import org.json.JSONObject;

import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.net.CSTJsonResponse;
import cn.edu.zju.isst1.v2.user.data.CSTUser;
import cn.edu.zju.isst1.v2.user.data.CSTUserDataDelegate;

/**
 * Created by theasir on 7/29/14.
 */
public class UserResponse extends CSTJsonResponse {

    private boolean clearDatabase;

    public UserResponse(Context context, boolean clearDatabase) {
        super(context);
        this.clearDatabase = clearDatabase;
    }

    @Override
    public void onResponse(JSONObject response) {
        CSTUser user = (CSTUser) CSTJsonParser.parseJson(response, new CSTUser());
        //TODO must not pass null or empty user
        if (clearDatabase) {
            CSTUserDataDelegate.deleteAllUsers(mContext);
        }
        CSTUserDataDelegate.saveUser(mContext, user);
    }
}

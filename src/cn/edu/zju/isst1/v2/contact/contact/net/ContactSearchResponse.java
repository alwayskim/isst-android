package cn.edu.zju.isst1.v2.contact.contact.net;

import android.content.Context;

import org.json.JSONObject;

import cn.edu.zju.isst1.v2.contact.contact.gui.BaseContactListFragment;
import cn.edu.zju.isst1.v2.net.CSTJsonResponse;

/**
 * Created by alwayking on 14/11/29.
 */
public class ContactSearchResponse extends CSTJsonResponse {
    private boolean clearDatabase;

    public ContactSearchResponse(Context context) {
        super(context);
        this.clearDatabase = clearDatabase;
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
    }
}

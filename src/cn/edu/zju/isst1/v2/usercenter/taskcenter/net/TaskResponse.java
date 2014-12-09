package cn.edu.zju.isst1.v2.usercenter.taskcenter.net;

import android.content.Context;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import cn.edu.zju.isst1.v2.net.CSTJsonResponse;
import cn.edu.zju.isst1.v2.net.CSTResponse;

/**
 * Created by lqynydyxf on 14/12/6.
 */
public class TaskResponse extends CSTJsonResponse {

    protected TaskResponse(Context context) {
        super(context);
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
    }
}

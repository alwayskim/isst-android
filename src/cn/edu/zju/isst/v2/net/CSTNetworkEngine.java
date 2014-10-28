package cn.edu.zju.isst.v2.net;

import org.json.JSONObject;

/**
 * Created by i308844 on 8/18/14.
 */
public class CSTNetworkEngine {

    private static CSTNetworkEngine INSTANCE;

    private CSTNetworkEngine() {

    }

    public static CSTNetworkEngine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CSTNetworkEngine();
        }
        return INSTANCE;
    }

    public synchronized void requestJson(CSTRequest<JSONObject> jsonRequest) {
        VolleyImpl.requestJsonObject(jsonRequest, null);
    }

}

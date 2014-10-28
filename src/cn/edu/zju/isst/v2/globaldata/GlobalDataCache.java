package cn.edu.zju.isst.v2.globaldata;

import org.json.JSONObject;

import android.content.Context;

import cn.edu.zju.isst.util.Lgr;
import cn.edu.zju.isst.v2.net.CSTJsonRequest;
import cn.edu.zju.isst.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst.v2.net.CSTRequest;

/**
 * Created by always on 9/15/2014.
 */
public class GlobalDataCache {

    private static CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private static final String SUB_URL = "/api";

    public static GlobalDataCategory mGlobalDataCategory;

    public static void cacheCityList(Context mContext) {
        Lgr.i("cache", "CITY");
        mGlobalDataCategory = GlobalDataCategory.CITYLIST;
        globalDataCacheRequest(mContext);
    }

    public static void cacheClassList(Context mContext) {
        Lgr.i("cache", "CLASS");
        mGlobalDataCategory = GlobalDataCategory.CLASSLIST;
        globalDataCacheRequest(mContext);
    }

    public static void cacheMajorList(Context mContext) {
        Lgr.i("cache", "MAJOR");
        mGlobalDataCategory = GlobalDataCategory.MAJORLIST;
        globalDataCacheRequest(mContext);
    }

    private static void globalDataCacheRequest(Context mContext) {
        GlobalDataResponse globalDataResponse = new GlobalDataResponse(mContext,
                mGlobalDataCategory) {
            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
            }
        };

        CSTJsonRequest request = new CSTJsonRequest(CSTRequest.Method.GET,
                SUB_URL + mGlobalDataCategory.getSubUrl(), null,
                globalDataResponse);
        mEngine.requestJson(request);
    }
}


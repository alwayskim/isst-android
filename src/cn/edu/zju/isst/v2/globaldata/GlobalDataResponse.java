package cn.edu.zju.isst.v2.globaldata;

import org.json.JSONObject;

import android.content.Context;

import cn.edu.zju.isst.v2.data.CSTJsonParser;
import cn.edu.zju.isst.v2.data.CSTKlass;
import cn.edu.zju.isst.v2.data.CSTMajor;
import cn.edu.zju.isst.v2.globaldata.citylist.CSTCity;
import cn.edu.zju.isst.v2.globaldata.citylist.CSTCityDataDelegate;
import cn.edu.zju.isst.v2.globaldata.classlist.CSTKlassDataDelegate;
import cn.edu.zju.isst.v2.globaldata.majorlist.CSTMajorDataDelegate;
import cn.edu.zju.isst.v2.net.CSTJsonResponse;

/**
 * Created by always on 9/15/2014.
 */
public class GlobalDataResponse extends CSTJsonResponse {

    private GlobalDataCategory mGlobalDataCategory;

    protected GlobalDataResponse(Context context, GlobalDataCategory globalDataCategory) {
        super(context);
        mGlobalDataCategory = globalDataCategory;
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
        switch (mGlobalDataCategory) {
            case CITYLIST:
                CSTCity city = (CSTCity) CSTJsonParser.parseJson(response, new CSTCity());
                CSTCityDataDelegate.saveCityList(mContext, city);
                break;
            case MAJORLIST:
                CSTMajor major = (CSTMajor) CSTJsonParser.parseJson(response, new CSTMajor());
                CSTMajorDataDelegate.saveMajorList(mContext, major);
                break;
            case CLASSLIST:
                CSTKlass klass = (CSTKlass) CSTJsonParser.parseJson(response, new CSTKlass());
                CSTKlassDataDelegate.saveKlassList(mContext, klass);
                break;
        }

    }
}

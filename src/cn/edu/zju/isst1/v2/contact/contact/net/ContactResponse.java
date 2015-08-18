package cn.edu.zju.isst1.v2.contact.contact.net;

import android.content.Context;

import org.json.JSONObject;

import cn.edu.zju.isst1.v2.contact.contact.data.CSTAddressListDataDelegate;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTAlumni;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTAlumniDataDelegate;
import cn.edu.zju.isst1.v2.contact.contact.gui.BaseContactListFragment;
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.net.CSTJsonResponse;

/**
 * Created by tan on 2014/8/25.
 */
public class ContactResponse extends CSTJsonResponse {
    private boolean clearDatabase;
    private BaseContactListFragment.FilterType mFilterType;

    public ContactResponse(Context context, boolean clearDatabase, BaseContactListFragment.FilterType mFilterType) {
        super(context);
        this.clearDatabase = clearDatabase;
        this.mFilterType = mFilterType;
    }

    @Override
    public void onResponse(JSONObject response) {
        if (response != null) {
            CSTAlumni alumni = (CSTAlumni) CSTJsonParser.parseJson(response, new CSTAlumni());
            if (clearDatabase) {
                if (mFilterType == BaseContactListFragment.FilterType.MY_CITY) {
                    CSTAlumniDataDelegate.deleteAllAlumni(mContext);
                } else {
                    CSTAddressListDataDelegate.deleteAllAlumni(mContext);
                }
            }
            if (mFilterType == BaseContactListFragment.FilterType.MY_CITY) {
                CSTAlumniDataDelegate.saveAlumniList(mContext, alumni);
            } else {
                CSTAddressListDataDelegate.saveAlumniList(mContext, alumni);
            }
        }
    }
}

package cn.edu.zju.isst1.v2.archive.net;

import android.content.Context;

import org.json.JSONObject;

import cn.edu.zju.isst1.constant.Constants;
import cn.edu.zju.isst1.v2.archive.data.ArchiveCategory;
import cn.edu.zju.isst1.v2.archive.data.CSTArchive;
import cn.edu.zju.isst1.v2.archive.data.CSTArchiveDataDelegate;
import cn.edu.zju.isst1.v2.archive.data.CSTArchiveProvider;
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.net.CSTJsonResponse;

/**
 * Created by i308844 on 8/25/14.
 */
public class ArchiveResponse extends CSTJsonResponse {

    private boolean isClearDatabase;

    private ArchiveCategory mCategory;

    public ArchiveResponse(Context context, ArchiveCategory category, boolean isClearDatabase) {
        super(context);
        this.mCategory = category;
        this.isClearDatabase = isClearDatabase;
    }

    @Override
    public void onResponse(JSONObject response) {
        CSTArchive archive = (CSTArchive) CSTJsonParser.parseJson(response, new CSTArchive());
        if (archive.getStatusInfo().status != Constants.STATUS_REQUEST_SUCCESS) {
            onErrorStatus(archive.getStatusInfo());
            return;
        }
        if (isClearDatabase) {
            CSTArchiveDataDelegate.delete(mContext,
                    CSTArchiveProvider.Columns.CATEGORY_ID.key + " = ?",
                    new String[]{
                            "" + mCategory.id
                    });
        }
        CSTArchiveDataDelegate.saveArchiveList(mContext, archive);
    }
}

package cn.edu.zju.isst.v2.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.edu.zju.isst.constant.Constants;
import cn.edu.zju.isst.v2.model.CSTDataItem;
import cn.edu.zju.isst.v2.net.CSTStatusInfo;


/**
 * Created by i308844 on 7/31/14.
 */
public class CSTJsonParser {

    public static final String JSON_RESULT_BODY = "body";

    public static <T> CSTDataItem<T> parseJson(JSONObject jsonRaw, CSTDataItem<T> item) {
        if (jsonRaw == null) {
            return null;
        }

        if (item == null) {
            throw new IllegalArgumentException("CSTDataItem MUST NOT be null!");
        }

        try {
            CSTStatusInfo parsedStatusInfo = CSTJsonParserUtil
                    .readJsonObject(jsonRaw, CSTStatusInfo.class);
            if (parsedStatusInfo.status == Constants.STATUS_REQUEST_SUCCESS) {
                Object jsonRoot = getJsonBody(jsonRaw);
                item = parseJsonObjOrArray(jsonRoot, item);
            }

            return item.setStatusInfo(parsedStatusInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Object getJsonBody(JSONObject jsonRaw) {
        try {
            return jsonRaw.get(JSON_RESULT_BODY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> CSTDataItem<T> parseJsonObjOrArray(Object jsonRoot,
            CSTDataItem<T> item) {
        if (jsonRoot != null) {
            try {
                if (jsonRoot instanceof JSONObject) {
                    item = CSTJsonParserUtil.readJsonObject((JSONObject) jsonRoot, item.getClass());
                } else if (jsonRoot instanceof JSONArray) {
                    item.itemList.clear();
                    item.itemList.addAll(
                            CSTJsonParserUtil.readJsonArray((JSONArray) jsonRoot,
                                    (Class<T>) item.getClass())
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return item;
    }

}
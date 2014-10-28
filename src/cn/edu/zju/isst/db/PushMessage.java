/**
 *
 */
package cn.edu.zju.isst.db;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.edu.zju.isst.util.Judge;

/**
 * @deprecated
 * @author theasir
 */
public class PushMessage implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2915741690852964093L;

    public int id;

    public String title;

    public String content;

    public long createdTime;

    public PushMessage(JSONObject jsonObject) throws JSONException {
        id = -1;
        title = "";
        content = "";
        createdTime = 0;
        update(jsonObject);
    }

    public void update(JSONObject jsonObject) throws JSONException {
        if (!Judge.isNullOrEmpty(jsonObject)) {
            if (Judge.isValidJsonValue("id", jsonObject)) {
                id = jsonObject.getInt("id");
            }
            if (Judge.isValidJsonValue("title", jsonObject)) {
                title = jsonObject.getString("title");
            }
            if (Judge.isValidJsonValue("createdAt", jsonObject)) {
                createdTime = jsonObject.getLong("createdAt");
            }
            if (Judge.isValidJsonValue("content", jsonObject)) {
                content = jsonObject.getString("content");
            }
        }
    }
}

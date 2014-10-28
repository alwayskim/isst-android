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
public class CityActivity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8625251876718593076L;

    public int id;

    public String title;

    public String imgUrl;

    public int cityId;

    public String location;

    public long startTime;

    public long expireTime;

    public long updatedAt;

    public String content;

    public boolean isParticipate;

    public Publisher publisher;

    public CityActivity(JSONObject jsonObject) throws JSONException {
        id = -1;
        title = "";
        imgUrl = "";
        cityId = -1;
        location = "";
        startTime = 0;
        expireTime = 0;
        updatedAt = 0;
        content = "";
        isParticipate = false;
        publisher = new Publisher(null);
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
            if (Judge.isValidJsonValue("picture", jsonObject)) {
                imgUrl = jsonObject.getString("picture");
            }
            if (Judge.isValidJsonValue("cityId", jsonObject)) {
                cityId = jsonObject.getInt("cityId");
            }
            if (Judge.isValidJsonValue("location", jsonObject)) {
                location = jsonObject.getString("location");
            }
            if (Judge.isValidJsonValue("user", jsonObject)) {
                publisher.update(jsonObject.getJSONObject("user"));
            }
            if (Judge.isValidJsonValue("updatedAt", jsonObject)) {
                updatedAt = jsonObject.getLong("updatedAt");
            }
            if (Judge.isValidJsonValue("startTime", jsonObject)) {
                startTime = jsonObject.getLong("startTime");
            }
            if (Judge.isValidJsonValue("expireTime", jsonObject)) {
                expireTime = jsonObject.getLong("expireTime");
            }
            if (Judge.isValidJsonValue("content", jsonObject)) {
                content = jsonObject.getString("content");
            }
            if (Judge.isValidJsonValue("participated", jsonObject)) {
                isParticipate = jsonObject.getBoolean("participated");
            }
        }
    }

}

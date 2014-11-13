package cn.edu.zju.isst1.db;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.edu.zju.isst1.util.Judge;

/**
 * @deprecated
 */
public class UserCenterList implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1327987432503433683L;

    private int id;

    private String title;

    private String content;

    private long updatedAt;

    /**
     * 默认值初始化并更新
     *
     * @param jsonObject 数据源
     * @throws JSONException 未处理异常
     */
    public UserCenterList(JSONObject jsonObject) throws JSONException {
        id = -1;
        title = "";
        updatedAt = 0;

        update(jsonObject);
    }

    /**
     * 更新数据，强制判断设计
     *
     * @param jsonObject 数据源
     * @throws JSONException 未处理异常
     */
    public void update(JSONObject jsonObject) throws JSONException {
        if (!Judge.isNullOrEmpty(jsonObject)) {
            if (Judge.isValidJsonValue("id", jsonObject)) {
                id = jsonObject.getInt("id");
            }
            if (Judge.isValidJsonValue("title", jsonObject)) {
                title = jsonObject.getString("title");
            }
            if (Judge.isValidJsonValue("updatedAt", jsonObject)) {
                updatedAt = jsonObject.getLong("updatedAt");
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}

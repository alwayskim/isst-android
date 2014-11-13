/**
 *
 */
package cn.edu.zju.isst1.db;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.edu.zju.isst1.util.Judge;

/**
 * @deprecated
 * 归档解析类
 *
 * @author xyj
 */
public class Comment implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 8921134999128514894L;

    /**
     * 以下字段详见服务器接口文档
     */
    private int id;

    private String title;

    private String content;

    private long createdAt;

    private User user;

    /**
     * 默认值初始化并更新
     *
     * @param jsonObject 数据源
     * @throws JSONException 未处理异常
     */
    public Comment(JSONObject jsonObject) throws JSONException {
        id = -1;
        title = "";
        content = "";
        createdAt = 0;
        user = new User(new JSONObject("{}"));// TODO 怎样初始化比较好？

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
            if (Judge.isValidJsonValue("content", jsonObject)) {
                content = jsonObject.getString("content");
            }
            if (Judge.isValidJsonValue("createdAt", jsonObject)) {
                createdAt = jsonObject.getLong("createdAt");
            }
            if (Judge.isValidJsonValue("user", jsonObject)) {
                user = new User(jsonObject.getJSONObject("user"));
            }
        }
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @return the createdAt
     */
    public long getCreatedAt() {
        return createdAt;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }


}

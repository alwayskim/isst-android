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
 * @author xyj
 */
public class Klass implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -808116211507079409L;

    private int id;

    private String name;

    public Klass(JSONObject jsonObject) throws JSONException {
        id = -1;
        name = "";
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
            if (Judge.isValidJsonValue("name", jsonObject)) {
                name = jsonObject.getString("name");
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
     * @return the name
     */
    public String getName() {
        return name;
    }
}

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
public class Major implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2601333053594383811L;

    private String name;

    public Major(JSONObject jsonObject) throws JSONException {
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
            if (Judge.isValidJsonValue("name", jsonObject)) {
                name = jsonObject.getString("name");
            }
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}

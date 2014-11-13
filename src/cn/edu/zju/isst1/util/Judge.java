/**
 *
 */
package cn.edu.zju.isst1.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;

/**
 * Judgement
 *
 * @author theasir
 *         <p/>
 *         TODO WIP
 */
public class Judge {

    /**
     * 私有构造器，防止对象实例化
     */
    private Judge() {
    }

    /**
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
     *
     * @param obj 目标对象
     * @return 是否为空
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj.equals(null)) {
            return true;
        }

        if (obj instanceof String) {
            return ((String) obj).trim().length() == 0;
        }

        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }

        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }

        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean isEmpty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    isEmpty = false;
                    break;
                }
            }
            return isEmpty;
        }

        return false;
    }

    public static boolean isValidJsonValue(String key, JSONObject jsonObject)
            throws JSONException {
        return jsonObject.has(key) && !isNullOrEmpty(jsonObject.get(key));
    }
}

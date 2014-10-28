/**
 *
 */
package cn.edu.zju.isst.v2.user.net;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst.api.CSTApi;
import cn.edu.zju.isst.db.User;
import cn.edu.zju.isst.net.RequestListener;

/**
 * @author theasir
 */
public class UserApi extends CSTApi {

    private static final String SUB_URL = "/api/user";

    public static void update(User currentUser, RequestListener listener) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("cityId", "" + currentUser.getCityId());
        paramsMap.put("email", currentUser.getEmail());
        paramsMap.put("phone", currentUser.getPhone());
        paramsMap.put("qq", currentUser.getQq());
        paramsMap.put("company", currentUser.getCompany());
        paramsMap.put("position", currentUser.getPosition());
        paramsMap.put("signature", currentUser.getSignature());
        paramsMap.put("privateQQ", "" + currentUser.isPrivateQQ());
        paramsMap.put("privateEmail", "" + currentUser.isPrivateEmail());
        paramsMap.put("privatePhone", "" + currentUser.isPrivatePhone());
        paramsMap.put("privateCompany", "" + currentUser.isPrivateCompany());
        paramsMap.put("privatePosition", "" + currentUser.isPrivatePosition());

        request("POST", SUB_URL, paramsMap, listener);
    }
}

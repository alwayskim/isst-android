/**
 *
 */
package cn.edu.zju.isst1.v2.user.net;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;
import cn.edu.zju.isst1.v2.user.data.CSTUser;

/**
 * @author theasir
 */
public class UserApi {

    private static final String SUB_URL = "/api/user";
    private static CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    public static void update(CSTUser currentUser, UserResponse listener) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("cityId", "" + currentUser.cityId);
        paramsMap.put("cityName","" + currentUser.cityName);
        paramsMap.put("email", currentUser.email);
        paramsMap.put("phone", currentUser.phoneNum);
        paramsMap.put("qq", currentUser.qqNum);
        paramsMap.put("company", currentUser.company);
        paramsMap.put("position", currentUser.jobTitle);
        paramsMap.put("signature", currentUser.sign);
        paramsMap.put("privateQQ", "" + currentUser.pvtQq);
        paramsMap.put("privateEmail", "" + currentUser.pvtEmail);
        paramsMap.put("privatePhone", "" + currentUser.pvtPhoneNum);
        paramsMap.put("privateCompany", "" + currentUser.pvtCompany);
        paramsMap.put("privatePosition", "" + currentUser.pvtJobTitle);

        CSTJsonRequest updateRequest = new CSTJsonRequest(CSTRequest.Method.POST, SUB_URL, paramsMap,
                listener);
        mEngine.requestJson(updateRequest);
    }
}

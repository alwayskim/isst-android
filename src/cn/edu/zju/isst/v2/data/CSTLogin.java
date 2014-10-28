package cn.edu.zju.isst.v2.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst.v2.model.CSTDataItem;

/**
 * Created by tan on 2014/8/3.
 */
public class CSTLogin extends CSTDataItem<CSTLogin> {

    @JsonProperty("userId")
    public int userId;

    @JsonProperty("username")
    public String userName;

    @JsonProperty("password")
    public String password;

    @JsonProperty("token")
    public String token;

    @JsonProperty("timestamp")
    public String loginTime;

    @JsonProperty("longitude")
    public double longitude;

    @JsonProperty("latitude")
    public double latitude;

    public CSTLogin() {
    }
}

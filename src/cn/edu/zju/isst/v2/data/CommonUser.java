package cn.edu.zju.isst.v2.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst.v2.user.data.CSTUser;

/**
 * Created by tan on 2014/8/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonUser extends BasicUser {

    @JsonProperty("username")
    public String userName;

    @JsonProperty("gender")
    public CSTUser.Gender gender;

    @JsonProperty("classId")
    public int clazzId;

    @JsonProperty("className")
    public String clazzName;

    @JsonProperty("grade")
    public int grade;

    @JsonProperty("cityId")
    public int cityId;

    @JsonProperty("cityName")
    public String cityName;

    @JsonProperty("major")
    public String majorName;

    @JsonProperty("company")
    public String company;

    @JsonProperty("position")
    public String jobTitle;

    @JsonProperty("signature")
    public String sign;

    public CommonUser() {
    }
}

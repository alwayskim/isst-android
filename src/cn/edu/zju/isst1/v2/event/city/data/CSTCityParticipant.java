package cn.edu.zju.isst1.v2.event.city.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst1.v2.model.CSTDataItem;
import cn.edu.zju.isst1.v2.user.data.CSTUser;

/**
 * Created by tan on 2014/8/3.
 */
public class CSTCityParticipant extends CSTDataItem<CSTCityParticipant> {

    @JsonProperty("id")
    public int id;

    @JsonProperty("username")
    public String userName;

    @JsonProperty("name")
    public String name;

    @JsonProperty("grade")
    public int grade;

    @JsonProperty("gender")
    public CSTUser.Gender gender;

    @JsonProperty("classId")
    public int clazzId;

    @JsonProperty("className")
    public String clazzName;

    @JsonProperty("major")
    public String major;

    @JsonProperty("email")
    public String email;

    @JsonProperty("phone")
    public String phoneNum;

    @JsonProperty("qq")
    public String qqNum;

    @JsonProperty("company")
    public String company;

    @JsonProperty("position")
    public String jobTitle;

    @JsonProperty("signature")
    public String signature;

    @JsonProperty("cityId")
    public String cityId;

    @JsonProperty("cityName")
    public String cityName;

    public CSTCityParticipant() {
    }
}

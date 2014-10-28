package cn.edu.zju.isst.v2.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst.v2.model.CSTDataItem;
import cn.edu.zju.isst.v2.user.data.CSTUser;

/**
 * Created by tan on 2014/8/3.
 */
public class CSTParticipated extends CSTDataItem<CSTParticipated> {

    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("picture")
    public String picture;

    @JsonProperty("cityid")
    public int cityId;

    @JsonProperty("location")
    public String location;

    @JsonProperty("startTime")
    public long startTime;

    @JsonProperty("expireTime")
    public long expireTime;

    @JsonProperty("updatedAt")
    public long updatedAt;

    @JsonProperty("content")
    public String content;

    @JsonProperty("user")
    public CSTUser user;

    public CSTParticipated() {
    }
}

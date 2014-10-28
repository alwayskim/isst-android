package cn.edu.zju.isst.v2.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst.v2.model.CSTDataItem;
import cn.edu.zju.isst.v2.user.data.CSTUser;

/**
 * Created by tan on 2014/8/3.
 */
public class CSTJob extends CSTDataItem<CSTJob> {

    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("company")
    public String company;

    @JsonProperty("position")
    public String jobTitle;

    @JsonProperty("updatedAt")
    public long updateAt;

    @JsonProperty("description")
    public String description;

    @JsonProperty("content")
    public String content;

    @JsonProperty("user")
    public CSTUser user;

    @JsonProperty("cityId")
    public int cityId;

    public CSTJob() {
    }
}

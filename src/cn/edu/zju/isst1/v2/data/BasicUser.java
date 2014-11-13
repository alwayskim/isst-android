package cn.edu.zju.isst1.v2.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst1.v2.model.CSTDataItem;

/**
 * Created by tan on 2014/8/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicUser extends CSTDataItem<BasicUser> {

    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("phone")
    public String phoneNum;

    @JsonProperty("qq")
    public String qqNum;

    @JsonProperty("email")
    public String email;

    public BasicUser() {
    }
}

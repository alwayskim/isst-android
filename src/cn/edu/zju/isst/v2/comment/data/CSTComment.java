package cn.edu.zju.isst.v2.comment.data;


import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst.v2.model.CSTDataItem;
import cn.edu.zju.isst.v2.user.data.CSTUser;

/**
 * Created by tan on 2014/8/3.
 */
public class CSTComment extends CSTDataItem<CSTComment> {

    @JsonProperty("id")
    public int id;

    @JsonProperty("content")
    public String content;

    @JsonProperty("createdAt")
    public long createdAt;

    @JsonProperty("user")
    public CSTUser user;
}

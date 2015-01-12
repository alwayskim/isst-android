package cn.edu.zju.isst1.v2.usercenter.messagecenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst1.v2.model.CSTDataItem;

/**
 * Created by alwayking on 14/11/30.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class CSTMessage extends CSTDataItem<CSTMessage> {

    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("content")
    public String content;

    @JsonProperty("createdAt")
    public String createdAt;

    public CSTMessage() {
    }
}


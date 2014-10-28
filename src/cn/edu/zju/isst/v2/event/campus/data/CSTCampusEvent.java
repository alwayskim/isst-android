package cn.edu.zju.isst.v2.event.campus.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst.v2.model.CSTDataItem;

/**
 * Created by tan on 2014/7/30.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CSTCampusEvent extends CSTDataItem<CSTCampusEvent> {

    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("picture")
    public String picture;

    @JsonProperty("description")
    public String description;

    @JsonProperty("content")
    public String content;

    @JsonProperty("publisherName")
    public String pubName;

    @JsonProperty("location")
    public String location;

    @JsonProperty("updatedAt")
    public long updatedAt;

    @JsonProperty("startTime")
    public long startTime;

    @JsonProperty("expireTime")
    public long expireTime;

    public CSTCampusEvent() {
    }
}

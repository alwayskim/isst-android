package cn.edu.zju.isst1.v2.archive.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst1.v2.model.CSTDataItem;
import cn.edu.zju.isst1.v2.user.data.CSTUser;

/**
 * Created by i308844 on 7/17/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CSTArchive extends CSTDataItem<CSTArchive> {

    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("categoryId")
    public int categoryId;

    @JsonProperty("description")
    public String description;

    @JsonProperty("updatedAt")
    public long updateTime;

    @JsonProperty("userId")
    public int publisherId;

    @JsonProperty("user")
    public CSTUser publisher;

    @JsonProperty("content")
    public String content;

    public CSTArchive() {
    }
}

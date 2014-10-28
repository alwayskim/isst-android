package cn.edu.zju.isst.v2.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst.v2.model.CSTDataItem;

/**
 * Created by tan on 2014/8/3.
 */
public class CSTExperience extends CSTDataItem<CSTExperience> {

    @JsonProperty("id")
    public String id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("content")
    public String content;

    public CSTExperience() {
    }
}

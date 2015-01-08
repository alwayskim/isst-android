package cn.edu.zju.isst1.v2.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst1.v2.model.CSTDataItem;

/**
 * Created by tan on 2014/8/3.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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

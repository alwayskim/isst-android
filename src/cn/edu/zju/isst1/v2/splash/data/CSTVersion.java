package cn.edu.zju.isst1.v2.splash.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst1.v2.model.CSTDataItem;

/**
 * Created by i308844 on 8/18/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CSTVersion extends CSTDataItem<CSTVersion> {

    @JsonProperty("id")
    public int id;

    @JsonProperty("build")
    public int buildNum;

    @JsonProperty("version")
    public String version;

    @JsonProperty("url")
    public String downloadUrl;

    @JsonProperty("postTime")
    public long postTime;

    @JsonProperty("readme")
    public String readme;
}

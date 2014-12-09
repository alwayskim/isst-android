package cn.edu.zju.isst1.v2.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst1.v2.model.CSTDataItem;

/**
 * Created by tan on 2014/8/3.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CSTTask extends CSTDataItem<CSTTask> {

    @JsonProperty("id")
    public int id;

    @JsonProperty("type")
    public int type;

    @JsonProperty("finishedId")
    public int finishedId;

    @JsonProperty("name")
    public String name;

    @JsonProperty("description")
    public String description;

    @JsonProperty("updatedAt")
    public long updatedAt;

    @JsonProperty("startTime")
    public long startTime;

    @JsonProperty("expireTime")
    public long expireTime;

    public CSTTask() {

    }
    @Override
    public String toString(){
        return name;
    }
}

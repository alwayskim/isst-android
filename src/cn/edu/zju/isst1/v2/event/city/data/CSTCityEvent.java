package cn.edu.zju.isst1.v2.event.city.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst1.v2.data.CSTPublisher;
import cn.edu.zju.isst1.v2.model.CSTDataItem;

/**
 * Created by tan on 2014/8/3.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CSTCityEvent extends CSTDataItem<CSTCityEvent> {

    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("imgUrl")
    public String imgUrl;

    @JsonProperty("cityId")
    public int cityId;

    @JsonProperty("location")
    public String location;

    @JsonProperty("startTime")
    public long startTime;

    @JsonProperty("expireTime")
    public long expireTime;

    @JsonProperty("updatedAt")
    public long updatedAt;

    @JsonProperty("content")
    public String content;

    @JsonProperty("description")
    public String description;

    @JsonProperty("participated")
    public boolean isParticipate;

    @JsonProperty("user")
    public CSTPublisher publisher;

    public CSTCityEvent() {
    }
}

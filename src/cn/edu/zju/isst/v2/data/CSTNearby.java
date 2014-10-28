package cn.edu.zju.isst.v2.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst.v2.model.CSTDataItem;
import cn.edu.zju.isst.v2.user.data.CSTUser;

/**
 * Created by tan on 2014/8/3.
 */
public class CSTNearby extends CSTDataItem<CSTNearby> {

    @JsonProperty("longitude")
    public String longitude;

    @JsonProperty("latitude")
    public String latitude;

    @JsonProperty("user")
    public CSTUser user;

    public CSTNearby() {
    }
}

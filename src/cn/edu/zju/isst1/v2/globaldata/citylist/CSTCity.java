package cn.edu.zju.isst1.v2.globaldata.citylist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst1.v2.model.CSTDataItem;
import cn.edu.zju.isst1.v2.user.data.CSTUser;

/**
 * Created by tan on 2014/7/30.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CSTCity extends CSTDataItem<CSTCity> {



    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("user")
    public CSTUser cityMaster;

}

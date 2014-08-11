package cn.edu.zju.isst.v2.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst.v2.model.CSTDataItem;

/**
 * Created by tan on 2014/8/3.
 */
public class CSTMajor extends CSTDataItem<CSTMajor> {

    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String name;

    public CSTMajor() {
    }
}

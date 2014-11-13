package cn.edu.zju.isst1.v2.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst1.v2.model.CSTDataItem;

/**
 * Created by tan on 2014/8/3.
 */
public class CSTMajor extends CSTDataItem<CSTMajor> {

    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;

    public CSTMajor() {
    }
}

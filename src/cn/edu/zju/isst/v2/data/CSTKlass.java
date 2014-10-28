package cn.edu.zju.isst.v2.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst.v2.model.CSTDataItem;

/**
 * Created by tan on 2014/8/3.
 */
public class CSTKlass extends CSTDataItem<CSTKlass> {

    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;

    public CSTKlass() {
    }
}

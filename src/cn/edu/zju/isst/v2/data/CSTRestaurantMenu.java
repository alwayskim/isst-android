package cn.edu.zju.isst.v2.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst.v2.model.CSTDataItem;

/**
 * Created by tan on 2014/8/3.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CSTRestaurantMenu extends CSTDataItem<CSTRestaurantMenu> {

    @JsonProperty("name")
    public String name;

    @JsonProperty("picture")
    public String picture;

    @JsonProperty("description")
    public String description;

    @JsonProperty("price")
    public float price;

    public CSTRestaurantMenu() {
    }
}

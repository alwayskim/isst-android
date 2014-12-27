package cn.edu.zju.isst1.v2.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import cn.edu.zju.isst1.v2.model.CSTDataItem;
import cn.edu.zju.isst1.v2.user.data.CSTUser;

/**
 * Created by tan on 2014/8/3.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CSTJob extends CSTDataItem<CSTJob> implements Parcelable{

    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("company")
    public String company;

    @JsonProperty("position")
    public String jobTitle;

    @JsonProperty("updatedAt")
    public long updateAt;

    @JsonProperty("description")
    public String description;

    @JsonProperty("content")
    public String content;

    @JsonProperty("user")
    public CSTUser user;

    @JsonProperty("cityId")
    public int cityId;

    public CSTJob() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

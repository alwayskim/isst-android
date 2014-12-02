package cn.edu.zju.isst1.v2.usercenter.setting;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst1.v2.model.CSTDataItem;

/**
 * Created by alwayking on 14/12/1.
 */
public class CSTFeedBack extends CSTDataItem<CSTFeedBack> {

    @JsonProperty("usename")
    public String usename;

    @JsonProperty("email")
    public String email;

    @JsonProperty("feedbacktype")
    public String feedbacktype;

    @JsonProperty("os")
    public String os;

    @JsonProperty("appversion")
    public String appversion;

    @JsonProperty("content")
    public String content;

    public CSTFeedBack() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getFeedbacktype() {
        return feedbacktype;
    }

    public void setFeedbacktype(String feedbacktype) {
        this.feedbacktype = feedbacktype;
    }

    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

package cn.edu.zju.isst1.v2.net;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by i308844 on 7/31/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CSTStatusInfo {

    @JsonProperty("status")
    public int status;

    @JsonProperty("message")
    public String message;
}

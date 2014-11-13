package cn.edu.zju.isst1.v2.contact.contact.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.edu.zju.isst1.v2.data.CommonUser;

/**
 * Created by tan on 2014/8/3.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CSTAlumni extends CommonUser {

    @JsonProperty("cityPrincipal")
    public boolean cityPrincipal;

    @JsonProperty("privatePosition")
    public boolean pvtPosition;

    @JsonProperty("privateCompany")
    public boolean pvtCompany;

    @JsonProperty("privateQQ")
    public boolean pvtQQ;

    @JsonProperty("privateEmail")
    public boolean pvtEmail;

    @JsonProperty("privatePhone")
    public boolean pvtPhone;

    public CSTAlumni() {
    }
}

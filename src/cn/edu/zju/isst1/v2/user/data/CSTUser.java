package cn.edu.zju.isst1.v2.user.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import cn.edu.zju.isst1.v2.data.CommonUser;

/**
 * Created by i308844 on 7/15/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CSTUser extends CommonUser {

    @JsonProperty("password")
    public String pwd;

    @JsonProperty("cityPrincipal")
    public boolean castellan;

    @JsonProperty("privateQQ")
    public boolean pvtQq;

    @JsonProperty("privateEmail")
    public boolean pvtEmail;

    @JsonProperty("privatePhone")
    public boolean pvtPhoneNum;

    @JsonProperty("privateCompany")
    public boolean pvtCompany;

    @JsonProperty("privatePosition")
    public boolean pvtJobTitle;

    public CSTUser() {

    }

    public enum Gender {
        MALE(1), FEMALE(2);

        private final int key;

        private String typeName;

        Gender(int key) {
            this.key = key;
            this.typeName = key == 1 ? "男" : key == 2 ? "女" : null;
        }

        @JsonCreator
        public static Gender fromValue(int typeCode) {
            for (Gender g : Gender.values()) {
                if (g.key == typeCode) {
                    return g;
                }
            }
            throw new IllegalArgumentException("Invalid Gender type code: " + typeCode);
        }

        public String getTypeName() {
            return typeName;
        }

        @JsonValue
        public int getKey() {
            return this.key;
        }

        @Override
        public String toString() {
            return this.typeName;
        }
    }

}

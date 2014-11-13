/**
 *
 */
package cn.edu.zju.isst1.db;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.edu.zju.isst1.util.Judge;

/**
 * @deprecated
 * 用户解析类
 *
 * @author theasir
 */
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1512168798814691417L;

    /**
     * 以下字段详见服务器接口文档
     */
    private int id;

    private String username;

    private String password;

    private String name;

    private int gender;

    private int grade;

    private int classId;

    private String major;

    private int cityId;

    private String email;

    private String phone;

    private String qq;

    private String company;

    private String position;

    private String signature;

    private boolean cityPrincipal;

    private boolean privateQQ;

    private boolean privateEmail;

    private boolean privatePhone;

    private boolean privateCompany;

    private boolean privatePosition;

    /**
     * 默认值初始化并更新
     *
     * @param jsonObject 数据源
     * @throws JSONException 未处理异常
     */
    public User(JSONObject jsonObject) throws JSONException {
        id = -1;
        username = "";
        password = "";
        name = "";
        gender = -1;
        grade = -1;
        classId = -1;
        major = "";
        cityId = -1;
        email = "";
        phone = "";
        qq = "";
        company = "";
        position = "";
        signature = "";
        cityPrincipal = false;
        privateQQ = false;
        privateEmail = false;
        privatePhone = false;
        privateCompany = false;
        privatePosition = false;
        update(jsonObject);
    }

    /**
     * 更新数据，强制判断设计
     *
     * @param jsonObject 数据源
     * @throws JSONException 未处理异常
     */
    public void update(JSONObject jsonObject) throws JSONException {
        if (!Judge.isNullOrEmpty(jsonObject)) {
            if (Judge.isValidJsonValue("id", jsonObject)) {
                id = jsonObject.getInt("id");
            }
            if (Judge.isValidJsonValue("username", jsonObject)) {
                username = jsonObject.getString("username");
            }
            if (Judge.isValidJsonValue("password", jsonObject)) {
                password = jsonObject.getString("password");
            }
            if (Judge.isValidJsonValue("name", jsonObject)) {
                name = jsonObject.getString("name");
            }
            if (Judge.isValidJsonValue("gender", jsonObject)) {
                gender = jsonObject.getInt("gender");
            }
            if (Judge.isValidJsonValue("grade", jsonObject)) {
                grade = jsonObject.getInt("grade");
            }
            if (Judge.isValidJsonValue("classId", jsonObject)) {
                classId = jsonObject.getInt("classId");
            }
            if (Judge.isValidJsonValue("major", jsonObject)) {
                major = jsonObject.getString("major");
            }
            if (Judge.isValidJsonValue("cityId", jsonObject)) {
                cityId = jsonObject.getInt("cityId");
            }
            if (Judge.isValidJsonValue("email", jsonObject)) {
                email = jsonObject.getString("email");
            }
            if (Judge.isValidJsonValue("phone", jsonObject)) {
                phone = jsonObject.getString("phone");
            }
            if (Judge.isValidJsonValue("qq", jsonObject)) {
                qq = jsonObject.getString("qq");
            }
            if (Judge.isValidJsonValue("company", jsonObject)) {
                company = jsonObject.getString("company");
            }
            if (Judge.isValidJsonValue("position", jsonObject)) {
                position = jsonObject.getString("position");
            }
            if (Judge.isValidJsonValue("signature", jsonObject)) {
                signature = jsonObject.getString("signature");
            }
            if (Judge.isValidJsonValue("cityPrincipal", jsonObject)) {
                cityPrincipal = jsonObject.getBoolean("cityPrincipal");
            }
            if (Judge.isValidJsonValue("privateQQ", jsonObject)) {
                privateQQ = jsonObject.getBoolean("privateQQ");
            }
            if (Judge.isValidJsonValue("privateEmail", jsonObject)) {
                privateEmail = jsonObject.getBoolean("privateEmail");
            }
            if (Judge.isValidJsonValue("privatePhone", jsonObject)) {
                privatePhone = jsonObject.getBoolean("privatePhone");
            }
            if (Judge.isValidJsonValue("privateCompany", jsonObject)) {
                privateCompany = jsonObject.getBoolean("privateCompany");
            }
            if (Judge.isValidJsonValue("privatePosition", jsonObject)) {
                privatePosition = jsonObject.getBoolean("privatePosition");
            }
        }
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the gender
     */
    public int getGender() {
        return gender;
    }

    /**
     * @return the grade
     */
    public int getGrade() {
        return grade;
    }

    /**
     * @return the classId
     */
    public int getClassId() {
        return classId;
    }

    /**
     * @return the majotId
     */
    public String getMajor() {
        return major;
    }

    /**
     * @return the cityId
     */
    public int getCityId() {
        return cityId;
    }

    /**
     * @param cityId the cityId to set
     */
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the qq
     */
    public String getQq() {
        return qq;
    }

    /**
     * @param qq the qq to set
     */
    public void setQq(String qq) {
        this.qq = qq;
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * @return the cityPrincipal
     */
    public boolean isCityPrincipal() {
        return cityPrincipal;
    }

    /**
     * @return the privateQQ
     */
    public boolean isPrivateQQ() {
        return privateQQ;
    }

    /**
     * @param privateQQ the privateQQ to set
     */
    public void setPrivateQQ(boolean privateQQ) {
        this.privateQQ = privateQQ;
    }

    /**
     * @return the privateEmail
     */
    public boolean isPrivateEmail() {
        return privateEmail;
    }

    /**
     * @param privateEmail the privateEmail to set
     */
    public void setPrivateEmail(boolean privateEmail) {
        this.privateEmail = privateEmail;
    }

    /**
     * @return the privatePhone
     */
    public boolean isPrivatePhone() {
        return privatePhone;
    }

    /**
     * @param privatePhone the privatePhone to set
     */
    public void setPrivatePhone(boolean privatePhone) {
        this.privatePhone = privatePhone;
    }

    /**
     * @return the privateCompany
     */
    public boolean isPrivateCompany() {
        return privateCompany;
    }

    /**
     * @param privateCompany the privateCompany to set
     */
    public void setPrivateCompany(boolean privateCompany) {
        this.privateCompany = privateCompany;
    }

    /**
     * @return the privatePosition
     */
    public boolean isPrivatePosition() {
        return privatePosition;
    }

    /**
     * @param privatePosition the privatePosition to set
     */
    public void setPrivatePosition(boolean privatePosition) {
        this.privatePosition = privatePosition;
    }

}

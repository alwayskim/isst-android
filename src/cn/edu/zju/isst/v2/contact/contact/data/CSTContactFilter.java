package cn.edu.zju.isst.v2.contact.contact.data;

import java.io.Serializable;

/**
 * Created by tan on 2014/8/30.
 */
public class CSTContactFilter implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 890650792358672364L;

    public int id;

    public String username;

    public String name;

    public int gender;

    public int grade;

    public int clazzId;

    public String clazzName;

    public String major;

    public int cityId;

    public String company;

    //以下是现实的字符串
    public String genderString;

    public String cityString;

    public CSTContactFilter() {
        clear();
    }

    //清空条件
    public void clear() {
        id = 0;
        username = "";
        name = "";
        gender = 0;
        grade = 0;
        clazzId = 0;
        major = "";
        cityId = 0;
        company = "";
    }

    public void setContactFilter(CSTContactFilter mFilter) {
        this.id = mFilter.id;
        this.username = mFilter.username;
        this.name = mFilter.name;
        this.gender = mFilter.gender;
        this.grade = mFilter.grade;
        this.clazzId = mFilter.clazzId;
        this.major = mFilter.major;
        this.cityId = mFilter.cityId;
        this.company = mFilter.company;
    }
}

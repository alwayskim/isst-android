package cn.edu.zju.isst1.v2.globaldata;

/**
 * Created by always on 9/15/2014.
 */
public enum GlobalDataCategory {
    CITYLIST("cityList", "/cities"),
    MAJORLIST("majorList", "/majors"),
    CLASSLIST("classList", "/classes");

    String name;
    String subUrl;

    private GlobalDataCategory(String name, String subUrl) {
        this.name = name;
        this.subUrl = subUrl;
    }

    public String getName() {
        return name;
    }

    public String getSubUrl() {
        return subUrl;
    }
}
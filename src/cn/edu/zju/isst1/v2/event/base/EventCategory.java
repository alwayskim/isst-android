package cn.edu.zju.isst1.v2.event.base;

/**
 * Created by always on 04/09/2014.
 */
public enum EventCategory {
    CITYEVENT("cityEvent", "/api/cities/"),
    CAMPUSEVENT("campusEvent", "/api/campus/activities");

    String name;
    String subUrl;

    private EventCategory(String name, String subUrl) {
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

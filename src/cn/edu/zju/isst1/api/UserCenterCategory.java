package cn.edu.zju.isst1.api;

import cn.edu.zju.isst1.db.DataManager;

/**
 * @deprecated
 * 归档类别枚举类
 *
 * @author theasir
 */
public enum UserCenterCategory {

    MYRECOMMEND("jobs/recommend", DataManager.MYRECOMMEND_LIST_IN_DB), MYEXPIENCE(
            "archives/experience",
            DataManager.MYEXPIENCE_LIST_IN_DB), MYACTIVITIES("activities /participated",
            DataManager.MYACTIVITIES_LIST_IN_DB);

    private String subUrl;

    private String nameInDB;

    private UserCenterCategory(String subUrl, String nameInDB) {
        this.subUrl = subUrl;
        this.nameInDB = nameInDB;
    }

    /**
     * @return the subUrl
     */
    public String getSubUrl() {
        return subUrl;
    }

    /**
     * @return the nameInDB
     */
    public String getNameInDB() {
        return nameInDB;
    }

}
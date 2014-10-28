/**
 *
 */
package cn.edu.zju.isst.api;

import cn.edu.zju.isst.db.DataManager;

/**
 * @deprecated
 * 归档类别枚举类
 *
 * @author theasir
 */
public enum JobCategory {

    EMPLOYMENT("employment", DataManager.EMPLOYMENT_LIST__IN_DB), INTERNSHIP("internship",
            DataManager.INTERNSHIP_LIST_IN_DB), RECOMMEND("recommend",
            DataManager.RECOMMEND_LIST_IN_DB);

    private String subUrl;

    private String nameInDB;

    private JobCategory(String subUrl, String nameInDB) {
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

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
public enum ArchiveCategory {

    CAMPUS("campus", DataManager.NEWS_LIST_IN_DB), ENCYCLOPEDIA("encyclopedia",
            DataManager.WIKI_LIST_IN_DB), STUDING("studying",
            DataManager.STUD_LIST_IN_DB), EXPERIENCE("experience",
            DataManager.EXPERIENCE_LIST_IN_DB);

    private String subUrl;

    private String nameInDB;

    private ArchiveCategory(String subUrl, String nameInDB) {
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

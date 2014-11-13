/**
 *
 */
package cn.edu.zju.isst1.constant;

/**
 * @deprecated
 * 导航项常量类
 *
 * @author theasir
 */
public enum Nav {

    NEWS("软院新闻", 0, 0), // news
    WIKI("软院百科", 0, 1), // wiki
    SCAC("在校活动", 0, 2), // school activity
    SERV("便捷服务", 0, 3), // service
    STUD("学习园地", 0, 4), // study
    INTE("实习", 1, 0), // intern
    JOBS("就业", 1, 1), // jobs
    REFE("内推", 1, 2), // referral
    EXPE("经验交流", 1, 3), // experience
    CIMA("城主", 2, 0), // city master
    CIAC("同城活动", 2, 1), // city activity
    CIAL("同城校友", 2, 2), // city alumni
    CONT("通讯录", 3, 0), // contacts
    USCE("个人中心", 4, 0);// user center

    /**
     * 导航项名称名
     */
    private String name;

    /**
     * 导航项组号
     */
    private int index;

    /**
     * 导航项组内序号
     */
    private int indexOfGroup;

    private Nav(String name, int index, int indexOfGroup) {
        this.name = name;
        this.index = index;
        this.indexOfGroup = indexOfGroup;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public int getIndexOfGroup() {
        return indexOfGroup;
    }
}

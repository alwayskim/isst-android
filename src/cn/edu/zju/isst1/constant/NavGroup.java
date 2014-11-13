/**
 *
 */
package cn.edu.zju.isst1.constant;

/**
 * @deprecated
 * 导航组常量类
 *
 * @author theasir
 */
public enum NavGroup {

    LIFE("软院生活", 0), WORK("职场信息", 1), CITY("同城", 2), CONT("通讯录", 3), // contacts
    USCE("个人中心", 4);// user center

    /**
     * 导航组名称
     */
    private String name;

    /**
     * index
     */
    private int index;

    private NavGroup(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}

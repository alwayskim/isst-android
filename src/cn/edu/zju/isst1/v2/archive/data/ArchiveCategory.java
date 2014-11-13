package cn.edu.zju.isst1.v2.archive.data;

/**
 * Created by i308844 on 8/25/14.
 */
public enum ArchiveCategory {

    NEWS(4, "/campus"),
    WIKI(5, "/encyclopedia"),
    STUDY(2, "/studying"),
    EXPERIENCE(3, "/experience");

    public int id;

    public String subUrl;

    private ArchiveCategory(int id, String subUrl) {
        this.id = id;
        this.subUrl = subUrl;
    }
}

package cn.edu.zju.isst1.v2.archive.gui;

import cn.edu.zju.isst1.v2.archive.data.ArchiveCategory;

/**
 * Created by i308844 on 8/25/14.
 */
public class StudyFragment extends BaseArchiveListFragment {

    private static StudyFragment INSTANCE;

    public static StudyFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StudyFragment();
        }
        return INSTANCE;
    }

    @Override
    protected void setCategory() {
        this.mCategory = ArchiveCategory.STUDY;
    }
}

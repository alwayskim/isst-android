package cn.edu.zju.isst1.v2.archive.gui;

import cn.edu.zju.isst1.v2.archive.data.ArchiveCategory;

/**
 * Created by i308844 on 8/25/14.
 */
public class ExperienceFragment extends BaseArchiveListFragment {

    private static ExperienceFragment INSTANCE;

    public static ExperienceFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExperienceFragment();
        }
        return INSTANCE;
    }

    @Override
    protected void setCategory() {
        this.mCategory = ArchiveCategory.EXPERIENCE;
    }
}

package cn.edu.zju.isst1.v2.archive.gui;

import cn.edu.zju.isst1.v2.archive.data.ArchiveCategory;

/**
 * Created by i308844 on 8/25/14.
 */
public class NewsFragment extends BaseArchiveListFragment {

    private static NewsFragment INSTANCE;

    public static NewsFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NewsFragment();
        }
        return INSTANCE;
    }

    @Override
    protected void setCategory() {
        this.mCategory = ArchiveCategory.NEWS;
    }
}

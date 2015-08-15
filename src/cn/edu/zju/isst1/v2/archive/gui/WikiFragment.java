package cn.edu.zju.isst1.v2.archive.gui;

import cn.edu.zju.isst1.v2.archive.data.ArchiveCategory;

/**
 * Created by alwayking on 15/8/15.
 */
public class WikiFragment extends BaseArchiveListFragment {

    private static WikiFragment INSTANCE;

    public static WikiFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WikiFragment();
        }
        return INSTANCE;
    }

    @Override
    protected void setCategory() {
        this.mCategory = ArchiveCategory.WIKI;
    }
}

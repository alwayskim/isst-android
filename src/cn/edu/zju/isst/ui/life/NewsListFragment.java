/**
 *
 */
package cn.edu.zju.isst.ui.life;

import cn.edu.zju.isst.api.ArchiveCategory;
import cn.edu.zju.isst.v2.archive.gui.*;


/**
 * 新闻列表页
 *
 * @author theasir
 */
public class NewsListFragment extends cn.edu.zju.isst.v2.archive.gui.BaseArchiveListFragment {

    private static NewsListFragment INSTANCE = new NewsListFragment();

    public NewsListFragment() {
        super();
//        super.setArchiveCategory(ArchiveCategory.CAMPUS);
    }

    @Override
    protected void setCategory() {

    }

}
/**
 *
 */
package cn.edu.zju.isst.ui.job;

import cn.edu.zju.isst.api.ArchiveCategory;
import cn.edu.zju.isst.ui.life.BaseArchiveListFragment;

/**
 * @author theasir
 */
public class ExperienceListFragment extends BaseArchiveListFragment {

    private static ExperienceListFragment INSTANCE = new ExperienceListFragment();

    public ExperienceListFragment() {
        super();
        super.setArchiveCategory(ArchiveCategory.EXPERIENCE);
    }

    public static ExperienceListFragment getInstance() {
        return INSTANCE;
    }
}

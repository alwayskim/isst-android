/**
 *
 */
package cn.edu.zju.isst1.ui.job;

import cn.edu.zju.isst1.api.JobCategory;
import cn.edu.zju.isst1.ui.main.BaseJobsListFragment;

/**
 * 新闻列表页
 *
 * @author theasir
 */
public class RecommedListFragment extends BaseJobsListFragment {

    private static RecommedListFragment INSTANCE = new RecommedListFragment();

    public RecommedListFragment() {
        super();
        super.setJobCategory(JobCategory.RECOMMEND);
    }

    public static RecommedListFragment getInstance() {
        return INSTANCE;
    }
}
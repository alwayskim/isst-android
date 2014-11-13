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
public class InternshipListFragment extends BaseJobsListFragment {

    private static InternshipListFragment INSTANCE = new InternshipListFragment();

    public InternshipListFragment() {
        super();
        super.setJobCategory(JobCategory.INTERNSHIP);
    }

    public static InternshipListFragment getInstance() {
        return INSTANCE;
    }
}
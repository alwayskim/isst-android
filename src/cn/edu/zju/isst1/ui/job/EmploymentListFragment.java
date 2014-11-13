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
public class EmploymentListFragment extends BaseJobsListFragment {

    private static EmploymentListFragment INSTANCE = new EmploymentListFragment();

    public EmploymentListFragment() {
        super();
        super.setJobCategory(JobCategory.EMPLOYMENT);
    }

    public static EmploymentListFragment getInstance() {
        return INSTANCE;
    }
}
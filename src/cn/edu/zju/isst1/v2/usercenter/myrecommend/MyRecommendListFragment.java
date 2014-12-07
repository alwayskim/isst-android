/**
 *
 */
package cn.edu.zju.isst1.v2.usercenter.myrecommend;

import cn.edu.zju.isst1.api.UserCenterCategory;
import cn.edu.zju.isst1.ui.main.BaseUserCenterListFragment;

/**
 * 我的内推列表
 *
 * @author xyj
 */
public class MyRecommendListFragment extends BaseUserCenterListFragment {

    private static MyRecommendListFragment INSTANCE = new MyRecommendListFragment();

    public MyRecommendListFragment() {
        super();
        super.setUserCenterCategory(UserCenterCategory.MYRECOMMEND);
        //super.setJobCategory(JobCategory.INTERNSHIP);
    }

    public static MyRecommendListFragment getInstance() {
        return INSTANCE;
    }
}
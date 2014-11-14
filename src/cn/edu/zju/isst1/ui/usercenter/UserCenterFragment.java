/**
 *
 */
package cn.edu.zju.isst1.ui.usercenter;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.db.DataManager;
import cn.edu.zju.isst1.db.User;
import cn.edu.zju.isst1.ui.main.NewMainActivity;

/**
 * @author theasir
 */
public class UserCenterFragment extends Fragment {

    private static UserCenterFragment INSTANCE = new UserCenterFragment();

    private User m_userCurrent;

    private ViewHolder mViewHolder = new ViewHolder();

    public UserCenterFragment() {
    }

    public static UserCenterFragment getInstance() {
        return INSTANCE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_center_fragment, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onViewCreated(android.view.View,
     * android.os.Bundle)
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);

        setUpListener();

        initUser();

        show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_center_fragment_ab_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.message_center:
                Intent intent = new Intent(getActivity(), PushMessagesActivity.class);
                getActivity().startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initComponent(View view) {
        mViewHolder.userView = view
                .findViewById(R.id.user_center_fragment_user);
        mViewHolder.avatarImgv = (ImageView) view
                .findViewById(R.id.user_center_fragment_user_avatar_imgv);
        mViewHolder.nameTxv = (TextView) view
                .findViewById(R.id.user_center_fragment_name_txv);
        mViewHolder.signTxv = (TextView) view
                .findViewById(R.id.user_center_fragment_signature_txv);
        mViewHolder.logoutBtn = (Button) view
                .findViewById(R.id.user_center_fragment_logout_btn);
        mViewHolder.stAffairView = view.findViewById(R.id.user_center_student_affair_txv);
        mViewHolder.taskCenterView = view.findViewById(R.id.user_center_task_center_txv);
        mViewHolder.myRecomView = view.findViewById(R.id.user_center_my_recommend_txv);
        mViewHolder.myExpView = view.findViewById(R.id.user_center_my_experience_txv);
        mViewHolder.myActivityView = view.findViewById(R.id.user_center_my_activity_txv);
        mViewHolder.peopleNearbyView = view.findViewById(R.id.user_center_people_around_txv);
        mViewHolder.personalSettingvView = view.findViewById(R.id.user_center_personal_setting_txv);
    }

    private void setUpListener() {
        mViewHolder.userView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().startActivity(
                        new Intent(getActivity(), UserInfoActivity.class));
            }
        });

        mViewHolder.logoutBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ((NewMainActivity) getActivity()).logout();
            }
        });

        mViewHolder.stAffairView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "该功能暂未实现", Toast.LENGTH_SHORT).show();
            }
        });

        mViewHolder.taskCenterView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "该功能暂未实现", Toast.LENGTH_SHORT).show();
            }
        });

        //任务中心
        mViewHolder.myRecomView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, MyRecommendListFragment.getInstance())
                        .commit();
                //Toast.makeText(getActivity(), "该功能暂未实现", Toast.LENGTH_SHORT).show();
            }
        });

        //
        mViewHolder.myExpView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, MyExpListFragment.getInstance()).commit();
                //Toast.makeText(getActivity(), "该功能暂未实现", Toast.LENGTH_SHORT).show();
            }
        });

        //活动管理
        mViewHolder.myActivityView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, MyActivitiesFragment.getInstance()).commit();

                //Toast.makeText(getActivity(), "该功能暂未实现", Toast.LENGTH_SHORT).show();
            }
        });

        mViewHolder.peopleNearbyView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "该功能暂未实现", Toast.LENGTH_SHORT).show();
            }
        });

        mViewHolder.personalSettingvView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "该功能暂未实现", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUser() {
        m_userCurrent = DataManager.getCurrentUser();
    }

    private void show() {
        mViewHolder.nameTxv.setText(m_userCurrent.getName());
        mViewHolder.signTxv.setText(m_userCurrent.getSignature());
    }

    private class ViewHolder {

        View userView;

        ImageView avatarImgv;

        TextView nameTxv;

        TextView signTxv;

        Button logoutBtn;

        View stAffairView;

        View taskCenterView;

        View myRecomView;

        View myExpView;

        View myActivityView;

        View peopleNearbyView;

        View personalSettingvView;

    }

}

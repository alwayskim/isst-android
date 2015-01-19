/**
 *
 */
package cn.edu.zju.isst1.v2.usercenter;

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
import cn.edu.zju.isst1.v2.usercenter.messagecenter.gui.PushMessagesActivity;
import cn.edu.zju.isst1.v2.usercenter.myactivity.MyEventActivity;
import cn.edu.zju.isst1.v2.usercenter.myexperience.MyExperienceActivity;
import cn.edu.zju.isst1.v2.usercenter.myrecommend.MyRecommendListActivity;
import cn.edu.zju.isst1.v2.usercenter.setting.SettingActivity;
import cn.edu.zju.isst1.v2.usercenter.twodimensionalcode.example.qr_codescan.MainActivity;
import cn.edu.zju.isst1.v2.usercenter.userinfo.UserInfoActivity;
import cn.edu.zju.isst1.v2.usercenter.taskcenter.gui.TaskCenterActivity;


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
        mViewHolder.taskCenterView = view.findViewById(R.id.user_center_task_center_txv);
        mViewHolder.myRecomView = view.findViewById(R.id.user_center_my_recommend_txv);
        mViewHolder.myExpView = view.findViewById(R.id.user_center_my_experience_txv);
        mViewHolder.myActivityView = view.findViewById(R.id.user_center_my_activity_txv);
        mViewHolder.peopleNearbyView = view.findViewById(R.id.user_center_people_around_txv);
        mViewHolder.personalSettingvView = view.findViewById(R.id.user_center_personal_setting_txv);
        mViewHolder.appAbout = view.findViewById(R.id.user_center_app_about_txv);
        mViewHolder.twoCodeView = view.findViewById(R.id.user_center_two_code_txv);
    }

    private void setUpListener() {
        //显示用户信息
        mViewHolder.userView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().startActivity(
                        new Intent(getActivity(), UserInfoActivity.class));
            }
        });

        //注销
        mViewHolder.logoutBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ((NewMainActivity) getActivity()).logout();
            }
        });

        mViewHolder.taskCenterView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "该功能尚未开通", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), TaskCenterActivity.class);
//                startActivity(intent);
            }
        });

        //我的内推
        mViewHolder.myRecomView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getActivity().startActivity(new Intent(getActivity(), MyRecommendListActivity.class));
                //Toast.makeText(getActivity(), "该功能暂未实现", Toast.LENGTH_SHORT).show();
            }
        });

        //我的经验
        mViewHolder.myExpView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getActivity().startActivity(new Intent(getActivity(), MyExperienceActivity.class));
                //Toast.makeText(getActivity(), "该功能暂未实现", Toast.LENGTH_SHORT).show();
            }
        });

        //活动管理
        mViewHolder.myActivityView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getActivity().startActivity(new Intent(getActivity(), MyEventActivity.class));

                Toast.makeText(getActivity(), "该功能尚未开通", Toast.LENGTH_SHORT).show();
            }
        });

        //附近的人
        mViewHolder.peopleNearbyView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "该功能尚未开通", Toast.LENGTH_SHORT).show();
            }
        });

        //二维码签到
        mViewHolder.twoCodeView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//             getActivity().startActivity(
//                new Intent(getActivity(), MainActivity.class));
                Toast.makeText(getActivity(), "该功能尚未开通", Toast.LENGTH_SHORT).show();
            }
        });

        //设置
        mViewHolder.personalSettingvView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getActivity().startActivity(
                        new Intent(getActivity(), UserInfoActivity.class));
            }
        });

        mViewHolder.appAbout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getActivity().startActivity(
                        new Intent(getActivity(), SettingActivity.class));
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

        View taskCenterView;

        View myRecomView;

        View myExpView;

        View myActivityView;

        View peopleNearbyView;

        View personalSettingvView;

        View appAbout;

        View twoCodeView;

    }

}

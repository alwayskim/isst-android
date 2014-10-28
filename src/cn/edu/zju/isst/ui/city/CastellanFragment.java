/**
 *
 */
package cn.edu.zju.isst.ui.city;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst.R;
import cn.edu.zju.isst.db.City;
import cn.edu.zju.isst.db.DataManager;
import cn.edu.zju.isst.db.User;
import cn.edu.zju.isst.ui.main.NewMainActivity;
import cn.edu.zju.isst.util.Judge;
import cn.edu.zju.isst.util.Lgr;
import cn.edu.zju.isst.v2.globaldata.citylist.CSTCity;
import cn.edu.zju.isst.v2.globaldata.citylist.CSTCityDataDelegate;
import cn.edu.zju.isst.v2.user.data.CSTUser;
import cn.edu.zju.isst.v2.user.data.CSTUserDataDelegate;
import cn.edu.zju.isst.v2.user.data.CSTUserProvider;

/**
 * @author yyy
 */
public class CastellanFragment extends Fragment {

    private static final String PRIVATE_INFO = "未公开";

    private static CastellanFragment INSTANCE = new CastellanFragment();

    private List<CSTCity> mListCity = new ArrayList<CSTCity>();

    private TextView m_tvName;

    private TextView m_tvGender;

    private TextView m_tvGrade;

    private TextView m_tvMajor;

    private TextView m_tvMobile;

    private TextView m_tvEmail;

    private TextView m_tvCity;

    private TextView m_tvCompany;

    private TextView m_tvPosition;

    private ImageButton m_ibtnMobileCall;

    private ImageButton m_ibtnMessage;

    private ImageButton m_ibtnEmail;

    private CSTCity m_city;

    private CSTUser m_user;

    private ArrayList<String> m_arrayListCity = new ArrayList<String>();

    /**
     *
     */
//    public CastellanFragment() {
//        // TODO Auto-generated constructor stub
//        getCityList();
//    }

    public static CastellanFragment GetInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        getCityList();
        SpinnerAdapter adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.action_bar_city_item, m_arrayListCity);
        // 得到ActionBar
        ActionBar actionBar = getActivity().getActionBar();
        // 将ActionBar的操作模型设置为NAVIGATION_MODE_LIST
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // 为ActionBar设置下拉菜单和监听器
        actionBar.setListNavigationCallbacks(adapter, new DropDownListenser());

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.castellan_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // 控件
        m_tvName = (TextView) view
                .findViewById(R.id.castellan_fragment_name_txv);
        m_tvGender = (TextView) view
                .findViewById(R.id.castellan_fragment_gender_txv);
        m_tvGrade = (TextView) view
                .findViewById(R.id.castellan_fragment_grade_txv);
        m_tvMajor = (TextView) view
                .findViewById(R.id.castellan_fragment_major_txv);
        m_tvMobile = (TextView) view
                .findViewById(R.id.castellan_fragment_mobile_txv);
        m_tvCity = (TextView) view
                .findViewById(R.id.castellan_fragment_city_txv);
        m_tvCompany = (TextView) view
                .findViewById(R.id.castellan_fragment_company_txv);
        m_tvEmail = (TextView) view
                .findViewById(R.id.castellan_fragment_email_txv);
        m_ibtnMobileCall = (ImageButton) view
                .findViewById(R.id.castellan_fragment_mobile_ibtn);
        m_tvPosition = (TextView) view
                .findViewById(R.id.castellan_fragment_position_txv);
        m_ibtnMessage = (ImageButton) view
                .findViewById(R.id.castellan_fragment_message_ibtn);
        m_ibtnEmail = (ImageButton) view
                .findViewById(R.id.castellan_fragment_email_ibtn);

        m_ibtnMobileCall.setOnClickListener(new onMobileCallClickListner());
        m_ibtnMessage.setOnClickListener(new onMessageClickListner());
        m_ibtnEmail.setOnClickListener(new onEmailClickListner());

        Cursor mCursor = getActivity().getContentResolver().query(CSTUserProvider.CONTENT_URI,
                null, null, null, null);
        mCursor.moveToFirst();
        m_city = getCity(CSTUserDataDelegate.getUser(mCursor).cityId);
        mCursor.close();
        if (m_city != null) {
            ActionBar actionBar = getActivity().getActionBar();
            actionBar
                    .setSelectedNavigationItem(getCityListIndex(m_city.id));
        }

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        // 得到ActionBar
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        super.onDestroyView();
    }

    /**
     * 显示用户详情
     */
    private void showUserDetail() {
        if (m_city == null) {
            return;
        }
        m_user = m_city.cityMaster;
        if (m_user == null) {
            return;
        }
        // 姓名
        m_tvName.setText(m_user.name);
//        // 性别
//        if (m_user.gender.getKey() > 0) {
//            m_tvGender.setText(m_user.gender.getTypeName());
//        }
        // 年级
//        m_tvGrade.setText(m_user.grade + "级");
        // 专业
        m_tvMajor.setText(m_user.majorName);
        // 电话
        m_tvMobile.setText(m_user.phoneNum);
        // Email
        m_tvEmail.setText(m_user.email);
        // 城市
        m_tvCity.setText(m_city.name);
        // 公司
        m_tvCompany.setText(m_user.company);
        // 职位
        m_tvPosition.setText(m_user.jobTitle);
        if (m_user.pvtPhoneNum) {
            m_tvMobile.setText(PRIVATE_INFO);
            m_ibtnMobileCall.setVisibility(View.GONE);
            m_ibtnMessage.setVisibility(View.GONE);
        }
        if (m_user.pvtEmail) {
            m_tvEmail.setText(PRIVATE_INFO);
            m_ibtnEmail.setVisibility(View.GONE);
        }
        if (m_user.pvtCompany) {
            m_tvCompany.setText(PRIVATE_INFO);
        }
        if (m_user.pvtJobTitle) {
            m_tvPosition.setText(PRIVATE_INFO);
        }
    }

    /**
     * 初始化城市列表
     */
    private void getCityList() {


        mListCity = CSTCityDataDelegate.getCityList(this.getActivity());

        m_arrayListCity.add("城市");
        if (!Judge.isNullOrEmpty(mListCity)) {
            for (CSTCity city : mListCity) {
                m_arrayListCity.add(city.name);
            }
        }
        Lgr.i(" yyy getCityList");
    }

    /**
     * 按cityID获取city
     */
    private CSTCity getCity(int cityID) {
        for (CSTCity city : mListCity) {
            if (city.id == cityID) {
                return city;
            }
        }
        return null;
    }

    private int getCityListIndex(int cityID) {
        int index = 0;
        for (int i = 0; i < mListCity.size(); i++) {
            if (mListCity.get(i).id == m_city.id) {
                index = i + 1;
                break;
            }
        }
        return index;
    }

    /**
     * 实现 ActionBar.OnNavigationListener接口
     */
    private class DropDownListenser implements OnNavigationListener {

        @Override
        public boolean onNavigationItemSelected(int arg0, long arg1) {
            if (arg0 > 0) {
                m_city = mListCity.get(arg0 - 1);
                showUserDetail();
            }
            return false;
        }
    }

    /**
     * 拨打电话Listner
     *
     * @author yyy
     */
    private class onMobileCallClickListner implements OnClickListener {

        @Override
        public void onClick(View v) {
            String number = m_tvMobile.getText().toString();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                    + number));
            startActivity(intent);
        }

    }

    /**
     * 发送短信Listner
     *
     * @author yyy
     */
    private class onMessageClickListner implements OnClickListener {

        @Override
        public void onClick(View v) {
            String number = m_tvMobile.getText().toString();
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
                    + number));
            startActivity(intent);
        }

    }

    /**
     * 发送邮件listner
     *
     * @author yyy
     */
    private class onEmailClickListner implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            String email = m_tvEmail.getText().toString();
            Intent intent = new Intent(Intent.ACTION_SENDTO,
                    Uri.parse("mailto:" + email));
            try {
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

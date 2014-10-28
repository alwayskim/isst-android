package cn.edu.zju.isst.ui.contact;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst.R;
import cn.edu.zju.isst.db.City;
import cn.edu.zju.isst.db.DataManager;
import cn.edu.zju.isst.db.User;
import cn.edu.zju.isst.ui.main.BaseActivity;
import cn.edu.zju.isst.util.Judge;
import cn.edu.zju.isst.util.Lgr;

public class ContactDetailActivity extends BaseActivity {

    private final static String PRIVATE_INFO = "未公开";

    private final List<City> m_listCity = new ArrayList<City>();

    // 用户
    private User m_user;

    // 控件
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

    public ContactDetailActivity() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail_activity);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 初始化城市列表
        getCityList();

        // 用户
        m_user = (User) getIntent().getExtras().getSerializable("user");

        // 控件
        m_tvName = (TextView) findViewById(R.id.contact_detail_activity_name_txv);
        m_tvGender = (TextView) findViewById(R.id.contact_detail_activity_gender_txv);
        m_tvGrade = (TextView) findViewById(R.id.contact_detail_activity_grade_txv);
        m_tvMajor = (TextView) findViewById(R.id.contact_detail_activity_major_txv);
        m_tvMobile = (TextView) findViewById(R.id.contact_detail_activity_mobile_txv);
        m_tvCity = (TextView) findViewById(R.id.contact_detail_activity_city_txv);
        m_tvCompany = (TextView) findViewById(R.id.contact_detail_activity_company_txv);
        m_tvEmail = (TextView) findViewById(R.id.contact_detail_activity_email_txv);
        m_ibtnMobileCall = (ImageButton) findViewById(R.id.contact_detail_activity_mobile_ibtn);
        m_tvPosition = (TextView) findViewById(R.id.contact_detail_activity_position_txv);
        m_ibtnMessage = (ImageButton) findViewById(R.id.contact_detail_activity_message_ibtn);
        m_ibtnEmail = (ImageButton) findViewById(R.id.contact_detail_activity_email_ibtn);

        m_ibtnMobileCall.setOnClickListener(new onMobileCallClickListner());
        m_ibtnMessage.setOnClickListener(new onMessageClickListner());
        m_ibtnEmail.setOnClickListener(new onEmailClickListner());

        // 显示
        showUserDetail();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                ContactDetailActivity.this.finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 初始化城市列表
     */
    private void getCityList() {
        List<City> dbList = DataManager.getCityList();
        if (!Judge.isNullOrEmpty(dbList)) {
            for (City city : dbList) {
                m_listCity.add(city);
            }
        }
        Lgr.i(" yyy getCityList");
    }

    /**
     * 按cityID获取cityName
     */
    private String getCityName(int cityID) {
        String res = null;
        for (int i = 0; i < m_listCity.size(); i++) {
            if (m_listCity.get(i).getId() == cityID) {
                res = m_listCity.get(i).getName();
                break;
            }
        }
        return res;
    }

    /**
     * 显示用户详情
     */
    private void showUserDetail() {
        if (Judge.isNullOrEmpty(m_user)) {
            return;
        }
        // 姓名
        m_tvName.setText(m_user.getName());
        // 性别
        if (m_user.getGender() > 0) {
            m_tvGender.setText(m_user.getGender() == 1 ? "男" : "女");
        }
        // 年级
        m_tvGrade.setText("" + m_user.getGrade() + "级");
        // 专业
        m_tvMajor.setText(m_user.getMajor());
        // 电话
        m_tvMobile.setText(m_user.getPhone());
        // Email
        m_tvEmail.setText(m_user.getEmail());
        // 城市
        int cityID = m_user.getCityId();
        m_tvCity.setText(getCityName(cityID));
        // 公司
        m_tvCompany.setText(m_user.getCompany());
        // 职位
        m_tvPosition.setText(m_user.getPosition());
        if (m_user.isPrivatePhone()) {
            m_tvMobile.setText(PRIVATE_INFO);
            m_ibtnMobileCall.setVisibility(View.GONE);
            m_ibtnMessage.setVisibility(View.GONE);
        }
        if (m_user.isPrivateEmail()) {
            m_tvEmail.setText(PRIVATE_INFO);
            m_ibtnEmail.setVisibility(View.GONE);
        }
        if (m_user.isPrivateCompany()) {
            m_tvCompany.setText(PRIVATE_INFO);
        }
        if (m_user.isPrivatePosition()) {
            m_tvPosition.setText(PRIVATE_INFO);
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
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
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

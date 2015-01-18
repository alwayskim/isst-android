package cn.edu.zju.isst1.v2.contact.contact.gui;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTAlumni;
import cn.edu.zju.isst1.v2.globaldata.citylist.CSTCity;
import cn.edu.zju.isst1.v2.globaldata.citylist.CSTCityDataDelegate;


/**
 * Created by tan on 2014/8/27.
 */
public class CSTContactDetailActivity extends BaseActivity {

    private final static String PRIVATE_INFO = "未公开";

    private final List<CSTCity> m_listCity = new ArrayList<CSTCity>();

    private CSTAlumni mAlumni;

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

    public CSTContactDetailActivity() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        setUpActionBar();
        // 用户
        mAlumni = (CSTAlumni) getIntent().getExtras().getSerializable("alumni");
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
        showAlumniDetail();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                CSTContactDetailActivity.this.finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void setUpActionBar() {
        super.setUpActionBar();
        setTitle("详细资料");
    }

    /**
     * 显示用户详情
     */
    private void showAlumniDetail() {
        if (Judge.isNullOrEmpty(mAlumni)) {
            return;
        }
        // 姓名
        m_tvName.setText(mAlumni.name);
        // 性别
        m_tvGender.setText(mAlumni.gender.getTypeName());
        // 年级
        m_tvGrade.setText("" + mAlumni.grade + "级");
        // 专业
        m_tvMajor.setText(mAlumni.majorName);
        // 电话
        m_tvMobile.setText(mAlumni.phoneNum);
        // Email
        m_tvEmail.setText(mAlumni.email);

        // 城市
        List<CSTCity> cityList = CSTCityDataDelegate.getCityList(this);
        String cityName = "";
        for (CSTCity city : cityList) {
            if (city.id == mAlumni.cityId) {
                cityName = city.name;
            }
        }
        m_tvCity.setText(cityName);
        // 公司
        m_tvCompany.setText(mAlumni.company);
        // 职位
        m_tvPosition.setText(mAlumni.jobTitle);

        if (mAlumni.pvtPhone||mAlumni.phoneNum.equals(R.string.information_protect)) {
            m_ibtnMobileCall.setVisibility(View.GONE);
            m_ibtnMessage.setVisibility(View.GONE);
        }
        if (mAlumni.pvtEmail||mAlumni.email.equals(R.string.information_protect)) {
            m_ibtnEmail.setVisibility(View.GONE);
        }
//        if (mAlumni.pvtCompany || Judge.isNullOrEmpty(mAlumni.company)) {
//            m_tvCompany.setText(PRIVATE_INFO);
//        }
//        if (mAlumni.pvtPosition || Judge.isNullOrEmpty(mAlumni.jobTitle)) {
//            m_tvPosition.setText(PRIVATE_INFO);
//        }
    }

    /**
     * 拨打电话Listner
     *
     * @author yyy
     */
    private class onMobileCallClickListner implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String number = mAlumni.phoneNum;

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                    + number));
            startActivity(intent);
            try {
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                CroMan.showAlert(CSTContactDetailActivity.this,"通讯异常");
                Lgr.i("phone error:", e.toString());
            }
        }

    }

    /**
     * 发送短信Listner
     *
     * @author yyy
     */
    private class onMessageClickListner implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String number = mAlumni.phoneNum;
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
                    + number));
            try {
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                CroMan.showAlert(CSTContactDetailActivity.this,"打开短信异常");
                Lgr.i("message error:", e.toString());
            }
        }

    }

    /**
     * 发送邮件listner
     *
     * @author yyy
     */
    private class onEmailClickListner implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            String email = m_tvEmail.getText().toString();
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + mAlumni.email));
            try {
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                CroMan.showAlert(CSTContactDetailActivity.this,"此设备无Email客户端");
                Lgr.i("Email error:", e.toString());
            }
        }

    }
}

/**
 *
 */
package cn.edu.zju.isst1.ui.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.constant.Constants;
import cn.edu.zju.isst1.db.City;
import cn.edu.zju.isst1.db.DataManager;
import cn.edu.zju.isst1.db.Major;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;

/**
 * @author yyy
 */
public class ContactFilterActivity extends BaseActivity {

    private final List<City> m_listCity = new ArrayList<City>();

    private final List<Major> m_listMajor = new ArrayList<Major>();

    private ArrayList<String> m_arrayListCity = new ArrayList<String>();

    private ArrayList<String> m_arrayListMajor = new ArrayList<String>();

    private ArrayList<String> m_arrayListGrade = new ArrayList<String>();

    // 控件
    private EditText m_edtName;

    private RadioGroup m_rdgGender;

    private Spinner m_spnGrade;

    private EditText m_edtCompany;

    private Spinner m_spnMajor;

    private Spinner m_spnCity;

    private Button m_btnOK;

    private Button m_btnCancel;

    /**
     *
     */
    public ContactFilterActivity() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setUpActivity();

        // 获取控件
        m_edtName = (EditText) findViewById(R.id.contact_filter_activity_name_edtx);
        m_rdgGender = (RadioGroup) findViewById(R.id.contact_filter_activity_gender_rdg);
        m_spnGrade = (Spinner) findViewById(R.id.contact_filter_activity_grade_spn);
        m_spnMajor = (Spinner) findViewById(R.id.contact_filter_activity_major_spn);
        m_spnCity = (Spinner) findViewById(R.id.contact_filter_activity_city_spn);
        m_edtCompany = (EditText) findViewById(R.id.contact_filter_activity_company_edtx);
        m_btnOK = (Button) findViewById(R.id.contact_filter_activity_confirm_btn);
        m_btnCancel = (Button) findViewById(R.id.contact_filter_activity_cancel_btn);

        // 按钮监听
        m_btnOK.setOnClickListener(new onBtnOkClickListener());
        m_btnCancel.setOnClickListener(new onBtnCancelClickListener());

        // 获取数据库中的城市列表，专业列表
        getCityList();
        getMajorList();
        //获取年级列表
        getGradeList();

        // 设置下拉框
        initSpanner(m_spnCity, m_arrayListCity);
        initSpanner(m_spnMajor, m_arrayListMajor);
        initSpanner(m_spnGrade, m_arrayListGrade);
    }

    /**
     * 自定义函数，初始化下拉框
     *
     * @param spanner 下拉框控件
     * @param list    绑定字符串数组
     */
    private void initSpanner(Spinner spanner, ArrayList<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        // 设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将adapter 添加到spinner中
        spanner.setAdapter(adapter);
    }

    /**
     * 初始化城市列表
     */
    private void getCityList() {
        List<City> dbList = DataManager.getCityList();
        m_arrayListCity.add("不限");

        if (!Judge.isNullOrEmpty(dbList)) {
            for (City city : dbList) {
                m_listCity.add(city);
                m_arrayListCity.add(city.getName());
            }
        }
        Lgr.i(" yyy getCityList");
    }

    /**
     * 初始化专业列表
     */
    private void getMajorList() {
        List<Major> dbList = DataManager.getMajorList();
        m_arrayListMajor.add("不限");
        if (!Judge.isNullOrEmpty(dbList)) {
            for (Major major : dbList) {
                m_listMajor.add(major);
                m_arrayListMajor.add(major.getName());
            }
        }
        Lgr.i(" yyy getMajorList");
    }

    /**
     * 设置年级列表,假设从2009年开始
     */
    private void getGradeList() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        m_arrayListGrade.add("不限");
        for (int i = 2009; i < year; i++) {
            m_arrayListGrade.add(String.valueOf(i));
        }

    }

    private void setUpActivity() {
        // requestWindowFeature(Window.FEATURE_ACTION_BAR);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
        // WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // LayoutParams params = getWindow().getAttributes();
        // params.softInputMode = LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
        // params.height = LayoutParams.MATCH_PARENT; // fixed height
        // params.width = LayoutParams.MATCH_PARENT; // fixed width
        // params.alpha = 1.0f;
        // params.dimAmount = 0.5f;
        // getWindow().setAttributes(params);
        setContentView(R.layout.contact_filter_activity);
    }

    class onBtnOkClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            Intent data = new Intent();
            ContactFilter uf = new ContactFilter();

            // 姓名
            String name = m_edtName.getText().toString().trim();
            // 获取性别ID的方法可能有其他的，现在不好
            int genderId = 0;
            String genderString = "";
            int radioBtnId = m_rdgGender.getCheckedRadioButtonId();
            switch (radioBtnId) {
                case R.id.contact_filter_activity_gender_unset_rdbtn:
                    break;
                case R.id.contact_filter_activity_gender_male_rdbtn:
                    genderId = 1;
                    genderString = "男";
                    break;
                case R.id.contact_filter_activity_gender_female_rdbtn:
                    genderId = 2;
                    genderString = "女";
                    break;
                default:
                    break;
            }
            // 城市ID
            int cityId = 0;
            String cityString = "";
            int selectedCityPosition = m_spnCity.getSelectedItemPosition() - 1;
            if (selectedCityPosition >= 0) {
                cityId = (m_listCity.get(selectedCityPosition)).getId();
                cityString = (m_listCity.get(selectedCityPosition)).getName();
            }
            // 专业
            String major = "";
            int selectedmajorPosition = m_spnMajor.getSelectedItemPosition() - 1;
            if (selectedmajorPosition >= 0) {
                major = (m_listMajor.get(selectedmajorPosition))
                        .getName();
            }
            // 年级Id
            int grade = 0;
            int selectGradePosition = m_spnGrade.getSelectedItemPosition();
            if (selectGradePosition >= 1) {
                grade = Integer.valueOf(m_arrayListGrade.get(selectGradePosition));
            }
            // 公司
            String company = m_edtCompany.getText().toString().trim();

            uf.name = name;
            uf.gender = genderId;
            uf.cityId = cityId;
            uf.grade = grade;
            uf.major = major;
            uf.company = company;
            uf.cityString = cityString;
            uf.genderString = genderString;

            data.putExtra("data", (Serializable) uf);
            setResult(Constants.RESULT_CODE_BETWEEN_CONTACT, data);

            // 关闭掉这个Activity
            finish();
        }
    }

    class onBtnCancelClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            ContactFilterActivity.this.finish();
        }
    }
}

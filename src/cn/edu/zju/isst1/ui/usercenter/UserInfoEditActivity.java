/**
 *
 */
package cn.edu.zju.isst1.ui.usercenter;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.db.City;
import cn.edu.zju.isst1.db.DataManager;
import cn.edu.zju.isst1.db.User;
import cn.edu.zju.isst1.net.CSTResponse;
import cn.edu.zju.isst1.net.RequestListener;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.v2.globaldata.citylist.CSTCity;
import cn.edu.zju.isst1.v2.globaldata.citylist.CSTCityDataDelegate;
import cn.edu.zju.isst1.v2.net.CSTStatusInfo;
import cn.edu.zju.isst1.v2.user.data.CSTUser;
import cn.edu.zju.isst1.v2.user.data.CSTUserDataDelegate;
import cn.edu.zju.isst1.v2.user.net.UserApi;
import cn.edu.zju.isst1.v2.user.net.UserResponse;

import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * @author theasir
 */
public class UserInfoEditActivity extends BaseActivity {

    private CSTUser m_userCurrent;

    private List<CSTCity> m_listCity = new ArrayList<CSTCity>();

    private List<String> m_listCityString = new ArrayList<String>();

    private Handler m_handler;

    private Button m_btnDone;

    private Button m_btnCancel;

    private Spinner m_spnCity;

    private EditText m_edtxEmail;

    private EditText m_edtxPhone;

    private EditText m_edtxQq;

    private EditText m_edtxCompany;

    private EditText m_edtxPosition;

    private EditText m_edtxSignature;

    private CheckBox m_chbPublicEmail;

    private CheckBox m_chbPublicPhone;

    private CheckBox m_chbPublicQq;

    private CheckBox m_chbPublicCompany;

    private CheckBox m_chbPublicPosition;

    private ProgressDialog m_pgdWating;

    /*
     * (non-Javadoc)
     *
     * @see cn.edu.zju.isst1.ui.main.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_edit_activity);

        setUpActionBar();

        initComponent();

        initUser();

        initHandler();

        initCityList();

        initSpanner(m_spnCity, m_listCityString);

        setUpListener();

        bindData();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.user_info_edit_custom_actionbar);
    }

    private View getActionBarView() {
        return getActionBar().getCustomView();
    }

    private void initComponent() {
        m_btnDone = (Button) getActionBarView().findViewById(
                R.id.user_info_edit_custom_actionbar_action_done_btn);
        m_btnCancel = (Button) getActionBarView().findViewById(
                R.id.user_info_edit_custom_actionbar_action_cancel_btn);

        m_spnCity = (Spinner) findViewById(R.id.user_info_edit_activity_city_spn);

        m_edtxEmail = (EditText) findViewById(R.id.user_info_edit_activity_email_edtx);
        m_edtxPhone = (EditText) findViewById(R.id.user_info_edit_activity_phone_edtx);
        m_edtxQq = (EditText) findViewById(R.id.user_info_edit_activity_qq_edtx);
        m_edtxCompany = (EditText) findViewById(R.id.user_info_edit_activity_company_edtx);
        m_edtxPosition = (EditText) findViewById(R.id.user_info_edit_activity_position_edtx);
        m_edtxSignature = (EditText) findViewById(R.id.user_info_edit_activity_signature_edtx);
        m_chbPublicEmail = (CheckBox) findViewById(R.id.user_info_edit_activity_email_chb);
        m_chbPublicPhone = (CheckBox) findViewById(R.id.user_info_edit_activity_phone_chb);
        m_chbPublicQq = (CheckBox) findViewById(R.id.user_info_edit_activity_qq_chb);
        m_chbPublicCompany = (CheckBox) findViewById(R.id.user_info_edit_activity_company_chb);
        m_chbPublicPosition = (CheckBox) findViewById(R.id.user_info_edit_activity_position_chb);
    }

    private void initUser() {
        m_userCurrent = getIntent().hasExtra("currentUser") ? (CSTUser) getIntent()
                .getSerializableExtra("currentUser") : CSTUserDataDelegate.getCurrentUser(this);
    }

    private void initHandler() {
        m_handler = new Handler() {

            /*
             * (non-Javadoc)
             *
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        complete();
                        break;
                    case STATUS_NOT_LOGIN:
                        updateLogin();
                        sendRequest(m_userCurrent);
                        break;
                    default:
                        m_pgdWating.dismiss();
                        dispose(msg);
                        break;
                }
            }

        };
    }

    private void setUpListener() {
        m_btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isValidEmail(m_edtxEmail.getText().toString())
                        && isValidPhone(m_edtxPhone.getText().toString())) {
                    m_pgdWating = ProgressDialog.show(
                            UserInfoEditActivity.this,
                            getString(R.string.loading),
                            getString(R.string.please_wait), true, false);
                    retriveData();
                    sendRequest(m_userCurrent);
                } else if (!isValidEmail(m_edtxEmail.getText().toString())) {
                    CroMan.showInfo(UserInfoEditActivity.this, "请输入有效的电子邮箱地址！");
                } else {
                    CroMan.showInfo(UserInfoEditActivity.this, "请输入有效的移动电话号码！");
                }
            }
        });

        m_btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void initSpanner(Spinner spanner, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spanner.setAdapter(adapter);
    }

    private void initCityList() {
        List<CSTCity> dbList = CSTCityDataDelegate.getCityList(this);
        if (!Judge.isNullOrEmpty(dbList)) {
            for (CSTCity city : dbList) {
                m_listCity.add(city);
                m_listCityString.add(city.name);
            }
        }
        m_listCityString.add("其他");
    }

    private void bindData() {
        m_spnCity.setSelection(getPositionForCityId(m_userCurrent.cityId));

        m_edtxEmail.setText(m_userCurrent.email);
        m_edtxPhone.setText(m_userCurrent.phoneNum);
        m_edtxQq.setText(m_userCurrent.qqNum);
        m_edtxCompany.setText(m_userCurrent.company);
        m_edtxPosition.setText(m_userCurrent.jobTitle);
        m_edtxSignature.setText(m_userCurrent.sign);

        m_chbPublicEmail.setChecked(!m_userCurrent.pvtEmail);
        m_chbPublicPhone.setChecked(!m_userCurrent.pvtPhoneNum);
        m_chbPublicQq.setChecked(!m_userCurrent.pvtQq);
        m_chbPublicCompany.setChecked(!m_userCurrent.pvtCompany);
        m_chbPublicPosition.setChecked(!m_userCurrent.pvtJobTitle);
    }

    private void retriveData() {
        m_userCurrent.cityId = m_spnCity.getSelectedItemPosition() < m_listCity
                .size() ? m_listCity.get(m_spnCity.getSelectedItemPosition()).id : 0;
//                .setCityId(m_spnCity.getSelectedItemPosition() < m_listCity
//                        .size() ? m_listCity.get(
//                        m_spnCity.getSelectedItemPosition()).getId() : 0);
        m_userCurrent.cityName = m_spnCity.getSelectedItemPosition() < m_listCity
                .size() ? m_listCity.get(m_spnCity.getSelectedItemPosition()).name:"其他";

        m_userCurrent.email = m_edtxEmail.getText().toString();
//                setEmail(m_edtxEmail.getText().toString());
        m_userCurrent.phoneNum = m_edtxPhone.getText().toString();
//                setPhone(m_edtxPhone.getText().toString());
        m_userCurrent.qqNum = m_edtxQq.getText().toString();
//                setQq(m_edtxQq.getText().toString());
        m_userCurrent.company = m_edtxCompany.getText().toString();
//                setCompany(m_edtxCompany.getText().toString());
        m_userCurrent.jobTitle = m_edtxPosition.getText().toString();
//                setPosition(m_edtxPosition.getText().toString());
        m_userCurrent.sign = m_edtxSignature.getText().toString();
//                setSignature(m_edtxSignature.getText().toString());

        m_userCurrent.pvtEmail = !m_chbPublicEmail.isChecked();
//                setPrivateEmail(!m_chbPublicEmail.isChecked());
        m_userCurrent.pvtPhoneNum = !m_chbPublicPhone.isChecked();
//                setPrivatePhone(!m_chbPublicPhone.isChecked());
        m_userCurrent.pvtQq = !m_chbPublicQq.isChecked();
//                setPrivateQQ(!m_chbPublicQq.isChecked());
        m_userCurrent.pvtCompany = !m_chbPublicCompany.isChecked();
//                setPrivateCompany(!m_chbPublicCompany.isChecked());
        m_userCurrent.pvtJobTitle = !m_chbPublicPosition.isChecked();
//                setPrivatePosition(!m_chbPublicPosition.isChecked());
    }

    private void sendRequest(CSTUser currentUser) {
//        UserApi.update(currentUser, new RequestListener() {
//
//            @Override
//            public void onComplete(Object result) {
//                Message msg = m_handler.obtainMessage();
//                try {
//                    final int status = ((JSONObject) result).getInt("status");
//                    msg.what = status;
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//                m_handler.sendMessage(msg);
//            }
//
//            @Override
//            public void onHttpError(CSTResponse response) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onException(Exception e) {
//                // TODO Auto-generated method stub
//
//            }
//        });
        UserApi.update(m_userCurrent, new UserResponse(this, true) {
            @Override
            public void onResponse(JSONObject response) {
                Message msg = m_handler.obtainMessage();
                try {
                    msg.what = response.getInt("status");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                m_handler.sendMessage(msg);
            }

            @Override
            public Object onErrorStatus(CSTStatusInfo statusInfo) {
                return super.onErrorStatus(statusInfo);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }

            @Override
            public void onSuccessStatus() {
                super.onSuccessStatus();
            }
        });
    }

    private void complete() {
//        DataManager.syncCurrentUser(m_userCurrent);
        CSTUserDataDelegate.deleteAllUsers(this);
        CSTUserDataDelegate.saveUser(this, m_userCurrent);
        m_pgdWating.dismiss();

        Intent intent = new Intent();
        intent.putExtra("updatedUser", m_userCurrent);
        setResult(UserInfoActivity.RESULT_CODE_DONE, intent);
        UserInfoEditActivity.this.finish();
    }

    private void cancel() {
        setResult(UserInfoActivity.RESULT_CODE_CANCEL, null);
        UserInfoEditActivity.this.finish();
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern
                .compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]{2,3}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile("^13\\d{9}||15\\d{9}||18\\d{9}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    private int getPositionForCityId(int cityId) {
        for (int i = 0; i < m_listCity.size(); i++) {
            if (cityId == m_listCity.get(i).id) {
                return i;
            }
        }
        return m_listCity.size();
    }
}

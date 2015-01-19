/**
 *
 */
package cn.edu.zju.isst1.v2.login.gui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.db.DataManager;
import cn.edu.zju.isst1.db.User;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.settings.CSTSettings;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.ui.main.NewMainActivity;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.net.CSTStatusInfo;
import cn.edu.zju.isst1.v2.user.data.CSTUser;
import cn.edu.zju.isst1.v2.user.data.CSTUserDataDelegate;
import cn.edu.zju.isst1.v2.user.net.UserResponse;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_LOGIN_AUTH_EXPIRED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_LOGIN_AUTH_FAILED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_LOGIN_PASSWORD_ERROR;
import static cn.edu.zju.isst1.constant.Constants.STATUS_LOGIN_USERNAME_NOT_EXIST;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * 登录页面
 *
 * @author theasir
 */
public class LoginActivity extends BaseActivity {

    private boolean m_bIsLoginAgain;

    private String m_strUserName;

    private char[] m_strPassword;

    private Handler m_handlerLogin;

    private EditText m_edtxUserName;

    private EditText m_edtxPassword;

    private CheckBox m_chbAutologin;

    private ImageButton m_btnLogin;

    private ProgressDialog m_pgdWating;

    /*
     * (non-Javadoc)
     *
     * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // 为什么要这么做？参看CSTSettings类以及SharedPreferences#getBoolean(String key,
        // boolean defValue)
        if (CSTSettings.isForTheFirstTime(LoginActivity.this)) {
            CSTSettings.setForTheFirstTime(true, LoginActivity.this);
        }

        m_bIsLoginAgain = getIntent().getBooleanExtra("isLoginAgain", false);

        if (getIntent().getBooleanExtra("isAuthFailed", false)){
            CroMan.showAlert(this,"密码错误");
        }

//        if (m_bIsLoginAgain) {
//            ActionBar actionBar = getActionBar();
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        initComponent();

        initHandler();

        setUpListener();

    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (m_bIsLoginAgain) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    LoginActivity.this.finish();
                    return true;
                default:
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initComponent() {
        m_edtxUserName = (EditText) findViewById(R.id.login_activity_username_input);
        m_edtxPassword = (EditText) findViewById(R.id.login_activity_password_input);
        m_chbAutologin = (CheckBox) findViewById(R.id.login_activity_autologin_chb);
        if (CSTSettings.isAutoLogin(LoginActivity.this)) {
            m_chbAutologin.setChecked(true);
        }
        m_btnLogin = (ImageButton) findViewById(R.id.login_activity_login_btn);
    }

    private void initHandler() {
        m_handlerLogin = new Handler() {

            /*
             * (non-Javadoc)
             *
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
            @Override
            public void handleMessage(Message msg) {
                m_pgdWating.dismiss();

                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        if (m_chbAutologin.isChecked()) {
                            CSTSettings.setAutoLogin(true, LoginActivity.this);
                        } else {
                            CSTSettings.setAutoLogin(false, LoginActivity.this);
                        }
                        try {
                            DataManager.syncCurrentUser(
                                    new User(((JSONObject) msg.obj)
                                            .getJSONObject("body"))
                            );
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (!m_bIsLoginAgain) {
                            LoginActivity.this.startActivity(new Intent(
                                    LoginActivity.this, NewMainActivity.class));
                        }
                        LoginActivity.this.finish();
                        break;
                    case STATUS_LOGIN_USERNAME_NOT_EXIST:
                        m_edtxUserName.setText("");
                        m_edtxUserName.requestFocus();
                        m_edtxPassword.setText("");
                        try {
                            CroMan.showAlert(LoginActivity.this,
                                    ((JSONObject) msg.obj).getString("message"));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    case STATUS_LOGIN_PASSWORD_ERROR:
                        m_edtxPassword.setText("");
                        m_edtxPassword.requestFocus();
                        try {
                            CroMan.showAlert(LoginActivity.this,
                                    ((JSONObject) msg.obj).getString("message"));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    case STATUS_LOGIN_AUTH_EXPIRED:
                    case STATUS_LOGIN_AUTH_FAILED:
                        // TODO
                        break;
                    case NETWORK_NOT_CONNECTED:
                        CroMan.showAlert(LoginActivity.this, R.string.network_not_connected);
                        break;
                    default:
                        dispose(msg);
                        break;
                }
            }

        };
    }

    private void setUpListener() {
        m_btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                m_strUserName = m_edtxUserName.getText().toString();
                m_strPassword = m_edtxPassword.getText().toString()
                        .toCharArray();
                if (m_strUserName.trim().length() == 0
                        || String.valueOf(m_strPassword).trim().length() == 0) {
                    AlertDialog.Builder builder = new Builder(
                            LoginActivity.this);
                    builder.setTitle(R.string.login_alertdialog_title);
                    builder.setMessage(R.string.login_alertdialog_message);
                    builder.setPositiveButton(R.string.OK,
                            new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    dialog.dismiss();
                                    // TODO
                                }
                            }
                    );
                    Dialog dialog = builder.create();
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    return;
                }
                m_pgdWating = ProgressDialog.show(LoginActivity.this,
                        getString(R.string.loading),
                        getString(R.string.please_wait), true, false);

                if (NetworkConnection.isNetworkConnected(LoginActivity.this)){
                    UserResponse logResponse = new UserResponse(LoginActivity.this, true){
                        @Override
                        public void onResponse(JSONObject response) {

                            Lgr.i(response.toString());
                            Message msg = m_handlerLogin.obtainMessage();
                            try {
                                msg.what = response.getInt("status");
                                msg.obj = response;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(msg.what == STATUS_REQUEST_SUCCESS){
                                CSTUser user = (CSTUser) CSTJsonParser
                                    .parseJson(response, new CSTUser());
                                CSTUserDataDelegate.deleteAllUsers(mContext);
                                CSTUserDataDelegate.saveUser(mContext, user);
                            }
//                            msg.obj = login;
                            m_handlerLogin.sendMessage(msg);
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            super.onErrorResponse(error);
                            Message msg = m_handlerLogin.obtainMessage();
                            msg.what = mErrorStatusCode;
                            m_handlerLogin.sendMessage(msg);
                        }
                    };
                    cn.edu.zju.isst1.v2.login.net.LoginApi.validate(m_strUserName,
                            String.valueOf(m_strPassword), 0.0, 0.0, logResponse);


                } else {
                    Message msg = m_handlerLogin.obtainMessage();
                    msg.what = NETWORK_NOT_CONNECTED;
                    m_handlerLogin.sendMessage(msg);
                }
            }
        });
    }
}

package cn.edu.zju.isst1.v2.usercenter.myexperience;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.api.UserCenterApi;
import cn.edu.zju.isst1.db.Archive;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.login.net.UpDateLogin;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTJsonResponse;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;

import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;

/**
 * Created by alwayking on 14/12/27.
 */
public class PublishExpActivity extends BaseActivity{
    private Handler m_handler;

    private UserCenterApi m_userApi;

    private final String IS_EDITE = "is_edit";

    private final String EXP_DETAIL = "exp_detail";

    private final String SUB_URL = "/api/users/archives/experience";

    private int mId;

    private boolean mIsEdite;

    private Archive jobDetail;

    private Button m_btnDone;

    private Button m_btnCancel;

    private EditText m_edtxTitle;

    private EditText m_edtxContent;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    /* (non-Javadoc)
     * @see cn.edu.zju.isst1.ui.main.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_exp_acticity_layout);
        mIsEdite = getIntent().getBooleanExtra(IS_EDITE, false);
        setUpActionbar();
        mId = 0;
        if (mIsEdite) {
            jobDetail = (Archive) getIntent().getSerializableExtra(EXP_DETAIL);
            mId = jobDetail.getId();
        }
        m_userApi = new UserCenterApi();
        initComponent();
        initHandler();


        setUpListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                PublishExpActivity.this.finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setUpActionbar() {
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


        if (mIsEdite) {
            m_btnDone.setText("更新");
        }

        m_edtxTitle = (EditText) findViewById(R.id.publish_recommend_title);
        m_edtxContent = (EditText) findViewById(R.id.publish_recommend_content);

        if (mIsEdite) {
            m_edtxTitle.setText(jobDetail.getTitle());
            m_edtxContent.setText(jobDetail.getContent());
        }

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
                    case 0:
                        if (mIsEdite) {
                            CroMan.showInfo(PublishExpActivity.this, "提交成功！");
                        }
                        PublishExpActivity.this.finish();
                        break;

                    case STATUS_NOT_LOGIN:
                        UpDateLogin.getInstance().updateLogin(PublishExpActivity.this);
                        sendRequest();
                        break;
                    default:
                        CSTHttpUtil.dispose(msg.what, PublishExpActivity.this);
                        break;
                }
            }

        };
    }





    private void setUpListener() {
        m_btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequest();
            }
        });

        m_btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PublishExpActivity.this.finish();
            }
        });
    }

    private void sendRequest() {
        String title = m_edtxTitle.getText().toString();
        String content = m_edtxContent.getText().toString();
        Map<String, String> paramsMap = new HashMap<String, String>();

        paramsMap.put("id", "" + mId);
        paramsMap.put("title", title);
        paramsMap.put("content", content);

        CSTJsonResponse response = new CSTJsonResponse(this) {
            @Override
            public void onResponse(JSONObject result) {
                super.onResponse(result);
                Lgr.i(result.toString());
                Message msg = m_handler.obtainMessage();
                try {
                    msg.what = result.getInt("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                m_handler.sendMessage(msg);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
                Message msg = m_handler.obtainMessage();
                msg.what = mErrorStatusCode;
                m_handler.sendMessage(msg);
            }
        };

        CSTJsonRequest detailRequest = new CSTJsonRequest(CSTRequest.Method.POST,
                SUB_URL, paramsMap, response);
        mEngine.requestJson(detailRequest);

    }
}

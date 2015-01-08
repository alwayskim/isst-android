package cn.edu.zju.isst1.v2.usercenter.myrecommend;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.api.UserCenterApi;
import cn.edu.zju.isst1.db.Job;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.globaldata.citylist.CSTCity;
import cn.edu.zju.isst1.v2.globaldata.citylist.CSTCityDataDelegate;
import cn.edu.zju.isst1.v2.login.net.UpDateLogin;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTJsonResponse;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;

import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;

public class PublishRecommendActivity extends BaseActivity {

    private Handler m_handler;

    private UserCenterApi m_userApi;

    private final String IS_EDITE = "is_edit";

    private final String RECOMMEND_DETAIL = "recommend_detail";

    private final String SUB_URL = "/api/users/jobs/recommend";

    private int mId;

    private boolean mIsEdite;

    private Job jobDetail;

    private List<CSTCity> m_listCity = new ArrayList<CSTCity>();

    private List<String> m_listCityString = new ArrayList<String>();

    private Button m_btnDone;

    private Button m_btnCancel;

    private EditText m_edtxTitle;

    private EditText m_edtxContent;

    private EditText m_edtxCompany;

    private EditText m_edtxPosition;

    private Spinner m_spnCity;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    /* (non-Javadoc)
     * @see cn.edu.zju.isst1.ui.main.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_publish_recommend_activity);
        mIsEdite = getIntent().getBooleanExtra(IS_EDITE, false);
        mId = 0;
        if (mIsEdite) {
            jobDetail = (Job) getIntent().getSerializableExtra(RECOMMEND_DETAIL);
            mId = jobDetail.getId();
        }
        m_userApi = new UserCenterApi();
        setUpActionBar();
        initComponent();
        initHandler();
        initCityList();
        initSpanner(m_spnCity, m_listCityString);
        setUpListener();
    }

    @Override
    protected void setUpActionBar() {
        super.setUpActionBar();
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
        m_edtxCompany = (EditText) findViewById(R.id.publish_recommend_company);
        m_edtxPosition = (EditText) findViewById(R.id.publish_recommend_position);
        m_spnCity = (Spinner) findViewById(R.id.publish_recommend_city);

        if (mIsEdite) {
            m_edtxTitle.setText(jobDetail.getTitle());
            m_edtxContent.setText(jobDetail.getContent());
            m_edtxCompany.setText(jobDetail.getCompany());
            m_edtxPosition.setText(jobDetail.getTitle());
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
                            CroMan.showInfo(PublishRecommendActivity.this, "提交成功！");
                        }
                        PublishRecommendActivity.this.finish();
                        break;

                    case STATUS_NOT_LOGIN:
                        UpDateLogin.getInstance().updateLogin(PublishRecommendActivity.this);
                        sendRequest();
                        break;
                    default:
                        CSTHttpUtil.dispose(msg.what, PublishRecommendActivity.this);
                        break;
                }
            }

        };
    }

    private void initSpanner(Spinner spanner, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spanner.setAdapter(adapter);

        if (mIsEdite) {
            for (int i = 0; i < m_listCity.size(); i++) {
                if (m_listCity.get(i).id == jobDetail.getCityId()) {
                    spanner.setSelection(i, true);
                    break;
                }
            }
        }

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
                PublishRecommendActivity.this.finish();
            }
        });
    }

    private void sendRequest() {
        String title = m_edtxTitle.getText().toString();
        String content = m_edtxContent.getText().toString();
        String company = m_edtxCompany.getText().toString();
        String position = m_edtxPosition.getText().toString();
        int cityId = m_spnCity.getSelectedItemPosition() < m_listCity
                .size() ? m_listCity.get(
                m_spnCity.getSelectedItemPosition()).id : 0;
        Map<String, String> paramsMap = new HashMap<String, String>();

        paramsMap.put("id", "" + mId);
        paramsMap.put("title", title);
        paramsMap.put("content", content);
        paramsMap.put("company", company);
        paramsMap.put("position", position);
        paramsMap.put("cityId", "" + cityId);


//        m_userApi.publishRecommend(mId, title, content, company, position, cityId,
//                new RequestListener() {
//
//                    @Override
//                    public void onComplete(Object result) {
//                        // TODO Auto-generated method stub
//                        Message msg = m_handler.obtainMessage();
//                        try {
//                            final int status = ((JSONObject) result).getInt("status");
//                            msg.obj = ((JSONObject) result).getString("message");
//                            msg.what = status;
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//
//                        m_handler.sendMessage(msg);
//                    }
//
//                    @Override
//                    public void onHttpError(CSTResponse response) {
//                        // TODO Auto-generated method stub
//
//                    }
//
//                    @Override
//                    public void onException(Exception e) {
//                        // TODO Auto-generated method stub
//
//                    }
//                }
//        );

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

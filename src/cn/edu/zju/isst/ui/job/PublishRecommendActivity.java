package cn.edu.zju.isst.ui.job;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst.R;
import cn.edu.zju.isst.api.UserCenterApi;
import cn.edu.zju.isst.db.City;
import cn.edu.zju.isst.db.DataManager;
import cn.edu.zju.isst.net.CSTResponse;
import cn.edu.zju.isst.net.RequestListener;
import cn.edu.zju.isst.ui.main.BaseActivity;
import cn.edu.zju.isst.util.CroMan;
import cn.edu.zju.isst.util.Judge;

public class PublishRecommendActivity extends BaseActivity {

    private Handler m_handler;

    private UserCenterApi m_userApi;

    private List<City> m_listCity = new ArrayList<City>();

    private List<String> m_listCityString = new ArrayList<String>();

    private Button m_btnDone;

    private Button m_btnCancel;

    private EditText m_edtxTitle;

    private EditText m_edtxContent;

    private EditText m_edtxCompany;

    private EditText m_edtxPosition;

    private Spinner m_spnCity;

    /* (non-Javadoc)
     * @see cn.edu.zju.isst.ui.main.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_publish_recommend_activity);
        m_userApi = new UserCenterApi();
        setUpActionBar();
        initComponent();
        initHandler();
        initCityList();
        initSpanner(m_spnCity, m_listCityString);
        setUpListener();
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

        m_edtxTitle = (EditText) findViewById(R.id.publish_recommend_title);
        m_edtxContent = (EditText) findViewById(R.id.publish_recommend_content);
        m_edtxCompany = (EditText) findViewById(R.id.publish_recommend_company);
        m_edtxPosition = (EditText) findViewById(R.id.publish_recommend_position);
        m_spnCity = (Spinner) findViewById(R.id.publish_recommend_city);
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
                        CroMan.showInfo(PublishRecommendActivity.this, msg.obj.toString());
                        PublishRecommendActivity.this.finish();
                        break;

                    default:
                        CroMan.showInfo(PublishRecommendActivity.this, msg.obj.toString());
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
    }

    private void initCityList() {
        List<City> dbList = DataManager.getCityList();
        if (!Judge.isNullOrEmpty(dbList)) {
            for (City city : dbList) {
                m_listCity.add(city);
                m_listCityString.add(city.getName());
            }
        }
        m_listCityString.add("其他");
    }

    private void setUpListener() {
        m_btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String title = m_edtxTitle.getText().toString();
                String content = m_edtxContent.getText().toString();
                String company = m_edtxCompany.getText().toString();
                String position = m_edtxPosition.getText().toString();
                int cityId = m_spnCity.getSelectedItemPosition() < m_listCity
                        .size() ? m_listCity.get(
                        m_spnCity.getSelectedItemPosition()).getId() : 0;

                m_userApi.publishRecommend(0, title, content, company, position, cityId,
                        new RequestListener() {

                            @Override
                            public void onComplete(Object result) {
                                // TODO Auto-generated method stub
                                Message msg = m_handler.obtainMessage();
                                try {
                                    final int status = ((JSONObject) result).getInt("status");
                                    msg.obj = ((JSONObject) result).getString("message");
                                    msg.what = status;
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                m_handler.sendMessage(msg);
                            }

                            @Override
                            public void onHttpError(CSTResponse response) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onException(Exception e) {
                                // TODO Auto-generated method stub

                            }
                        }
                );
            }
        });

        m_btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PublishRecommendActivity.this.finish();
            }
        });
    }

}

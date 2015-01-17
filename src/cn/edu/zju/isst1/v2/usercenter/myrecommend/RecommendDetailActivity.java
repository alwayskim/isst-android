/**
 *
 */
package cn.edu.zju.isst1.v2.usercenter.myrecommend;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.api.JobApi;
import cn.edu.zju.isst1.db.Job;
import cn.edu.zju.isst1.db.User;
import cn.edu.zju.isst1.net.CSTResponse;
import cn.edu.zju.isst1.net.RequestListener;
import cn.edu.zju.isst1.ui.contact.ContactDetailActivity;
import cn.edu.zju.isst1.ui.job.JobCommentListActivity;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.v2.data.CSTJob;
import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTJsonResponse;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;

import static cn.edu.zju.isst1.constant.Constants.PUBLISHER_NAME;
import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * 归档详情页
 *
 * @author theasir
 *         <p/>
 *         TODO WIP
 */
public class RecommendDetailActivity extends BaseActivity {

    /**
     * 归档id
     */
    private int m_nId;

    private boolean mEditable;

    private Job m_jobCurrent;

    private CSTJob mJobCurrent;

    private User m_jobPublisher;

    private Handler m_handlerJobDetail;

    private TextView m_txvTitle;

    private TextView m_txvDate;

    private TextView m_txvPublisher;

    private WebView m_webvContent;

    private ImageView m_imgBtnPublisher;

    private Button m_BtnCommened;

    private boolean m_isEditView;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    /*
     * (non-Javadoc)
     *
     * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_recommend_detail_activity);

        initComponent();

        setUpActionBar();

        // 注意默认值-1，当Intent中没有id时是无效的，故启动这个JobDetailActivity的Activity必须在Intent中放置"id"参数
        m_nId = getIntent().getIntExtra("id", -1);
        mEditable = getIntent().getBooleanExtra("editable", false);
        m_handlerJobDetail = new Handler() {

            /*
             * (non-Javadoc)
             *
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        Lgr.i("Handler Success Archieve id = " + m_jobCurrent.getId());
                        initPublisherBtn();
                        showJobDetail();
                        break;
                    case STATUS_NOT_LOGIN:
                        break;
                    default:
                        break;
                }
            }

        };
        sendRequest();

    }

    @Override
    protected void setUpActionBar() {
        super.setUpActionBar();
        setTitle(R.string.action_bar_recommend_detail);
    }

    /*
         * (non-Javadoc)
         *
         * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
         */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                // Intent intentParent = new Intent(JobDetailActivity.this,
                // MainActivity.class);
                // intentParent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // JobDetailActivity.this.startActivity(intentParent);
                RecommendDetailActivity.this.finish();
                return true;
            }
            case R.id.recommend_list_edt:
                Intent intent = new Intent(RecommendDetailActivity.this, PublishRecommendActivity.class);
                intent.putExtra("recommend_detail", m_jobCurrent);
                intent.putExtra("is_edit", true);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        sendRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mEditable) {
            getMenuInflater().inflate(R.menu.recommend_list_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 初始化组件
     */
    private void initComponent() {
        m_txvTitle = (TextView) findViewById(R.id.job_detail_activity_title_txv);
        m_txvDate = (TextView) findViewById(R.id.job_detail_activity_date_txv);
        m_txvPublisher = (TextView) findViewById(R.id.job_detail_activity_publisher_txv);
        m_webvContent = (WebView) findViewById(R.id.job_detail_activity_content_webv);
        m_imgBtnPublisher = (ImageButton) findViewById(R.id.job_detail_activity_publisher_btn);
        m_BtnCommened = (Button) findViewById(R.id.job_recommend_imgbtn);

        m_BtnCommened.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//				if (m_isEditView) {
//					getFragmentManager()
//							.beginTransaction()
//							.replace(R.id.content_frame,
//									MyRecommendListFragment.getInstance())
//							.commit();
//				} else {
                Intent intent = new Intent(RecommendDetailActivity.this,
                        JobCommentListActivity.class);
                int id = -1;
                if (!Judge.isNullOrEmpty(m_jobCurrent)) {
                    id = m_jobCurrent.getId();
                }
                intent.putExtra("id", id);
                startActivity(intent);
            }

//			}
        });
//        m_webvContent.setInitialScale(25);//为25%，最小缩放等级
        WebSettings settings = m_webvContent.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        settings.setSupportZoom(true);// 支持缩放
        settings.setDefaultFontSize(15);
    }

    /**
     * 初始化pulisher按钮
     */
    private void initPublisherBtn() {
        if (Judge.isNullOrEmpty(m_jobCurrent)) {
            Lgr.i(this.getClass().getName()
                    + "initPublisherBtn-------m_jobCurrent is null------");
            return;
        } else if (m_jobCurrent.getPublisherId() <= 0) { // 是管理员0,不需要链接发布者,‘<’做保险，正常不出现
            Lgr.i(this.getClass().getName()
                    + "initPublisherBtn-------m_jobCurrent ＝0------");
            m_imgBtnPublisher.setVisibility(View.INVISIBLE);
        } else {
            m_imgBtnPublisher.setVisibility(View.VISIBLE);
            m_imgBtnPublisher.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(RecommendDetailActivity.this,
                            ContactDetailActivity.class);
                    int id = -1;
                    id = m_jobCurrent.getPublisherId();
                    intent.putExtra("id", id);

                    intent.putExtra("user", m_jobPublisher);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * 绑定数据并显示
     */
    private void showJobDetail() {
        if (Judge.isNullOrEmpty(m_jobCurrent)) {
            return;
        }
        m_txvTitle.setText(m_jobCurrent.getTitle());
        m_txvDate.setText(TSUtil.toFull(m_jobCurrent.getUpdatedAt()));
        m_txvPublisher.setText(PUBLISHER_NAME + " " + m_jobCurrent.getPublisher().getName());
        m_webvContent.loadData(getHtmlData(m_jobCurrent.getContent()), "text/html; charset=utf-8", "utf-8");
//        m_webvContent.loadDataWithBaseURL(null, getHtmlData(m_jobCurrent.getContent()), "text/html; charset=utf-8", "utf-8", null);
        Lgr.i("jobContent",m_jobCurrent.getContent());
    }

    private void sendRequest() {
        JobApi.getJobDetail(m_nId, new RequestListener() {

            @Override
            public void onComplete(Object result) {
                Message msg = m_handlerJobDetail.obtainMessage();

                try {
                    JSONObject jsonObject = (JSONObject) result;
                    if (!Judge.isValidJsonValue("status", jsonObject)) {
                        return;
                    }
                    final int status = jsonObject.getInt("status");
                    switch (status) {
                        case STATUS_REQUEST_SUCCESS:
                            if (!Judge.isValidJsonValue("status", jsonObject)) {
                                break;
                            }
                            m_jobCurrent = new Job(jsonObject.getJSONObject("body"));
                            m_jobPublisher = new User(
                                    jsonObject.getJSONObject("body").getJSONObject("user"));
                            break;
                        case STATUS_NOT_LOGIN:
                            updateLogin();
                            break;
                        default:
                            dispose(msg);
                            break;
                    }
                    msg.what = status;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                m_handlerJobDetail.sendMessage(msg);

            }

            @Override
            public void onHttpError(CSTResponse response) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onException(Exception e) {
                // m_jobCurrent = new Job(null);
                Lgr.i("JobDetailActivity onError : id = " + m_nId + "!");
                if (Lgr.isDebuggable()) {
                    e.printStackTrace();
                }

            }

        });


        CSTJsonResponse response = new CSTJsonResponse(this) {
            @Override
            public void onResponse(JSONObject result) {
                super.onResponse(result);
                Lgr.i(result.toString());
                Message msg = m_handlerJobDetail.obtainMessage();
                try {
                    msg.what = result.getInt("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                m_handlerJobDetail.sendMessage(msg);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
                Message msg = m_handlerJobDetail.obtainMessage();
                msg.what = mErrorStatusCode;
                m_handlerJobDetail.sendMessage(msg);
            }
        };

//        CSTJsonRequest detailRequest = new CSTJsonRequest(CSTRequest.Method.GET,
//                SUB_URL, paramsMap, response);
//        mEngine.requestJson(detailRequest);

    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

}

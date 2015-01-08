/**
 *
 */
package cn.edu.zju.isst1.ui.job;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.api.JobApi;
import cn.edu.zju.isst1.db.Job;
import cn.edu.zju.isst1.net.CSTResponse;
import cn.edu.zju.isst1.net.RequestListener;
import cn.edu.zju.isst1.ui.contact.ContactDetailActivity;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;

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
public class JobDetailActivity extends BaseActivity {

    /**
     * 归档id
     */
    private int m_nId;

    private Job m_jobCurrent;

    private Handler m_handlerJobDetail;

    private TextView m_txvTitle;

    private TextView m_txvDate;

    private TextView m_txvPublisher;

    private WebView m_webvContent;

    private ImageView m_imgBtnPublisher;

    // Activity需要工厂方法吗？
    // public JobDetailActivity(){
    // }
    //
    // public static JobDetailActivity newInstance(){
    // return new JobDetailActivity();
    // }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_detail_activity);
        initComponent();

        setUpActionBar();

        // 注意默认值-1，当Intent中没有id时是无效的，故启动这个JobDetailActivity的Activity必须在Intent中放置"id"参数
        m_nId = getIntent().getIntExtra("id", -1);

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
                            break;
                        case STATUS_NOT_LOGIN:

                            break;
                        default:
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
                JobDetailActivity.this.finish();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void setUpActionBar() {
        super.setUpActionBar();
        setTitle(R.string.action_bar_archive_detail);
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

        WebSettings settings = m_webvContent.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        settings.setSupportZoom(true);// 支持缩放
        settings.setDefaultFontSize(48);

        // settings.setTextSize(TextSize.NORMAL);
    }

    /**
     * 初始化pulisher按钮
     */
    private void initPublisherBtn() {
        if (Judge.isNullOrEmpty(m_jobCurrent)) {
            Lgr.i(this.getClass().getName() + "initPublisherBtn-------m_jobCurrent is null------");
            return;
        } else if (m_jobCurrent.getPublisherId() <= 0) { // 是管理员0,不需要链接发布者,‘<’做保险，正常不出现
            Lgr.i(this.getClass().getName() + "initPublisherBtn-------m_jobCurrent ＝0------");
            m_imgBtnPublisher.setVisibility(View.INVISIBLE);
        } else {
            m_imgBtnPublisher.setVisibility(View.VISIBLE);
            m_imgBtnPublisher.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(JobDetailActivity.this,
                            ContactDetailActivity.class);
                    int id = -1;
                    id = m_jobCurrent.getPublisherId();
                    intent.putExtra("id", id);
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
        m_txvPublisher.setText(PUBLISHER_NAME + m_jobCurrent.getPublisherId()
                + " " + m_jobCurrent.getPublisher().getName());
        m_webvContent.loadDataWithBaseURL(null, m_jobCurrent.getContent(),
                "text/html", "utf-8", null);

    }

}

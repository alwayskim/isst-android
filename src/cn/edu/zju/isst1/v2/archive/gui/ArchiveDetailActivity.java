/**
 *
 */
package cn.edu.zju.isst1.v2.archive.gui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.db.Archive;
import cn.edu.zju.isst1.net.CSTResponse;
import cn.edu.zju.isst1.net.RequestListener;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.v2.archive.net.ArchiveApi;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.usercenter.myexperience.PublishExpActivity;

import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * 归档详情页
 *
 * @author theasir
 *         <p/>
 *         TODO WIP
 */
public class ArchiveDetailActivity extends BaseActivity {

    /**
     * 归档id
     */
    private int m_nId;

    private Archive m_archiveCurrent;

    private Handler m_handlerArchiveDetail;

    private TextView m_txvTitle;

    private TextView m_txvDate;

    private TextView m_txvPublisher;

    private WebView m_webvContent;

    private boolean mEditable;

    // Activity需要工厂方法吗？
    // public ArchiveDetailActivity(){
    // }
    //
    // public static ArchiveDetailActivity newInstance(){
    // return new ArchiveDetailActivity();
    // }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_detail_activity);

        mEditable = getIntent().getBooleanExtra("editable", false);

        initComponent();

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 注意默认值-1，当Intent中没有id时是无效的，故启动这个ArchiveDetailActivity的Activity必须在Intent中放置"id"参数
        m_nId = getIntent().getIntExtra("id", -1);

        initHandler();
        requestData();

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
                // Intent intentParent = new Intent(ArchiveDetailActivity.this,
                // MainActivity.class);
                // intentParent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // ArchiveDetailActivity.this.startActivity(intentParent);
                ArchiveDetailActivity.this.finish();
                return true;
            }

            case R.id.recommend_list_edt:
                Intent intent = new Intent(ArchiveDetailActivity.this, PublishExpActivity.class);
                intent.putExtra("exp_detail", m_archiveCurrent);
                intent.putExtra("is_edit", true);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mEditable) {
            getMenuInflater().inflate(R.menu.recommend_list_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    //初始化handler
    private void initHandler() {
        m_handlerArchiveDetail = new Handler() {

            /*
             * (non-Javadoc)
             *
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        Lgr.i("Handler Success Archieve id = "
                                + m_archiveCurrent.getId());
                        showArchiveDetail();
                        break;
                    case STATUS_NOT_LOGIN:
                        updateLogin();
                        requestData();
                        break;
                    default:
                        CSTHttpUtil.dispose(msg.what, ArchiveDetailActivity.this);
                        break;
                }
            }

        };
    }

    //请求详情数据
    private void requestData() {
        ArchiveApi.getArchiveDetail(m_nId, new RequestListener() {

            @Override
            public void onComplete(Object result) {
                Message msg = m_handlerArchiveDetail.obtainMessage();

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
                            m_archiveCurrent = new Archive(jsonObject
                                    .getJSONObject("body"));
                            break;
                        case STATUS_NOT_LOGIN:
                            updateLogin();
                            break;
                        default:
                            break;
                    }
                    msg.what = status;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                m_handlerArchiveDetail.sendMessage(msg);

            }

            @Override
            public void onHttpError(CSTResponse response) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onException(Exception e) {
                // m_archiveCurrent = new Archive(null);
                Lgr.i("ArchiveDetailActivity onError : id = " + m_nId + "!");
                if (Lgr.isDebuggable()) {
                    e.printStackTrace();
                }

            }

        });
    }

    /**
     * 初始化组件
     */
    private void initComponent() {
        m_txvTitle = (TextView) findViewById(R.id.archive_detail_activity_title_txv);
        m_txvDate = (TextView) findViewById(R.id.archive_detail_activity_date_txv);
        m_txvPublisher = (TextView) findViewById(R.id.archive_detail_activity_publisher_txv);
        m_webvContent = (WebView) findViewById(R.id.archive_detail_activity_content_webv);
        WebSettings settings = m_webvContent.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);

        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        settings.setSupportZoom(true);// 支持缩放
        settings.setDefaultFontSize(48);
        // settings.setTextSize(TextSize.NORMAL);
    }

    /**
     * 绑定数据并显示
     */
    private void showArchiveDetail() {
        if (Judge.isNullOrEmpty(m_archiveCurrent)) {
            return;
        }
        m_txvTitle.setText(m_archiveCurrent.getTitle());
        m_txvDate.setText(TSUtil.toFull(m_archiveCurrent.getUpdatedAt()));
        m_txvPublisher.setText(m_archiveCurrent.getPublisher().getName());
        m_webvContent.loadDataWithBaseURL(null, m_archiveCurrent.getContent(),
                "text/html", "utf-8", null);

    }

}

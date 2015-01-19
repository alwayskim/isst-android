/**
 *
 */
package cn.edu.zju.isst1.ui.life;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.constant.Constants;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.event.campus.data.CSTCampusEvent;
import cn.edu.zju.isst1.v2.event.campus.net.CampusEventDetailResponse;
import cn.edu.zju.isst1.v2.login.net.UpDateLogin;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * @author theasir
 */
public class CampusActivityDetailActivity extends BaseActivity {

    private CSTCampusEvent mCSTCampusEvent;

    private Handler mHandler;

    private ImageView mImgvPicture;

    private TextView mTxvDuration;

    private TextView mTxvLocation;

    private WebView mWebvContent;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private static final String SUB_URL = "/api/campus/activities/";

    private static final String EVENT_ID = "id";

    private int mId;

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campus_activity_detail_activity);
        mId = getIntent().getIntExtra(EVENT_ID, -1);
        initComponent();
        setUpActionBar();
        initHandle();
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
            case android.R.id.home:
                CampusActivityDetailActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void setUpActionbar() {
//        ActionBar actionBar = getActionBar();
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//    }

    @Override
    protected void setUpActionBar() {
        super.setUpActionBar();
        setTitle(R.string.action_bar_event_detail);
    }

    private void initHandle() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        showCampusActivityDetail();
                        break;
                    case STATUS_NOT_LOGIN:
                        UpDateLogin.getInstance().updateLogin(CampusActivityDetailActivity.this);
                        requestData();
                    case NETWORK_NOT_CONNECTED:
                        CroMan.showAlert(CampusActivityDetailActivity.this, R.string.network_not_connected);
                        break;
                    default:
                        CSTHttpUtil.dispose(msg.what, CampusActivityDetailActivity.this);
                        break;
                }
            }

        };
    }

    private void requestData() {
        if (NetworkConnection.isNetworkConnected(this)) {
            CampusEventDetailResponse detailResponse = new CampusEventDetailResponse(this) {
                @Override
                public void onResponse(JSONObject response) {
                    Message msg = mHandler.obtainMessage();
                    mCSTCampusEvent = (CSTCampusEvent) CSTJsonParser
                            .parseJson(response, new CSTCampusEvent());
                    Lgr.i(response.toString());
                    final int status = mCSTCampusEvent.getStatusInfo().status;
                    msg.what = status;
                    mHandler.sendMessage(msg);
                }
            };
            CSTJsonRequest detailRequest = new CSTJsonRequest(CSTRequest.Method.GET, SUB_URL + mId,
                    null, detailResponse);
            mEngine.requestJson(detailRequest);
        } else {
            Message msg = mHandler.obtainMessage();
            msg.what = Constants.NETWORK_NOT_CONNECTED;
            mHandler.sendMessage(msg);
        }
    }

    private void initComponent() {
        mImgvPicture = (ImageView) findViewById(R.id.campus_activity_detail_activity_picture_imgv);
        mTxvDuration = (TextView) findViewById(R.id.campus_activity_detail_activity_duration_txv);
        mTxvLocation = (TextView) findViewById(R.id.campus_activity_detail_activity_loaction_txv);
        mWebvContent = (WebView) findViewById(R.id.campus_activity_detail_activity_content_webv);
    }

    private void showCampusActivityDetail() {
        mTxvDuration.setText(getResources().getString(R.string.note_event_duration)
                + TSUtil.toFull(Long.parseLong(mCSTCampusEvent.startTime)) + "--"
                + TSUtil.toFull(Long.parseLong(mCSTCampusEvent.expireTime)));
        mTxvLocation.setText(
                getResources().getString(R.string.note_event_location) + mCSTCampusEvent.location);
        mWebvContent.loadDataWithBaseURL(null, mCSTCampusEvent.content,
                "text/html", "utf-8", null);
    }

}

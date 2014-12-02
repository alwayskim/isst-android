/**
 *
 */
package cn.edu.zju.isst1.v2.event.city.gui;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.constant.Constants;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.event.base.EventRequest;
import cn.edu.zju.isst1.v2.event.city.event.data.CSTCityEvent;
import cn.edu.zju.isst1.v2.event.city.net.CityEventDetailResponse;
import cn.edu.zju.isst1.v2.event.city.net.CityEventParticipateResponse;
import cn.edu.zju.isst1.v2.login.net.UpDateLogin;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;
import cn.edu.zju.isst1.v2.net.CSTStatusInfo;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * @author theasir
 */
public class CityEventDetailActivity extends BaseActivity {

    private final ViewHolder mViewHolder = new ViewHolder();

    private CSTCityEvent mCSTCityEvent;

    private Handler mHandler;

    private Handler mBtnHandler;

    private ProgressDialog mPgdWaiting;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private static final String SUB_URL = "/api/cities";

    private static final String CITY_ID = "cityId";

    private static final String EVENT_ID = "id";

    private static final String EVENT_TITLE = "eventTitle";

    private int mId;

    private int mCityId;

    private String mEventTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_activity_detail_activity);

        setUpActionbar();

        mId = getIntent().getIntExtra(EVENT_ID, -1);
        mCityId = getIntent().getIntExtra(CITY_ID, -1);
        mEventTitle = getIntent().getStringExtra(EVENT_TITLE);

        initComponent();

        initHandler();

        setUpListener();

        requestData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CityEventDetailActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpActionbar() {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        mViewHolder.pictureImgv = (ImageView) findViewById(R.id.city_event_detail_picture_imgv);
        mViewHolder.durationTxv = (TextView) findViewById(R.id.city_event_detail_duration_txv);
        mViewHolder.locationTxv = (TextView) findViewById(R.id.city_event_detail_location_txv);
        mViewHolder.contentWebv = (WebView) findViewById(R.id.city_event_detail_content_webv);
        mViewHolder.showParticipantsBtn = (Button) findViewById(
                R.id.city_event_show_participants_list_btn);
        mViewHolder.participateBtn = (Button) findViewById(
                R.id.city_event_detail_participate_btn);
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        showDetail();
                        break;
                    case STATUS_NOT_LOGIN:
                        UpDateLogin.getInstance().updateLogin(CityEventDetailActivity.this);
                        requestData();
                        break;
                    default:
                        CSTHttpUtil.dispose(msg.what, CityEventDetailActivity.this);
                        break;
                }

            }
        };

        mBtnHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                mPgdWaiting.dismiss();
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        CroMan.showConfirm(CityEventDetailActivity.this,
                                R.string.participate_commit_success);
                        mCSTCityEvent.isParticipate = !mCSTCityEvent.isParticipate;
                        showDetail();
                        Lgr.i(getResources().getString(R.string.participate_commit_success));
                        break;
                    case STATUS_NOT_LOGIN:
                        UpDateLogin.getInstance().updateLogin(CityEventDetailActivity.this);
                        performParticipateAction();
                        break;
                    default:
                        CSTHttpUtil.dispose(msg.what, CityEventDetailActivity.this);
                        break;
                }
            }
        };
    }

    private void setUpListener() {
        mViewHolder.participateBtn
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        performParticipateAction();
                    }
                });
        mViewHolder.showParticipantsBtn
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CityEventDetailActivity.this,
                                CityEventParticipantsListActivity.class);
                        intent.putExtra(EVENT_ID, mId);
                        intent.putExtra(CITY_ID, mCityId);
                        intent.putExtra(EVENT_TITLE, mEventTitle);
                        startActivity(intent);
                    }
                });
    }

    private void requestData() {
        if (NetworkConnection.isNetworkConnected(this)) {
            StringBuilder sb = new StringBuilder();
            sb.append(SUB_URL).append("/" + mCityId).append("/activities")
                    .append("/" + mId);
            CityEventDetailResponse detailResponse = new CityEventDetailResponse(this) {
                @Override
                public void onResponse(JSONObject response) {
                    Message msg = mHandler.obtainMessage();
                    Lgr.i(response.toString());
                    mCSTCityEvent = (CSTCityEvent) CSTJsonParser
                            .parseJson(response, new CSTCityEvent());
                    final int status = mCSTCityEvent.getStatusInfo().status;
                    msg.what = status;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    super.onErrorResponse(error);
                    Message msg = mHandler.obtainMessage();
                    msg.what = mErrorStatusCode;
                    mHandler.sendMessage(msg);
                }
            };

            CSTJsonRequest detailRequest = new EventRequest(CSTRequest.Method.GET,
                    sb.toString(), null,
                    detailResponse);
            mEngine.requestJson(detailRequest);
        } else {
            Message msg = mHandler.obtainMessage();
            msg.what = Constants.NETWORK_NOT_CONNECTED;
            mHandler.sendMessage(msg);
        }
    }

    private void performParticipateAction() {
        if (NetworkConnection.isNetworkConnected(this)) {
            mPgdWaiting = ProgressDialog.show(CityEventDetailActivity.this,
                    getString(R.string.loading), getString(R.string.please_wait),
                    true, false);
            CityEventParticipateResponse participateResponse = new CityEventParticipateResponse(
                    this) {
                @Override
                public void onResponse(JSONObject response) {
                    Message msg = mBtnHandler.obtainMessage();
                    Lgr.i(response.toString());
                    mCSTCityEvent = (CSTCityEvent) CSTJsonParser
                            .parseJson(response, new CSTCityEvent());
                    final int status = mCSTCityEvent.getStatusInfo().status;
                    msg.what = status;
                    msg.obj = mCSTCityEvent.getStatusInfo().message;
                    mBtnHandler.sendMessage(msg);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    super.onErrorResponse(error);
                    Message msg = mBtnHandler.obtainMessage();
                    msg.what = mErrorStatusCode;
                    mBtnHandler.sendMessage(msg);
                }
            };
            StringBuilder sb = new StringBuilder();

            sb.append(SUB_URL).append("/" + mCityId).append("/activities")
                    .append("/" + mId);

            if (mCSTCityEvent.isParticipate) {
                sb.append("/unparticipate");
            } else {
                sb.append("/participate");
            }
            Lgr.i(sb.toString());

            CSTJsonRequest participateRequest = new CSTJsonRequest(CSTRequest.Method.POST,
                    sb.toString(), null,
                    participateResponse);
            mEngine.requestJson(participateRequest);
        } else {
            Message msg = mBtnHandler.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            mHandler.sendMessage(msg);
        }

    }

    private void showDetail() {
        setTitle(mCSTCityEvent.title);
        mViewHolder.durationTxv.setText(getResources().getString(R.string.note_event_duration)
                + TSUtil.toHM(mCSTCityEvent.startTime) + "-"
                + TSUtil.toHM(mCSTCityEvent.expireTime));
        mViewHolder.locationTxv.setText(getResources().getString(R.string.note_event_location)
                + mCSTCityEvent.location);
        mViewHolder.contentWebv.loadDataWithBaseURL(null, mCSTCityEvent.content,
                "text/html", "utf-8", null);
        mViewHolder.participateBtn
                .setText(mCSTCityEvent.isParticipate ? R.string.participate_event_cancel
                        : R.string.participate_event);
        mViewHolder.showParticipantsBtn.setText(R.string.participate_show_list);
    }

    private class ViewHolder {

        public ImageView pictureImgv;

        public TextView durationTxv;

        public TextView locationTxv;

        public WebView contentWebv;

        public Button participateBtn;

        public Button showParticipantsBtn;
    }
}

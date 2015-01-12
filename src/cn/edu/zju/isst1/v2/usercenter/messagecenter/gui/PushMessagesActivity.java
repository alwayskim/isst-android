/**
 *
 */
package cn.edu.zju.isst1.v2.usercenter.messagecenter.gui;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.constant.Constants;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.settings.CSTSettings;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.login.net.UpDateLogin;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.CSTMessage;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.CSTMessageDataDelegate;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.CSTMessageProvider;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.net.PushMessageRequest;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.net.PushMessageResponse;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;

/**
 * @author theasir
 */
public class PushMessagesActivity extends BaseActivity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor> {

    private Handler mHandler;

    private ListView mListView;

    private LayoutInflater mInflater;

    private CSTMessage mMessageList;

    private int mCurrentPage = 1;

    private int DEFAULT_PAGE_SIZE = 20;

    private boolean mIsFirstTime;

    private boolean isLoadMore = false;

    private boolean isMoreData = false;

    private View mFooter;

    private ProgressBar mLoadMorePrgb;

    private TextView mLoadMoreHint;

    private static final String MESSAGE_URL = "/api/messages";

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private PushMessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_archive_list_fragment);

        mIsFirstTime = true;

        CSTSettings.setPushActivityOn(true, this);

        getLoaderManager().initLoader(0, null, this);

        setUpActionBar();

        initComponent();

        if (mIsFirstTime) {
            requestData();
            mIsFirstTime = false;
        }

        bindAdapter();

        setUpListener();

        initHandler();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                PushMessagesActivity.this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void setUpActionBar() {
        super.setUpActionBar();
        setTitle(R.string.message_center);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CSTSettings.setPushActivityOn(false, this);
    }

    private void initComponent() {

        mInflater = LayoutInflater.from(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorScheme(R.color.deepskyblue, R.color.deepskyblue, R.color.white,
                R.color.white);
        mListView = (ListView) findViewById(R.id.simple_list);
        mFooter = mInflater.inflate(R.layout.loadmore_footer, mListView, false);
        mListView.addFooterView(mFooter);
        mLoadMorePrgb = (ProgressBar) mFooter.findViewById(R.id.footer_loading_progress);
        mLoadMorePrgb.setVisibility(ProgressBar.GONE);
        mLoadMoreHint = (TextView) mFooter.findViewById(R.id.footer_loading_hint);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadmore_footer:
                startLoadMore();
                break;
            default:
                break;
        }
    }

    public void startLoadMore() {
        isLoadMore = true;
        mLoadMorePrgb.setVisibility(ProgressBar.VISIBLE);
        mLoadMoreHint.setText(R.string.loading);
        requestData();
    }

    public void resetLoadingState() {
        mLoadMorePrgb.setVisibility(ProgressBar.GONE);
        if (isLoadMore && !isMoreData) {
            mLoadMoreHint.setText(R.string.footer_loading_hint_no_more_data);
        } else {
            mLoadMoreHint.setText(R.string.footer_loading_hint);
        }
    }

    public void requestData() {
        if (isLoadMore) {
            mCurrentPage++;
        } else {
            mCurrentPage = 1;
            mSwipeRefreshLayout.setRefreshing(true);
        }
        if (NetworkConnection.isNetworkConnected(this)) {
            PushMessageResponse pmResponse = new PushMessageResponse(this, !isLoadMore) {
                @Override
                public void onResponse(JSONObject response) {
                    super.onResponse(response);
                    mMessageList = (CSTMessage) CSTJsonParser
                            .parseJson(response, new CSTMessage());
                    for (CSTMessage message : mMessageList.itemList) {
                        CSTMessageDataDelegate.saveMessage(mContext, message);
                    }
                    if (isLoadMore) {
                        try {
                            isMoreData = response.getJSONArray("body").length() == 0 ? false : true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = Constants.STATUS_REQUEST_SUCCESS;
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
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("page", "" + mCurrentPage);
            paramsMap.put("pageSize", "" + DEFAULT_PAGE_SIZE);
            paramsMap.put("keywords", null);
            PushMessageRequest request = new PushMessageRequest(CSTRequest.Method.GET,
                    MESSAGE_URL, null,
                    pmResponse).setPage(mCurrentPage).setPageSize(DEFAULT_PAGE_SIZE);
            mEngine.requestJson(request);
        } else {
            Message msg = mHandler.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            mHandler.sendMessage(msg);
        }
    }

    private void setUpListener() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mFooter.setOnClickListener(this);
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.STATUS_REQUEST_SUCCESS:
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;
                    case STATUS_NOT_LOGIN:
                        UpDateLogin.getInstance().updateLogin(PushMessagesActivity.this);
                        requestData();
                    case NETWORK_NOT_CONNECTED:
                        CroMan.showAlert(PushMessagesActivity.this, R.string.network_not_connected);
                    default:
                        CSTHttpUtil.dispose(msg.what, PushMessagesActivity.this);
                        break;
                }
                resetLoadingState();
            }
        };

    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        requestData();
    }

    private void bindAdapter() {
        mAdapter = new PushMessageAdapter(PushMessagesActivity.this, null);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return CSTMessageDataDelegate.getDataCursor(PushMessagesActivity.this, null, null, null,
                CSTMessageProvider.Columns.ID.key + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}

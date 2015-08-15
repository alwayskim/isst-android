package cn.edu.zju.isst1.v2.archive.gui;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.constant.Constants;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.v2.archive.data.ArchiveCategory;
import cn.edu.zju.isst1.v2.archive.data.CSTArchive;
import cn.edu.zju.isst1.v2.archive.data.CSTArchiveDataDelegate;
import cn.edu.zju.isst1.v2.archive.data.CSTArchiveProvider;
import cn.edu.zju.isst1.v2.archive.net.ArchiveRequest;
import cn.edu.zju.isst1.v2.archive.net.ArchiveResponse;
import cn.edu.zju.isst1.v2.gui.CSTBaseFragment;
import cn.edu.zju.isst1.v2.login.net.UpDateLogin;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;
import pulltorefresh.widget.XListView;

/**
 * Created by i308844 on 8/12/14.
 */
public abstract class BaseArchiveListFragment extends CSTBaseFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, XListView.IXListViewListener,
        AdapterView.OnItemClickListener {

    public static final String INTENT_ID = "id";

    public static final int DEFAULT_PAGE_SIZE = 20;

    public static final String ARCHIVE_URL = "/api/archives/categories";

    protected ArchiveCategory mCategory;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

//    private LayoutInflater mInflater;

    private XListView mListView;

//    private View mFooter;
//
//    private ProgressBar mLoadMorePrgb;
//
//    private TextView mLoadMoreHint;

    private ArchiveListAdapter mAdapter;

//    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Handler mHandler;

    private Handler rHandler;

    private boolean isLoadMore;

    private boolean isMoreData;

    private int mCurrentPage;

    private boolean mIsFirst = true;

    //better implementation is use Fragment#newInstance(args...) instead.
    protected BaseArchiveListFragment() {
        setCategory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLoadMore = false;
        isMoreData = true;
//        mIsFirst = true;
        mCurrentPage = 1;
        rHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mInflater = inflater;
        return inflater.inflate(R.layout.base_archive_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void initComponent(View view) {
//        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
//        mSwipeRefreshLayout
//                .setColorScheme(R.color.deepskyblue, R.color.darkorange, R.color.darkviolet,
//                        R.color.lightcoral);
        mListView = (XListView) view.findViewById(R.id.simple_list);

//        mFooter = mInflater.inflate(R.layout.loadmore_footer, mListView, false);
//        mListView.addFooterView(mFooter);
//        mLoadMorePrgb = (ProgressBar) mFooter.findViewById(R.id.footer_loading_progress);
//        mLoadMorePrgb.setVisibility(View.GONE);
//        mLoadMoreHint = (TextView) mFooter.findViewById(R.id.footer_loading_hint);
        ViewTreeObserver observer = view.getViewTreeObserver();
//        observer.addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
//            @Override
//            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
//                if (mIsFirst) {
//                    mListView.autoRefresh();
//                    mIsFirst = false;
//                }
//            }
//        });
//        mListView.autoRefresh();

//        requestData();
        bindAdapter();
        setUpListener();
        initHandler();
        if (mIsFirst) {
//            mListView.autoRefresh();
//            rHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
                    requestData();
                    mAdapter.notifyDataSetChanged();
                    onLoad();
//                }
//            }, 1000);
            mIsFirst = false;
        }
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        isMoreData = true;
//        mListView.setPullLoadEnable(true);
        rHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                requestData();
                mAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        isLoadMore = true;
        rHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startLoadMore();
                mAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 1000);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return CSTArchiveDataDelegate.getDataCursor(getActivity(),
                null,
                CSTArchiveProvider.Columns.CATEGORY_ID.key + " = ?",
                new String[]{
                        "" + mCategory.id
                },
                CSTArchiveProvider.Columns.UPDATE_TIME.key + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ArchiveDetailActivity.class);
        intent.putExtra(INTENT_ID, ((CSTArchive) view.getTag()).id);
        getActivity().startActivity(intent);
    }

    protected abstract void setCategory();

    private void bindAdapter() {
        mAdapter = new ArchiveListAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);
    }

    private void setUpListener() {
        mListView.setOnItemClickListener(this);
//        mFooter.setOnClickListener(this);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAutoLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setRefreshTime(TSUtil.getTime());
//        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (getActivity() != null) {
                    switch (msg.what) {
                        case Constants.STATUS_REQUEST_SUCCESS:
                            resetLoadingState();
                            break;

                        case Constants.STATUS_NOT_LOGIN:
                            UpDateLogin.getInstance().updateLogin(getActivity());
                            Lgr.i("BaseArchiListFragment ----！------更新登录了-------！");
                            if (isLoadMore) {
                                mCurrentPage--;
                            }
                            requestData();
                            break;
                        default:
                            CSTHttpUtil.dispose(msg.what, getActivity());
                            break;
                    }
//                    mSwipeRefreshLayout.setRefreshing(false);
//                    mLoadMorePrgb.setVisibility(View.GONE);
                }
            }
        };
    }

    private void requestData() {
//        if (NetworkConnection.isNetworkConnected(getActivity())) {
        if (isLoadMore) {
            mCurrentPage++;
        } else {
            mCurrentPage = 1;
//            if (mIsFirst) {
//                mListView.autoRefresh();
//                mIsFirst = false;
//            }
        }
        ArchiveResponse archiveResponse = new ArchiveResponse(getActivity(), mCategory,
                !isLoadMore) {
            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                Lgr.i("Archive:  ", response.toString());
                Message msg = mHandler.obtainMessage();

                try {
                    if (isLoadMore) {
                        isMoreData = response.getJSONArray("body").length() == 0 ? false : true;
//                        if (!isMoreData) {
//                            Toast.makeText(getActivity(), R.string.no_more_data, Toast.LENGTH_SHORT).show();
//                            mListView.setPullLoadEnable(false);
//                        }
                    }
                    msg.what = response.getInt("status");
                    resetLoadingState();
                    mHandler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
                Message msg = mHandler.obtainMessage();
                msg.what = mErrorStatusCode;
                mHandler.sendMessage(msg);
            }
        };
        ArchiveRequest archiveRequest = new ArchiveRequest(CSTRequest.Method.GET,
                ARCHIVE_URL + mCategory.subUrl, null, archiveResponse)
                .setPage(mCurrentPage)
                .setPageSize(DEFAULT_PAGE_SIZE);

        mEngine.requestJson(archiveRequest);
//        }else{
//            Message msg = mHandler.obtainMessage();
//            msg.what = Constants.NETWORK_NOT_CONNECTED;
//            mHandler.sendMessage(msg);
//        }
    }

    private void startLoadMore() {
        isLoadMore = true;
//        mLoadMorePrgb.setVisibility(View.VISIBLE);
//        mLoadMoreHint.setText(R.string.loading);
        requestData();
    }

    private void resetLoadingState() {
//        mSwipeRefreshLayout.setRefreshing(false);
//        mLoadMorePrgb.setVisibility(View.GONE);
//        if (isLoadMore && !isMoreData) {
//            mLoadMoreHint.setText(R.string.footer_loading_hint_no_more_data);
//        } else {
//            mLoadMoreHint.setText(R.string.footer_loading_hint);
//        }
        mListView.stopRefresh();
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(TSUtil.getTime());
    }
}

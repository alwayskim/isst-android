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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
        implements LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {

    public static final String INTENT_ID = "id";

    public static final int DEFAULT_PAGE_SIZE = 20;

    public static final String ARCHIVE_URL = "/api/archives/categories";

    protected ArchiveCategory mCategory;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private PullToRefreshListView listView;

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

//        mListView = (XListView) view.findViewById(R.id.simple_list);
        listView = (PullToRefreshListView) view.findViewById(R.id.base_archive_lv);
        listView.setOnItemClickListener(this);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(TSUtil.getTime());

                // Do work to refresh the list here.
                isLoadMore = false;
                requestData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadMore = true;
                requestData();
            }
        });

        bindAdapter();
//        setUpListener();
        initHandler();
//        if (mIsFirst) {
////            mListView.autoRefresh();
////            rHandler.postDelayed(new Runnable() {
////                @Override
////                public void run() {
//                    requestData();
//                    mAdapter.notifyDataSetChanged();
//                    onLoad();
////                }
////            }, 1000);
//            mIsFirst = false;
//        }
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
        listView.setAdapter(mAdapter);
    }


    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (getActivity() != null) {
                    switch (msg.what) {
                        case Constants.STATUS_REQUEST_SUCCESS:
//                            resetLoadingState();
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
                    listView.onRefreshComplete();
//                    mSwipeRefreshLayout.setRefreshing(false);
//                    mLoadMorePrgb.setVisibility(View.GONE);
                }
            }
        };
    }

    private void requestData() {
        if (isLoadMore) {
            mCurrentPage++;
        } else {
            mCurrentPage = 1;
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
                    }
                    msg.what = response.getInt("status");
//                    resetLoadingState();
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
    }

}

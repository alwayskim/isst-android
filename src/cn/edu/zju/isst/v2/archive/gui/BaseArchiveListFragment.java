package cn.edu.zju.isst.v2.archive.gui;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import cn.edu.zju.isst.R;
import cn.edu.zju.isst.constant.Constants;
import cn.edu.zju.isst.v2.archive.data.ArchiveCategory;
import cn.edu.zju.isst.v2.archive.data.CSTArchive;
import cn.edu.zju.isst.v2.archive.data.CSTArchiveDataDelegate;
import cn.edu.zju.isst.v2.archive.data.CSTArchiveProvider;
import cn.edu.zju.isst.v2.archive.net.ArchiveRequest;
import cn.edu.zju.isst.v2.archive.net.ArchiveResponse;
import cn.edu.zju.isst.v2.gui.CSTBaseFragment;
import cn.edu.zju.isst.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst.v2.net.CSTRequest;
import cn.edu.zju.isst.v2.net.CSTStatusInfo;

/**
 * Created by i308844 on 8/12/14.
 */
public abstract class BaseArchiveListFragment extends CSTBaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String INTENT_ID = "id";

    public static final int DEFAULT_PAGE_SIZE = 20;

    public static final String ARCHIVE_URL = "/api/archives/categories";

    protected ArchiveCategory mCategory;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private LayoutInflater mInflater;

    private ListView mListView;

    private View mFooter;

    private ProgressBar mLoadMorePrgb;

    private TextView mLoadMoreHint;

    private ArchiveListAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Handler mHandler;

    private boolean isLoadMore = false;

    private int mCurrentPage = 1;

    //better implementation is use Fragment#newInstance(args...) instead.
    protected BaseArchiveListFragment() {
        setCategory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mInflater = inflater;
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
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout
                .setColorScheme(R.color.blue, R.color.orange, R.color.blue,
                        R.color.orange);
        mListView = (ListView) view.findViewById(R.id.simple_list);
        mFooter = mInflater.inflate(R.layout.loadmore_footer, mListView, false);
        mListView.addFooterView(mFooter);
        mLoadMorePrgb = (ProgressBar) mFooter.findViewById(R.id.footer_loading_progress);
        mLoadMorePrgb.setVisibility(View.GONE);
        mLoadMoreHint = (TextView) mFooter.findViewById(R.id.footer_loading_hint);
        requestData();

        bindAdapter();
        setUpListener();
        initHandler();
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        requestData();
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

    protected abstract void setCategory();

    private void bindAdapter() {
        mAdapter = new ArchiveListAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);
    }

    private void setUpListener() {
        mListView.setOnItemClickListener(this);
        mFooter.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.STATUS_REQUEST_SUCCESS:

                        break;
                    default:
                        break;
                }
                resetLoadingState();
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
                Message msg = mHandler.obtainMessage();
                msg.what = Constants.STATUS_REQUEST_SUCCESS;
                mHandler.sendMessage(msg);
            }

            @Override
            public Object onErrorStatus(CSTStatusInfo statusInfo) {
                return super.onErrorStatus(statusInfo);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        };
        ArchiveRequest archiveRequest = new ArchiveRequest(CSTRequest.Method.GET,
                ARCHIVE_URL + mCategory.subUrl, null, archiveResponse)
                .setPage(mCurrentPage)
                .setPageSize(DEFAULT_PAGE_SIZE);

        mEngine.requestJson(archiveRequest);
    }

    private void startLoadMore() {
        isLoadMore = true;
        mLoadMorePrgb.setVisibility(View.VISIBLE);
        mLoadMoreHint.setText(R.string.loading);
        requestData();
    }

    private void resetLoadingState() {
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadMorePrgb.setVisibility(View.GONE);
        mLoadMoreHint.setText(R.string.footer_loading_hint);
    }
}

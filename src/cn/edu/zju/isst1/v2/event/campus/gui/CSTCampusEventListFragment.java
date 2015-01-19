package cn.edu.zju.isst1.v2.event.campus.gui;

import android.app.LoaderManager;
import android.content.Context;
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
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.ui.life.CampusActivityDetailActivity;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.v2.event.base.BaseEventListAdapter;
import cn.edu.zju.isst1.v2.event.base.EventCategory;
import cn.edu.zju.isst1.v2.event.base.EventRequest;
import cn.edu.zju.isst1.v2.event.campus.data.CSTCampusEvent;
import cn.edu.zju.isst1.v2.event.campus.data.CSTCampusEventDataDelegate;
import cn.edu.zju.isst1.v2.event.campus.data.CSTCampusEventProvider;
import cn.edu.zju.isst1.v2.event.campus.net.CampusEventResponse;
import cn.edu.zju.isst1.v2.event.city.data.CSTCityEvent;
import cn.edu.zju.isst1.v2.event.city.data.CSTCityEventDataDelegate;
import cn.edu.zju.isst1.v2.gui.CSTBaseFragment;
import cn.edu.zju.isst1.v2.login.net.UpDateLogin;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;
import pulltorefresh.widget.XListView;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * Created by always on 21/08/2014.
 */
public class CSTCampusEventListFragment extends CSTBaseFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, XListView.IXListViewListener,
        AdapterView.OnItemClickListener {

    private static CSTCampusEventListFragment INSTANCE = new CSTCampusEventListFragment();

    private int mCurrentPage;

    private int DEFAULT_PAGE_SIZE = 20;

    private boolean isLoadMore;

    private boolean isMoreData;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private static final String EVENT_ID = "id";

    private Handler mHandler;

    private Handler rHandler;

    private XListView mListView;

//    private LayoutInflater mInflater;

    private boolean mIsFirst = true;

//    private View mFooter;
//
//    private ProgressBar mLoadMorePrgb;
//
//    private TextView mLoadMoreHint;

    private CampusEventListAdapter mAdapter;

//    private SwipeRefreshLayout mSwipeRefreshLayout;

    public CSTCampusEventListFragment() {
    }

    public static CSTCampusEventListFragment getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isMoreData = true;
        isLoadMore = false;
        mCurrentPage = 1;
        rHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
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
//        mSwipeRefreshLayout.setColorScheme(R.color.deepskyblue, R.color.darkorange, R.color.darkviolet,
//                R.color.lightcoral);
        mListView = (XListView) view.findViewById(R.id.simple_list);
//        ViewTreeObserver observer = view.getViewTreeObserver();
//        observer.addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
//            @Override
//            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
//                if (mIsFirst) {
//                    mListView.autoRefresh();
//                    mIsFirst = false;
//                }
//            }
//        });
//        mFooter = mInflater.inflate(R.layout.loadmore_footer, mListView, false);
//        mListView.addFooterView(mFooter);
//        mLoadMorePrgb = (ProgressBar) mFooter.findViewById(R.id.footer_loading_progress);
//        mLoadMorePrgb.setVisibility(View.GONE);
//        mLoadMoreHint = (TextView) mFooter.findViewById(R.id.footer_loading_hint);
        requestData();
        bindAdapter();
        setUpListener();
        initHandler();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return CSTCampusEventDataDelegate.getDataCursor(getActivity(), null, null, null,
                CSTCampusEventProvider.Columns.UPDATEAT.key + " DESC");
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
        Intent intent = new Intent(getActivity(), CampusActivityDetailActivity.class);
        intent.putExtra(EVENT_ID, ((CSTCampusEvent) view.getTag()).id);
        getActivity().startActivity(intent);
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

    private void bindAdapter() {
        mAdapter = new CampusEventListAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);
    }

    private void setUpListener() {
        mListView.setOnItemClickListener(this);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAutoLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setRefreshTime(TSUtil.getTime());
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (getActivity() != null) {
                    switch (msg.what) {
                        case STATUS_REQUEST_SUCCESS:
//                            mSwipeRefreshLayout.setRefreshing(false);
                            break;
                        case STATUS_NOT_LOGIN:
                            UpDateLogin.getInstance().updateLogin(getActivity());
                            Lgr.i("CSTCampusEventListFragment ----！------更新登录了-------！");
                            if (isLoadMore) {
                                mCurrentPage--;
                            }
                            requestData();
                            break;
                        default:
                            CSTHttpUtil.dispose(msg.what, getActivity());
                            break;
                    }
                    resetLoadingState();
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
        if (mIsFirst) {
            mListView.autoRefresh();
            mIsFirst = false;
        }
        if (NetworkConnection.isNetworkConnected(getActivity())) {
            CampusEventResponse activityResponse = new CampusEventResponse(getActivity(),
                    !isLoadMore) {
                @Override
                public void onResponse(JSONObject result) {
                    super.onResponse(result);
                    Lgr.i(result.toString());
                    Message msg = mHandler.obtainMessage();
                    try {
                        msg.what = result.getInt("status");
                        if (isLoadMore) {
                            isMoreData = result.getJSONArray("body").length() == 0 ? false : true;
//                            if (!isMoreData) {
//                                Toast.makeText(getActivity(),R.string.no_more_data, Toast.LENGTH_SHORT).show();
//                                mListView.setPullLoadEnable(false);
//                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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

            EventRequest eventRequest = new EventRequest(CSTRequest.Method.GET,
                    "/api/campus/activities", null,
                    activityResponse).setPage(mCurrentPage).setPageSize(DEFAULT_PAGE_SIZE);
            mEngine.requestJson(eventRequest);
        } else {
            Message msg = mHandler.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            mHandler.sendMessage(msg);
        }
    }

    private void startLoadMore() {
        isLoadMore = true;
        requestData();
    }

    private void resetLoadingState() {
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(TSUtil.getTime());
    }


    private class CampusEventListAdapter extends CursorAdapter {

        private CSTCampusEvent campusEvent;

        public CampusEventListAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return inflater.inflate(R.layout.activity_list_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            campusEvent = CSTCampusEventDataDelegate.getCampusEvent(cursor);
            view.setTag(campusEvent);
            ViewHolder holder = getBindViewHolder(view);
            holder.titleTxv.setText(campusEvent.title);
//            holder.updateTimeTxv.setText(TSUtil.toYMD(campusEvent.updatedAt));
//            holder.startTimeTxv.setText(TSUtil.toHM(campusEvent.startTime));
//            holder.expireTimeTxv.setText(TSUtil.toHM(campusEvent.expireTime));
            holder.updateTimeTxv.setText(TSUtil.toYMD(Long.parseLong(campusEvent.updatedAt)));
//            holder.startTimeTxv.setText(TSUtil.toHM(Long.parseLong(campusEvent.startTime)));
//            holder.expireTimeTxv.setText(TSUtil.toHM(Long.parseLong(campusEvent.expireTime)));
            holder.descriptionTxv.setText(campusEvent.description);


        }

        protected ViewHolder getBindViewHolder(View view) {
            ViewHolder holder = new ViewHolder();
            holder.titleTxv = (TextView) view
                    .findViewById(R.id.activity_list_item_title_txv);
            holder.updateTimeTxv = (TextView) view
                    .findViewById(R.id.activity_list_item_updatetime_txv);
//        holder.startTimeTxv = (TextView) view
//                .findViewById(R.id.activity_list_item_starttime_txv);
//        holder.expireTimeTxv = (TextView) view
//                .findViewById(R.id.activity_list_item_expiretime_txv);
            holder.descriptionTxv = (TextView) view
                    .findViewById(R.id.activity_list_item_description_txv);
//        holder.indicatorView = (View) view
//                .findViewById(R.id.activity_list_item_indicator_view);
            return holder;
        }

        protected final class ViewHolder {

            public TextView titleTxv;

            public TextView updateTimeTxv;

            public TextView startTimeTxv;

            public TextView expireTimeTxv;

            public TextView descriptionTxv;

            public ImageView headImgv;

            public View indicatorView;
        }
    }
}



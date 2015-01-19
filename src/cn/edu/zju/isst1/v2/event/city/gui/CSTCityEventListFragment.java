package cn.edu.zju.isst1.v2.event.city.gui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.v2.event.base.EventCategory;
import cn.edu.zju.isst1.v2.event.base.EventRequest;
import cn.edu.zju.isst1.v2.event.city.data.CSTCityEvent;
import cn.edu.zju.isst1.v2.event.city.data.CSTCityEventDataDelegate;
import cn.edu.zju.isst1.v2.event.city.data.CSTCityEventProvider;
import cn.edu.zju.isst1.v2.event.city.net.CityEventResponse;
import cn.edu.zju.isst1.v2.gui.CSTBaseFragment;
import cn.edu.zju.isst1.v2.login.net.UpDateLogin;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;
import cn.edu.zju.isst1.v2.net.CSTStatusInfo;
import cn.edu.zju.isst1.v2.user.data.CSTUser;
import cn.edu.zju.isst1.v2.user.data.CSTUserDataDelegate;
import cn.edu.zju.isst1.v2.user.data.CSTUserProvider;
import pulltorefresh.widget.XListView;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * Created by always on 25/08/2014.
 */
public class CSTCityEventListFragment extends CSTBaseFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, XListView.IXListViewListener,
        AdapterView.OnItemClickListener {

    private static CSTCityEventListFragment INSTANCE = new CSTCityEventListFragment();

    private int mCurrentPage;

    private int DEFAULT_PAGE_SIZE = 20;

    private EventCategory mEventCategory = EventCategory.CITYEVENT;

    private boolean isLoadMore;

    private boolean isMoreData;

    private static final String SUB_URL = "/activities";

    private static final String CITY_ID = "cityId";

    private static final String EVENT_ID = "id";

    private static final String EVENT_TITLE = "eventTitle";

    private static int cityId;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private boolean mIsFirst;

    private Handler mHandler;

    private XListView mListView;

    private Handler rHandler;

//    private LayoutInflater mInflater;
//
//    private View mFooter;
//
//    private ProgressBar mLoadMorePrgb;
//
//    private TextView mLoadMoreHint;

    private CityEventListAdapter mAdapter;

//    private SwipeRefreshLayout mSwipeRefreshLayout;

    public CSTCityEventListFragment() {

    }

    public static CSTCityEventListFragment getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLoadMore = false;
        isMoreData = true;
        mIsFirst = true;
        mCurrentPage = 1;
        rHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mInflater = inflater;
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);

        getLoaderManager().initLoader(0, null, this);

        if (mIsFirst) {
            getCityId();
            onRefresh();
            mIsFirst = false;
        }
    }


    @Override
    protected void initComponent(View view) {
//        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
//        mSwipeRefreshLayout.setColorScheme(R.color.deepskyblue, R.color.darkorange, R.color.darkviolet,
//                R.color.lightcoral);
        mListView = (XListView) view.findViewById(R.id.simple_list);
//        mFooter = mInflater.inflate(R.layout.loadmore_footer, mListView, false);
//        mListView.addFooterView(mFooter);
//        mLoadMorePrgb = (ProgressBar) mFooter.findViewById(R.id.footer_loading_progress);
//        mLoadMorePrgb.setVisibility(View.GONE);
//        mLoadMoreHint = (TextView) mFooter.findViewById(R.id.footer_loading_hint);
//        requestData();
        bindAdapter();
        setUpListener();
        initHandler();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return CSTCityEventDataDelegate.getDataCursor(getActivity(), null, null, null,
                CSTCityEventProvider.Columns.UPDATEAT.key + " DESC");
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
        Intent intent = new Intent(getActivity(), CityEventDetailActivity.class);

        intent.putExtra(EVENT_ID, ((CSTCityEvent) view.getTag()).id);
        intent.putExtra(CITY_ID, ((CSTCityEvent) view.getTag()).cityId);
        intent.putExtra(EVENT_TITLE, ((CSTCityEvent) view.getTag()).title);
        getActivity().startActivity(intent);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.loadmore_footer:
//                startLoadMore();
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        isMoreData = true;
        mListView.setPullLoadEnable(true);
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
        mAdapter = new CityEventListAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);
    }

    private void setUpListener() {
        mListView.setOnItemClickListener(this);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAutoLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setRefreshTime(TSUtil.getTime());
//        mFooter.setOnClickListener(this);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
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
                            Lgr.i("CSTCityEventListFragment ----！------更新登录了-------！");
                            if (isLoadMore) {
                                mCurrentPage--;
                            }
                            requestData();
                        default:
                            CSTHttpUtil.dispose(msg.what, getActivity());
                            break;
                    }
                    resetLoadingState();
                }
            }
        };
    }

    private int getCityId() {
        ArrayList<CSTUser> users = new ArrayList<CSTUser>();
        Cursor cursor = getActivity().getContentResolver()
                .query(CSTUserProvider.CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            CSTUser userDemo = CSTUserDataDelegate.getUser(cursor);
            users.add(userDemo);
        }
        for (CSTUser user : users) {
            cityId = user.cityId;
            Lgr.i("cityId = " + cityId + "    username = " + user.userName.toString());
        }
        cursor.close();
        return cityId;
    }

    private void requestData() {
        if (isLoadMore) {
            mCurrentPage++;
        } else {
            mCurrentPage = 1;
//            mSwipeRefreshLayout.setRefreshing(true);
        }
        if (NetworkConnection.isNetworkConnected(getActivity())) {
            CityEventResponse eventResponse = new CityEventResponse(getActivity(),
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
                            if (!isMoreData) {
                                Toast.makeText(getActivity(), R.string.no_more_data, Toast.LENGTH_SHORT).show();
                                mListView.setPullLoadEnable(false);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

            EventRequest eventRequest = new EventRequest(CSTRequest.Method.GET,
                    "/api/cities/"+ cityId + SUB_URL, null,
                    eventResponse).setPage(mCurrentPage).setPageSize(DEFAULT_PAGE_SIZE);
            mEngine.requestJson(eventRequest);
        } else {
            Message msg = mHandler.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            mHandler.sendMessage(msg);
        }
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
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(TSUtil.getTime());
    }

    public class CityEventListAdapter extends CursorAdapter {

        private CSTCityEvent cityEvent;

        public CityEventListAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return inflater.inflate(R.layout.activity_list_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            cityEvent = CSTCityEventDataDelegate.getCityevent(cursor);
            view.setTag(cityEvent);
            ViewHolder holder = getBindViewHolder(view);
            holder.titleTxv.setText(cityEvent.title);
            holder.updateTimeTxv.setText(TSUtil.toYMD(cityEvent.updatedAt));
//            holder.startTimeTxv.setText(TSUtil.toHM(cityEvent.startTime));
//            holder.expireTimeTxv.setText(TSUtil.toHM(cityEvent.expireTime));
            holder.descriptionTxv.setText(cityEvent.description);


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

package cn.edu.zju.isst1.v2.restaurant.gui;

import android.app.LoaderManager;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.constant.Constants;
import cn.edu.zju.isst1.net.BetterAsyncWebServiceRunner;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.data.CSTRestaurant;
import cn.edu.zju.isst1.v2.gui.CSTBaseFragment;
import cn.edu.zju.isst1.v2.login.net.UpDateLogin;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;
import cn.edu.zju.isst1.v2.restaurant.data.CSTRestaurantDataDelegate;
import cn.edu.zju.isst1.v2.restaurant.data.CSTRestaurantProvider;
import cn.edu.zju.isst1.v2.restaurant.net.RestaurantResponse;

import static cn.edu.zju.isst1.constant.Constants.*;

/**
 * Created by lqynydyxf on 2014/8/28.
 */
public class NewRestaurantListFragment extends CSTBaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView mListView;

    private int mCurrentPage = 1;

    private int DEFAULT_PAGE_SIZE = 20;

    private LayoutInflater mInflater;

    private boolean isLoadMore = false;

    private boolean isMoreData = false;

    private View mFooter;

    private ProgressBar mLoadMorePrgb;

    private TextView mLoadMoreHint;

    private boolean mIsFirstTime;

    private RestaurantListAdapter mAdapter;

    private static NewRestaurantListFragment INSTANCE = new NewRestaurantListFragment();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Handler mHandler;

    private String ID = "id";

    public NewRestaurantListFragment() {
        mIsFirstTime = true;
    }

    public static NewRestaurantListFragment getInstance() {
        return INSTANCE;
    }

    private static final String RESTAURANT_URL = "/api/restaurants";

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

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

        if (mIsFirstTime) {
            requestData();
            mIsFirstTime = false;
        }
    }

    @Override
    protected void initComponent(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorScheme(R.color.lightbluetheme_color,
                R.color.lightbluetheme_color_half_alpha, R.color.lightbluetheme_color,
                R.color.lightbluetheme_color_half_alpha);
        mListView = (ListView) view.findViewById(R.id.simple_list);
        mFooter = mInflater.inflate(R.layout.loadmore_footer, mListView, false);
        mListView.addFooterView(mFooter);
        mLoadMorePrgb = (ProgressBar) mFooter.findViewById(R.id.footer_loading_progress);
        mLoadMorePrgb.setVisibility(ProgressBar.GONE);
        mLoadMoreHint = (TextView) mFooter.findViewById(R.id.footer_loading_hint);
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
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return CSTRestaurantDataDelegate.getDataCursor(getActivity(), null, null, null,
                CSTRestaurantProvider.Columns.ID.key + " DESC");
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), NewRestaurantDetailActivity.class);
        intent.putExtra(ID, ((CSTRestaurant) view.getTag()).id);
        getActivity().startActivity(intent);
    }

    private void bindAdapter() {
        mAdapter = new RestaurantListAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);
    }

    private void setUpListener() {
        mListView.setOnItemClickListener(this);
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
                        UpDateLogin.getInstance().updateLogin(getActivity());
                        requestData();
                    case NETWORK_NOT_CONNECTED:
                        CroMan.showAlert(getActivity(), R.string.network_not_connected);
                    default:
                        CSTHttpUtil.dispose(msg.what, getActivity());
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
        if (NetworkConnection.isNetworkConnected(getActivity())) {
            RestaurantResponse resResponse = new RestaurantResponse(getActivity(), !isLoadMore) {
                @Override
                public void onResponse(JSONObject response) {
                    Lgr.i(response.toString());
                    CSTRestaurant restaurant = (CSTRestaurant) CSTJsonParser.parseJson(response, new CSTRestaurant());
                    for (CSTRestaurant restaurant_demo : restaurant.itemList) {
                        CSTRestaurantDataDelegate.saveRestaurant(mContext, restaurant_demo);
                    }
                    Lgr.i(Integer.toString(restaurant.itemList.size()));
                    Message msg = mHandler.obtainMessage();
                    if (isLoadMore) {
                        try {
                            isMoreData = response.getJSONArray("body").length() == 0 ? false : true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
            String subUrlParams = null;
            try {
                subUrlParams = RESTAURANT_URL + (Judge.isNullOrEmpty(paramsMap) ? ""
                        : ("?" + BetterAsyncWebServiceRunner
                        .getInstance().paramsToString(paramsMap)));
                Lgr.i(subUrlParams);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            CSTJsonRequest resRequest = new CSTJsonRequest(CSTRequest.Method.GET, subUrlParams,
                    null, resResponse);
            mEngine.requestJson(resRequest);
        } else {
            Message msg = mHandler.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            mHandler.sendMessage(msg);
        }
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
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadMorePrgb.setVisibility(ProgressBar.GONE);
        if (isLoadMore && !isMoreData) {
            mLoadMoreHint.setText(R.string.footer_loading_hint_no_more_data);
        } else {
            mLoadMoreHint.setText(R.string.footer_loading_hint);
        }
    }
}

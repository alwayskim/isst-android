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
import cn.edu.zju.isst1.util.TSUtil;
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
import cn.edu.zju.isst1.v2.restaurant.net.RestaurantRequest;
import cn.edu.zju.isst1.v2.restaurant.net.RestaurantResponse;
import cn.edu.zju.isst1.widget.NewSwipeRefreshLayout;
import pulltorefresh.widget.XListView;

import static cn.edu.zju.isst1.constant.Constants.*;

/**
 * Created by lqynydyxf on 2014/8/28.
 */
public class NewRestaurantListFragment extends CSTBaseFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private ListView listView;

    private NewSwipeRefreshLayout mSwipeRefreshLayout;

    private int mCurrentPage = 1;

    private int DEFAULT_PAGE_SIZE = 20;

//    private LayoutInflater mInflater;

    private boolean isLoadMore;

    private Handler rHandler;

    private boolean mIsFirst = true;

    private RestaurantListAdapter mAdapter;

    private static NewRestaurantListFragment INSTANCE = new NewRestaurantListFragment();


    private Handler mHandler;

    private String ID = "id";

    public NewRestaurantListFragment() {
        mIsFirst = true;
    }

    public static NewRestaurantListFragment getInstance() {
        return INSTANCE;
    }

    private static final String RESTAURANT_URL = "/api/restaurants";


    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLoadMore = false;
        mIsFirst = true;
        mCurrentPage = 1;
        rHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mInflater = inflater;
        return inflater.inflate(R.layout.new_swipe_to_refresh, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void initComponent(View view) {


        listView = (ListView) view.findViewById(R.id.listview);
        mSwipeRefreshLayout = (NewSwipeRefreshLayout) view.findViewById(R.id.fragment_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorScheme(R.color.red,
                R.color.blueviolet, R.color.green, R.color.white);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mSwipeRefreshLayout.isLoading()) {
                    isLoadMore = false;
                    requestData();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        mSwipeRefreshLayout.setOnLoadListener(new NewSwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                isLoadMore = true;
                requestData();
            }
        });
        mAdapter = new RestaurantListAdapter(mContext, null);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        initHandler();
        if (mIsFirst) {
            isLoadMore = false;
            mSwipeRefreshLayout.setRefreshing(true);
            requestData();
            mIsFirst = false;
        }
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return CSTRestaurantDataDelegate.getDataCursor(mContext, null, null, null,
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
        Intent intent = new Intent(mContext, NewRestaurantDetailActivity.class);
        intent.putExtra(ID, ((CSTRestaurant) ((RestaurantListAdapter.ViewHolder) view.getTag()).nameTxv.getTag()).id);
        mContext.startActivity(intent);
    }


    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.STATUS_REQUEST_SUCCESS:
                        break;
                    case STATUS_NOT_LOGIN:

                        UpDateLogin.getInstance().updateLogin(mContext);
                        requestData();

                    case NETWORK_NOT_CONNECTED:

                        CroMan.showAlert(mContext, R.string.network_not_connected);

                    default:

                        CSTHttpUtil.dispose(msg.what, mContext);

                        break;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setLoading(false);
                    }
                }, 1500);
            }
        };

    }

    private void requestData() {
        if (isLoadMore) {
            mCurrentPage++;
        } else {
            mCurrentPage = 1;
        }
        if (NetworkConnection.isNetworkConnected(mContext)) {
            RestaurantResponse resResponse = new RestaurantResponse(mContext, !isLoadMore) {
                @Override
                public void onResponse(JSONObject response) {
                    super.onResponse(response);
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

            RestaurantRequest resRequest = new RestaurantRequest(CSTRequest.Method.GET,
                    RESTAURANT_URL, null,
                    resResponse).setPage(mCurrentPage).setPageSize(DEFAULT_PAGE_SIZE);
            mEngine.requestJson(resRequest);
        } else {
            Message msg = mHandler.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            mHandler.sendMessage(msg);
        }
    }

}

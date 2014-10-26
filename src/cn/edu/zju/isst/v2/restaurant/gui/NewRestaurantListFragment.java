package cn.edu.zju.isst.v2.restaurant.gui;

import org.json.JSONObject;

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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst.R;
import cn.edu.zju.isst.net.BetterAsyncWebServiceRunner;
import cn.edu.zju.isst.util.Judge;
import cn.edu.zju.isst.util.Lgr;
import cn.edu.zju.isst.v2.data.CSTJsonParser;
import cn.edu.zju.isst.v2.data.CSTRestaurant;
import cn.edu.zju.isst.v2.gui.CSTBaseFragment;
import cn.edu.zju.isst.v2.net.CSTJsonRequest;
import cn.edu.zju.isst.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst.v2.net.CSTRequest;
import cn.edu.zju.isst.v2.restaurant.data.CSTRestaurantDataDelegate;
import cn.edu.zju.isst.v2.restaurant.data.CSTRestaurantProvider;
import cn.edu.zju.isst.v2.restaurant.net.RestaurantResponse;
import cn.edu.zju.isst.constant.Constants;

/**
 * Created by lqynydyxf on 2014/8/28.
 */
public class NewRestaurantListFragment extends CSTBaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {

    private ListView mListView;

    private RestaurantListAdapter mAdapter;

    private static NewRestaurantListFragment INSTANCE = new NewRestaurantListFragment();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Handler mHandler;

    private String ID = "id";

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
        return inflater.inflate(R.layout.base_archive_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);

        getLoaderManager().initLoader(0, null, this);
        requestData();
    }

    @Override
    protected void initComponent(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorScheme(R.color.lightbluetheme_color,
                R.color.lightbluetheme_color_half_alpha, R.color.lightbluetheme_color,
                R.color.lightbluetheme_color_half_alpha);
        mListView = (ListView) view.findViewById(R.id.simple_list);

        bindAdapter();
        setUpListener();
        initHandler();
    }

    @Override
    public void onRefresh() {
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
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.STATUS_REQUEST_SUCCESS:
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void requestData() {
        RestaurantResponse resResponse = new RestaurantResponse(getActivity()) {
            @Override
            public void onResponse(JSONObject response) {
                Lgr.i(response.toString());
                CSTRestaurant restaurant = (CSTRestaurant) CSTJsonParser
                        .parseJson(response, new CSTRestaurant());
                CSTRestaurantDataDelegate
                        .saveRestaurantList(NewRestaurantListFragment.this.getActivity(),
                                restaurant);
                Message msg = mHandler.obtainMessage();
                msg.what = Constants.STATUS_REQUEST_SUCCESS;
                mHandler.sendMessage(msg);
            }
        };
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("page", "" + 1);
        paramsMap.put("pageSize", "" + 20);
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
                null,
                resResponse);
        mEngine.requestJson(resRequest);
    }
}

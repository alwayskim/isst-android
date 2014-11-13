/**
 *
 */
package cn.edu.zju.isst1.ui.life;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.api.CampusActivityApi;
import cn.edu.zju.isst1.db.CampusActivity;
import cn.edu.zju.isst1.db.DataManager;
import cn.edu.zju.isst1.exception.ExceptionWeeder;
import cn.edu.zju.isst1.exception.HttpErrorWeeder;
import cn.edu.zju.isst1.net.CSTResponse;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.net.RequestListener;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * @author theasir
 */
public class CampusActivityListFragment extends ListFragment implements
        OnScrollListener {

    private static CampusActivityListFragment INSTANCE = new CampusActivityListFragment();

    private final List<CampusActivity> m_listCampusActivity = new ArrayList<CampusActivity>();

    private int m_nVisibleLastIndex;

    private int m_nCurrentPage;

    private boolean m_bIsFirstTime;

    private LoadType m_loadType;

    private Handler m_handlerCampusActivityList;

    private CampusActivityListAdapter m_adapterCampusActivityList;

    private ListView m_lsvCampusActivityList;

    public CampusActivityListFragment() {
        m_nCurrentPage = 1;
        m_bIsFirstTime = true;
    }

    public static CampusActivityListFragment getInstance() {
        return INSTANCE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu,
     * android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.news_list_fragment_ab_menu, menu);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem
     * )
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                requestData(LoadType.REFRESH);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater
     * , android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.ListFragment#onViewCreated(android.view.View,
     * android.os.Bundle)
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);

        if (m_bIsFirstTime) {
            initCampusActivityList();
        }

        initHandler();

        setUpAdapter();

        setUpListener();

        if (m_bIsFirstTime) {
            requestData(LoadType.REFRESH);
            m_bIsFirstTime = false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView
     * , android.view.View, int, long)
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Lgr.i(this.getClass().getName() + " onListItemClick postion = "
                + position);
        Intent intent = new Intent(getActivity(),
                CampusActivityDetailActivity.class);
        intent.putExtra("id", m_listCampusActivity.get(position).getId());
        getActivity().startActivity(intent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Lgr.i(this.getClass().getName()
                + " onScrollStateChanged VisibleLastIndex = "
                + m_nVisibleLastIndex);
        if (scrollState == SCROLL_STATE_IDLE
                && m_nVisibleLastIndex == m_adapterCampusActivityList
                .getCount() - 1) {
            requestData(LoadType.LOADMORE);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        m_nVisibleLastIndex = firstVisibleItem + visibleItemCount - 1;
    }

    private void initComponent(View view) {
        m_lsvCampusActivityList = (ListView) view
                .findViewById(android.R.id.list);
    }

    /**
     * 初始化在校活动列表，若有缓存则读取缓存
     */
    private void initCampusActivityList() {
        List<CampusActivity> dbCampusActivityList = DataManager
                .getCampusActivityList();
        if (!Judge.isNullOrEmpty(dbCampusActivityList)) {
            for (CampusActivity news : dbCampusActivityList) {
                m_listCampusActivity.add(news);
            }
        }
    }

    private void initHandler() {
        m_handlerCampusActivityList = new Handler() {

            /*
             * (non-Javadoc)
             *
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        switch (m_loadType) {
                            case REFRESH:
                                refresh((JSONObject) msg.obj);
                                break;
                            case LOADMORE:
                                loadMore((JSONObject) msg.obj);
                                break;
                            default:
                                break;
                        }
                        m_adapterCampusActivityList.notifyDataSetChanged();
                        break;
                    case STATUS_NOT_LOGIN:// TODO
                        ((BaseActivity) getActivity()).updateLogin();
                        requestData(m_loadType);
                        break;
                    default:
                        ((BaseActivity) getActivity()).dispose(msg);
                        break;
                }
            }

        };
    }

    private void setUpAdapter() {
        m_adapterCampusActivityList = new CampusActivityListAdapter(
                getActivity());
        setListAdapter(m_adapterCampusActivityList);

    }

    private void setUpListener() {
        m_lsvCampusActivityList.setOnScrollListener(this);

    }

    /**
     * 刷新列表
     *
     * @param jsonObject 数据源
     */
    private void refresh(JSONObject jsonObject) {
        if (!m_listCampusActivity.isEmpty()) {
            m_listCampusActivity.clear();
        }
        try {
            if (!Judge.isValidJsonValue("body", jsonObject)) {
                return;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("body");

            for (int i = 0; i < jsonArray.length(); i++) {
                m_listCampusActivity.add(new CampusActivity(
                        (JSONObject) jsonArray.get(i)));
            }
            Lgr.i(this.getClass().getName() + " refreshList: "
                    + "Added campusActivity to newsList!");
            DataManager.syncCampusActivityList(m_listCampusActivity);
        } catch (JSONException e) {
            Lgr.i(this.getClass().getName() + " refreshList!");
            e.printStackTrace();
        }
    }

    /**
     * 加载更多
     *
     * @param jsonObject 数据源
     */
    private void loadMore(JSONObject jsonObject) {
        JSONArray jsonArray;
        try {
            if (!Judge.isValidJsonValue("body", jsonObject)) {
                return;
            }
            jsonArray = jsonObject.getJSONArray("body");

            for (int i = 0; i < jsonArray.length(); i++) {
                m_listCampusActivity.add(new CampusActivity(
                        (JSONObject) jsonArray.get(i)));
            }
            Lgr.i(this.getClass().getName() + " loadMore: "
                    + "Added campusActivity to newsList!");
        } catch (JSONException e) {
            Lgr.i(this.getClass().getName() + " loadMore!");
            e.printStackTrace();
        }
    }

    /**
     * 请求数据
     *
     * @param type 加载方式
     */
    private void requestData(LoadType type) {
        if (NetworkConnection.isNetworkConnected(getActivity())) {
            m_loadType = type;
            switch (type) {
                case REFRESH:
                    CampusActivityApi.getCampusActivityList(1, 20, null,
                            new NewsListRequestListener());
                    m_nCurrentPage = 1;
                    break;
                case LOADMORE:
                    CampusActivityApi.getCampusActivityList(++m_nCurrentPage, 20,
                            null, new NewsListRequestListener());
                    break;
                default:
                    break;
            }
        } else {
            Message msg = m_handlerCampusActivityList.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            m_handlerCampusActivityList.sendMessage(msg);
        }
    }

    /**
     * 加载方式枚举类
     *
     * @author theasir
     */
    private enum LoadType {
        REFRESH, LOADMORE;
    }

    /**
     * 新闻列表RequestListener类
     *
     * @author theasir
     */
    private class NewsListRequestListener implements RequestListener {

        @Override
        public void onComplete(Object result) {
            Message msg = m_handlerCampusActivityList.obtainMessage();
            try {
                if (!Judge.isValidJsonValue("status", (JSONObject) result)) {
                    return;
                }
                msg.what = ((JSONObject) result).getInt("status");
                msg.obj = (JSONObject) result;
            } catch (JSONException e) {
                Lgr.i(this.getClass().getName() + " onComplete!");
                e.printStackTrace();
            }

            m_handlerCampusActivityList.sendMessage(msg);
        }

        @Override
        public void onHttpError(CSTResponse response) {
            Lgr.i(this.getClass().getName() + " onHttpError!");
            Message msg = m_handlerCampusActivityList.obtainMessage();
            HttpErrorWeeder.fckHttpError(response, msg);
            m_handlerCampusActivityList.sendMessage(msg);
        }

        @Override
        public void onException(Exception e) {
            Lgr.i(this.getClass().getName() + " onException!");
            Message msg = m_handlerCampusActivityList.obtainMessage();
            ExceptionWeeder.fckException(e, msg);
            m_handlerCampusActivityList.sendMessage(msg);
        }

    }

    /**
     * View容器类
     *
     * @author theasir
     */
    private final class ViewHolder {

        public TextView titleTxv;

        public TextView updateTimeTxv;

        public TextView startTimeTxv;

        public TextView expireTimeTxv;

        public TextView descriptionTxv;

        public View indicatorView;
    }

    /**
     * 在校活动列表自定义适配器类
     *
     * @author theasir
     */
    private class CampusActivityListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public CampusActivityListAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return m_listCampusActivity.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(
                        R.layout.campus_activity_list_item, null);
                holder.titleTxv = (TextView) convertView
                        .findViewById(R.id.campus_activity_list_item_title_txv);
                holder.updateTimeTxv = (TextView) convertView
                        .findViewById(R.id.campus_activity_list_item_updatetime_txv);
                holder.startTimeTxv = (TextView) convertView
                        .findViewById(R.id.campus_activity_list_item_starttime_txv);
                holder.expireTimeTxv = (TextView) convertView
                        .findViewById(R.id.campus_activity_list_item_expiretime_txv);
                holder.descriptionTxv = (TextView) convertView
                        .findViewById(R.id.campus_activity_list_item_description_txv);
                holder.indicatorView = (View) convertView
                        .findViewById(R.id.campus_activity_list_item_indicator_view);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.titleTxv.setText(m_listCampusActivity.get(position)
                    .getTitle());
            holder.updateTimeTxv.setText(TSUtil.toYMD(m_listCampusActivity
                    .get(position).getUpdatedAt()));
            holder.startTimeTxv.setText(TSUtil.toHM(m_listCampusActivity
                    .get(position).getStartTime()));
            holder.expireTimeTxv.setText(TSUtil.toHM(m_listCampusActivity
                    .get(position).getExpireTime()));
            holder.descriptionTxv.setText(m_listCampusActivity.get(position)
                    .getDescription());

            return convertView;
        }

    }
}

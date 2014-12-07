/**
 *
 */
package cn.edu.zju.isst1.v2.usercenter.myactivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.api.UserCenterApi;
import cn.edu.zju.isst1.db.DataManager;
import cn.edu.zju.isst1.db.MyParticipatedActivity;
import cn.edu.zju.isst1.db.MyPublicActivity;
import cn.edu.zju.isst1.exception.ExceptionWeeder;
import cn.edu.zju.isst1.exception.HttpErrorWeeder;
import cn.edu.zju.isst1.net.CSTResponse;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.net.RequestListener;
import cn.edu.zju.isst1.ui.main.NewMainActivity;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.v2.event.city.gui.CityEventDetailActivity;
import cn.edu.zju.isst1.widget.PullToRefeshView;
import cn.edu.zju.isst1.widget.PullToRefeshView.PullToRefreshListener;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * @author yyy
 */
public class MyActivitiesFragment extends ListFragment implements
        OnScrollListener {

    private static MyActivitiesFragment INSTANCE = new MyActivitiesFragment();

    private final List<MyPublicActivity> m_listPublic = new ArrayList<MyPublicActivity>();

    private final List<MyParticipatedActivity> m_listParticipated
            = new ArrayList<MyParticipatedActivity>();

    private int m_nVisibleLastIndex;

    private int m_nCurrentPage;

    private boolean m_bIsFirstTime;

    private int m_type;// 0发布的，1参加的

    private ArrayList<String> m_arrayListType = new ArrayList<String>();

    private LoadType m_loadType;

    private Handler m_handlerArchiveList;

    private ArchiveListAdapter m_adapterArchiveList;

    private PullToRefeshView m_ptrView;

    private ListView m_lsvArchiveList;

    /**
     *
     */
    public MyActivitiesFragment() {
        // TODO Auto-generated constructor stub
        m_arrayListType.add("我发布的活动");
        m_arrayListType.add("我参加的活动");
        m_nCurrentPage = 1;
        m_bIsFirstTime = true;

    }

    public static MyActivitiesFragment getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SpinnerAdapter adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.action_bar_city_item, m_arrayListType);
        // 得到ActionBar
        ActionBar actionBar = getActivity().getActionBar();
        // 将ActionBar的操作模型设置为NAVIGATION_MODE_LIST
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // 为ActionBar设置下拉菜单和监听器
        actionBar.setListNavigationCallbacks(adapter, new DropDownListenser());

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        // inflater.inflate(R.menu.news_list_fragment_ab_menu, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_activties_list_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // 控件

        m_type = 0;
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setSelectedNavigationItem(m_type);
        initComponent(view);

        if (m_bIsFirstTime) {
            initArchiveList();
            m_bIsFirstTime = false;
        }

        initHandler();

        setUpAdapter();

        setUpListener();

        // L.d(tag, msg)
        m_ptrView.setOnRefreshListener(new PullToRefreshListener() {

            @Override
            public void onRefresh() {
                Lgr.i(this.getClass().getName()
                        + "-----enter onViewCreated---onRefresh---");
                requestData(LoadType.REFRESH);
            }
        }, 0);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        // 得到ActionBar
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        super.onDestroyView();
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
                CityEventDetailActivity.class);
        if (m_type == 0) {
            intent.putExtra("id", m_listPublic.get(position).id);
            intent.putExtra("cityId", m_listPublic.get(position).cityId);
        } else {
            intent.putExtra("id", m_listParticipated.get(position).id);
            intent.putExtra("cityId", m_listParticipated.get(position).cityId);

        }
        getActivity().startActivity(intent);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE
                && m_nVisibleLastIndex == m_adapterArchiveList.getCount() - 1) {
            requestData(LoadType.LOADMORE);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        m_nVisibleLastIndex = firstVisibleItem + visibleItemCount - 1;
    }

    protected void initComponent(View view) {
        m_ptrView = (PullToRefeshView) view
                .findViewById(R.id.archive_list_fragment_ptr_view);
        m_lsvArchiveList = (ListView) view.findViewById(android.R.id.list);
    }

    /**
     * 初始化归档列表，若有缓存则读取缓存
     */
    protected void initArchiveList() {
        List<MyPublicActivity> dbPublicList = getPublicList();
        m_listPublic.clear();
        if (!Judge.isNullOrEmpty(dbPublicList)) {
            for (MyPublicActivity publicActivity : dbPublicList) {
                m_listPublic.add(publicActivity);
            }
        }
        if (Judge.isNullOrEmpty(m_listPublic)) {
            requestData(LoadType.REFRESH);
        }
        // /
        List<MyParticipatedActivity> dbParticipatedList = getParticipatedList();
        m_listParticipated.clear();
        if (!Judge.isNullOrEmpty(dbParticipatedList)) {
            for (MyParticipatedActivity participatedActivity : dbParticipatedList) {
                m_listParticipated.add(participatedActivity);
            }
        }
        if (Judge.isNullOrEmpty(m_listParticipated)) {
            requestData(LoadType.REFRESH);
        }
    }

    @SuppressLint("HandlerLeak")
    protected void initHandler() {
        m_handlerArchiveList = new Handler() {

            /*
             * (non-Javadoc)
             *
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
            @Override
            public void handleMessage(Message msg) {
                m_ptrView.finishRefreshing();
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
                        m_adapterArchiveList.notifyDataSetChanged();
                        break;
                    case STATUS_NOT_LOGIN:// TODO
                        ((NewMainActivity) getActivity()).updateLogin();
                        requestData(m_loadType);
                        break;
                    default:
                        ((NewMainActivity) getActivity()).dispose(msg);
                        break;
                }
            }

        };
    }

    protected void setUpAdapter() {
        m_adapterArchiveList = new ArchiveListAdapter(getActivity());
        setListAdapter(m_adapterArchiveList);
    }

    protected void setUpListener() {
        m_lsvArchiveList.setOnScrollListener(this);
    }

    /**
     * 刷新列表
     *
     * @param jsonObject 数据源
     */
    protected void refresh(JSONObject jsonObject) {
        if (!m_listPublic.isEmpty()) {
            m_listPublic.clear();
        }
        if (!m_listParticipated.isEmpty()) {
            m_listParticipated.clear();
        }
        try {
            if (!Judge.isValidJsonValue("body", jsonObject)) {
                return;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("body");
            if (m_type == 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    m_listPublic.add(new MyPublicActivity(
                            (JSONObject) jsonArray.get(i)));
                }
                syncPublicList();
            } else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    m_listParticipated.add(new MyParticipatedActivity(
                            (JSONObject) jsonArray.get(i)));
                }
                syncParticipatedList();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载更多
     *
     * @param jsonObject 数据源
     */
    protected void loadMore(JSONObject jsonObject) {
        JSONArray jsonArray;
        try {
            if (!Judge.isValidJsonValue("body", jsonObject)) {
                return;
            }
            jsonArray = jsonObject.getJSONArray("body");
            if (m_type == 0) {
                for (int i = 0; i < jsonArray.length(); i++) {

                    m_listPublic.add(new MyPublicActivity(
                            (JSONObject) jsonArray.get(i)));
                }
            } else {
                for (int i = 0; i < jsonArray.length(); i++) {

                    m_listParticipated.add(new MyParticipatedActivity(
                            (JSONObject) jsonArray.get(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求数据
     *
     * @param type 加载方式
     */
    protected void requestData(LoadType type) {

        if (NetworkConnection.isNetworkConnected(getActivity())) {
            m_loadType = type;
            switch (type) {
                case REFRESH:
                    if (m_type == 1) {
                        UserCenterApi.getMyActivities(1, 20,
                                new ListRequestListener());
                    } else {
                        UserCenterApi.getMyPublicActivities(1, 20,
                                new ListRequestListener());
                    }
                    m_nCurrentPage = 1;
                    break;
                case LOADMORE:
                    if (m_type == 1) {
                        UserCenterApi.getMyActivities(++m_nCurrentPage, 20,
                                new ListRequestListener());
                    } else {
                        UserCenterApi.getMyPublicActivities(++m_nCurrentPage, 20,
                                new ListRequestListener());
                    }
                    break;
                default:
                    break;
            }
        } else {
            Message msg = m_handlerArchiveList.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            m_handlerArchiveList.sendMessage(msg);
        }

    }

    protected List<MyPublicActivity> getPublicList() {
        return DataManager.getMyPublicActivityList();
    }

    protected void syncPublicList() {
        DataManager.syncMyPublicActivityList(m_listPublic);
    }

    protected List<MyParticipatedActivity> getParticipatedList() {
        return DataManager.getMyParticipatedActivityList();
    }

    protected void syncParticipatedList() {
        DataManager.syncMyParticipatedActivityList(m_listParticipated);
    }

    /**
     * 加载方式枚举类
     *
     * @author theasir
     */
    public enum LoadType {
        REFRESH, LOADMORE;
    }

    /**
     * 实现 ActionBar.OnNavigationListener接口
     */
    private class DropDownListenser implements OnNavigationListener {

        @Override
        public boolean onNavigationItemSelected(int arg0, long arg1) {
            Lgr.i("arg0 = " + arg0 + " ; m_type = " + m_type);
            if (arg0 != m_type) {
                m_type = arg0;
                requestData(LoadType.REFRESH);
            }
            return false;
        }
    }

    /**
     * 归档列表RequestListener类
     *
     * @author theasir
     */
    public class ListRequestListener implements RequestListener {

        @Override
        public void onComplete(Object result) {
            Message msg = m_handlerArchiveList.obtainMessage();
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

            m_handlerArchiveList.sendMessage(msg);
        }

        @Override
        public void onHttpError(CSTResponse response) {
            Lgr.i(this.getClass().getName() + " onHttpError!");
            Message msg = m_handlerArchiveList.obtainMessage();
            HttpErrorWeeder.fckHttpError(response, msg);
            m_handlerArchiveList.sendMessage(msg);
        }

        @Override
        public void onException(Exception e) {
            Lgr.i(this.getClass().getName() + " onException!");
            Message msg = m_handlerArchiveList.obtainMessage();
            ExceptionWeeder.fckException(e, msg);
            m_handlerArchiveList.sendMessage(msg);
        }

    }

    /**
     * View容器类
     *
     * @author theasir
     */
    protected final class ViewHolder {

        public TextView titleTxv;

        public TextView dateTxv;

        public TextView publisherTxv;

        public TextView descriptionTxv;

        public View indicatorView;
    }

    /**
     * 归档列表自定义适配器类
     *
     * @author theasir
     */
    public class ArchiveListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public ArchiveListAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (m_type == 0) {
                return m_listPublic.size();
            } else {
                return m_listParticipated.size();
            }
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

                convertView = inflater
                        .inflate(R.layout.archive_list_item, null);
                holder.titleTxv = (TextView) convertView
                        .findViewById(R.id.title_txv);
                holder.dateTxv = (TextView) convertView
                        .findViewById(R.id.date_txv);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (m_type == 0) {
                holder.titleTxv.setText(m_listPublic.get(position).getTitle());
                holder.dateTxv.setText(TSUtil.toYMD(m_listPublic.get(
                        position).getUpdatedAt()));
            } else {
                holder.titleTxv.setText(m_listParticipated.get(position)
                        .getTitle());
                holder.dateTxv.setText(TSUtil.toYMD(m_listParticipated.get(
                        position).getUpdatedAt()));

            }
            // holder.indicatorView.setBackgroundColor(Color.BLUE);

            return convertView;
        }

    }

}

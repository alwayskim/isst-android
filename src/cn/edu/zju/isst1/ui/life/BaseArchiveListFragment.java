package cn.edu.zju.isst1.ui.life;

import android.annotation.SuppressLint;
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
import cn.edu.zju.isst1.api.ArchiveCategory;
import cn.edu.zju.isst1.db.Archive;
import cn.edu.zju.isst1.db.DataManager;
import cn.edu.zju.isst1.exception.ExceptionWeeder;
import cn.edu.zju.isst1.exception.HttpErrorWeeder;
import cn.edu.zju.isst1.net.CSTResponse;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.net.RequestListener;
import cn.edu.zju.isst1.ui.main.NewMainActivity;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.v2.archive.gui.ArchiveDetailActivity;
import cn.edu.zju.isst1.v2.archive.net.ArchiveApi;
import cn.edu.zju.isst1.widget.PullToRefeshView;
import cn.edu.zju.isst1.widget.PullToRefeshView.PullToRefreshListener;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * 归档列表基类
 *
 * @author theasir
 */
public class BaseArchiveListFragment extends ListFragment implements
        OnScrollListener {

    private final List<Archive> m_listAchive = new ArrayList<Archive>();

    private int m_nVisibleLastIndex;

    private int m_nCurrentPage;

    private boolean m_bIsFirstTime;

    private ArchiveCategory m_archiveCategory;

    private LoadType m_loadType;

    private Handler m_handlerArchiveList;

    private ArchiveListAdapter m_adapterArchiveList;

    private PullToRefeshView m_ptrView;

    private ListView m_lsvArchiveList;

    public BaseArchiveListFragment() {
        m_nCurrentPage = 1;
        m_bIsFirstTime = true;
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
//		inflater.inflate(R.menu.news_list_fragment_ab_menu, menu);
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
        return inflater.inflate(R.layout.archive_list_fragment, null);
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
        Lgr.i(this.getClass().getName() + "----enter---- onViewCreated");
        initComponent(view);

        if (m_bIsFirstTime) {
            initArchiveList();
            m_bIsFirstTime = false;
        }

        initHandler();

        setUpAdapter();

        setUpListener();

        //L.d(tag, msg)
        m_ptrView.setOnRefreshListener(new PullToRefreshListener() {

            @Override
            public void onRefresh() {
                Lgr.i(this.getClass().getName() + "-----enter onViewCreated---onRefresh---");
                requestData(LoadType.REFRESH);
            }
        }, 0);

//		if (m_bIsFirstTime) {
////			requestData(LoadType.REFRESH);
//			m_bIsFirstTime = false;
//		}

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
        Intent intent = new Intent(getActivity(), ArchiveDetailActivity.class);
        intent.putExtra("id", m_listAchive.get(position).getId());
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

    public void setArchiveCategory(ArchiveCategory archiveCategory) {
        m_archiveCategory = archiveCategory;
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
        List<Archive> dbArchiveList = getArchiveList();
        m_listAchive.clear();
        if (!Judge.isNullOrEmpty(dbArchiveList)) {
            for (Archive archive : dbArchiveList) {
                m_listAchive.add(archive);
            }
        }
        if (Judge.isNullOrEmpty(m_listAchive)) {
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
        if (!m_listAchive.isEmpty()) {
            m_listAchive.clear();
        }
        try {
            if (!Judge.isValidJsonValue("body", jsonObject)) {
                return;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("body");

            for (int i = 0; i < jsonArray.length(); i++) {
                m_listAchive.add(new Archive((JSONObject) jsonArray.get(i)));
            }
            syncArchiveList();
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

            for (int i = 0; i < jsonArray.length(); i++) {
                m_listAchive.add(new Archive((JSONObject) jsonArray.get(i)));
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
                    ArchiveApi.getArchiveList(m_archiveCategory, 1, 20, "",
                            new ArchiveListRequestListener());
                    m_nCurrentPage = 1;
                    break;
                case LOADMORE:
                    ArchiveApi.getArchiveList(m_archiveCategory, ++m_nCurrentPage,
                            20, "", new ArchiveListRequestListener());
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

    protected List<Archive> getArchiveList() {
        return DataManager.getArchiveList(m_archiveCategory);
    }

    protected void syncArchiveList() {
        DataManager.syncArchiveList(m_archiveCategory, m_listAchive);
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
     * 归档列表RequestListener类
     *
     * @author theasir
     */
    public class ArchiveListRequestListener implements RequestListener {

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
            return m_listAchive.size();
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
                holder.publisherTxv = (TextView) convertView
                        .findViewById(R.id.publisher_txv);
//                holder.descriptionTxv = (TextView) convertView
//                        .findViewById(R.id.description_txv);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.titleTxv.setText(m_listAchive.get(position).getTitle());
            holder.dateTxv.setText(TSUtil.toYMD(m_listAchive.get(position)
                    .getUpdatedAt()));
            holder.publisherTxv.setText(m_listAchive.get(position)
                    .getPublisher().getName());
            holder.descriptionTxv.setText(m_listAchive.get(position)
                    .getDescription());

            return convertView;
        }

    }

}
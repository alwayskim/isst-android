package cn.edu.zju.isst1.ui.main;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.api.JobApi;
import cn.edu.zju.isst1.api.JobCategory;
import cn.edu.zju.isst1.db.DataManager;
import cn.edu.zju.isst1.db.Job;
import cn.edu.zju.isst1.exception.ExceptionWeeder;
import cn.edu.zju.isst1.exception.HttpErrorWeeder;
import cn.edu.zju.isst1.net.CSTResponse;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.net.RequestListener;
import cn.edu.zju.isst1.ui.job.JobDetailActivity;
import cn.edu.zju.isst1.v2.usercenter.myrecommend.PublishRecommendActivity;
import cn.edu.zju.isst1.v2.usercenter.myrecommend.RecommendDetailActivity;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.widget.NewSwipeRefreshLayout;
import pulltorefresh.widget.XListView;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * 归档列表基类
 *
 * @author theasir
 */
public class BaseJobsListFragment extends ListFragment {

    private final List<Job> m_listAchive = new ArrayList<Job>();

    private int m_nCurrentPage;

    private boolean m_bIsFirstTime;

    private JobCategory m_jobCategory;

    private LoadType m_loadType;

    private Handler m_handlerJobList;

    private JobListAdapter m_adapterJobList;

    private View m_viewContainer;

    private Handler rHandler = new Handler();

    private ListView listView;

    private NewSwipeRefreshLayout mSwipeRefreshLayout;

    private boolean isLoadMore;


    public BaseJobsListFragment() {
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
        m_nCurrentPage = 1;
        m_bIsFirstTime = true;
        isLoadMore = false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.job_list_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        initComponent(view);

        if (m_bIsFirstTime) {
            initJobList();
        }

        setUpAdapter();

        initHandler();
        if (m_bIsFirstTime) {
            isLoadMore = false;
            mSwipeRefreshLayout.setRefreshing(true);
            requestData(LoadType.REFRESH);
            m_bIsFirstTime = false;
        }
    }

    public void setJobCategory(JobCategory jobCategory) {
        m_jobCategory = jobCategory;
    }

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
                    requestData(LoadType.REFRESH);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        mSwipeRefreshLayout.setOnLoadListener(new NewSwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                isLoadMore = true;
                requestData(LoadType.LOADMORE);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lgr.i(this.getClass().getName() + " onListItemClick postion = "
                        + position);
                Intent intent = new Intent(getActivity(), JobDetailActivity.class);
                if (m_jobCategory == JobCategory.RECOMMEND) {
                    intent = new Intent(getActivity(), RecommendDetailActivity.class);
                }
                intent.putExtra("id", m_listAchive.get(position - 1).getId());
                getActivity().startActivity(intent);
            }
        });

        m_viewContainer = view.findViewById(R.id.job_recommend_imgbtn_container);
        if (m_jobCategory == JobCategory.RECOMMEND) {
            m_viewContainer.setVisibility(view.VISIBLE);
            Button btnPublish = (Button) view.findViewById(R.id.job_recommend_imgbtn);
            btnPublish.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getActivity(),
                            PublishRecommendActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            m_viewContainer.setVisibility(view.GONE);
        }
    }

    /**
     * 初始化归档列表，若有缓存则读取缓存
     */
    protected void initJobList() {
        List<Job> dbJobList = getJobList();
        if (!Judge.isNullOrEmpty(dbJobList)) {
            for (Job job : dbJobList) {
                m_listAchive.add(job);
            }
        }
    }

    protected void initHandler() {
        m_handlerJobList = new Handler() {
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
                        m_adapterJobList.notifyDataSetChanged();
                        break;
                    case STATUS_NOT_LOGIN:// TODO
                        ((NewMainActivity) getActivity()).updateLogin();
                        requestData(m_loadType);
                        break;
                    default:
                        ((NewMainActivity) getActivity()).dispose(msg);
                        break;

                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setLoading(false);
                    }
                }, 1000);
            }

        };
    }

    protected void setUpAdapter() {
        m_adapterJobList = new JobListAdapter(getActivity());
        listView.setAdapter(m_adapterJobList);
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
                return;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("body");

            for (int i = 0; i < jsonArray.length(); i++) {
                m_listAchive.add(new Job((JSONObject) jsonArray.get(i)));
            }
            syncJobList();
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setLoading(false);
                    }
                }, 1000);
                return;
            }
            jsonArray = jsonObject.getJSONArray("body");
            for (int i = 0; i < jsonArray.length(); i++) {
                m_listAchive.add(new Job((JSONObject) jsonArray.get(i)));
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
                    JobApi.getJobList(m_jobCategory, 1, 20, null,
                            new JobListRequestListener());
                    m_nCurrentPage = 1;
                    break;
                case LOADMORE:
                    JobApi.getJobList(m_jobCategory, ++m_nCurrentPage,
                            20, null, new JobListRequestListener());
                    break;
                default:
                    break;
            }
        } else {
            Message msg = m_handlerJobList.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            m_handlerJobList.sendMessage(msg);
        }
    }

    protected List<Job> getJobList() {
        return DataManager.getJobList(m_jobCategory);
    }

    protected void syncJobList() {
        DataManager.syncJobList(m_jobCategory, m_listAchive);
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
    public class JobListRequestListener implements RequestListener {

        @Override
        public void onComplete(Object result) {
            Message msg = m_handlerJobList.obtainMessage();
            try {
                if (!(((JSONObject) result).getJSONArray("body").length() == 0 ? false : true)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                            mSwipeRefreshLayout.setLoading(false);
                        }
                    }, 1000);
                    return;
                }
                msg.what = ((JSONObject) result).getInt("status");
                msg.obj = (JSONObject) result;
            } catch (JSONException e) {
                Lgr.i(this.getClass().getName() + " onComplete!");
                e.printStackTrace();
            }

            m_handlerJobList.sendMessage(msg);
        }

        @Override
        public void onHttpError(CSTResponse response) {
            Lgr.i(this.getClass().getName() + " onHttpError!");
            Message msg = m_handlerJobList.obtainMessage();
            HttpErrorWeeder.fckHttpError(response, msg);
            m_handlerJobList.sendMessage(msg);
        }

        @Override
        public void onException(Exception e) {
            Lgr.i(this.getClass().getName() + " onException!");
            Message msg = m_handlerJobList.obtainMessage();
            ExceptionWeeder.fckException(e, msg);
            m_handlerJobList.sendMessage(msg);
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
    public class JobListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public JobListAdapter(Context context) {
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
                        .inflate(R.layout.job_list_item, null);
                holder.titleTxv = (TextView) convertView
                        .findViewById(R.id.job_list_item_title_txv);
                holder.dateTxv = (TextView) convertView
                        .findViewById(R.id.job_list_item_date_txv);
                holder.publisherTxv = (TextView) convertView
                        .findViewById(R.id.job_list_item_publisher_txv);
                holder.descriptionTxv = (TextView) convertView
                        .findViewById(R.id.job_list_item_description_txv);

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

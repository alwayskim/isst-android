package cn.edu.zju.isst.v2.event.city.gui;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst.R;
import cn.edu.zju.isst.net.NetworkConnection;
import cn.edu.zju.isst.util.CroMan;
import cn.edu.zju.isst.util.Lgr;
import cn.edu.zju.isst.v2.data.CSTJsonParser;
import cn.edu.zju.isst.v2.event.base.EventRequest;
import cn.edu.zju.isst.v2.event.city.net.CityEventParticipantsListResponse;
import cn.edu.zju.isst.v2.gui.CSTBaseFragment;
import cn.edu.zju.isst.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst.v2.net.CSTRequest;
import cn.edu.zju.isst.v2.net.CSTStatusInfo;
import cn.edu.zju.isst.v2.user.data.CSTUser;

import static cn.edu.zju.isst.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * Created by always on 02/09/2014.
 */
public class CityEventParticipantsListFragment extends CSTBaseFragment implements
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private final List<CSTUser> mParticipantsList = new ArrayList<>();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;

    private Handler mHandler;

    private CityEventParticipantsAdapter mListAdapter;

    private LayoutInflater mInflater;

    private View mFooter;

    private ProgressBar mLoadMorePrgb;

    private TextView mLoadMoreHint;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private String SUB_URL = "/api/cities/";

    private String EVENT_ID = "id";

    private String CITY_ID = "cityId";

    private int DEFAULT_PAGE_SIZE = 20;

    private int mCurrentPage = 1;

    private int LIST_EMPTY = 0;

    private int mId;

    private int cityId;

    private boolean isLoadMore = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getArguments().getInt(EVENT_ID);
        cityId = getArguments().getInt(CITY_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mInflater = inflater;
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initComponent(view);
        setUpListener();
        setUpAdapter();
        initHandler();
        requestData();
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
        mLoadMorePrgb.setVisibility(View.GONE);
        mLoadMoreHint = (TextView) mFooter.findViewById(R.id.footer_loading_hint);
    }

    void setUpListener() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mFooter.setOnClickListener(this);
    }

    private void setUpAdapter() {
        mListAdapter = new CityEventParticipantsAdapter(getActivity());
        mListView.setAdapter(mListAdapter);

    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        requestData();
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

    void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        if (mParticipantsList.size() == LIST_EMPTY) {
                            CroMan.showConfirm(getActivity(), R.string.participate_empty);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;
                    case NETWORK_NOT_CONNECTED:
                        CroMan.showAlert(getActivity(), R.string.network_not_connected);
                        break;
                    default:
                        break;
                }
                mListAdapter.notifyDataSetChanged();
                resetLoadingState();
            }
        };
    }

    public void requestData() {
        if (NetworkConnection.isNetworkConnected(getActivity())) {
            if (isLoadMore) {
                mCurrentPage++;
            } else {
                mCurrentPage = 1;
            }
            CityEventParticipantsListResponse participantsListResponse = new
                    CityEventParticipantsListResponse(getActivity()) {
                        @Override
                        public void onResponse(JSONObject response) {

                            CSTUser userParticipants = (CSTUser) CSTJsonParser
                                    .parseJson(response, new CSTUser());
                            updateParticipantsList(userParticipants);
                            Lgr.i(response.toString());
                            Message msg = mHandler.obtainMessage();
                            msg.what = userParticipants.getStatusInfo().status;

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
            String subUrl = SUB_URL + cityId + "/activities/" + mId + "/participants";
            Lgr.i(subUrl);
            EventRequest eventRequest = new EventRequest(CSTRequest.Method.GET, subUrl, null,
                    participantsListResponse).setPage(mCurrentPage).setPageSize(DEFAULT_PAGE_SIZE);
            mEngine.requestJson(eventRequest);
        } else {
            Message msg = mHandler.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            mHandler.sendMessage(msg);
        }
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

    void updateParticipantsList(CSTUser userParticipants) {
        if (!isLoadMore) {
            mParticipantsList.clear();
        }

        for (int i = 0; i < userParticipants.itemList.size(); i++) {
            mParticipantsList.add((CSTUser) userParticipants.itemList.get(i));
        }

    }

    private final class ViewHolder {

        public TextView participantNameTxv;

        public TextView genderTxv;

        public TextView majorTxv;

    }

    private class CityEventParticipantsAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public CityEventParticipantsAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mParticipantsList.size();
        }

        @Override
        public Object getItem(int position) {
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
                        R.layout.city_event_participants_list_item, null);
                holder.participantNameTxv = (TextView) convertView
                        .findViewById(R.id.city_event_list_item_participant_name_txv);
                holder.genderTxv = (TextView) convertView
                        .findViewById(R.id.city_event_list_item_participant_gender_txv);
                holder.majorTxv = (TextView) convertView
                        .findViewById(R.id.city_event_list_item_participant_major_txv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.participantNameTxv.setText(mParticipantsList.get(position).name);
            holder.majorTxv
                    .setText((mParticipantsList.get(position)).majorName);
            holder.genderTxv
                    .setText((mParticipantsList.get(position)).gender.toString());

            return convertView;
        }
    }
}

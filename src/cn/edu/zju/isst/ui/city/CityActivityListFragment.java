/**
 *
 */
package cn.edu.zju.isst.ui.city;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst.R;
import cn.edu.zju.isst.api.CityActivityApi;
import cn.edu.zju.isst.db.CityActivity;
import cn.edu.zju.isst.db.DataManager;
import cn.edu.zju.isst.db.User;
import cn.edu.zju.isst.exception.ExceptionWeeder;
import cn.edu.zju.isst.exception.HttpErrorWeeder;
import cn.edu.zju.isst.net.CSTResponse;
import cn.edu.zju.isst.net.NetworkConnection;
import cn.edu.zju.isst.net.RequestListener;
import cn.edu.zju.isst.util.Judge;
import cn.edu.zju.isst.util.Lgr;
import cn.edu.zju.isst.util.TSUtil;
import cn.edu.zju.isst.v2.event.city.gui.CityEventDetailActivity;

import static cn.edu.zju.isst.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * @author theasir
 */
public class CityActivityListFragment extends ListFragment implements
        OnScrollListener {

    private static CityActivityListFragment INSTANCE = new CityActivityListFragment();

    private final List<CityActivity> mCityActivities = new ArrayList<CityActivity>();

    private User m_currentUser;

    private Handler mHandler;

    private CityActivityListAdapter mListAdapter;

    private ListView mCityActivityListView;

    public CityActivityListFragment() {
    }

    public static CityActivityListFragment getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);

        m_currentUser = DataManager.getCurrentUser();

        initHandler();

        setUpAdapter();

        setUpListener();

        requestData();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), CityEventDetailActivity.class);
        intent.putExtra("id", mCityActivities.get(position).id);
        intent.putExtra("cityId", mCityActivities.get(position).cityId);
        getActivity().startActivity(intent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub

    }

    private void initComponent(View view) {
        mCityActivityListView = (ListView) view.findViewById(android.R.id.list);
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        refresh((JSONObject) msg.obj);
                        break;

                    default:
                        break;
                }
                mListAdapter.notifyDataSetChanged();
            }

        };
    }

    private void setUpAdapter() {
        mListAdapter = new CityActivityListAdapter(getActivity());
        setListAdapter(mListAdapter);

    }

    private void setUpListener() {
        mCityActivityListView.setOnScrollListener(this);

    }

    private void refresh(JSONObject jsonObject) {
        if (!mCityActivities.isEmpty()) {
            mCityActivities.clear();
        }
        try {
            if (!Judge.isValidJsonValue("body", jsonObject)) {
                return;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("body");

            for (int i = 0; i < jsonArray.length(); i++) {
                mCityActivities.add(new CityActivity((JSONObject) jsonArray
                        .get(i)));
            }
            // DataManager.syncCampusActivityList(m_listCampusActivity);
        } catch (JSONException e) {
            Lgr.i(this.getClass().getName() + " refreshList!");
            e.printStackTrace();
        }
    }

    private void requestData() {
        if (NetworkConnection.isNetworkConnected(getActivity())) {
            CityActivityApi.getCityActivityList(m_currentUser.getCityId(), 1, 20, null,
                    new CityActivityListRequestListener());
        } else {
            Message msg = mHandler.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            mHandler.sendMessage(msg);
        }
    }

    private class CityActivityListRequestListener implements RequestListener {

        @Override
        public void onComplete(Object result) {
            Message msg = mHandler.obtainMessage();
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

            mHandler.sendMessage(msg);
        }

        @Override
        public void onHttpError(CSTResponse response) {
            Lgr.i(this.getClass().getName() + " onHttpError!");
            Message msg = mHandler.obtainMessage();
            HttpErrorWeeder.fckHttpError(response, msg);
            mHandler.sendMessage(msg);
        }

        @Override
        public void onException(Exception e) {
            Lgr.i(this.getClass().getName() + " onException!");
            Message msg = mHandler.obtainMessage();
            ExceptionWeeder.fckException(e, msg);
            mHandler.sendMessage(msg);
        }

    }

    private final class ViewHolder {

        public TextView titleTxv;

        public TextView updateTimeTxv;

        public TextView publisherTxv;

        public TextView startTimeTxv;

        public TextView expireTimeTxv;
    }

    private class CityActivityListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public CityActivityListAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mCityActivities.size();
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
                        R.layout.city_activity_list_item, null);
                holder.titleTxv = (TextView) convertView
                        .findViewById(R.id.city_activity_title);
                holder.updateTimeTxv = (TextView) convertView
                        .findViewById(R.id.city_activity_update_time);
                holder.startTimeTxv = (TextView) convertView
                        .findViewById(R.id.city_activity_start_time);
                holder.expireTimeTxv = (TextView) convertView
                        .findViewById(R.id.city_activity_expire_time);
                holder.publisherTxv = (TextView) convertView
                        .findViewById(R.id.city_activity_publisher);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.titleTxv.setText(mCityActivities.get(position).title);
            holder.updateTimeTxv
                    .setText("发布时间:"
                            + TSUtil.toYMD(mCityActivities.get(position).updatedAt));
            holder.startTimeTxv.setText("开始时间:"
                    + TSUtil.toMD(mCityActivities.get(position).startTime));
            holder.expireTimeTxv
                    .setText("结束时间:"
                            + TSUtil.toMD(mCityActivities.get(position).expireTime));
            holder.publisherTxv.setText("发布者:"
                    + mCityActivities.get(position).publisher.getName());

            return convertView;
        }

    }

}

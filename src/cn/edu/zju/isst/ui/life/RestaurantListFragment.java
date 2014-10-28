/**
 *
 */
package cn.edu.zju.isst.ui.life;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst.R;
import cn.edu.zju.isst.api.RestaurantApi;
import cn.edu.zju.isst.db.DataManager;
import cn.edu.zju.isst.db.Restaurant;
import cn.edu.zju.isst.net.CSTResponse;
import cn.edu.zju.isst.net.NetworkConnection;
import cn.edu.zju.isst.net.RequestListener;
import cn.edu.zju.isst.util.Judge;
import cn.edu.zju.isst.util.Lgr;

import static cn.edu.zju.isst.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * @author theasir
 */
public class RestaurantListFragment extends ListFragment {

    private static RestaurantListFragment INSTANCE = new RestaurantListFragment();

    private final List<Restaurant> m_listRestaurant = new ArrayList<Restaurant>();

    private Handler m_handlerRestaurantList;

    private RestaurantListAdapter m_adapterRestaurantList;

    private ListView m_lsvRestaurantList;

    public RestaurantListFragment() {
    }

    public static RestaurantListFragment getInstance() {
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
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
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
        return inflater.inflate(R.layout.restaurant_list_fragment, null);
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

        m_lsvRestaurantList = (ListView) view.findViewById(android.R.id.list);

        initRestaurantList();

        if (Judge.isNullOrEmpty(m_listRestaurant)) {
            requestData();
        }

        m_handlerRestaurantList = new Handler() {

            /*
             * (non-Javadoc)
             *
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        m_adapterRestaurantList.notifyDataSetChanged();
                        break;
                    case STATUS_NOT_LOGIN:
                        break;
                    default:
                        break;
                }
            }

        };

        m_adapterRestaurantList = new RestaurantListAdapter(getActivity());

        setListAdapter(m_adapterRestaurantList);
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
                RestaurantDetailActivity.class);
        intent.putExtra("id", m_listRestaurant.get(position).getId());
        getActivity().startActivity(intent);
    }

    private void initRestaurantList() {
        List<Restaurant> dbRestaurantList = DataManager.getRestaurantList();
        if (!m_listRestaurant.isEmpty()) {
            m_listRestaurant.clear();
        }
        if (!Judge.isNullOrEmpty(dbRestaurantList)) {
            for (Restaurant restaurant : dbRestaurantList) {
                m_listRestaurant.add(restaurant);
            }
        }
    }

    private void updateList(JSONObject jsonObject) {
        if (!m_listRestaurant.isEmpty()) {
            m_listRestaurant.clear();
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("body");
            for (int i = 0; i < jsonArray.length(); i++) {
                m_listRestaurant.add(new Restaurant((JSONObject) jsonArray
                        .get(i)));
            }
            Lgr.i(this.getClass().getName() + " updateList: "
                    + "Added restautants!");
            DataManager.syncRestaurantList(m_listRestaurant);
        } catch (JSONException e) {
            Lgr.i(this.getClass().getName() + " updateList!");
            e.printStackTrace();
        }
    }

    private void requestData() {
        if (NetworkConnection.isNetworkConnected(getActivity())) {
            RestaurantApi.getRestaurantList(1, 100, null,
                    new RestaurantListRequestListener());
        } else {
            Message msg = m_handlerRestaurantList.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            m_handlerRestaurantList.sendMessage(msg);
        }
    }

    private class RestaurantListRequestListener implements RequestListener {

        @Override
        public void onComplete(Object result) {
            Message msg = m_handlerRestaurantList.obtainMessage();
            try {
                msg.what = ((JSONObject) result).getInt("status");
                if (msg.what == STATUS_REQUEST_SUCCESS) {
                    updateList((JSONObject) result);
                }
            } catch (JSONException e) {
                Lgr.i(this.getClass().getName() + " onComplete!");
                e.printStackTrace();
            }

            m_handlerRestaurantList.sendMessage(msg);

        }

        @Override
        public void onHttpError(CSTResponse response) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onException(Exception e) {
            // TODO Auto-generated method stub

        }

    }

    private final class ViewHolder {

        public ImageView iconImgv;

        public TextView nameTxv;

        public TextView hotlineTxv;

        public ImageButton dialIbtn;
    }

    private class RestaurantListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public RestaurantListAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return m_listRestaurant.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.restaurant_list_item,
                        null);
                holder.iconImgv = (ImageView) convertView
                        .findViewById(R.id.restaurant_list_item_icon_imgv);
                holder.nameTxv = (TextView) convertView
                        .findViewById(R.id.restaurant_list_item_name_txv);
                holder.hotlineTxv = (TextView) convertView
                        .findViewById(R.id.restaurant_list_item_hotline_txv);
                holder.dialIbtn = (ImageButton) convertView
                        .findViewById(R.id.restaurant_list_item_dial_ibtn);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // holder.iconImgv.setBackgroundColor(Color.BLUE);
            holder.nameTxv.setText(m_listRestaurant.get(position).getName());
            holder.hotlineTxv.setText(m_listRestaurant.get(position)
                    .getHotline());

            final String dialNumber = m_listRestaurant.get(position)
                    .getHotline();

            holder.dialIbtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!Judge.isNullOrEmpty(dialNumber)) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                                .parse("tel://" + dialNumber));
                        RestaurantListFragment.this.getActivity()
                                .startActivity(intent);
                    }
                }
            });

            return convertView;
        }

    }

}

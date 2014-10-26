/**
 *
 */
package cn.edu.zju.isst.ui.life;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst.R;
import cn.edu.zju.isst.api.RestaurantApi;
import cn.edu.zju.isst.db.Restaurant;
import cn.edu.zju.isst.db.RestaurantMenu;
import cn.edu.zju.isst.net.CSTResponse;
import cn.edu.zju.isst.net.RequestListener;
import cn.edu.zju.isst.ui.main.BaseActivity;
import cn.edu.zju.isst.util.Judge;
import cn.edu.zju.isst.util.Lgr;

import static cn.edu.zju.isst.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * @author theasir
 */
public class RestaurantDetailActivity extends BaseActivity {

    private final List<RestaurantMenu> m_listRestaurantMenu = new ArrayList<RestaurantMenu>();

    private int m_nId;

    private Restaurant m_restaurantCurrent;

    private Handler m_handlerRestaurantDetail;

    private MenuListAdapter m_adapterMenu;

    private TextView m_txvContent;

    private TextView m_txvHotline;

    private ImageButton m_ibtnDial;

    private ListView m_lsvMenu;

    /*
     * (non-Javadoc)
     *
     * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_detail_activity);
        initComponent();

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        m_nId = getIntent().getIntExtra("id", -1);

        m_handlerRestaurantDetail = new Handler() {

            /*
             * (non-Javadoc)
             *
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        // TODO 优化策略
                        if (!Judge.isNullOrEmpty(m_restaurantCurrent)) {
                            Lgr.i("Handler Success Restaurant id = "
                                    + m_restaurantCurrent.getId());
                            showRestaurantDetail();
                        }
                        if (!Judge.isNullOrEmpty(m_listRestaurantMenu)) {
                            m_adapterMenu.notifyDataSetChanged();
                        }
                        break;
                    case STATUS_NOT_LOGIN:
                        break;
                    default:
                        break;
                }
            }

        };

        m_adapterMenu = new MenuListAdapter();
        m_lsvMenu.setAdapter(m_adapterMenu);

        requestDataOfRestaurant();
        requestDataOfMenu();

    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                RestaurantDetailActivity.this.finish();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initComponent() {
        m_txvContent = (TextView) findViewById(R.id.restaurant_detail_activity_description_txv);
        m_txvHotline = (TextView) findViewById(R.id.restaurant_detail_activity_hotline_txv);
        m_ibtnDial = (ImageButton) findViewById(R.id.restaurant_detail_activity_dial_ibtn);
        m_lsvMenu = (ListView) findViewById(R.id.restaurant_detail_activity_menu_lsv);
    }

    private void updateMenu(JSONObject jsonObject) {
        if (!m_listRestaurantMenu.isEmpty()) {
            m_listRestaurantMenu.clear();
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("body");
            for (int i = 0; i < jsonArray.length(); i++) {
                m_listRestaurantMenu.add(new RestaurantMenu(
                        (JSONObject) jsonArray.get(i)));
            }
            Lgr.i(this.getClass().getName() + " updateMenu: "
                    + "Added menu to menuList!");
        } catch (JSONException e) {
            Lgr.i(this.getClass().getName() + " updateMenu!");
            e.printStackTrace();
        }
    }

    private void requestDataOfRestaurant() {
        RestaurantApi.getRestaurantDetail(m_nId, new RequestListener() {

            @Override
            public void onComplete(Object result) {
                Message msg = m_handlerRestaurantDetail.obtainMessage();

                try {
                    JSONObject jsonObject = (JSONObject) result;
                    final int status = jsonObject.getInt("status");
                    switch (status) {
                        case STATUS_REQUEST_SUCCESS:
                            m_restaurantCurrent = new Restaurant(jsonObject
                                    .getJSONObject("body"));
                            break;
                        case STATUS_NOT_LOGIN:
                            break;
                        default:
                            break;
                    }
                    msg.what = status;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                m_handlerRestaurantDetail.sendMessage(msg);

            }

            @Override
            public void onHttpError(CSTResponse response) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onException(Exception e) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void requestDataOfMenu() {
        RestaurantApi.getRestaurantMenu(m_nId, new RequestListener() {

            @Override
            public void onComplete(Object result) {
                Message msg = m_handlerRestaurantDetail.obtainMessage();

                try {
                    JSONObject jsonObject = (JSONObject) result;
                    final int status = jsonObject.getInt("status");
                    switch (status) {
                        case STATUS_REQUEST_SUCCESS:
                            updateMenu(jsonObject);
                            break;
                        case STATUS_NOT_LOGIN:
                            break;
                        default:
                            break;
                    }
                    msg.what = status;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                m_handlerRestaurantDetail.sendMessage(msg);

            }

            @Override
            public void onHttpError(CSTResponse response) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onException(Exception e) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void showRestaurantDetail() {
        setTitle(m_restaurantCurrent.getName());

        m_txvContent.setText(m_restaurantCurrent.getContent());
        m_txvHotline.setText(m_restaurantCurrent.getHotline());

        final String dialNumber = m_restaurantCurrent.getHotline();

        m_ibtnDial.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!Judge.isNullOrEmpty(dialNumber)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                            .parse("tel://" + dialNumber));
                    RestaurantDetailActivity.this.startActivity(intent);
                }

            }
        });
    }

    private final class ViewHolder {

        public TextView nameTxv;

        public TextView priceTxv;
    }

    private class MenuListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public MenuListAdapter() {
            this.inflater = LayoutInflater.from(RestaurantDetailActivity.this);
        }

        @Override
        public int getCount() {
            return m_listRestaurantMenu.size();
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

                convertView = inflater.inflate(R.layout.restaurant_menu_item,
                        null);
                holder.nameTxv = (TextView) convertView
                        .findViewById(R.id.restaurant_menu_item_name_txv);
                holder.priceTxv = (TextView) convertView
                        .findViewById(R.id.restaurant_menu_item_price_txv);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.nameTxv
                    .setText(m_listRestaurantMenu.get(position).getName());
            holder.priceTxv.setText(String.valueOf(m_listRestaurantMenu.get(
                    position).getPrice()));

            return convertView;
        }

    }

}

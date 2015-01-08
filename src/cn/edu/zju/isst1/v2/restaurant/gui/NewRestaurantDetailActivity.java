package cn.edu.zju.isst1.v2.restaurant.gui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import cn.edu.zju.isst.tests.util.CSTImageUtil;
import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.data.CSTRestaurant;
import cn.edu.zju.isst1.v2.data.CSTRestaurantMenu;
import cn.edu.zju.isst1.v2.login.net.UpDateLogin;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;
import cn.edu.zju.isst1.v2.restaurant.data.CSTRestaurantDataDelegate;
import cn.edu.zju.isst1.v2.restaurant.net.RestaurantMenuResponse;

import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

public class NewRestaurantDetailActivity extends Activity {

    private int m_nId;

    private static String RESTAURANT_MENU_URL = "/api/restaurants";

    private CSTRestaurant m_restaurantCurrent;

    private TextView m_txvDescription;

    private TextView m_txvHotline;

    private TextView m_txvAddress;

    private TextView m_txvbusinessHours;

    private TextView m_txvRestaurantName;

//    private ImageButton m_ibtnDial;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private Handler mHandler;

    private ListView m_lsvMenu;

    public NewRestaurantDetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_detail_activity);
        initComponent();
        m_nId = getIntent().getIntExtra("id", -1);
        m_restaurantCurrent = CSTRestaurantDataDelegate
                .getRestaurant(getApplicationContext(), Integer.toString(m_nId));
        setUpActionbar();
        showRestaurantDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                NewRestaurantDetailActivity.this.finish();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpActionbar() {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        m_txvRestaurantName = (TextView) findViewById(R.id.restaurant_detail_activity_rn);
        m_txvDescription = (TextView) findViewById(R.id.restaurant_detail_activity_description_txv);
        m_txvHotline = (TextView) findViewById(R.id.restaurant_detail_activity_hotline_txv);
        m_txvAddress = (TextView) findViewById(R.id.restaurant_detail_activity_address_txv);
        m_txvbusinessHours = (TextView) findViewById(R.id.restaurant_detail_activity_business_hours_txv);
//        m_ibtnDial = (ImageButton) findViewById(R.id.restaurant_detail_activity_dial_ibtn);
        m_lsvMenu = (ListView) findViewById(R.id.restaurant_detail_activity_menu_lsv);
    }

    private void showRestaurantDetail() {
        setTitle(m_restaurantCurrent.name);

        m_txvRestaurantName.setText(m_restaurantCurrent.name);
        m_txvDescription.setText(m_restaurantCurrent.description);
        m_txvHotline.setText(m_restaurantCurrent.hotLine);
        m_txvAddress.setText(m_restaurantCurrent.address);
        m_txvbusinessHours.setText(m_restaurantCurrent.businessHours);

//        final String dialNumber = m_restaurantCurrent.hotLine;
//        m_ibtnDial.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (!Judge.isNullOrEmpty(dialNumber)) {
//                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri
//                            .parse("tel://" + dialNumber));
//                    NewRestaurantDetailActivity.this.startActivity(intent);
//                }
//
//            }
//        });

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        CSTRestaurantMenu restaurantMenu = (CSTRestaurantMenu) msg.obj;
//                        List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
//                        for (CSTRestaurantMenu rm : restaurantMenu.itemList) {
//                            Map<String, String> map = new HashMap<String, String>();
//                            map.put("name", rm.name);
//                            map.put("price", Float.toString(rm.price));
//                            listItems.add(map);
//                        }
//                        Lgr.i(listItems.toString());
//                        SimpleAdapter adapter = new SimpleAdapter(NewRestaurantDetailActivity.this, listItems,
//                                android.R.layout.simple_list_item_2, new String[]{"name", "price"},
//                                new int[]{android.R.id.text1, android.R.id.text2});
                        MenuAdapter adapter = new MenuAdapter(restaurantMenu, NewRestaurantDetailActivity.this);
                        m_lsvMenu.setAdapter(adapter);
                        break;
                    case STATUS_NOT_LOGIN:
                        UpDateLogin.getInstance().updateLogin(NewRestaurantDetailActivity.this);
                        requestData();
                        break;
                    default:
                        CSTHttpUtil.dispose(msg.what, NewRestaurantDetailActivity.this);
                        break;
                }
            }
        };
        requestData();

    }

    private void requestData() {
        RestaurantMenuResponse rmResponse = new RestaurantMenuResponse(this) {
            @Override
            public void onResponse(JSONObject response) {
                CSTRestaurantMenu menu = (CSTRestaurantMenu) CSTJsonParser
                        .parseJson(response, new CSTRestaurantMenu());
                Lgr.i(response.toString());
                Message msg = mHandler.obtainMessage();
                msg.obj = menu;
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
        String str = RESTAURANT_MENU_URL + "/" + Integer.toString(m_nId) + "/" + "menus";
        CSTJsonRequest rmRequest = new CSTJsonRequest(CSTRequest.Method.GET, str, null,
                rmResponse);
        mEngine.requestJson(rmRequest);
    }

    public class MenuAdapter extends BaseAdapter {

        private CSTRestaurantMenu menu;
        private LayoutInflater mInflater;

        public MenuAdapter(CSTRestaurantMenu menu, Context context) {
            this.menu = menu;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return menu.itemList.size();
        }

        @Override
        public CSTRestaurantMenu getItem(int position) {
            return menu.itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.restaurant_menu_item, null);
                holder = new ViewHolder();
                holder.dish_icon = (ImageView) convertView.findViewById(R.id.restaurant_menu_item_dish_icon);
                holder.dish_name = (TextView) convertView.findViewById(R.id.restaurant_menu_item_name_txv);
                holder.dish_price = (TextView) convertView.findViewById(R.id.restaurant_menu_item_price_txv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            final Handler mHandler = new Handler() {
//                public void handleMessage(Message msg) {
//                    holder.dish_icon.setImageBitmap((Bitmap) msg.obj);
//                }
//            };
//            Runnable show_icon = new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Bitmap bitmap = CSTImageUtil.getImage(getItem(position).picture);
//                        Message msg = mHandler.obtainMessage();
//                        msg.obj = bitmap;
//                        mHandler.sendMessage(msg);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            new Thread(show_icon).start();
            holder.dish_name.setText(getItem(position).name);
            holder.dish_price.setText(Float.toString(getItem(position).price) + "元");
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView dish_icon;
        public TextView dish_name;
        public TextView dish_price;
    }
}

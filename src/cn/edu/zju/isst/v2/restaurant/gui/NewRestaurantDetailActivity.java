package cn.edu.zju.isst.v2.restaurant.gui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.zju.isst.R;
import cn.edu.zju.isst.util.Judge;
import cn.edu.zju.isst.util.Lgr;
import cn.edu.zju.isst.v2.data.CSTJsonParser;
import cn.edu.zju.isst.v2.data.CSTRestaurant;
import cn.edu.zju.isst.v2.data.CSTRestaurantMenu;
import cn.edu.zju.isst.v2.net.CSTJsonRequest;
import cn.edu.zju.isst.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst.v2.net.CSTRequest;
import cn.edu.zju.isst.v2.restaurant.data.CSTRestaurantDataDelegate;
import cn.edu.zju.isst.v2.restaurant.net.RestaurantMenuResponse;

public class NewRestaurantDetailActivity extends Activity {

    private int m_nId;

    private static String RESTAURANT_MENU_URL = "/api/restaurants";

    private CSTRestaurant m_restaurantCurrent;

    private TextView m_txvDescription;

    private TextView m_txvHotline;

    private TextView m_txvAddress;

    private TextView m_txvbusinessHours;

    private ImageButton m_ibtnDial;

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
        m_txvDescription = (TextView) findViewById(R.id.restaurant_detail_activity_description_txv);
        m_txvHotline = (TextView) findViewById(R.id.restaurant_detail_activity_hotline_txv);
        m_txvAddress = (TextView) findViewById(R.id.restaurant_detail_activity_address_txv);
        m_txvbusinessHours = (TextView) findViewById(R.id.restaurant_detail_activity_business_hours_txv);
        m_ibtnDial = (ImageButton) findViewById(R.id.restaurant_detail_activity_dial_ibtn);
        m_lsvMenu = (ListView) findViewById(R.id.restaurant_detail_activity_menu_lsv);
    }

    private void showRestaurantDetail() {
        setTitle(m_restaurantCurrent.name);

        m_txvDescription.setText(m_restaurantCurrent.description);
        m_txvHotline.setText(m_restaurantCurrent.hotLine);
        m_txvAddress.setText(m_restaurantCurrent.address);
        m_txvbusinessHours.setText(m_restaurantCurrent.businessHours);

        final String dialNumber = m_restaurantCurrent.hotLine;

        m_ibtnDial.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!Judge.isNullOrEmpty(dialNumber)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                            .parse("tel://" + dialNumber));
                    NewRestaurantDetailActivity.this.startActivity(intent);
                }

            }
        });

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                CSTRestaurantMenu restaurantMenu = (CSTRestaurantMenu) msg.obj;
                List<Map<String, String>> listItems=new ArrayList<Map<String, String>>();
                for (CSTRestaurantMenu rm : restaurantMenu.itemList) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", rm.name);
                    map.put("price", Float.toString(rm.price));
                        listItems.add(map);
                }
                Lgr.i(listItems.toString());
                SimpleAdapter adapter = new SimpleAdapter(NewRestaurantDetailActivity.this, listItems,
                        android.R.layout.simple_list_item_2, new String[]{"name", "price"},
                        new int[]{android.R.id.text1, android.R.id.text2});
                m_lsvMenu.setAdapter(adapter);
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
        };
        String str = RESTAURANT_MENU_URL + "/" + Integer.toString(m_nId) + "/" + "menus";
        CSTJsonRequest rmRequest = new CSTJsonRequest(CSTRequest.Method.GET, str, null,
                rmResponse);

        mEngine.requestJson(rmRequest);
    }
}

package cn.edu.zju.isst.v2.restaurant.gui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    private Handler mHandler_icon;

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
                myAdapter adapter = new myAdapter(restaurantMenu);
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

    private byte[] getImage(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(5000);
        InputStream inputStream = conn.getInputStream();
        byte[] data = readInputStream(inputStream);
        return data;
    }

    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    private Bitmap getBitmap(String path) throws IOException {
        byte[] data = getImage(path);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }

    class myAdapter extends BaseAdapter {

        private CSTRestaurantMenu rm;

        public myAdapter(CSTRestaurantMenu rm) {
            this.rm = rm;
        }

        @Override
        public int getCount() {
            return rm.itemList.size();
        }

        @Override
        public CSTRestaurantMenu getItem(int position) {
            return rm.itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.restaurant_menu_item, null);
                vh = new ViewHolder();
                vh.resIcon = (ImageView) convertView.findViewById(R.id.restaurant_menu_item_icon);
                vh.nameTxv = (TextView) convertView.findViewById(R.id.restaurant_menu_item_name_txv);
                vh.priceTxv = (TextView) convertView.findViewById(R.id.restaurant_menu_item_price_txv);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.nameTxv.setText(getItem(position).name);
            vh.priceTxv.setText(Float.toString(getItem(position).price)+"元");
            RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
            ImageLoader imageLoader = new ImageLoader(mQueue, new ImageLoader.ImageCache() {
                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                }

                @Override
                public Bitmap getBitmap(String url) {
                    return null;
                }
            });
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(vh.resIcon,
                    R.drawable.ic_launcher, R.drawable.ic_launcher);
            imageLoader.get(getItem(position).picture, listener, 80, 80);
            return convertView;
        }

        protected final class ViewHolder {

            public ImageView resIcon;

            public TextView nameTxv;

            public TextView priceTxv;

        }
    }
}

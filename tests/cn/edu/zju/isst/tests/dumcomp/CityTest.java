package cn.edu.zju.isst.tests.dumcomp;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.v2.globaldata.citylist.CSTCity;
import cn.edu.zju.isst1.v2.globaldata.citylist.CSTCityDataDelegate;
import cn.edu.zju.isst1.v2.globaldata.citylist.CSTCityProvider;
import cn.edu.zju.isst1.v2.user.data.CSTUser;

public class CityTest extends Activity {

    private Button add_city;

    private Button delete_city;

    private CSTCityProvider provider;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_test);
        provider = new CSTCityProvider(getApplicationContext());
        listView = (ListView) findViewById(R.id.listView);
        add_city = (Button) findViewById(R.id.add);
        add_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CSTUser user = new CSTUser();
                user.id = 1;
                user.userName = "admin";
                user.pwd = "admin";
                user.name = "administrator";
                user.gender = CSTUser.Gender.MALE;
                user.grade = 90;
                user.clazzId = 1;
                user.clazzName = "software";
                user.majorName = "software";
                user.cityId = 1;
                user.cityName = "宁波";
                user.email = "admin@qq.com";
                user.phoneNum = "1232414";
                user.qqNum = "12421515123";
                user.company = "ms";
                user.jobTitle = "PM";
                user.sign = "just do it";
                user.castellan = true;
                user.pvtCompany = true;
                user.pvtEmail = true;
                user.pvtJobTitle = true;
                user.pvtPhoneNum = true;
                user.pvtQq = true;
                CSTCity city = new CSTCity();
                city.id = 1;
                city.name = "宁波";
                city.cityMaster = user;
                CSTCityDataDelegate.saveCity(getApplicationContext(), city);
                show();
            }
        });
        delete_city = (Button) findViewById(R.id.delete);
        delete_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CSTCityDataDelegate.deleteAllCity(getApplicationContext());
                show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.city_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void show() {
        ArrayList<CSTCity> cities = new ArrayList<CSTCity>();
        Cursor cursor = getContentResolver()
                .query(CSTCityProvider.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            CSTCity citydemo = CSTCityDataDelegate.getCity(cursor);
            cities.add(citydemo);
        }
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (CSTCity city : cities) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", city.cityMaster.gender.getTypeName());
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_1,
                new String[]{"name"}, new int[]{android.R.id.text1});
        listView.setAdapter(adapter);
    }
}

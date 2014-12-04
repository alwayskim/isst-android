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
import cn.edu.zju.isst1.v2.user.data.CSTUser;
import cn.edu.zju.isst1.v2.user.data.CSTUserDataDelegate;
import cn.edu.zju.isst1.v2.user.data.CSTUserProvider;

public class UserTest extends Activity{

    private Button add_user;

    private Button delete_user;

    private ListView listView;

    private CSTUserProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_test);
        provider = new CSTUserProvider(getApplicationContext());
        listView = (ListView) findViewById(R.id.listView);
        add_user = (Button) findViewById(R.id.add);
        add_user.setOnClickListener(new View.OnClickListener() {
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
                CSTUser userr = new CSTUser();
                userr.id = 2;
                userr.userName = "admin";
                userr.pwd = "admin";
                userr.name = "administrator1";
                userr.gender = CSTUser.Gender.MALE;
                userr.grade = 90;
                userr.clazzId = 1;
                userr.clazzName = "software";
                userr.majorName = "software";
                userr.cityId = 1;
                userr.cityName = "宁波";
                userr.email = "admin@qq.com";
                userr.phoneNum = "1232414";
                userr.qqNum = "12421515123";
                userr.company = "ms";
                userr.jobTitle = "PM";
                userr.sign = "just do it";
                userr.castellan = true;
                userr.pvtCompany = true;
                userr.pvtEmail = true;
                userr.pvtJobTitle = true;
                userr.pvtPhoneNum = true;
                userr.pvtQq = true;
                CSTUserDataDelegate.saveUser(getApplicationContext(), user);
                CSTUserDataDelegate.saveUser(getApplicationContext(), userr);
                show();
            }
        });
        delete_user = (Button) findViewById(R.id.delete);
        delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CSTUserDataDelegate.deleteAllUsers(getApplicationContext());
                show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return true;
    }

    public void show() {
        ArrayList<CSTUser> users = new ArrayList<CSTUser>();
        Cursor cursor = getContentResolver()
                .query(CSTUserProvider.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            CSTUser userdemo = CSTUserDataDelegate.getUser(cursor);
            users.add(userdemo);
        }
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (CSTUser user : users) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", user.gender.getTypeName());
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_1,
                new String[]{"name"}, new int[]{android.R.id.text1});
        listView.setAdapter(adapter);
    }
}

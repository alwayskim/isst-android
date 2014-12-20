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
import cn.edu.zju.isst1.v2.event.campus.data.CSTCampusEvent;
import cn.edu.zju.isst1.v2.event.campus.data.CSTCampusEventDataDelegate;
import cn.edu.zju.isst1.v2.event.campus.data.CSTCampusEventProvider;


public class CampusEventTest extends Activity {

    private Button add_campusevent;

    private Button delete_campusevent;

    private CSTCampusEventProvider provider;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_test);
        provider = new CSTCampusEventProvider(getApplicationContext());
        listView = (ListView) findViewById(R.id.listView);
        add_campusevent = (Button) findViewById(R.id.add);
        add_campusevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CSTCampusEvent ce = new CSTCampusEvent();
                ce.id = 1;
                ce.title = "足球赛";
                ce.picture = "picture";
                ce.description = "非常精彩";
                ce.content = "太好玩了";
                ce.pubName = "cb";
//                ce.updatedAt = 10;
//                ce.startTime = 9;
//                ce.expireTime = 11;
                CSTCampusEvent ce1 = new CSTCampusEvent();
                ce1.id = 2;
                ce1.title = "篮球赛";
                ce1.picture = "picture";
                ce1.description = "非常精彩";
                ce1.content = "太好玩了";
                ce1.pubName = "cb";
//                ce1.updatedAt = 10;
//                ce1.startTime = 9;
//                ce1.expireTime = 11;
                CSTCampusEventDataDelegate.saveCampusEvent(getApplicationContext(), ce);
                CSTCampusEventDataDelegate.saveCampusEvent(getApplicationContext(), ce1);
                show();
            }
        });
        delete_campusevent = (Button) findViewById(R.id.delete);
        delete_campusevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CSTCampusEventDataDelegate.deleteAllCampusEvent(getApplicationContext());
                show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.campus_activity_test, menu);
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
        ArrayList<CSTCampusEvent> campusactivities = new ArrayList<CSTCampusEvent>();
        Cursor cursor = getContentResolver()
                .query(CSTCampusEventProvider.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            CSTCampusEvent campusactivitydemo = CSTCampusEventDataDelegate.getCampusEvent(cursor);
            campusactivities.add(campusactivitydemo);
        }
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (CSTCampusEvent ca : campusactivities) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("title", ca.description);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_1,
                new String[]{"title"}, new int[]{android.R.id.text1});
        listView.setAdapter(adapter);
    }
}

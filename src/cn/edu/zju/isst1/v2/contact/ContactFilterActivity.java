package cn.edu.zju.isst1.v2.contact;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.ui.main.BaseActivity;

/**
 * Created by always on 9/11/2014.
 */
public class ContactFilterActivity extends BaseActivity {

    ContactFilterFragment mContactFilterFragment = new ContactFilterFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_activity);
        setUpActionbar();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.content_container, mContactFilterFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ContactFilterActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_filter_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void setUpActionbar() {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.action_filter);
    }


}

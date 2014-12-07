package cn.edu.zju.isst1.v2.event.city.gui;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.Lgr;

/**
 * Created by always on 02/09/2014.
 */
public class CityEventParticipantsListActivity extends BaseActivity {

    private String EVENT_ID = "id";

    private String CITY_ID = "cityId";

    private String EVENT_TITLE = "eventTitle";

    private int mId;

    private int cityId;

    private String eventTitle;

    CityEventParticipantsListFragment listFragment = new CityEventParticipantsListFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container_activity);
        mId = getIntent().getIntExtra(EVENT_ID, -1);
        cityId = getIntent().getIntExtra(CITY_ID, -1);
        eventTitle = getIntent().getStringExtra(EVENT_TITLE);
        Lgr.i(eventTitle);
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(EVENT_ID, mId);
            bundle.putInt(CITY_ID, cityId);
            listFragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .add(R.id.content_container, listFragment).commit();
        }
        setUpActionbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CityEventParticipantsListActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpActionbar() {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(eventTitle + getResources().getString(R.string.note_participants_list));
    }
}

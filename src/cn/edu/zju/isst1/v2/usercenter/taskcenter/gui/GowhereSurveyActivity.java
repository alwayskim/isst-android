package cn.edu.zju.isst1.v2.usercenter.taskcenter.gui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.v2.data.CSTTask;

public class GowhereSurveyActivity extends Activity {

    public TextView mTask_name;
    public TextView mTask_starttime;
    public TextView mTask_expiretime;
    public TextView mTask_type;
    public TextView mTask_description;
    public TextView mTask_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gowhere_survey);
        initview();
        setUpActionbar();
        Lgr.i(this.getIntent().getExtras().toString());
        CSTTask task = (CSTTask) getIntent().getExtras().getSerializable("task");
        mTask_name.setText(task.name);
        mTask_starttime.setText(TSUtil.toYMD(task.startTime));
        mTask_expiretime.setText(TSUtil.toYMD(task.expireTime));
        if (task.type == 0) mTask_type.setText("去向调查");
        else mTask_type.setText("其他任务");
        mTask_description.setText(task.description);
        if (task.finishedId == 0) mTask_finish.setText("已完成");
        else mTask_finish.setText("未完成");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gowhere_survey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                GowhereSurveyActivity.this.finish();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initview(){
        mTask_name = (TextView) findViewById(R.id.gw_name_c);
        mTask_starttime = (TextView) findViewById(R.id.gw_starttime_c);
        mTask_expiretime = (TextView) findViewById(R.id.gw_expiretime_c);
        mTask_type = (TextView) findViewById(R.id.gw_tasktype_c);
        mTask_description = (TextView) findViewById(R.id.gw_description_c);
        mTask_finish = (TextView) findViewById(R.id.gw_finish_c);
    }
    private void setUpActionbar() {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("任务详情");
    }
}

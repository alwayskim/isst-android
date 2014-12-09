package cn.edu.zju.isst1.v2.usercenter.taskcenter.gui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.constant.Constants;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.data.CSTTask;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;
import cn.edu.zju.isst1.v2.usercenter.taskcenter.net.TaskRequest;
import cn.edu.zju.isst1.v2.usercenter.taskcenter.net.TaskResponse;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

public class TaskCenterActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private ListView mlist;
    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();
    private int mCurrentPage = 1;
    private int DEFAULT_PAGE_SIZE = 20;
    public static final String ARCHIVE_URL = "/api/tasks";
    private Handler mHandler;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayAdapter<CSTTask> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_center);
        mlist = (ListView) findViewById(R.id.simple_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorScheme(R.color.lightbluetheme_color,
                R.color.lightbluetheme_color_half_alpha, R.color.lightbluetheme_color,
                R.color.lightbluetheme_color_half_alpha);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        setUpActionbar();
        requestData();
        initHandler();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_center, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            TaskCenterActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpActionbar() {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("任务中心");
    }

    private void requestData() {
        if (NetworkConnection.isNetworkConnected(getApplicationContext())) {
            TaskResponse taskResponse = new TaskResponse(getApplicationContext()) {
                @Override
                public void onResponse(JSONObject response) {
                    Lgr.i(response.toString());
                    CSTTask task = (CSTTask) CSTJsonParser.parseJson(response, new CSTTask());
//                  adapter = new ArrayAdapter<CSTTask>(getApplicationContext(), android.R.layout.simple_list_item_1, task.itemList);
                    Message msg = mHandler.obtainMessage();
                    msg.what = STATUS_REQUEST_SUCCESS;
                    msg.obj = task;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    super.onErrorResponse(error);
                }

            };
            TaskRequest taskRequest = new TaskRequest(CSTRequest.Method.GET,
                    ARCHIVE_URL, null, taskResponse)
                    .setPage(mCurrentPage)
                    .setPageSize(DEFAULT_PAGE_SIZE);
            mEngine.requestJson(taskRequest);
        } else {
            Message msg = mHandler.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            mHandler.sendMessage(msg);
        }
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                final CSTTask task = (CSTTask) msg.obj;
                adapter = new ArrayAdapter<CSTTask>(getApplicationContext(), R.layout.list_item, task.itemList);
                mlist.setAdapter(adapter);
                mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.setClass(TaskCenterActivity.this, GowhereSurveyActivity.class);
                        intent.putExtra("task", task.itemList.get(position));
                        startActivity(intent);
                    }
                });
                switch (msg.what) {
                    case Constants.STATUS_REQUEST_SUCCESS:
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;
                    case NETWORK_NOT_CONNECTED:
                        CroMan.showAlert((Activity) getApplicationContext(), NETWORK_NOT_CONNECTED);
                    default:
                        break;
                }
            }
        };

    }

    @Override
    public void onRefresh() {
        requestData();
        adapter.notifyDataSetChanged();
    }
}

package cn.edu.zju.isst1.v2.usercenter.setting;

import android.app.ActionBar;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.event.campus.net.CampusEventDetailResponse;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.user.data.CSTUser;
import cn.edu.zju.isst1.v2.user.data.CSTUserDataDelegate;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * Created by alwayking on 14/12/1.
 */
public class FeedBackActivity extends BaseActivity {

    private Handler mHandler;

    private EditText emailEdt;

    private EditText contentEdt;

    private Spinner typeSpinner;

    private TextView baseInfoTxv;

    private String version;

    private String currentApiVersion;

    private String userName;

    private String email;

    private int feedBackType;

    private final String operateSystem = "Android";

    String url = "http://10.82.60.35/feedback/app_feedbacks.json";

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private ArrayList<String> mArrayListType = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);
        setUpActionbar();
        initComponent();
        initHandler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedback_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FeedBackActivity.this.finish();
                return true;
            case R.id.action_send:
                sendFeedbackData();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpActionbar() {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("意见反馈");
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        CroMan.showConfirm(FeedBackActivity.this, "发送成功");
                        break;
                    case NETWORK_NOT_CONNECTED:
                        CroMan.showAlert(FeedBackActivity.this, R.string.network_not_connected);
                        break;
                    default:
                        CSTHttpUtil.dispose(msg.what, FeedBackActivity.this);
                        break;
                }
            }

        };
    }

    public void sendFeedbackData() {
        if (contentEdt.getText().toString().length() > 5) {
            feedBackType = typeSpinner.getSelectedItemPosition();
            Lgr.i(String.valueOf(feedBackType));


            CampusEventDetailResponse response = new CampusEventDetailResponse(this) {
                @Override
                public void onResponse(JSONObject response) {
                    super.onResponse(response);

                    Lgr.i(response.toString());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    super.onErrorResponse(error);
                    Lgr.i(error.toString());
                }
            };
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", userName);
            params.put("email", email);
            params.put("feedbacktype", String.valueOf(feedBackType));
            params.put("os", operateSystem + " " + currentApiVersion);
            params.put("appversion", version);
            params.put("content", contentEdt.getText().toString());

            JSONObject json = new JSONObject(params);
            Lgr.i(json.toString());

            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                    new CampusEventDetailResponse(this) {
                        @Override
                        public void onResponse(JSONObject response) {
                            super.onResponse(response);
                            Message msg = mHandler.obtainMessage();
                            msg.what = 0;
                            mHandler.sendMessage(msg);
                        }
                    },
                    new CampusEventDetailResponse(this) {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Lgr.i("errorResponse -> " + error.toString());
                            Message msg = mHandler.obtainMessage();
                            msg.what = mErrorStatusCode;
                            mHandler.sendMessage(msg);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    headers.put("Content-Type", "application/json; charset=UTF-8");
                    return headers;
                }
            };
            mEngine.requestCommon(jsonRequest);
        } else {
            Toast.makeText(this, "多写两句吧", Toast.LENGTH_SHORT).show();
        }
    }

    public void initComponent() {
        emailEdt = (EditText) findViewById(R.id.feedback_email_edt);
        baseInfoTxv = (TextView) findViewById(R.id.feedback_baseinfo_txv);
        typeSpinner = (Spinner) findViewById(R.id.feedback_type_spinner);
        contentEdt = (EditText) findViewById(R.id.feedback_content_edt);

        initSpinner();

        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = info.versionName;
        currentApiVersion = Build.VERSION.RELEASE;
        CSTUser user = CSTUserDataDelegate.getCurrentUser(this);
        userName = user.userName;
        if (user.email == null) {
            email = user.phoneNum;
        } else {
            email = user.email;
        }
        baseInfoTxv.setText("设备:" + operateSystem + "  系统:" + currentApiVersion + "  客户端版本:" + version);
    }

    public void initSpinner() {
        mArrayListType
                .add("请选择反馈类型");
        mArrayListType
                .add("Bug反馈");
        mArrayListType
                .add("功能添加");
        mArrayListType
                .add("交互改进");
        mArrayListType
                .add("运行效率");
        mArrayListType
                .add("硬件兼容性");
        mArrayListType
                .add("网络连接");
        mArrayListType
                .add("其他");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mArrayListType);
        // 设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将adapter 添加到spinner中
        typeSpinner.setAdapter(adapter);
    }
}

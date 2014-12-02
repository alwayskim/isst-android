package cn.edu.zju.isst1.v2.usercenter.setting;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.net.UpdateManager;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;
import cn.edu.zju.isst1.v2.splash.data.CSTVersion;
import cn.edu.zju.isst1.v2.splash.net.VersionResponse;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * Created by alwayking on 14/11/30.
 */
public class SettingActivity extends BaseActivity {

    private Button btnCheckUpdate;
    private Button btnFeedBack;
    private Button btnAppInfo;
    private TextView txvAppInfo;

    private String versionName;

    private Handler mHandler;

    private AlertDialog.Builder mAldUpdate;

    private CSTVersion newVersion;

    private static final String VERSION_URL = "/api/android/version";

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        setUpActionbar();
        initComponent();
        initAlertDialog();
        initHandler();
        setUpListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SettingActivity.this.finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initComponent() {
        btnFeedBack = (Button) findViewById(R.id.setting_feedback_btn);
        btnAppInfo = (Button) findViewById(R.id.setting_app_info_btn);
        btnCheckUpdate = (Button) findViewById(R.id.setting_check_update_btn);
        txvAppInfo = (TextView) findViewById(R.id.setting_app_info_txv);

        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionName = info.versionName;
        txvAppInfo.setText("version " + versionName);
    }

    private void initHandler() {
        mHandler = new Handler() {

            /*
             * (non-Javadoc)
             *
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        try {
                            if (getPackageManager().getPackageInfo(getPackageName(), 0).versionCode
                                    < ((CSTVersion) msg.obj).buildNum) {
                                mAldUpdate.show();
                            } else {
                                Toast.makeText(SettingActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case NETWORK_NOT_CONNECTED:
                        CroMan.showAlert(SettingActivity.this, R.string.network_not_connected);
                        break;
                    default:
                        CSTHttpUtil.dispose(msg.what, SettingActivity.this);
                        break;
                }
            }

        };
    }

    public void setUpListener() {
        btnCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestVersionInfo();
            }
        });

        btnAppInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, FeedBackActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpActionbar() {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("设置");
    }

    private void initAlertDialog() {
        mAldUpdate = new AlertDialog.Builder(this);
        mAldUpdate.setTitle(R.string.new_update_avaliable);
        mAldUpdate.setMessage(R.string.update_detail);
        mAldUpdate.setPositiveButton(R.string.OK,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateManager.createInstance(SettingActivity.this
                                .getApplicationContext());
                        UpdateManager.getInstance().downloadUpdate(newVersion.downloadUrl, newVersion.version);
                    }
                }
        );

        mAldUpdate.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }
        );

        mAldUpdate.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
    }

    private void requestVersionInfo() {
        if (NetworkConnection.isNetworkConnected(this)) {
            VersionResponse verResponse = new VersionResponse(this) {
                @Override
                public void onResponse(JSONObject response) {
                    super.onResponse(response);
                    newVersion = (CSTVersion) CSTJsonParser
                            .parseJson(response, new CSTVersion());
                    Message msg = mHandler.obtainMessage();
                    msg.what = newVersion.getStatusInfo().status;
                    msg.obj = newVersion;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    super.onErrorResponse(error);
                    Message msg = mHandler.obtainMessage();
                    msg.what = mErrorStatusCode;
                    mHandler.sendMessage(msg);
                }
            };
            CSTJsonRequest verRequest = new CSTJsonRequest(CSTRequest.Method.GET, VERSION_URL, null,
                    verResponse);
            mEngine.requestJson(verRequest);
        } else {
            Message msg = mHandler.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            mHandler.sendMessage(msg);
        }
    }
}

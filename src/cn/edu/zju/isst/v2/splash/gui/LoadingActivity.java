/**
 *
 */
package cn.edu.zju.isst.v2.splash.gui;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import cn.edu.zju.isst.R;
import cn.edu.zju.isst.net.NetworkConnection;
import cn.edu.zju.isst.net.UpdateManager;
import cn.edu.zju.isst.settings.CSTSettings;
import cn.edu.zju.isst.util.CroMan;
import cn.edu.zju.isst.v2.login.gui.LoginActivity;
import cn.edu.zju.isst.ui.main.NewMainActivity;
import cn.edu.zju.isst.util.Lgr;
import cn.edu.zju.isst.v2.data.CSTJsonParser;
import cn.edu.zju.isst.v2.gui.CSTBaseActivity;
import cn.edu.zju.isst.v2.net.CSTJsonRequest;
import cn.edu.zju.isst.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst.v2.net.CSTRequest;
import cn.edu.zju.isst.v2.splash.data.CSTVersion;
import cn.edu.zju.isst.v2.splash.net.VersionResponse;

import static cn.edu.zju.isst.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * 加载页面
 *
 * @author theasir
 */
public class LoadingActivity extends CSTBaseActivity {

    private static final String VERSION_URL = "/api/android/version";

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private Handler mHandler;

    private AlertDialog.Builder mAldUpdate;

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
//        // 打开推送
//        L.i("Loading____pushSetting");
//        PushSettings.enableDebugMode(getApplicationContext(), true);
//
//        // 以apikey的方式登录，一般放在主Activity的onCreate中
//
//        PushManager.startWork(getApplicationContext(),
//                PushConstants.LOGIN_TYPE_API_KEY, "PqDQfrucX3ubvW7fm0M23gWu");

        initAlertDialog();

        initHandler();

        requestVersionInfo();
    }

    private void initAlertDialog() {
        mAldUpdate = new AlertDialog.Builder(LoadingActivity.this);
        mAldUpdate.setTitle(R.string.new_update_avaliable);
        mAldUpdate.setMessage(R.string.update_detail);
        mAldUpdate.setPositiveButton(R.string.OK,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateManager.createInstance(LoadingActivity.this
                                .getApplicationContext());
                        UpdateManager.getInstance().downloadUpdate();

                        jump();
                    }
                }
        );

        mAldUpdate.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        jump();
                    }
                }
        );

        mAldUpdate.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                jump();
            }
        });
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
                                jump();
                            }
                        } catch (NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case NETWORK_NOT_CONNECTED:
                        CroMan.showAlert(LoadingActivity.this, R.string.network_not_connected);
                        break;
                    default:
                        jump();
                        break;
                }
            }

        };
    }

    private void requestVersionInfo() {
        if (NetworkConnection.isNetworkConnected(this)) {
            VersionResponse verResponse = new VersionResponse(this) {
                @Override
                public void onResponse(JSONObject response) {
                    super.onResponse(response);
                    CSTVersion version = (CSTVersion) CSTJsonParser
                            .parseJson(response, new CSTVersion());
                    Lgr.i(response.toString());
                    //Tricky for no authentication for this request. So if success, status will always be 0. Thus not handle status.
                    Message msg = mHandler.obtainMessage();
                    msg.what = version.getStatusInfo().status;
                    msg.obj = version;
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

    private void jump() {
        Lgr.i(this.getClass().getName() + " jump isAutoLogin? "
                + CSTSettings.isAutoLogin(LoadingActivity.this));
        if (CSTSettings.isAutoLogin(LoadingActivity.this)) {
            LoadingActivity.this.startActivity(new Intent(LoadingActivity.this,
                    NewMainActivity.class));

        } else {
            LoadingActivity.this.startActivity(new Intent(LoadingActivity.this,
                    LoginActivity.class));
        }
        LoadingActivity.this.finish();
    }

}

package cn.edu.zju.isst1.v2.login.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.db.DataManager;
import cn.edu.zju.isst1.db.User;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.ui.main.LoginSimulation;
import cn.edu.zju.isst1.ui.main.MessageDisposition;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.login.gui.LoginActivity;
import cn.edu.zju.isst1.v2.net.CSTStatusInfo;
import cn.edu.zju.isst1.v2.user.data.CSTUser;
import cn.edu.zju.isst1.v2.user.data.CSTUserDataDelegate;
import cn.edu.zju.isst1.v2.user.net.UserResponse;

import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_CLASSCAST;
import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_IO;
import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_JSON;
import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_SOCKETTIMEOUT;
import static cn.edu.zju.isst1.constant.Constants.EXCEPTION_UNKNOWN;
import static cn.edu.zju.isst1.constant.Constants.HTTPERROR_CLIENTERROR;
import static cn.edu.zju.isst1.constant.Constants.HTTPERROR_SERVERERROR;
import static cn.edu.zju.isst1.constant.Constants.HTTPERROR_UNKNOWN;
import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_LOGIN_AUTH_EXPIRED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_LOGIN_AUTH_FAILED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_LOGIN_USERNAME_NOT_EXIST;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * Created by alwayking on 14/11/14.
 */
public class UpDateLogin implements
        MessageDisposition {

    private Handler m_handlerUpdateLogin;

    public Context context;

    private static UpDateLogin INSTANCE;

    public UpDateLogin() {
        initHandler();
    }

    public static UpDateLogin getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UpDateLogin();
        }
        return INSTANCE;
    }

    public void updateLogin(Context context) {
        CSTUser currentUser = CSTUserDataDelegate.getCurrentUser(context);
        this.context = context;
        LoginApi.update(currentUser, 0.0, 0.0, new UserResponse(context, true) {
            @Override
            public void onResponse(JSONObject response) {
                Message msg = m_handlerUpdateLogin.obtainMessage();
                try {
                    msg.what = response.getInt("status");
                    msg.obj = response;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (msg.what == STATUS_REQUEST_SUCCESS) {
                    CSTUser user = (CSTUser) CSTJsonParser
                            .parseJson(response, new CSTUser());
                    CSTUserDataDelegate.deleteAllUsers(mContext);
                    CSTUserDataDelegate.saveUser(mContext, user);
                }
                m_handlerUpdateLogin.sendMessage(msg);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }

            @Override
            public void onSuccessStatus() {
                super.onSuccessStatus();
            }

            @Override
            public Object onErrorStatus(CSTStatusInfo statusInfo) {
                return super.onErrorStatus(statusInfo);
            }
        });

    }

    private void initHandler() {
        m_handlerUpdateLogin = new Handler() {

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
                            DataManager.syncCurrentUser(
                                    new User(((JSONObject) msg.obj)
                                            .getJSONObject("body"))
                            );
                            requestGlobalData();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    case STATUS_LOGIN_USERNAME_NOT_EXIST:
                    case STATUS_LOGIN_AUTH_EXPIRED:
                    case STATUS_LOGIN_AUTH_FAILED:
                        Intent intent = new Intent(context,
                                LoginActivity.class);
                        intent.putExtra("isLoginAgain", true);
                        context.startActivity(intent);
                        Lgr.i("Test", "Login Again");
                        break;
                    default:
                        dispose(msg);
                }
            }

        };
    }

    protected void requestGlobalData() {
        cn.edu.zju.isst1.v2.globaldata.GlobalDataCache.cacheCityList(context);
        cn.edu.zju.isst1.v2.globaldata.GlobalDataCache.cacheClassList(context);
        cn.edu.zju.isst1.v2.globaldata.GlobalDataCache.cacheMajorList(context);
    }

    @Override
    public int dispose(Message msg) {
        switch (msg.what) {
            case NETWORK_NOT_CONNECTED:
                CroMan.showAlert((Activity) context, R.string.network_not_connected);
                break;

            case HTTPERROR_UNKNOWN:
            case HTTPERROR_CLIENTERROR:
            case HTTPERROR_SERVERERROR:
                CroMan.showAlert((Activity) context, R.string.http_error);
                break;

            case EXCEPTION_SOCKETTIMEOUT:
                CroMan.showAlert((Activity) context, R.string.exception_socket_timeout);
                break;

            case EXCEPTION_UNKNOWN:
            case EXCEPTION_IO:
            case EXCEPTION_JSON:
            case EXCEPTION_CLASSCAST:
                CroMan.showAlert((Activity) context, R.string.exception);
                break;

            default:
                break;
        }
        return 0;
    }
}

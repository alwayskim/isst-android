/**
 *
 */
package cn.edu.zju.isst1.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.api.LoginApi;
import cn.edu.zju.isst1.db.DataManager;
import cn.edu.zju.isst1.db.User;
import cn.edu.zju.isst1.net.CSTResponse;
import cn.edu.zju.isst1.net.RequestListener;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.login.gui.LoginActivity;

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
 * @author theasir
 */
public class BaseActivity extends Activity implements LoginSimulation,
        MessageDisposition {

    private Handler m_handlerUpdateLogin;

    /*
     * (non-Javadoc)
     *
     * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initHandler();
    }

    @Override
    public void updateLogin() {
        User currentUser = DataManager.getCurrentUser();
        LoginApi.update(currentUser, 0.0, 0.0, new RequestListener() {

            @Override
            public void onComplete(Object result) {
                Message msg = m_handlerUpdateLogin.obtainMessage();
                try {
                    msg.what = ((JSONObject) result).getInt("status");
                    msg.obj = (JSONObject) result;
                } catch (Exception e) {
                    Lgr.i(this.getClass().getName() + " onComplete Exception!");
                    onException(e);
                }
                m_handlerUpdateLogin.sendMessage(msg);

            }

            @Override
            public void onHttpError(CSTResponse response) {
                Lgr.i(this.getClass().getName() + " onHttpError!");

            }

            @Override
            public void onException(Exception e) {
                Lgr.i(this.getClass().getName() + " onException!");
                e.printStackTrace();

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
                        Intent intent = new Intent(BaseActivity.this,
                                LoginActivity.class);
                        intent.putExtra("isLoginAgain", true);
                        BaseActivity.this.startActivity(intent);
                        Lgr.i("TesT", "Login Again");
                        break;
                    default:
                        break;
                }
            }

        };
    }

    @Override
    public int dispose(Message msg) {
        switch (msg.what) {
            case NETWORK_NOT_CONNECTED:
                CroMan.showAlert(BaseActivity.this, R.string.network_not_connected);
                break;

            case HTTPERROR_UNKNOWN:
            case HTTPERROR_CLIENTERROR:
            case HTTPERROR_SERVERERROR:
                CroMan.showAlert(BaseActivity.this, R.string.http_error);
                break;

            case EXCEPTION_SOCKETTIMEOUT:
                CroMan.showAlert(BaseActivity.this, R.string.exception_socket_timeout);
                break;

            case EXCEPTION_UNKNOWN:
            case EXCEPTION_IO:
            case EXCEPTION_JSON:
            case EXCEPTION_CLASSCAST:
                CroMan.showAlert(BaseActivity.this, R.string.exception);
                break;

            default:
                break;
        }
        return 0;
    }

    protected void requestGlobalData() {
        cn.edu.zju.isst1.v2.globaldata.GlobalDataCache.cacheCityList(this);
        cn.edu.zju.isst1.v2.globaldata.GlobalDataCache.cacheClassList(this);
        cn.edu.zju.isst1.v2.globaldata.GlobalDataCache.cacheMajorList(this);
    }

}

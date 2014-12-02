package cn.edu.zju.isst1.v2.net;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import cn.edu.zju.isst1.constant.Constants;
import cn.edu.zju.isst1.util.Lgr;

/**
 * Created by i308844 on 8/18/14.
 */
public class CSTJsonResponse extends CSTResponse<JSONObject> implements CSTResponseStatusListener {

    protected Context mContext;

    protected int mErrorStatusCode;

    protected CSTJsonResponse(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void onSuccessStatus() {

    }

    @Override
    public Object onErrorStatus(CSTStatusInfo statusInfo) {

        Lgr.e(statusInfo.toString());
        return null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Lgr.i("*VolleyError*:   " + error.toString());
        if (error instanceof ServerError) {
            mErrorStatusCode = Constants.HTTPERROR_SERVERERROR;
        } else if (error instanceof ParseError) {
            mErrorStatusCode = Constants.EXCEPTION_JSON;
        } else if (error instanceof NoConnectionError) {
            mErrorStatusCode = Constants.NETWORK_NOT_CONNECTED;
        } else if (error instanceof TimeoutError) {
            mErrorStatusCode = Constants.EXCEPTION_SOCKETTIMEOUT;
        } else if (error instanceof NetworkError) {
            mErrorStatusCode = Constants.NETWORK_NOT_CONNECTED;
        }
        else {
            mErrorStatusCode = Constants.EXCEPTION_UNKNOWN;
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        Lgr.i(response.toString());
    }
}

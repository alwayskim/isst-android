package cn.edu.zju.isst.v2.net;

import com.android.volley.Response;

import android.content.Context;

/**
 * Created by i308844 on 7/28/14.
 */
public abstract class CSTResponse<T> implements Response.Listener<T>, Response.ErrorListener {

    public CSTResponse(Context context){

    }
}

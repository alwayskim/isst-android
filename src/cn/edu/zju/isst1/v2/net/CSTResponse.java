package cn.edu.zju.isst1.v2.net;

import android.content.Context;

import com.android.volley.Response;

/**
 * Created by i308844 on 7/28/14.
 */
public abstract class CSTResponse<T> implements Response.Listener<T>, Response.ErrorListener {

    public CSTResponse(Context context){

    }
}

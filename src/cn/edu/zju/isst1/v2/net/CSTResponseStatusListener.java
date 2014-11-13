package cn.edu.zju.isst1.v2.net;

/**
 * Created by i308844 on 7/31/14.
 */
public interface CSTResponseStatusListener {

    public void onSuccessStatus();

    public Object onErrorStatus(CSTStatusInfo statusInfo);
}

package cn.edu.zju.isst1.net;

/**
 * @deprecated
 * 编写的Request请求的listener接口
 *
 * @author kbeta
 *         <p/>
 *         Updated by theasir
 */
public interface RequestListener {

    public void onComplete(Object result);

    public void onHttpError(CSTResponse response);

    public void onException(Exception e);

}

package cn.edu.zju.isst.net;

import org.json.JSONObject;

import java.util.Map;

import cn.edu.zju.isst.util.Judge;
import cn.edu.zju.isst.util.Lgr;

/**
 * 前辈遗留，感谢
 *
 * @deprecated
 */
public class AsyncWebServiceRunner {

    /**
     * 请求数据，并在获取到数据后通过RequestListener将result回传给调用者
     *
     * @param methodName 要调用的WebService方法名
     * @param url        URL
     * @param params     参数
     * @param listener   回调对象
     */
    public static void request(final String methodName, final String url,
            final Map<String, String> params, final RequestListener listener) {
        new Thread() {
            @Override
            public void run() {
                try {
                    JSONObject result;
                    String tempResult;
                    if (methodName.equalsIgnoreCase("GET")) {
                        Lgr.i("AsyncWebServiceRunner_____get");
                        tempResult = HttpInvoker.getRequest(url);
                        result = new JSONObject(tempResult);
                    } else if (methodName.equalsIgnoreCase("POST")) {
                        Lgr.i("AsyncWebServiceRunner_____post");
                        tempResult = HttpInvoker.postRequest(url, params);
                        result = new JSONObject(tempResult);
                    } else {
                        result = null;
                    }
                    if (!Judge.isNullOrEmpty(result))// ?有问题
                    {
                        Lgr.i("listener.onCompete");
                        listener.onComplete(result);

                    } else {
                        listener.onException(new Exception(
                                "Not Connected OR Unsupport Method"));
                    }
                } catch (Exception e) {
                    listener.onException(e);
                }

            }
        }.start();

    }
}

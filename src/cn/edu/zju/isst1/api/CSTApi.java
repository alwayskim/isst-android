/**
 *
 */

package cn.edu.zju.isst1.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Map;

import cn.edu.zju.isst1.net.BetterAsyncWebServiceRunner;
import cn.edu.zju.isst1.net.CSTResponse;
import cn.edu.zju.isst1.net.RequestListener;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;

/**
 * @author theasir
 * @deprecated 接口基类
 */
public class CSTApi {

    /**
     * 服务器前缀地址
     */
//    private static final String PREFIX = "http://www.cst.zju.edu.cn/isst";

    private static final String PREFIX = "http://10.82.60.35:8080/isst";

    /**
     * 发送请求
     *
     * @param methodName 方法类别
     * @param subUrl     子网址
     * @param params     要传递的参数
     * @param listener   回调对象
     */
    protected static void request(final String methodName, final String subUrl,
                                  final Map<String, String> params, RequestListener listener) {
        String requestUrl = PREFIX + subUrl;
        if (methodName.equalsIgnoreCase("POST")) {
            BetterAsyncWebServiceRunner.getInstance()
                    .request(methodName, requestUrl, params, listener);
        } else {
            try {
                requestUrl = requestUrl + (Judge.isNullOrEmpty(params) ? ""
                        : ("?" + BetterAsyncWebServiceRunner.getInstance().paramsToString(params)));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            BetterAsyncWebServiceRunner.getInstance()
                    .request(methodName, requestUrl, null, listener);
        }
        Lgr.i("CSTApi Request URL = " + requestUrl);
    }

    protected static CSTResponse responseOfRequest(final String methodName, final String subUrl,
                                                   final Map<String, String> params)
            throws MalformedURLException, IOException {
        String url = PREFIX + subUrl;
        return BetterAsyncWebServiceRunner.getInstance().responseOfRequest(methodName, url, params);
    }

}

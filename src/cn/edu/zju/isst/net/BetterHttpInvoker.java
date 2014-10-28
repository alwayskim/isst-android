/**
 *
 */
package cn.edu.zju.isst.net;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.zju.isst.util.Judge;
import cn.edu.zju.isst.util.Lgr;

import static cn.edu.zju.isst.constant.Constants.HTTP_CONNECT_TIMEOUT;

/**
 * @deprecated
 * HTTP请求类
 *
 * @see {@link https://code.google.com/p/aerc/}
 */
public class BetterHttpInvoker {

    /**
     * 单个实例
     */
    private static BetterHttpInvoker INSTANCE = new BetterHttpInvoker();

    /**
     * 私有构造器，防止初始化新的实例
     */
    private BetterHttpInvoker() {
    }

    /**
     * 单例模式
     *
     * @return 单个实例
     */
    public static BetterHttpInvoker getInstance() {
        return INSTANCE;
    }

    /**
     * 读取输入流
     *
     * @param in 输入流
     * @return 字节流
     * @throws IOException 未处理异常
     */
    private static byte[] readStream(InputStream in) throws IOException {
        byte[] buf = new byte[1024];
        int count = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        while ((count = in.read(buf)) != -1) {
            out.write(buf, 0, count);
        }
        return out.toByteArray();
    }

    /**
     * GET请求
     *
     * @param uri     URL
     * @param headers Http Headers
     * @return CSTResponse
     * @throws IOException 未处理异常
     */
    public CSTResponse get(URL uri, Map<String, List<String>> headers)
            throws IOException {
        GET get = new GET(uri, headers);
        return getOrPost(get);
    }

    /**
     * POST请求
     *
     * @param uri     URL
     * @param headers Http Headers
     * @param body    参数
     * @return CSTResponse
     * @throws IOException 未处理异常
     */
    public CSTResponse post(URL uri, Map<String, List<String>> headers,
            byte[] body) throws IOException {
        POST post = new POST(uri, headers, body);
        return getOrPost(post);
    }

    /**
     * 发送请求
     *
     * @param request 请求
     * @return CSTResponse
     * @throws IOException 未处理异常
     */
    private CSTResponse getOrPost(Request request) throws IOException {
        if (Judge.isNullOrEmpty(request)) {
            return null;
        }
        HttpURLConnection conn = null;
        CSTResponse response = null;
        System.setProperty("http.keepAlive", "false");
        try {
            conn = (HttpURLConnection) request.getUri().openConnection();
            Lgr.i("BetterHttpInvoker openConnection URL = "
                    + request.getUri().toString());

            if (!request.getHeaders().isEmpty()) {
                for (String header : request.getHeaders().keySet()) {
                    for (String value : request.getHeaders().get(header)) {
                        conn.addRequestProperty(header, value);
                    }
                }
            }

            if (request instanceof POST) {
                byte[] payload = ((POST) request).getBody();
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(payload.length);
                // conn.setDoInput(true);
                // conn.setRequestMethod("POST");
                // conn.setUseCaches(false);
                // conn.setInstanceFollowRedirects(true);
                // conn.setRequestProperty("Content-Type",
                // "application/x-www-form-urlencoded");
                conn.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
                conn.setReadTimeout(10000);

                Lgr.i("BetterHttpInvoker Before POST getOutputStream()");

                conn.getOutputStream().write(payload);

                Lgr.i("BetterHttpInvoker After POST getResponseCode() = "
                        + conn.getResponseCode());
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    response = new CSTResponse(conn.getResponseCode(),
                            new HashMap<String, List<String>>(), conn
                            .getResponseMessage().getBytes()
                    );
                }
            }

            if (Judge.isNullOrEmpty(response)) {
                // TODO handle httpError like 404: return nothing and response
                // won't initialize
                Lgr.i("BetterHttpInvoker Before getInputStream()");
                BufferedInputStream in = new BufferedInputStream(
                        conn.getInputStream());
                byte[] body = readStream(in);
                Lgr.i("BetterHttpInvoker After getResponseCode() = "
                        + conn.getResponseCode());
                response = new CSTResponse(conn.getResponseCode(),
                        conn.getHeaderFields(), body);
            }
        } finally {
            if (!Judge.isNullOrEmpty(conn)) {
                conn.disconnect();
            }
        }
        return response;
    }

    /**
     * 请求类
     *
     * @author theasir
     */
    private class Request {

        private URL uri;

        private Map<String, List<String>> headers;

        public Request(URL uri, Map<String, List<String>> headers) {
            this.uri = uri;
            this.headers = headers;
        }

        /**
         * @return the uri
         */
        public URL getUri() {
            return uri;
        }

        /**
         * @return the headers
         */
        public Map<String, List<String>> getHeaders() {
            return headers;
        }

    }

    /**
     * POST请求类
     *
     * @author theasir
     */
    private class POST extends Request {

        private byte[] body;

        public POST(URL uri, Map<String, List<String>> headers, byte[] body) {
            super(uri, headers);
            this.body = body;
        }

        /**
         * @return the body
         */
        public byte[] getBody() {
            return body;
        }

    }

    /**
     * GET请求类
     *
     * @author theasir
     */
    private class GET extends Request {

        public GET(URL uri, Map<String, List<String>> headers) {
            super(uri, headers);
        }
    }
}

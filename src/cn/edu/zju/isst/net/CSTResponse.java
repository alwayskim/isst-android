/**
 *
 */
package cn.edu.zju.isst.net;

import java.util.List;
import java.util.Map;

/**
 * @deprecated
 * 响应类
 *
 * @see {@link https://code.google.com/p/aerc/}
 * <p/>
 * Represents the result of a Http request.
 */
public class CSTResponse {

    /**
     * The HTTP status code
     */
    private int status;

    /**
     * The HTTP headers received in the response
     */
    private Map<String, List<String>> headers;

    /**
     * The response body, if any
     */
    private byte[] body;

    protected CSTResponse(int status, Map<String, List<String>> headers,
            byte[] body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return the headers
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * @return the body
     */
    public byte[] getBody() {
        return body;
    }

}

/**
 *
 */
package cn.edu.zju.isst.constant;

/**
 * 常量类
 *
 * @author theasir
 *         <p/>
 *         TODO WIP
 */
public final class Constants {

    /**
     * 成功返回
     */
    public static final int STATUS_REQUEST_SUCCESS = 0;

    /**
     * 用户不存在
     */
    public static final int STATUS_LOGIN_USERNAME_NOT_EXIST = 10;

    /**
     * 密码错误
     */
    public static final int STATUS_LOGIN_PASSWORD_ERROR = 11;

    /**
     * 认证失效
     */
    public static final int STATUS_LOGIN_AUTH_EXPIRED = 12;

    /**
     * 认证失败
     */
    public static final int STATUS_LOGIN_AUTH_FAILED = 13;

    /**
     * 未登录
     */
    public static final int STATUS_NOT_LOGIN = 1;

    /**
     * 网络未连接
     */
    public static final int NETWORK_NOT_CONNECTED = -0xa0;

    /**
     * 未知异常
     */
    public static final int EXCEPTION_UNKNOWN = -0xe0;

    /**
     * IO异常
     */
    public static final int EXCEPTION_IO = -0xe1;

    /**
     * Socket超时异常
     */
    public static final int EXCEPTION_SOCKETTIMEOUT = -0xe2;

    /**
     * JSON异常
     */
    public static final int EXCEPTION_JSON = -0xe3;

    /**
     * 类型转换异常
     */
    public static final int EXCEPTION_CLASSCAST = -0xe4;

    /**
     * 未知网络错误
     */
    public static final int HTTPERROR_UNKNOWN = -0xf0;

    /**
     * HTTP 4xx
     */
    public static final int HTTPERROR_CLIENTERROR = -0xf4;

    /**
     * HTTP 5xx
     */
    public static final int HTTPERROR_SERVERERROR = -0xf5;

    /**
     * HTTP请求超时
     */
    public static final int HTTP_CONNECT_TIMEOUT = 10000;

    /**
     * 包名
     */
    public static final String PACKAGE_NAME = "cn.edu.zju.isst";

    /**
     * 发布者
     */
    public static final String PUBLISHER_NAME = "发布者：";

    /**
     * the result code to propagate to the original activity,
     * this is the code between ContactFilter and ContactListFragment
     */
    public static final int RESULT_CODE_BETWEEN_CONTACT = 20;

    /**
     * 私有构造方法，防止对象实例化
     */
    private Constants() {
    }

}
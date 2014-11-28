/**
 *
 */
package cn.edu.zju.isst1;

import android.app.Application;
import android.webkit.CookieSyncManager;

import com.baidu.frontia.FrontiaApplication;

import cn.edu.zju.isst1.db.DBHelper;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.net.VolleyRequestManager;

/**
 * 主应用入口，可以存放全局变量（建议常量类单独管理）
 *
 * @author theasir
 */
public class CSTApp extends FrontiaApplication {

    /*
     * (non-Javadoc)
     *
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // 设置调试状态
        Lgr.setDebuggable(true);
        CookieSyncManager.createInstance(this);
        DBHelper.createInstance(this);
        VolleyRequestManager.createInstance(this);
    }
}

/**
 *
 */
package cn.edu.zju.isst;

import android.app.Application;
import android.webkit.CookieSyncManager;

import cn.edu.zju.isst.db.DBHelper;
import cn.edu.zju.isst.util.Lgr;
import cn.edu.zju.isst.v2.net.VolleyRequestManager;

/**
 * 主应用入口，可以存放全局变量（建议常量类单独管理）
 *
 * @author theasir
 */
public class CSTApp extends Application {

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

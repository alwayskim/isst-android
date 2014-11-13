package cn.edu.zju.isst1.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.edu.zju.isst1.util.Judge;

/**
 * 判断网络是否链接正常
 */
public class NetworkConnection {

    public static boolean isNetworkConnected(Context context) {
        if (!Judge.isNullOrEmpty(context)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
            if (Judge.isNullOrEmpty(networkInfo)) {
                return false;
            } else if (networkInfo.isAvailable()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
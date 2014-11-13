/**
 *
 */
package cn.edu.zju.isst1.net;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;

import cn.edu.zju.isst1.util.Lgr;

/**
 * @author stevwayne
 */
public class UpdateManager {

    //	public static final String UPDATE_APKFILE_URL = "http://www.zjucst.com/downloads/isst-1.0.0.apk";
    public static final String UPDATE_APKFILE_URL
            = "http://www.cst.zju.edu.cn/isst-releases/isst.apk";

    private static UpdateManager INSTANCE;

    private Context context;

    private DownloadManager dm;

    private UpdateManager(Context context) {
        this.context = context;
    }

    private String mSavePath;

    public static UpdateManager createInstance(Context context) {
        INSTANCE = new UpdateManager(context);
        return INSTANCE;
    }

    public static UpdateManager getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException(
                    "UpdateManager::createInstance() needs to be called "
                            + "before UpdateManager::getInstance()"
            );
        }

        return INSTANCE;
    }

    public void downloadUpdate(String updateApkUrl, String updateVersion) {

        String sdpath = Environment.getExternalStorageDirectory() + "/";
        mSavePath = sdpath + "Downloads";
        dm = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(updateApkUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir("Downloads", "ISST" + updateVersion + ".apk");
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        final long ref = dm.enqueue(request);
        Lgr.i("UpdateApkId", "" + ref);

        IntentFilter filter = new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE);

        BroadcastReceiver receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                long recceiveRef = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Lgr.i("UpdateApkId", "Downloaded Apk Id: " + recceiveRef);
                if (ref == recceiveRef) {
                    try {
                        Lgr.i("UpdateApkId", "Before Open File");
                        dm.openDownloadedFile(recceiveRef);
                        File apkfile = new File(mSavePath, "ISST2.apk");
                        if (!apkfile.exists()) {
                            return;
                        }
                        installApk(apkfile);
                        Lgr.i("UpdateApkId", "After Open File");
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        };

        context.registerReceiver(receiver, filter);
    }

    protected void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);

    }
}

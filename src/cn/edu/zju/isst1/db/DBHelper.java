/**
 *
 */
package cn.edu.zju.isst1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.edu.zju.isst1.util.Lgr;

/**
 * @deprecated
 * 数据库辅助类
 *
 * @author theasir
 */
public class DBHelper extends SQLiteOpenHelper {

    /**
     * 数据库名称（文件名）
     */
    private static final String DATABASE_NAME = "main.db";

    /**
     * 数据库版本
     */
    private static final int DATABASE_VERSION = 1;

    private static DBHelper INSTANCE;

    /**
     * @param context
     */
    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public synchronized static void createInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DBHelper(context.getApplicationContext());
        }
    }

    public synchronized static DBHelper getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException(
                    "DBHelper::createInstance() needs to be called "
                            + "before DBHelper::getInstance()"
            );
        }
        return INSTANCE;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
     * .SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists main (name TEXT PRIMARY KEY, object BLOB)");
        Lgr.i("Creat DB!");

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
     * .SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}

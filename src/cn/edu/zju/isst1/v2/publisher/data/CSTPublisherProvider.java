package cn.edu.zju.isst1.v2.publisher.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import cn.edu.zju.isst1.v2.db.CSTProvider;
import cn.edu.zju.isst1.v2.db.SimpleTableProvider;

/**
 * Created by lqynydyxf on 2014/8/7.
 */
public class CSTPublisherProvider extends SimpleTableProvider {

    public static final String TABLE_NAME = "publiser";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + Columns.ID.key + " INTEGER, "
            + Columns.NAME.key + " VARCHAR(255), "
            + Columns.PHONE.key + " VARCHAR(255), "
            + Columns.QQ.key + " VARCHAR(255), "
            + Columns.EMAIL.key + " VARCHAR(255), "
            + "UNIQUE (" + Columns.ID.key + ") ON CONFLICT REPLACE)";

    public static final Uri CONTENT_URI = CSTProvider.CONTENT_URI.buildUpon().appendPath(TABLE_NAME)
            .build();

    public CSTPublisherProvider(Context context) {
        super(context);
    }

    public SimpleTableProvider getInstance(Context context) {
        return null;
    }

    @Override
    protected Uri getBaseContentUri() {
        return CSTProvider.CONTENT_URI;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    public enum Columns {
        ID("id"),
        NAME("name"),
        PHONE("phoneNum"),
        QQ("qqNum"),
        EMAIL("email");

        public String key;

        private Columns(String key) {
            this.key = key;
        }
    }
}

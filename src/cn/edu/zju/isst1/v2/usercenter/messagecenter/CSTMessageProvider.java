package cn.edu.zju.isst1.v2.usercenter.messagecenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import cn.edu.zju.isst1.v2.db.CSTProvider;
import cn.edu.zju.isst1.v2.db.SimpleTableProvider;

/**
 * Created by alwayking on 14/11/30.
 */
public class CSTMessageProvider extends SimpleTableProvider{
    public static final String TABLE_NAME = "messagelist";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + Columns.ID.key + " VARCHAR(255), "
            + Columns.TITLE.key + " VARCHAR(255), "
            + Columns.CONTENT.key + " VARCHAR(255), "
            + Columns.CREATEDAT.key + " INTEGER, "
            + "UNIQUE (" + Columns.ID.key + ") ON CONFLICT REPLACE)";

    public static final Uri CONTENT_URI = CSTProvider.CONTENT_URI.buildUpon().appendPath(TABLE_NAME)
            .build();

    public CSTMessageProvider(Context context) {
        super(context);
    }

    public static CSTMessageProvider getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CSTMessageProvider(context);
        }
        return (CSTMessageProvider) INSTANCE;
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
        CONTENT("content"),
        TITLE("title"),
        CREATEDAT("createdAt");

        public String key;

        private Columns(String key) {
            this.key = key;
        }
    }
}

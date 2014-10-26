package cn.edu.zju.isst.v2.archive.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import cn.edu.zju.isst.v2.db.CSTProvider;
import cn.edu.zju.isst.v2.db.SimpleTableProvider;

/**
 * Created by i308844 on 8/12/14.
 */
public class CSTArchiveProvider extends SimpleTableProvider {

    public static final String TABLE_NAME = "archive";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + Columns.ID.key + " VARCHAR(255), "
            + Columns.TITLE.key + " VARCHAR(255), "
            + Columns.CATEGORY_ID.key + " INTEGER, "
            + Columns.DESCRIPTION.key + " VARCHAR(255), "
            + Columns.UPDATE_TIME.key + " INTEGER, "
            + Columns.PUBLISHER_ID.key + " INTEGER, "
            + Columns.PUBLISHER.key + " BLOB, "
            + Columns.CONTENT.key + " VARCHAR(255), "
            + "UNIQUE (" + Columns.ID.key + ") ON CONFLICT REPLACE)";

    public static final Uri CONTENT_URI = CSTProvider.CONTENT_URI.buildUpon().appendPath(TABLE_NAME)
            .build();

    private CSTArchiveProvider(Context context) {
        super(context);
    }

    public static CSTArchiveProvider getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CSTArchiveProvider(context);
        }
        return (CSTArchiveProvider) INSTANCE;
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
        TITLE("title"),
        CATEGORY_ID("category_id"),
        DESCRIPTION("description"),
        UPDATE_TIME("update_time"),
        PUBLISHER_ID("publisher_id"),
        PUBLISHER("publisher"),
        CONTENT("content");

        public String key;

        private Columns(String key) {
            this.key = key;
        }
    }
}

package cn.edu.zju.isst1.v2.globaldata.majorlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import cn.edu.zju.isst1.v2.db.CSTProvider;
import cn.edu.zju.isst1.v2.db.SimpleTableProvider;

/**
 * Created by always on 9/15/2014.
 */
public class CSTMajorProvider extends SimpleTableProvider {

    public static final String TABLE_NAME = "major";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + Columns.ID.key + " INTEGER, "
            + Columns.NAME.key + " VARCHAR(255), "
            + "UNIQUE (" + Columns.ID.key + ") ON CONFLICT REPLACE)";

    public static final Uri CONTENT_URI = CSTProvider.CONTENT_URI.buildUpon().appendPath(TABLE_NAME)
            .build();

    public CSTMajorProvider(Context context) {
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
        NAME("name");

        public String key;

        private Columns(String key) {
            this.key = key;
        }
    }
}

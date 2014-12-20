package cn.edu.zju.isst1.v2.event.campus.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import cn.edu.zju.isst1.v2.db.CSTProvider;
import cn.edu.zju.isst1.v2.db.SimpleTableProvider;

/**
 * Created by lqynydyxf on 2014/8/5.
 */
public class CSTCampusEventProvider extends SimpleTableProvider {

    public static final String TABLE_NAME = "campusevent";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + Columns.ID.key + " VARCHAR(255), "
            + Columns.TITLE.key + " VARCHAR(255), "
            + Columns.PICTURE.key + " VARCHAR(255), "
            + Columns.DESCRIPTION.key + " VARCHAR(255), "
            + Columns.CONTENT.key + " VARCHAR(32), "
            + Columns.PUBLISHER_NAME.key + " VARCHAR(255), "
            + Columns.UPDATEAT.key + " TEXT, "
            + Columns.STARTTIME.key + " TEXT, "
            + Columns.EXPIRETIME.key + " TEXT, "
            + "UNIQUE (" + Columns.ID.key + ") ON CONFLICT REPLACE)";

    public static final Uri CONTENT_URI = CSTProvider.CONTENT_URI.buildUpon().appendPath(TABLE_NAME)
            .build();

    public CSTCampusEventProvider(Context context) {
        super(context);
    }

//    public static CSTCampusEventProvider getInstance(Context context) {
//        if (INSTANCE == null) {
//            INSTANCE = new CSTCampusEventProvider(context);
//        }
//        return (CSTCampusEventProvider) INSTANCE;
//    }

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
        TITLE("title"),
        PICTURE("picture"),
        DESCRIPTION("description"),
        CONTENT("content"),
        PUBLISHER_NAME("publisherName"),
        UPDATEAT("updateAt"),
        STARTTIME("startTime"),
        EXPIRETIME("expireTime");

        public String key;

        private Columns(String key) {
            this.key = key;
        }
    }
}

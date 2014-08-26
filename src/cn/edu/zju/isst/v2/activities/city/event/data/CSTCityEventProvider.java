package cn.edu.zju.isst.v2.activities.city.event.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import cn.edu.zju.isst.v2.db.CSTProvider;
import cn.edu.zju.isst.v2.db.SimpleTableProvider;

/**
 * Created by lqynydyxf on 2014/8/7.
 */
public class CSTCityEventProvider extends SimpleTableProvider {

    public static final String TABLE_NAME = "cityevent";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + Columns.ID.key + " INTEGER, "
            + Columns.TITLE.key + " VARCHAR(255), "
            + Columns.IMGURL.key + " VARCHAR(255), "
            + Columns.CITYID.key + " INTEGER, "
            + Columns.LOCATION.key + " VARCHAR(255), "
            + Columns.STARTTIME.key + " INTEGER, "
            + Columns.EXPIRETIME.key + " INTEGER, "
            + Columns.UPDATEAT.key + " INTEGER, "
            + Columns.CONTENT.key + " VARCHAR(255), "
            + Columns.ISPARTICIPATE.key + " INTEGER, "
            + Columns.PUBLISHER.key + " BLOB, "
            + "UNIQUE (" + Columns.ID.key + ") ON CONFLICT REPLACE)";

    public static final Uri CONTENT_URI = CSTProvider.CONTENT_URI.buildUpon().appendPath(TABLE_NAME)
            .build();

    public CSTCityEventProvider(Context context) {
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
        TITLE("title"),
        IMGURL("imgUrl"),
        CITYID("cityId"),
        LOCATION("location"),
        STARTTIME("startTime"),
        EXPIRETIME("expireTime"),
        UPDATEAT("updateAt"),
        CONTENT("content"),
        ISPARTICIPATE("isParticipate"),
        PUBLISHER("publisher");

        public String key;

        private Columns(String key) {
            this.key = key;
        }
    }
}

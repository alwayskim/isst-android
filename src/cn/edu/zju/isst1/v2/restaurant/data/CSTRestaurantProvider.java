package cn.edu.zju.isst1.v2.restaurant.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import cn.edu.zju.isst1.v2.db.CSTProvider;
import cn.edu.zju.isst1.v2.db.SimpleTableProvider;

/**
 * Created by lqynydyxf on 2014/8/20.
 */
public class CSTRestaurantProvider extends SimpleTableProvider {

    public static final String TABLE_NAME = "restaurant";

    public enum Columns {
        ID("id"),
        NAME("name"),
        PICTURE("picture"),
        ADDRESS("address"),
        HOTLINE("hotline"),
        BUSINESS_HOURS("businessHours"),
        DESCRIPTION("description"),
        RESTAURANT_MENU("restaurantMenu");

        public String key;

        private Columns(String key) {
            this.key = key;
        }
    }

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + Columns.ID.key + " INTEGER, "
            + Columns.NAME.key + " VARCHAR(255), "
            + Columns.PICTURE.key + " VARCHAR(255), "
            + Columns.ADDRESS.key + " VARCHAR(255), "
            + Columns.HOTLINE.key + " VARCHAR(255), "
            + Columns.BUSINESS_HOURS.key + " VARCHAR(255), "
            + Columns.DESCRIPTION.key + " VARCHAR(255), "
            + Columns.RESTAURANT_MENU.key + " BLOB, "
            + "UNIQUE (" + Columns.ID.key + ") ON CONFLICT REPLACE)";

    public static final Uri CONTENT_URI = CSTProvider.CONTENT_URI.buildUpon().appendPath(TABLE_NAME)
            .build();

    public CSTRestaurantProvider(Context context) {
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
}

package cn.edu.zju.isst.v2.contact;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import cn.edu.zju.isst.v2.db.CSTProvider;
import cn.edu.zju.isst.v2.db.SimpleTableProvider;

/**
 * Created by always on 9/13/2014.
 */
public class CSTContactFilterProvider extends SimpleTableProvider {

    public static final String TABLE_NAME = "contactfilter";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + Columns.NAME.key + " VARCHAR(255), "
            + Columns.CITYID.key + " INTEGER, "
            + Columns.COMPANY.key + " VARCHAR(255), "
            + Columns.GENDER.key + " INTEGER, "
            + Columns.MAJOR.key + " VARCHAR(255), "
            + Columns.GENDERSTRING.key + " VARCHAR(255), "
            + Columns.CITYSTRING.key + " VARCHAR(255), "
            + Columns.FILTERSTRING.key + " VARCHAR(255), "
            + Columns.GRADE.key + " INTEGER, "
            + "UNIQUE (" + Columns.FILTERSTRING.key + ") ON CONFLICT REPLACE)";

    public static final Uri CONTENT_URI = CSTProvider.CONTENT_URI.buildUpon().appendPath(TABLE_NAME)
            .build();

    public CSTContactFilterProvider(Context context) {
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
        NAME("name"),
        GENDER("gender"),
        CITYID("cityId"),
        MAJOR("major"),
        GRADE("grade"),
        COMPANY("company"),
        CITYSTRING("citystring"),
        GENDERSTRING("genderstring"),
        FILTERSTRING("filterstring");

        public String key;

        private Columns(String key) {
            this.key = key;
        }
    }
}

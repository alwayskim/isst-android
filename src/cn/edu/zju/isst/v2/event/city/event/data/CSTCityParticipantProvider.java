package cn.edu.zju.isst.v2.event.city.event.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import cn.edu.zju.isst.v2.db.CSTProvider;
import cn.edu.zju.isst.v2.db.SimpleTableProvider;

/**
 * Created by lqynydyxf on 2014/8/8.
 */
public class CSTCityParticipantProvider extends SimpleTableProvider {

    public static final String TABLE_NAME = "cityparticipant";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + Columns.ID.key + " INTEGER, "
            + Columns.USER_NAME.key + " VARCHAR(255), "
            + Columns.NAME.key + " VARCHAR(255), "
            + Columns.GRADE.key + " INTEGER, "
            + Columns.GENDER.key + " VARCHAR(255), "
            + Columns.CLASS_ID.key + " INTEGER, "
            + Columns.CLASS_NAME.key + " VARCHAR(255), "
            + Columns.MAJOR.key + " VARCHAR(255), "
            + Columns.EMAIL.key + " VARCHAR(255), "
            + Columns.PHONE.key + " VARCHAR(255), "
            + Columns.QQ.key + " VARCHAR(255), "
            + Columns.COMPANY.key + " VARCHAR(255), "
            + Columns.JOB_TITLE.key + " VARCHAR(255), "
            + Columns.SIGN.key + " VARCHAR(255), "
            + Columns.CITY_ID.key + " INTEGER, "
            + Columns.CITY_NAME.key + " VARCHAR(255), "
            + "UNIQUE (" + Columns.ID.key + ") ON CONFLICT REPLACE)";

    public static final Uri CONTENT_URI = CSTProvider.CONTENT_URI.buildUpon().appendPath(TABLE_NAME)
            .build();

    public CSTCityParticipantProvider(Context context) {
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
        USER_NAME("user_name"),
        NAME("name"),
        GRADE("grade"),
        GENDER("gender"),
        CLASS_ID("class_id"),
        CLASS_NAME("class_name"),
        MAJOR("major"),
        EMAIL("email"),
        PHONE("phone"),
        QQ("qq"),
        COMPANY("company"),
        JOB_TITLE("job_title"),
        SIGN("sign"),
        CITY_ID("cityId"),
        CITY_NAME("city_name");

        public String key;

        private Columns(String key) {
            this.key = key;
        }
    }
}

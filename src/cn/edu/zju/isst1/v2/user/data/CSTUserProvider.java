package cn.edu.zju.isst1.v2.user.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import cn.edu.zju.isst1.v2.db.CSTProvider;
import cn.edu.zju.isst1.v2.db.SimpleTableProvider;

/**
 * Created by i308844 on 7/29/14.
 */
public class CSTUserProvider extends SimpleTableProvider {

    public static final String TABLE_NAME = "user";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + Columns.ID.key + " VARCHAR(255), "
            + Columns.USER_NAME.key + " VARCHAR(255), "
            + Columns.PASSWORD.key + " VARCHAR(255), "
            + Columns.NAME.key + " VARCHAR(255), "
            + Columns.GENDER.key + " VARCHAR(32), "
            + Columns.GRADE.key + " INTEGER, "
            + Columns.CLASS_ID.key + " INTEGER, "
            + Columns.CLASS_NAME.key + " VARCHAR(255), "
            + Columns.MAJOR.key + " VARCHAR(255), "
            + Columns.CITY_ID.key + " INTEGER, "
            + Columns.CITY_NAME.key + " VARCHAR(255), "
            + Columns.EMAIL.key + " VARCHAR(255), "
            + Columns.PHONE.key + " VARCHAR(255), "
            + Columns.QQ.key + " VARCHAR(255), "
            + Columns.COMPANY.key + " VARCHAR(255), "
            + Columns.JOB_TITLE.key + " VARCHAR(255), "
            + Columns.SIGN.key + " VARCHAR(255), "
            + Columns.IS_CASTELLAN.key + " INTEGER, "
            + Columns.PVT_QQ.key + " INTEGER, "
            + Columns.PVT_EMAIL.key + " INTEGER, "
            + Columns.PVT_PHONE.key + " INTEGER, "
            + Columns.PVT_COMPANY.key + " INTEGER, "
            + Columns.PVT_JOB_TITLE.key + " INTEGER, "
            + "UNIQUE (" + Columns.ID.key + ") ON CONFLICT REPLACE)";

    public static final Uri CONTENT_URI = CSTProvider.CONTENT_URI.buildUpon().appendPath(TABLE_NAME)
            .build();

    public CSTUserProvider(Context context) {
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
        PASSWORD("password"),
        NAME("name"),
        GENDER("gender"),
        GRADE("grade"),
        CLASS_ID("class_id"),
        CLASS_NAME("class_name"),
        MAJOR("major"),
        CITY_ID("city_id"),
        CITY_NAME("city_name"),
        EMAIL("email"),
        PHONE("phone"),
        QQ("qq"),
        COMPANY("company"),
        JOB_TITLE("job_title"),
        SIGN("sign"),
        IS_CASTELLAN("is_castellan"),
        PVT_QQ("pvt_qq"),
        PVT_EMAIL("pvt_email"),
        PVT_PHONE("pvt_phone"),
        PVT_COMPANY("pvt_company"),
        PVT_JOB_TITLE("pvt_job_title");

        public String key;

        private Columns(String key) {
            this.key = key;
        }
    }
}

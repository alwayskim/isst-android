package cn.edu.zju.isst1.v2.contact.contact.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import cn.edu.zju.isst1.v2.db.CSTProvider;
import cn.edu.zju.isst1.v2.db.SimpleTableProvider;

/**
 * Created by alwayking on 14/11/28.
 */
public class CSTAddressListProvider extends SimpleTableProvider {
    public static final String TABLE_NAME = "adresslist";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + Columns.ID.key + " VARCHAR(255), "
            + Columns.USERNAME.key + " VARCHAR(255), "
            + Columns.NAME.key + " VARCHAR(255), "
            + Columns.GRADE.key + " INTEGER, "
            + Columns.GENDER.key + " BLOB, "
            + Columns.CLAZZID.key + " INTEGER, "
            + Columns.CLAZZNAME.key + " VARCHAR(255), "
            + Columns.MAJORNAME.key + " VARCHAR(255), "
            + Columns.EMAIL.key + " VARCHAR(255), "
            + Columns.PHONENUM.key + " VARCHAR(255), "
            + Columns.QQNUM.key + " VARCHAR(255), "
            + Columns.COMPANY.key + " VARCHAR(255), "
            + Columns.JOBTITLE.key + " VARCHAR(255), "
            + Columns.SIGN.key + " VARCHAR(255), "
            + Columns.CITYID.key + " INTEGER, "
            + Columns.CITYNAME.key + " VARCHAR(255), "
            + "UNIQUE (" + Columns.ID.key + ") ON CONFLICT REPLACE)";

    public static final Uri CONTENT_URI = CSTProvider.CONTENT_URI.buildUpon().appendPath(TABLE_NAME)
            .build();

    public CSTAddressListProvider(Context context) {
        super(context);
    }

    public static CSTAlumniProvider getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CSTAlumniProvider(context);
        }
        return (CSTAlumniProvider) INSTANCE;
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
        USERNAME("username"),
        NAME("name"),
        GRADE("grade"),
        GENDER("gender"),
        CLAZZID("clazzId"),
        CLAZZNAME("clazzName"),
        MAJORNAME("majorName"),
        EMAIL("email"),
        PHONENUM("phoneNum"),
        QQNUM("qqNum"),
        COMPANY("company"),
        JOBTITLE("jobTitle"),
        SIGN("sign"),
        CITYID("cityId"),
        CITYNAME("cityName");

        public String key;

        private Columns(String key) {
            this.key = key;
        }
    }
}

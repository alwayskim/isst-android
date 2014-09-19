package cn.edu.zju.isst.v2.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.isst.v2.contact.CSTContactFilterProvider;
import cn.edu.zju.isst.v2.contact.contact.data.CSTAlumniProvider;
import cn.edu.zju.isst.v2.globaldata.citylist.CSTCityProvider;
import cn.edu.zju.isst.v2.event.city.event.data.CSTCityEventProvider;
import cn.edu.zju.isst.v2.archive.data.CSTArchiveProvider;
import cn.edu.zju.isst.v2.event.campus.data.CSTCampusEventProvider;
import cn.edu.zju.isst.v2.globaldata.classlist.CSTKlassProvider;
import cn.edu.zju.isst.v2.globaldata.majorlist.CSTMajorProvider;
import cn.edu.zju.isst.v2.user.data.CSTUserProvider;

/**
 * Created by i308844 on 7/28/14.
 */
public class CSTProvider extends ContentProvider {

    private static final int TABLE_USER_CODE = 1;

    private static final int TABLE_ARCHIVE_CODE = 0x02;

    private static final int TABLE_CAMPUSEvent_CODE = 0x03;
//
    private static final int TABLE_CITY_CODE = 4;

//
//    private static final int TABLE_PUBLISHER_CODE = 4;
//
    private static final int TABLE_CITYEVENT_CODE = 5;
//
//    private static final int TABLE_CITYPARTICIPANT_CODE = 6;
//
//    private static final int TABLE_COMMENT_CODE = 7;



    private static final int TABLE_CONTACTFilter_CODE = 8;

    private static final int TABLE_MAJORList_CODE = 9;

    private static final int TABLE_KLASSList_CODE = 10;

    private static final int TABLE_ALUMNI_CODE = 11;



    private static final String AUTHORITY = "cn.edu.zju.isst.v2.db.cstprovider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final String DATABASE_NAME = "isst_database.db";

    private static final int VER_03_ALPHA = 1;

    private static final int DB_VERSION = VER_03_ALPHA;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, CSTUserProvider.TABLE_NAME, TABLE_USER_CODE);
        sURIMatcher.addURI(AUTHORITY, CSTArchiveProvider.TABLE_NAME, TABLE_ARCHIVE_CODE);
        sURIMatcher.addURI(AUTHORITY, CSTCampusEventProvider.TABLE_NAME, TABLE_CAMPUSEvent_CODE);
        sURIMatcher.addURI(AUTHORITY, CSTCityProvider.TABLE_NAME, TABLE_CITY_CODE);
        sURIMatcher.addURI(AUTHORITY, CSTKlassProvider.TABLE_NAME, TABLE_KLASSList_CODE);
        sURIMatcher.addURI(AUTHORITY, CSTMajorProvider.TABLE_NAME, TABLE_MAJORList_CODE);
        sURIMatcher.addURI(AUTHORITY, CSTAlumniProvider.TABLE_NAME, TABLE_ALUMNI_CODE);
//        sURIMatcher.addURI(AUTHORITY, CSTPublisherProvider.TABLE_NAME, TABLE_PUBLISHER_CODE);
        sURIMatcher.addURI(AUTHORITY, CSTCityEventProvider.TABLE_NAME, TABLE_CITYEVENT_CODE);
//        sURIMatcher.addURI(AUTHORITY, CSTCityParticipantProvider.TABLE_NAME,
//                TABLE_CITYPARTICIPANT_CODE);
//        sURIMatcher.addURI(AUTHORITY, CSTCommentProvider.TABLE_NAME, TABLE_COMMENT_CODE);
        sURIMatcher.addURI(AUTHORITY, CSTContactFilterProvider.TABLE_NAME, TABLE_CONTACTFilter_CODE);
    }

    private Map<String, Provider> mProviderMap = new HashMap<>();

    private DatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        mProviderMap
                .put(CSTUserProvider.TABLE_NAME, new CSTUserProvider(getContext()));
        mProviderMap
                .put(CSTArchiveProvider.TABLE_NAME, CSTArchiveProvider.getInstance(getContext()));
        mProviderMap
                .put(CSTCampusEventProvider.TABLE_NAME, new CSTCampusEventProvider(getContext()));
        mProviderMap
                .put(CSTCityProvider.TABLE_NAME, new CSTCityProvider(getContext()));
        mProviderMap
                .put(CSTMajorProvider.TABLE_NAME, new CSTMajorProvider(getContext()));
        mProviderMap
                .put(CSTKlassProvider.TABLE_NAME, new CSTKlassProvider(getContext()));
//        mProviderMap.put(CSTPublisherProvider.TABLE_NAME, new CSTPublisherProvider(getContext()));
        mProviderMap
                .put(CSTCityEventProvider.TABLE_NAME, new CSTCityEventProvider(getContext()));
        mProviderMap
                .put(CSTAlumniProvider.TABLE_NAME, new CSTAlumniProvider(getContext()));
//        mProviderMap.put(CSTCityParticipantProvider.TABLE_NAME,
//                new CSTCityParticipantProvider(getContext()));
//        mProviderMap.put(CSTCommentProvider.TABLE_NAME, new CSTCommentProvider(getContext()));
        mProviderMap
                .put(CSTContactFilterProvider.TABLE_NAME, new CSTContactFilterProvider(getContext()));

        SQLiteDatabase writableDatabase = mDatabaseHelper.getWritableDatabase();
        for (Provider provider : mProviderMap.values()) {
            provider.setDBRef(writableDatabase);
        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        return getProvider(uri).query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;//not used for now
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return getProvider(uri).insert(uri, values);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        return getProvider(uri).bulkInsert(uri, values);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return getProvider(uri).delete(uri, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return getProvider(uri).update(uri, values, selection, selectionArgs);
    }

    private Provider getProvider(Uri uri) {
        Provider provider = mProviderMap.get(getTableName(uri));
        if (provider == null) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return provider;
    }

    public String getTableName(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case TABLE_USER_CODE:
                return CSTUserProvider.TABLE_NAME;
            case TABLE_ARCHIVE_CODE:
                return CSTArchiveProvider.TABLE_NAME;
            case TABLE_CAMPUSEvent_CODE:
                return CSTCampusEventProvider.TABLE_NAME;
            case TABLE_CITY_CODE:
                return CSTCityProvider.TABLE_NAME;
            case TABLE_KLASSList_CODE:
                return CSTKlassProvider.TABLE_NAME;
            case TABLE_MAJORList_CODE:
                return CSTMajorProvider.TABLE_NAME;
//            case TABLE_PUBLISHER_CODE:
//                return CSTPublisherProvider.TABLE_NAME;
            case TABLE_CITYEVENT_CODE:
                return CSTCityEventProvider.TABLE_NAME;
            case TABLE_ALUMNI_CODE:
                return CSTAlumniProvider.TABLE_NAME;
//            case TABLE_CITYPARTICIPANT_CODE:
//                return CSTCityParticipantProvider.TABLE_NAME;
//            case TABLE_COMMENT_CODE:
//                return CSTCommentProvider.TABLE_NAME;
            case TABLE_CONTACTFilter_CODE:
                return CSTContactFilterProvider.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        private DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for (Provider provider : mProviderMap.values()) {
                provider.onCreate(db);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            int version = oldVersion;

            switch (oldVersion) {

            }

            if (version != DB_VERSION) {
                for (Provider provider : mProviderMap.values()) {
                    provider.resetContents(db);
                }
            }
        }
    }
}

package cn.edu.zju.isst1.v2.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by i308844 on 7/28/14.
 */
public interface Provider {

    String getTableName();

    void onCreate(SQLiteDatabase db);

    void resetContents(SQLiteDatabase db);

    void setDBRef(SQLiteDatabase database);

    Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder);

    Uri insert(Uri uri, ContentValues values);

    int update(Uri uri, ContentValues values, String where, String[] whereArgs);

    int delete(Uri uri, String selection, String[] selectionArgs);

    int bulkInsert(Uri uri, ContentValues[] valuesArray);
}

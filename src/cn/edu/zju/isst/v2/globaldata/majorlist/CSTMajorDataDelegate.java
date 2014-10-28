package cn.edu.zju.isst.v2.globaldata.majorlist;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst.v2.data.CSTMajor;

/**
 * Created by always on 9/15/2014.
 */
public class CSTMajorDataDelegate {
    public static CSTMajor getMajor(Cursor cursor) {
        CSTMajor cstMajor = new CSTMajor();
        cstMajor.id = cursor.getInt(cursor.getColumnIndex(CSTMajorProvider.Columns.ID.key));
        cstMajor.name = cursor.getString(cursor.getColumnIndex(CSTMajorProvider.Columns.NAME.key));
        return cstMajor;
    }

    public static CSTMajor getMajor(Context context, String id) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver
                .query(CSTMajorProvider.CONTENT_URI, null, CSTMajorProvider.Columns.ID.key + " = ?",
                        new String[]{
                                id
                        }, null
                );
        if (cursor != null && cursor.moveToFirst()) {
            try {
                return getMajor(cursor);
            } finally {
                cursor.close();
            }
        }
        return null;//Should throw exception to avoid null pointer?
    }

    public static void saveMajor(Context context, CSTMajor cstMajor) {
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(CSTMajorProvider.CONTENT_URI, getMajorValue(cstMajor));
    }

    public static void deleteAllMajor(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(CSTMajorProvider.CONTENT_URI, null, null);
    }

    private static ContentValues getMajorValue(CSTMajor cstMajor) {
        ContentValues values = new ContentValues();
        values.put(CSTMajorProvider.Columns.ID.key, cstMajor.id);
        values.put(CSTMajorProvider.Columns.NAME.key, cstMajor.name);
        return values;
    }

    private static ContentValues[] getMajorListValues(CSTMajor cstMajor) {
        List<ContentValues> valuesList = new ArrayList<ContentValues>();
        for (CSTMajor singleMajor : cstMajor.itemList) {
            valuesList.add(getMajorValue(singleMajor));
        }
        return valuesList.toArray(new ContentValues[valuesList.size()]);
    }

    public static void saveMajorList(Context context, CSTMajor cstMajor) {
        ContentResolver resolver = context.getContentResolver();
        resolver.bulkInsert(CSTMajorProvider.CONTENT_URI, getMajorListValues(
                cstMajor));
    }

    public static List<CSTMajor> getMajorList(Context context) {
        Cursor cursor = context.getContentResolver().query(CSTMajorProvider.CONTENT_URI, null,
                null, null, null);
        List<CSTMajor> majorList = new ArrayList<CSTMajor>();
        while (cursor.moveToNext()){
            CSTMajor major = CSTMajorDataDelegate.getMajor(cursor);
            majorList.add(major);
        }
        return majorList;
    }
}

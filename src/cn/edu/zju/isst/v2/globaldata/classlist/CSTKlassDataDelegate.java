package cn.edu.zju.isst.v2.globaldata.classlist;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst.v2.data.CSTKlass;

/**
 * Created by always on 9/15/2014.
 */
public class CSTKlassDataDelegate {

    public static CSTKlass getKlass(Cursor cursor) {
        CSTKlass cstKlass = new CSTKlass();
        cstKlass.id = cursor.getInt(cursor.getColumnIndex(CSTKlassProvider.Columns.ID.key));
        cstKlass.name = cursor.getString(cursor.getColumnIndex(CSTKlassProvider.Columns.NAME.key));
        return cstKlass;
    }

    public static CSTKlass getKlass(Context context, String id) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver
                .query(CSTKlassProvider.CONTENT_URI, null, CSTKlassProvider.Columns.ID.key + " = ?",
                        new String[]{
                                id
                        }, null
                );
        if (cursor != null && cursor.moveToFirst()) {
            try {
                return getKlass(cursor);
            } finally {
                cursor.close();
            }
        }
        return null;//Should throw exception to avoid null pointer?
    }

    public static void saveKlass(Context context, CSTKlass city) {
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(CSTKlassProvider.CONTENT_URI, getKlassValue(city));
    }

    public static void deleteAllKlass(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(CSTKlassProvider.CONTENT_URI, null, null);
    }

    private static ContentValues getKlassValue(CSTKlass city) {
        ContentValues values = new ContentValues();
        values.put(CSTKlassProvider.Columns.ID.key, city.id);
        values.put(CSTKlassProvider.Columns.NAME.key, city.name);
        return values;
    }

    private static ContentValues[] getKlassListValues(CSTKlass cstKlass) {
        List<ContentValues> valuesList = new ArrayList<>();
        for (CSTKlass singleKlass : cstKlass.itemList) {
            valuesList.add(getKlassValue(singleKlass));
        }
        return valuesList.toArray(new ContentValues[valuesList.size()]);
    }

    public static void saveKlassList(Context context, CSTKlass cstKlass) {
        ContentResolver resolver = context.getContentResolver();
        resolver.bulkInsert(CSTKlassProvider.CONTENT_URI, getKlassListValues(
                cstKlass));
    }

    public static List<CSTKlass> getKlassList(Context context) {
        Cursor cursor = context.getContentResolver().query(CSTKlassProvider.CONTENT_URI, null,
                null, null, null);
        List<CSTKlass> klassList = new ArrayList<CSTKlass>();
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            CSTKlass cstKlass = CSTKlassDataDelegate.getKlass(cursor);
            klassList.add(cstKlass);
        }
        return klassList;
    }
}

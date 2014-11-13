package cn.edu.zju.isst1.v2.event.campus.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lqynydyxf on 2014/8/5.
 */
public class CSTCampusEventDataDelegate {

    public static CSTCampusEvent getCampusEvent(Cursor cursor) {
        CSTCampusEvent campusevent = new CSTCampusEvent();

        campusevent.id = cursor
                .getInt(cursor.getColumnIndex(CSTCampusEventProvider.Columns.ID.key));
        campusevent.title = cursor
                .getString(cursor.getColumnIndex(CSTCampusEventProvider.Columns.TITLE.key));
        campusevent.picture = cursor
                .getString(cursor.getColumnIndex(CSTCampusEventProvider.Columns.PICTURE.key));
        campusevent.description = cursor
                .getString(cursor.getColumnIndex(CSTCampusEventProvider.Columns.DESCRIPTION.key));
        campusevent.content = cursor
                .getString(cursor.getColumnIndex(CSTCampusEventProvider.Columns.CONTENT.key));
        campusevent.pubName = cursor.getString(
                cursor.getColumnIndex(CSTCampusEventProvider.Columns.PUBLISHER_NAME.key));
        campusevent.updatedAt = cursor
                .getInt(cursor.getColumnIndex(CSTCampusEventProvider.Columns.UPDATEAT.key));
        campusevent.startTime = cursor
                .getInt(cursor.getColumnIndex(CSTCampusEventProvider.Columns.STARTTIME.key));
        campusevent.expireTime = cursor
                .getInt(cursor.getColumnIndex(CSTCampusEventProvider.Columns.EXPIRETIME.key));
        return campusevent;
    }

    public static Loader<Cursor> getDataCursor(Context context, String[] projection,
            String selection, String[] selectionArgs, String sortOrder) {
        return new CursorLoader(context, CSTCampusEventProvider.CONTENT_URI, projection, selection,
                selectionArgs, sortOrder);
    }

    public static CSTCampusEvent getCampusEvent(Context context, String id) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver
                .query(CSTCampusEventProvider.CONTENT_URI, null,
                        CSTCampusEventProvider.Columns.ID.key + " = ?",
                        new String[]{
                                id
                        }, null
                );
        if (cursor != null && cursor.moveToFirst()) {
            try {
                return getCampusEvent(cursor);
            } finally {
                cursor.close();
            }
        }
        return null;//Should throw exception to avoid null pointer?
    }

    public static void saveCampusEvent(Context context, CSTCampusEvent campusevent) {
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(CSTCampusEventProvider.CONTENT_URI, getCampusEventValue(campusevent));
    }

    public static void deleteAllCampusEvent(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(CSTCampusEventProvider.CONTENT_URI, null, null);
    }

    private static ContentValues getCampusEventValue(CSTCampusEvent campusevent) {
        ContentValues values = new ContentValues();
        values.put(CSTCampusEventProvider.Columns.ID.key, campusevent.id);
        values.put(CSTCampusEventProvider.Columns.TITLE.key, campusevent.title);
        values.put(CSTCampusEventProvider.Columns.PICTURE.key, campusevent.picture);
        values.put(CSTCampusEventProvider.Columns.DESCRIPTION.key, campusevent.description);
        values.put(CSTCampusEventProvider.Columns.CONTENT.key, campusevent.content);
        values.put(CSTCampusEventProvider.Columns.PUBLISHER_NAME.key, campusevent.pubName);
        values.put(CSTCampusEventProvider.Columns.UPDATEAT.key, campusevent.updatedAt);
        values.put(CSTCampusEventProvider.Columns.STARTTIME.key, campusevent.startTime);
        values.put(CSTCampusEventProvider.Columns.EXPIRETIME.key, campusevent.expireTime);
        return values;
    }


    private static ContentValues[] getCampusEventListValues(CSTCampusEvent campusEvent) {
        List<ContentValues> valuesList = new ArrayList<ContentValues>();
        for (CSTCampusEvent singleCampusEvent : campusEvent.itemList) {
            valuesList.add(getCampusEventValue(singleCampusEvent));
        }
        return valuesList.toArray(new ContentValues[valuesList.size()]);
    }

    public static void saveCampusEventList(Context context, CSTCampusEvent campusEvent) {
        ContentResolver resolver = context.getContentResolver();
        resolver.bulkInsert(CSTCampusEventProvider.CONTENT_URI, getCampusEventListValues(
                campusEvent));
    }
}

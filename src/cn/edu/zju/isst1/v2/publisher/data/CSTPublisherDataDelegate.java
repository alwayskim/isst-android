package cn.edu.zju.isst1.v2.publisher.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import cn.edu.zju.isst1.v2.data.CSTPublisher;

/**
 * Created by lqynydyxf on 2014/8/7.
 */
public class CSTPublisherDataDelegate {

    public static CSTPublisher getPublisher(Cursor cursor) {
        CSTPublisher publisher = new CSTPublisher();
        publisher.id = cursor.getInt(cursor.getColumnIndex(CSTPublisherProvider.Columns.ID.key));
        publisher.name = cursor
                .getString(cursor.getColumnIndex(CSTPublisherProvider.Columns.NAME.key));
        publisher.qqNum = cursor
                .getString(cursor.getColumnIndex(CSTPublisherProvider.Columns.QQ.key));
        publisher.phoneNum = cursor
                .getString(cursor.getColumnIndex(CSTPublisherProvider.Columns.PHONE.key));
        publisher.email = cursor
                .getString(cursor.getColumnIndex(CSTPublisherProvider.Columns.EMAIL.key));
        return publisher;
    }

    public static CSTPublisher getPublisher(Context context, String id) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver
                .query(CSTPublisherProvider.CONTENT_URI, null,
                        CSTPublisherProvider.Columns.ID.key + " = ?",
                        new String[]{
                                id
                        }, null
                );
        if (cursor != null && cursor.moveToFirst()) {
            try {
                return getPublisher(cursor);
            } finally {
                cursor.close();
            }
        }
        return null;//Should throw exception to avoid null pointer?
    }

    public static void savePublisher(Context context, CSTPublisher publisher) {
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(CSTPublisherProvider.CONTENT_URI, getPublisherValue(publisher));
    }

    public static void deleteAllPublisher(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(CSTPublisherProvider.CONTENT_URI, null, null);
    }

    private static ContentValues getPublisherValue(CSTPublisher publisher) {
        ContentValues values = new ContentValues();
        values.put(CSTPublisherProvider.Columns.ID.key, publisher.id);
        values.put(CSTPublisherProvider.Columns.NAME.key, publisher.name);
        values.put(CSTPublisherProvider.Columns.PHONE.key, publisher.phoneNum);
        values.put(CSTPublisherProvider.Columns.QQ.key, publisher.qqNum);
        values.put(CSTPublisherProvider.Columns.EMAIL.key, publisher.email);
        return values;
    }
}

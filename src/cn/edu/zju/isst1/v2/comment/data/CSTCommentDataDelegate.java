package cn.edu.zju.isst1.v2.comment.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import cn.edu.zju.isst1.v2.db.util.CSTSerialUtil;
import cn.edu.zju.isst1.v2.user.data.CSTUser;

/**
 * Created by lqynydyxf on 2014/8/10.
 */
public class CSTCommentDataDelegate {

    public static CSTComment getComment(Cursor cursor) {
        CSTComment comment = new CSTComment();
        comment.id = cursor.getInt(cursor.getColumnIndex(CSTCommentProvider.Columns.ID.key));
        comment.content = cursor
                .getString(cursor.getColumnIndex(CSTCommentProvider.Columns.CONTENT.key));
        comment.user = (CSTUser) CSTSerialUtil.deserialize(
                cursor.getBlob(cursor.getColumnIndex(CSTCommentProvider.Columns.CSTUSER.key)));
        return comment;
    }

    public static CSTComment getCommet(Context context, String id) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver
                .query(CSTCommentProvider.CONTENT_URI, null,
                        CSTCommentProvider.Columns.ID.key + " = ?",
                        new String[]{
                                id
                        }, null
                );
        if (cursor != null && cursor.moveToFirst()) {
            try {
                return getComment(cursor);
            } finally {
                cursor.close();
            }
        }
        return null;//Should throw exception to avoid null pointer?
    }

    public static void saveComment(Context context, CSTComment comment) {
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(CSTCommentProvider.CONTENT_URI, getCommentValue(comment));
    }

    public static void deleteAllComment(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(CSTCommentProvider.CONTENT_URI, null, null);
    }

    private static ContentValues getCommentValue(CSTComment comment) {
        ContentValues values = new ContentValues();
        values.put(CSTCommentProvider.Columns.ID.key, comment.id);
        values.put(CSTCommentProvider.Columns.CONTENT.key, comment.content);
        values.put(CSTCommentProvider.Columns.CREATED_AT.key, comment.createdAt);
        values.put(CSTCommentProvider.Columns.CSTUSER.key, CSTSerialUtil.serialize(comment.user));
        return values;
    }
}

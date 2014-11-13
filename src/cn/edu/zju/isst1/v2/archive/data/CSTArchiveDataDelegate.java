package cn.edu.zju.isst1.v2.archive.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst1.v2.db.util.CSTSerialUtil;

/**
 * Created by i308844 on 8/12/14.
 */
public class CSTArchiveDataDelegate {

    public static CSTArchive getArchive(Cursor cursor) {
        CSTArchive archive = new CSTArchive();

        archive.id = cursor.getInt(cursor.getColumnIndex(CSTArchiveProvider.Columns.ID.key));
        archive.title = cursor
                .getString(cursor.getColumnIndex(CSTArchiveProvider.Columns.TITLE.key));
        archive.categoryId = cursor
                .getInt(cursor.getColumnIndex(CSTArchiveProvider.Columns.CATEGORY_ID.key));
        archive.description = cursor
                .getString(cursor.getColumnIndex(CSTArchiveProvider.Columns.DESCRIPTION.key));
        archive.updateTime = cursor
                .getLong(cursor.getColumnIndex(CSTArchiveProvider.Columns.UPDATE_TIME.key));
        archive.publisherId = cursor
                .getInt(cursor.getColumnIndex(CSTArchiveProvider.Columns.PUBLISHER_ID.key));
        archive.publisher = (cn.edu.zju.isst1.v2.user.data.CSTUser) CSTSerialUtil.deserialize(
                cursor.getBlob(cursor.getColumnIndex(CSTArchiveProvider.Columns.PUBLISHER.key)));
        archive.content = cursor
                .getString(cursor.getColumnIndex(CSTArchiveProvider.Columns.CONTENT.key));

        return archive;
    }

    public static void saveArchiveList(Context context, CSTArchive archive) {
        ContentResolver resolver = context.getContentResolver();
        resolver.bulkInsert(CSTArchiveProvider.CONTENT_URI, getArchiveListValues(archive));
    }

    public static void delete(Context context, String selection, String[] selectionArgs) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(CSTArchiveProvider.CONTENT_URI, selection, selectionArgs);
    }

    public static Loader<Cursor> getDataCursor(Context context, String[] projection,
            String selection, String[] selectionArgs, String sortOrder) {
        return new CursorLoader(context, CSTArchiveProvider.CONTENT_URI, projection, selection,
                selectionArgs, sortOrder);
    }

    private static ContentValues[] getArchiveListValues(CSTArchive archive) {
        List<ContentValues> valuesList = new ArrayList<ContentValues>();
        for (CSTArchive singleArchive : archive.itemList) {
            valuesList.add(getArchiveValue(singleArchive));
        }
        return valuesList.toArray(new ContentValues[valuesList.size()]);
    }

    private static ContentValues getArchiveValue(CSTArchive archive) {
        ContentValues values = new ContentValues();

        values.put(CSTArchiveProvider.Columns.ID.key, archive.id);
        values.put(CSTArchiveProvider.Columns.TITLE.key, archive.title);
        values.put(CSTArchiveProvider.Columns.CATEGORY_ID.key, archive.categoryId);
        values.put(CSTArchiveProvider.Columns.DESCRIPTION.key, archive.description);
        values.put(CSTArchiveProvider.Columns.UPDATE_TIME.key, archive.updateTime);
        values.put(CSTArchiveProvider.Columns.PUBLISHER_ID.key, archive.publisherId);
        values.put(CSTArchiveProvider.Columns.PUBLISHER.key,
                CSTSerialUtil.serialize(archive.publisher));
        values.put(CSTArchiveProvider.Columns.CONTENT.key, archive.content);

        return values;
    }
}

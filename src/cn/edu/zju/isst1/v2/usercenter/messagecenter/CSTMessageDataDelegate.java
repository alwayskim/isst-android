package cn.edu.zju.isst1.v2.usercenter.messagecenter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst1.v2.contact.contact.data.CSTAddressListProvider;
import cn.edu.zju.isst1.v2.contact.contact.data.Pinyin4j;
import cn.edu.zju.isst1.v2.data.BasicUser;
import cn.edu.zju.isst1.v2.model.CSTDataItem;

/**
 * Created by alwayking on 14/11/30.
 */
public class CSTMessageDataDelegate {

    public static CSTMessage getMessage(Cursor cursor) {
        CSTMessage cstMessage = new CSTMessage();
        cstMessage.id = cursor.getInt(cursor.getColumnIndex(CSTMessageProvider.Columns.ID.key));
        cstMessage.content = cursor
                .getString(cursor.getColumnIndex(CSTMessageProvider.Columns.CONTENT.key));
        cstMessage.createdAt = cursor
                .getString(cursor.getColumnIndex(CSTMessageProvider.Columns.CREATEDAT.key));
        cstMessage.title = cursor
                .getString(cursor.getColumnIndex(CSTMessageProvider.Columns.TITLE.key));
        return cstMessage;
    }

    public static void deleteAllMessage(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.delete(CSTMessageProvider.CONTENT_URI, null, null);
    }

    public static CSTMessage getAllMessage(Context context) {
        CSTMessage cstMessage = new CSTMessage();
        Cursor cursor = context.getContentResolver().query(CSTMessageProvider.CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        do {
            cstMessage.itemList.add(getMessage(cursor));
        } while (cursor.moveToNext());
        cursor.close();
        return cstMessage;
    }

    public static void saveMessage(Context context, CSTMessage message) {
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(CSTMessageProvider.CONTENT_URI, getMessageValue(message));
    }

    public static void saveMessageList(Context context, CSTMessage message) {
        ContentResolver resolver = context.getContentResolver();
        resolver.bulkInsert(CSTMessageProvider.CONTENT_URI, getMessageListValues(message));
    }

    public static Loader<Cursor> getDataCursor(Context context, String[] projection,
                                               String selection, String[] selectionArgs, String sortOrder) {
        return new CursorLoader(context, CSTAddressListProvider.CONTENT_URI, projection, selection,
                selectionArgs, sortOrder);
    }

    private static ContentValues[] getMessageListValues(CSTMessage message) {
        List<ContentValues> valuesList = new ArrayList<ContentValues>();
        for (CSTMessage singleMessage : message.itemList) {
            valuesList.add(getMessageValue(singleMessage));
        }
        return valuesList.toArray(new ContentValues[valuesList.size()]);
    }

    private static ContentValues getMessageValue(CSTMessage message) {
        ContentValues values = new ContentValues();
        values.put(CSTMessageProvider.Columns.ID.key, message.id);
        values.put(CSTMessageProvider.Columns.TITLE.key, message.title);
        values.put(CSTMessageProvider.Columns.CREATEDAT.key, message.createdAt);
        values.put(CSTMessageProvider.Columns.CONTENT.key, message.content);
        return values;
    }

}

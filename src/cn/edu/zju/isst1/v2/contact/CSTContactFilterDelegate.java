package cn.edu.zju.isst1.v2.contact;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

import cn.edu.zju.isst1.ui.contact.ContactFilter;

/**
 * Created by always on 9/13/2014.
 */
public class CSTContactFilterDelegate {

    public static ContactFilter getFilter(Cursor cursor) {
        ContactFilter contactFilter = new ContactFilter();

        contactFilter.name = cursor
                .getString(cursor.getColumnIndex(CSTContactFilterProvider.Columns.NAME.key));
        contactFilter.company = cursor
                .getString(cursor.getColumnIndex(CSTContactFilterProvider.Columns.COMPANY.key));
        contactFilter.cityId = cursor
                .getInt(cursor.getColumnIndex(CSTContactFilterProvider.Columns.CITYID.key));
        contactFilter.grade = cursor
                .getInt(cursor.getColumnIndex(CSTContactFilterProvider.Columns.GRADE.key));
        contactFilter.gender = cursor
                .getInt(cursor.getColumnIndex(CSTContactFilterProvider.Columns.GENDER.key));
        contactFilter.major = cursor
                .getString(cursor.getColumnIndex(CSTContactFilterProvider.Columns.MAJOR.key));
        contactFilter.genderString = cursor
                .getString(
                        cursor.getColumnIndex(CSTContactFilterProvider.Columns.GENDERSTRING.key));
        contactFilter.cityString = cursor
                .getString(cursor.getColumnIndex(CSTContactFilterProvider.Columns.CITYSTRING.key));
        contactFilter.filterString = cursor
                .getString(
                        cursor.getColumnIndex(CSTContactFilterProvider.Columns.FILTERSTRING.key));

        return contactFilter;
    }


    public static void saveFilter(Context context, ContactFilter contactFilter) {
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(CSTContactFilterProvider.CONTENT_URI, getFilterValue(contactFilter));
    }

    public static void deleteAllFilter(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(CSTContactFilterProvider.CONTENT_URI, null, null);
    }


    private static ContentValues getFilterValue(ContactFilter contactFilter) {
        ContentValues values = new ContentValues();
        values.put(CSTContactFilterProvider.Columns.NAME.key, contactFilter.name);
        values.put(CSTContactFilterProvider.Columns.GENDER.key, contactFilter.gender);
        values.put(CSTContactFilterProvider.Columns.GRADE.key, contactFilter.grade);
        values.put(CSTContactFilterProvider.Columns.CITYID.key, contactFilter.cityId);
        values.put(CSTContactFilterProvider.Columns.MAJOR.key, contactFilter.major);
        values.put(CSTContactFilterProvider.Columns.COMPANY.key, contactFilter.company);
        values.put(CSTContactFilterProvider.Columns.CITYSTRING.key, contactFilter.cityString);
        values.put(CSTContactFilterProvider.Columns.GENDERSTRING.key, contactFilter.genderString);
        values.put(CSTContactFilterProvider.Columns.FILTERSTRING.key, contactFilter.filterString);
        return values;
    }

    public static Loader<Cursor> getDataCursor(Context context, String[] projection,
            String selection, String[] selectionArgs, String sortOrder) {
        return new CursorLoader(context, CSTContactFilterProvider.CONTENT_URI, projection,
                selection,
                selectionArgs, sortOrder);
    }

    public static boolean isFilterRecordsEmpty(Context context) {
        ContentResolver resolver = context.getContentResolver();
        boolean isNull;
        Cursor c = resolver.query(CSTContactFilterProvider.CONTENT_URI, null, null, null, null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            isNull = false;
        } else {
            isNull = true;
        }
        c.close();
        return isNull;
    }

}

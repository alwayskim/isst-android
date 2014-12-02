package cn.edu.zju.isst1.v2.contact.contact.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.zju.isst1.v2.data.BasicUser;
import cn.edu.zju.isst1.v2.db.util.CSTSerialUtil;
import cn.edu.zju.isst1.v2.user.data.CSTUser;

/**
 * Created by alwayking on 14/11/28.
 */
public class CSTAddressListDataDelegate {
    public static CSTAlumni getAlumni(Cursor cursor) {
        CSTAlumni alumni = new CSTAlumni();
        alumni.id = cursor.getInt(cursor.getColumnIndex(CSTAddressListProvider.Columns.ID.key));
        alumni.userName = cursor
                .getString(cursor.getColumnIndex(CSTAddressListProvider.Columns.USERNAME.key));
        alumni.name = cursor
                .getString(cursor.getColumnIndex(CSTAddressListProvider.Columns.NAME.key));
        alumni.grade = cursor
                .getInt(cursor.getColumnIndex(CSTAddressListProvider.Columns.GRADE.key));
        alumni.gender = (CSTUser.Gender) CSTSerialUtil.deserialize(cursor
                .getBlob(cursor.getColumnIndex(CSTAddressListProvider.Columns.GENDER.key)));
        alumni.clazzId = cursor
                .getInt(cursor.getColumnIndex(CSTAddressListProvider.Columns.CLAZZID.key));
        alumni.clazzName = cursor
                .getString(cursor.getColumnIndex(CSTAddressListProvider.Columns.CLAZZNAME.key));
        alumni.majorName = cursor
                .getString(cursor.getColumnIndex(CSTAddressListProvider.Columns.MAJORNAME.key));
        alumni.email = cursor
                .getString(cursor.getColumnIndex(CSTAddressListProvider.Columns.EMAIL.key));
        alumni.phoneNum = cursor
                .getString(cursor.getColumnIndex(CSTAddressListProvider.Columns.PHONENUM.key));
        alumni.qqNum = cursor
                .getString(cursor.getColumnIndex(CSTAddressListProvider.Columns.QQNUM.key));
        alumni.company = cursor
                .getString(cursor.getColumnIndex(CSTAddressListProvider.Columns.COMPANY.key));
        alumni.jobTitle = cursor
                .getString(cursor.getColumnIndex(CSTAddressListProvider.Columns.JOBTITLE.key));
        alumni.sign = cursor
                .getString(cursor.getColumnIndex(CSTAddressListProvider.Columns.SIGN.key));
        alumni.cityId = cursor
                .getInt(cursor.getColumnIndex(CSTAddressListProvider.Columns.CITYID.key));
        alumni.cityName = cursor
                .getString(cursor.getColumnIndex(CSTAddressListProvider.Columns.CITYNAME.key));

        return alumni;
    }

    public static void deleteAllAlumni(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.delete(CSTAddressListProvider.CONTENT_URI, null, null);
    }

    public static void saveAlumniList(Context context, CSTAlumni alumni) {
        ContentResolver resolver = context.getContentResolver();
        resolver.bulkInsert(CSTAddressListProvider.CONTENT_URI, getAlumniListValues(alumni));
    }

    public static Loader<Cursor> getDataCursor(Context context, String[] projection,
                                               String selection, String[] selectionArgs, String sortOrder) {
        return new CursorLoader(context, CSTAddressListProvider.CONTENT_URI, projection, selection,
                selectionArgs, sortOrder);
    }

    private static ContentValues[] getAlumniListValues(CSTAlumni alumni) {
        List<ContentValues> valuesList = new ArrayList<ContentValues>();
        Collections.sort(alumni.itemList, new Pinyin4j.PinyinComparator());
        for (BasicUser singleAlumni : alumni.itemList) {
            valuesList.add(getAlumniValue((CSTAlumni) singleAlumni));
        }
        return valuesList.toArray(new ContentValues[valuesList.size()]);
    }

    private static ContentValues getAlumniValue(CSTAlumni alumni) {
        ContentValues values = new ContentValues();

        values.put(CSTAddressListProvider.Columns.ID.key, alumni.id);
        values.put(CSTAddressListProvider.Columns.USERNAME.key, alumni.userName);
        values.put(CSTAddressListProvider.Columns.NAME.key, alumni.name);
        values.put(CSTAddressListProvider.Columns.GRADE.key, alumni.grade);
        values.put(CSTAddressListProvider.Columns.GENDER.key, CSTSerialUtil.serialize(alumni.gender));
        values.put(CSTAddressListProvider.Columns.CLAZZID.key, alumni.clazzId);
        values.put(CSTAddressListProvider.Columns.CLAZZNAME.key, alumni.clazzName);
        values.put(CSTAddressListProvider.Columns.MAJORNAME.key, alumni.majorName);
        values.put(CSTAddressListProvider.Columns.EMAIL.key, alumni.email);
        values.put(CSTAddressListProvider.Columns.PHONENUM.key, alumni.phoneNum);
        values.put(CSTAddressListProvider.Columns.COMPANY.key, alumni.company);
        values.put(CSTAddressListProvider.Columns.JOBTITLE.key, alumni.jobTitle);
        values.put(CSTAddressListProvider.Columns.SIGN.key, alumni.sign);
        values.put(CSTAddressListProvider.Columns.CITYID.key, alumni.cityId);
        values.put(CSTAddressListProvider.Columns.CITYNAME.key, alumni.cityName);
        return values;
    }

}

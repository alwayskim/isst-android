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
 * Created by tan on 2014/8/26.
 */
public class CSTAlumniDataDelegate {

    public static CSTAlumni getAlumni(Cursor cursor) {
        CSTAlumni alumni = new CSTAlumni();
        alumni.id = cursor.getInt(cursor.getColumnIndex(CSTAlumniProvider.Columns.ID.key));
        alumni.userName = cursor
                .getString(cursor.getColumnIndex(CSTAlumniProvider.Columns.USERNAME.key));
        alumni.name = cursor
                .getString(cursor.getColumnIndex(CSTAlumniProvider.Columns.NAME.key));
        alumni.grade = cursor
                .getInt(cursor.getColumnIndex(CSTAlumniProvider.Columns.GRADE.key));
        alumni.gender = (CSTUser.Gender) CSTSerialUtil.deserialize(cursor
                .getBlob(cursor.getColumnIndex(CSTAlumniProvider.Columns.GENDER.key)));
        alumni.clazzId = cursor
                .getInt(cursor.getColumnIndex(CSTAlumniProvider.Columns.CLAZZID.key));
        alumni.clazzName = cursor
                .getString(cursor.getColumnIndex(CSTAlumniProvider.Columns.CLAZZNAME.key));
        alumni.majorName = cursor
                .getString(cursor.getColumnIndex(CSTAlumniProvider.Columns.MAJORNAME.key));
        alumni.email = cursor
                .getString(cursor.getColumnIndex(CSTAlumniProvider.Columns.EMAIL.key));
        alumni.phoneNum = cursor
                .getString(cursor.getColumnIndex(CSTAlumniProvider.Columns.PHONENUM.key));
        alumni.qqNum = cursor
                .getString(cursor.getColumnIndex(CSTAlumniProvider.Columns.QQNUM.key));
        alumni.company = cursor
                .getString(cursor.getColumnIndex(CSTAlumniProvider.Columns.COMPANY.key));
        alumni.jobTitle = cursor
                .getString(cursor.getColumnIndex(CSTAlumniProvider.Columns.JOBTITLE.key));
        alumni.sign = cursor
                .getString(cursor.getColumnIndex(CSTAlumniProvider.Columns.SIGN.key));
        alumni.cityId = cursor
                .getInt(cursor.getColumnIndex(CSTAlumniProvider.Columns.CITYID.key));
        alumni.cityName = cursor
                .getString(cursor.getColumnIndex(CSTAlumniProvider.Columns.CITYNAME.key));

        return alumni;
    }

    public static void deleteAllAlumni(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.delete(CSTAlumniProvider.CONTENT_URI, null, null);
    }

    public static void saveAlumniList(Context context, CSTAlumni alumni) {
        ContentResolver resolver = context.getContentResolver();
        resolver.bulkInsert(CSTAlumniProvider.CONTENT_URI, getAlumniListValues(alumni));
    }

    public static Loader<Cursor> getDataCursor(Context context, String[] projection,
            String selection, String[] selectionArgs, String sortOrder) {
        return new CursorLoader(context, CSTAlumniProvider.CONTENT_URI, projection, selection,
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

        values.put(CSTAlumniProvider.Columns.ID.key, alumni.id);
        values.put(CSTAlumniProvider.Columns.USERNAME.key, alumni.userName);
        values.put(CSTAlumniProvider.Columns.NAME.key, alumni.name);
        values.put(CSTAlumniProvider.Columns.GRADE.key, alumni.grade);
        values.put(CSTAlumniProvider.Columns.GENDER.key, CSTSerialUtil.serialize(alumni.gender));
        values.put(CSTAlumniProvider.Columns.CLAZZID.key, alumni.clazzId);
        values.put(CSTAlumniProvider.Columns.CLAZZNAME.key, alumni.clazzName);
        values.put(CSTAlumniProvider.Columns.MAJORNAME.key, alumni.majorName);
        values.put(CSTAlumniProvider.Columns.EMAIL.key, alumni.email);
        values.put(CSTAlumniProvider.Columns.PHONENUM.key, alumni.phoneNum);
        values.put(CSTAlumniProvider.Columns.COMPANY.key, alumni.company);
        values.put(CSTAlumniProvider.Columns.JOBTITLE.key, alumni.jobTitle);
        values.put(CSTAlumniProvider.Columns.SIGN.key, alumni.sign);
        values.put(CSTAlumniProvider.Columns.CITYID.key, alumni.cityId);
        values.put(CSTAlumniProvider.Columns.CITYNAME.key, alumni.cityName);
        return values;
    }

}

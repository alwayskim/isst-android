package cn.edu.zju.isst1.v2.event.city.event.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import cn.edu.zju.isst1.v2.user.data.CSTUser;

/**
 * Created by lqynydyxf on 2014/8/8.
 */
public class CSTCityParticipantDataDelegate {

    public static CSTCityParticipant getCityParticipant(Cursor cursor) {
        CSTCityParticipant cityParticipant = new CSTCityParticipant();
        cityParticipant.id = cursor
                .getInt(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.ID.key));
        cityParticipant.userName = cursor
                .getString(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.USER_NAME.key));
        cityParticipant.name = cursor
                .getString(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.NAME.key));
        cityParticipant.grade = cursor
                .getInt(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.GRADE.key));
        if (cursor.getString(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.GENDER.key))
                .equals("男")) {
            cityParticipant.gender = CSTUser.Gender.MALE;
        } else {
            cityParticipant.gender = CSTUser.Gender.FEMALE;
        }
        cityParticipant.clazzId = cursor
                .getInt(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.CLASS_ID.key));
        cityParticipant.clazzName = cursor.getString(
                cursor.getColumnIndex(CSTCityParticipantProvider.Columns.CLASS_NAME.key));
        cityParticipant.major = cursor
                .getString(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.MAJOR.key));
        cityParticipant.email = cursor
                .getString(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.EMAIL.key));
        cityParticipant.phoneNum = cursor
                .getString(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.PHONE.key));
        cityParticipant.qqNum = cursor
                .getString(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.QQ.key));
        cityParticipant.company = cursor
                .getString(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.COMPANY.key));
        cityParticipant.jobTitle = cursor
                .getString(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.JOB_TITLE.key));
        cityParticipant.signature = cursor
                .getString(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.SIGN.key));
        cityParticipant.cityId = cursor
                .getString(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.CITY_ID.key));
        cityParticipant.cityName = cursor
                .getString(cursor.getColumnIndex(CSTCityParticipantProvider.Columns.CITY_NAME.key));
        return cityParticipant;
    }

    public static CSTCityParticipant getCityParticipant(Context context, String id) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver
                .query(CSTCityParticipantProvider.CONTENT_URI, null,
                        CSTCityParticipantProvider.Columns.ID.key + " = ?",
                        new String[]{
                                id
                        }, null
                );
        if (cursor != null && cursor.moveToFirst()) {
            try {
                return getCityParticipant(cursor);
            } finally {
                cursor.close();
            }
        }
        return null;//Should throw exception to avoid null pointer?
    }

    public static void saveCityParticipant(Context context, CSTCityParticipant cityParticipant) {
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(CSTCityParticipantProvider.CONTENT_URI,
                getCityParticipantValue(cityParticipant));
    }

    public static void deleteAllCityParticipants(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(CSTCityParticipantProvider.CONTENT_URI, null, null);
    }

    private static ContentValues getCityParticipantValue(CSTCityParticipant cityParticipant) {
        ContentValues values = new ContentValues();

        values.put(CSTCityParticipantProvider.Columns.ID.key, cityParticipant.id);
        values.put(CSTCityParticipantProvider.Columns.USER_NAME.key, cityParticipant.userName);
        values.put(CSTCityParticipantProvider.Columns.NAME.key, cityParticipant.name);
        values.put(CSTCityParticipantProvider.Columns.GRADE.key, cityParticipant.grade);
        values.put(CSTCityParticipantProvider.Columns.GENDER.key,
                cityParticipant.gender.getTypeName());
        values.put(CSTCityParticipantProvider.Columns.CLASS_ID.key, cityParticipant.clazzId);
        values.put(CSTCityParticipantProvider.Columns.CLASS_NAME.key, cityParticipant.clazzName);
        values.put(CSTCityParticipantProvider.Columns.MAJOR.key, cityParticipant.major);
        values.put(CSTCityParticipantProvider.Columns.EMAIL.key, cityParticipant.email);
        values.put(CSTCityParticipantProvider.Columns.PHONE.key, cityParticipant.phoneNum);
        values.put(CSTCityParticipantProvider.Columns.QQ.key, cityParticipant.qqNum);
        values.put(CSTCityParticipantProvider.Columns.COMPANY.key, cityParticipant.company);
        values.put(CSTCityParticipantProvider.Columns.JOB_TITLE.key, cityParticipant.jobTitle);
        values.put(CSTCityParticipantProvider.Columns.SIGN.key, cityParticipant.signature);
        values.put(CSTCityParticipantProvider.Columns.CITY_ID.key, cityParticipant.cityId);
        values.put(CSTCityParticipantProvider.Columns.CITY_NAME.key, cityParticipant.cityName);
        return values;
    }
}

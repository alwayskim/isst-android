package cn.edu.zju.isst1.v2.user.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by theasir on 7/29/14.
 */
public class CSTUserDataDelegate {

    public static CSTUser getUser(Cursor cursor) {
        CSTUser user = new CSTUser();

        user.id = cursor.getInt(cursor.getColumnIndex(CSTUserProvider.Columns.ID.key));
        user.userName = cursor
                .getString(cursor.getColumnIndex(CSTUserProvider.Columns.USER_NAME.key));
        user.pwd = cursor.getString(cursor.getColumnIndex(CSTUserProvider.Columns.PASSWORD.key));
        user.name = cursor.getString(cursor.getColumnIndex(CSTUserProvider.Columns.NAME.key));
        if (cursor.getString(cursor.getColumnIndex(CSTUserProvider.Columns.GENDER.key))
                .equals(CSTUser.Gender.MALE.getTypeName())) {
            user.gender = CSTUser.Gender.MALE;
        } else {
            user.gender = CSTUser.Gender.FEMALE;
        }
        user.grade = cursor.getInt(cursor.getColumnIndex(CSTUserProvider.Columns.GRADE.key));
        user.clazzId = cursor.getInt(cursor.getColumnIndex(CSTUserProvider.Columns.CLASS_ID.key));
        user.clazzName = cursor
                .getString(cursor.getColumnIndex(CSTUserProvider.Columns.CLASS_NAME.key));
        user.majorName = cursor.getString(cursor.getColumnIndex(CSTUserProvider.Columns.MAJOR.key));
        user.cityId = cursor.getInt(cursor.getColumnIndex(CSTUserProvider.Columns.CITY_ID.key));
        user.cityName = cursor
                .getString(cursor.getColumnIndex(CSTUserProvider.Columns.CITY_NAME.key));
        user.email = cursor.getString(cursor.getColumnIndex(CSTUserProvider.Columns.EMAIL.key));
        user.phoneNum = cursor.getString(cursor.getColumnIndex(CSTUserProvider.Columns.PHONE.key));
        user.qqNum = cursor.getString(cursor.getColumnIndex(CSTUserProvider.Columns.QQ.key));
        user.company = cursor.getString(cursor.getColumnIndex(CSTUserProvider.Columns.COMPANY.key));
        user.jobTitle = cursor
                .getString(cursor.getColumnIndex(CSTUserProvider.Columns.JOB_TITLE.key));
        user.sign = cursor.getString(cursor.getColumnIndex(CSTUserProvider.Columns.SIGN.key));
        user.castellan =
                cursor.getInt(cursor.getColumnIndex(CSTUserProvider.Columns.IS_CASTELLAN.key)) != 0;
        user.pvtQq = cursor.getInt(cursor.getColumnIndex(CSTUserProvider.Columns.PVT_QQ.key)) != 0;
        user.pvtEmail = cursor.getInt(cursor.getColumnIndex(CSTUserProvider.Columns.PVT_EMAIL.key))
                != 0;
        user.pvtPhoneNum =
                cursor.getInt(cursor.getColumnIndex(CSTUserProvider.Columns.PVT_PHONE.key)) != 0;
        user.pvtCompany =
                cursor.getInt(cursor.getColumnIndex(CSTUserProvider.Columns.PVT_COMPANY.key)) != 0;
        user.pvtJobTitle =
                cursor.getInt(cursor.getColumnIndex(CSTUserProvider.Columns.PVT_JOB_TITLE.key))
                        != 0;

        return user;
    }

    public static CSTUser getUser(Context context, String id) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver
                .query(CSTUserProvider.CONTENT_URI, null, CSTUserProvider.Columns.ID.key + " = ?",
                        new String[]{
                                id
                        }, null
                );
        if (cursor != null && cursor.moveToFirst()) {
            try {
                return getUser(cursor);
            } finally {
                cursor.close();
            }
        }
        return null;//Should throw exception to avoid null pointer?
    }

    public static void saveUser(Context context, CSTUser user) {
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(CSTUserProvider.CONTENT_URI, getUserValue(user));
    }

    public static void deleteAllUsers(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(CSTUserProvider.CONTENT_URI, null, null);
    }

    private static ContentValues getUserValue(CSTUser user) {
        ContentValues values = new ContentValues();

        values.put(CSTUserProvider.Columns.ID.key, user.id);
        values.put(CSTUserProvider.Columns.USER_NAME.key, user.userName);
        values.put(CSTUserProvider.Columns.NAME.key, user.name);
        values.put(CSTUserProvider.Columns.PASSWORD.key, user.pwd);
        values.put(CSTUserProvider.Columns.GENDER.key, user.gender.getTypeName());
        values.put(CSTUserProvider.Columns.GRADE.key, user.grade);
        values.put(CSTUserProvider.Columns.CLASS_ID.key, user.clazzId);
        values.put(CSTUserProvider.Columns.CLASS_NAME.key, user.clazzName);
        values.put(CSTUserProvider.Columns.MAJOR.key, user.majorName);
        values.put(CSTUserProvider.Columns.CITY_ID.key, user.cityId);
        values.put(CSTUserProvider.Columns.CITY_NAME.key, user.cityName);
        values.put(CSTUserProvider.Columns.EMAIL.key, user.email);
        values.put(CSTUserProvider.Columns.PHONE.key, user.phoneNum);
        values.put(CSTUserProvider.Columns.QQ.key, user.qqNum);
        values.put(CSTUserProvider.Columns.COMPANY.key, user.company);
        values.put(CSTUserProvider.Columns.JOB_TITLE.key, user.jobTitle);
        values.put(CSTUserProvider.Columns.SIGN.key, user.sign);
        values.put(CSTUserProvider.Columns.IS_CASTELLAN.key, user.castellan);
        values.put(CSTUserProvider.Columns.PVT_QQ.key, user.pvtQq);
        values.put(CSTUserProvider.Columns.PVT_EMAIL.key, user.pvtEmail);
        values.put(CSTUserProvider.Columns.PVT_PHONE.key, user.pvtPhoneNum);
        values.put(CSTUserProvider.Columns.PVT_COMPANY.key, user.pvtCompany);
        values.put(CSTUserProvider.Columns.PVT_JOB_TITLE.key, user.pvtJobTitle);
        return values;
    }

    public static CSTUser getCurrentUser(Context context){
        Cursor mCursor = context.getContentResolver().query(CSTUserProvider.CONTENT_URI,
                null, null, null, null);
        mCursor.moveToFirst();
        CSTUser mUser = new CSTUser();
        mUser = CSTUserDataDelegate.getUser(mCursor);
        mCursor.close();
        return mUser;
    }
}

package cn.edu.zju.isst1.v2.restaurant.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst1.v2.data.CSTRestaurant;
import cn.edu.zju.isst1.v2.data.CSTRestaurantMenu;
import cn.edu.zju.isst1.v2.db.util.CSTSerialUtil;

/**
 * Created by lqynydyxf on 2014/8/20.
 */
public class CSTRestaurantDataDelegate {

    public static CSTRestaurant getRestaurant(Cursor cursor) {
        CSTRestaurant restaurant = new CSTRestaurant();
        restaurant.id = cursor.getInt(cursor.getColumnIndex(CSTRestaurantProvider.Columns.ID.key));
        restaurant.name = cursor
                .getString(cursor.getColumnIndex(CSTRestaurantProvider.Columns.NAME.key));
        restaurant.picture = cursor
                .getString(cursor.getColumnIndex(CSTRestaurantProvider.Columns.PICTURE.key));
        restaurant.address = cursor
                .getString(cursor.getColumnIndex(CSTRestaurantProvider.Columns.ADDRESS.key));
        restaurant.hotLine = cursor
                .getString(cursor.getColumnIndex(CSTRestaurantProvider.Columns.HOTLINE.key));
        restaurant.businessHours = cursor
                .getString(cursor.getColumnIndex(CSTRestaurantProvider.Columns.BUSINESS_HOURS.key));
        restaurant.description = cursor
                .getString(cursor.getColumnIndex(CSTRestaurantProvider.Columns.DESCRIPTION.key));
        restaurant.restaurantMenu = (CSTRestaurantMenu) CSTSerialUtil.deserialize(cursor.getBlob(
                cursor.getColumnIndex(CSTRestaurantProvider.Columns.RESTAURANT_MENU.key)));
        return restaurant;
    }

    public static CSTRestaurant getRestaurant(Context context, String id) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver
                .query(CSTRestaurantProvider.CONTENT_URI, null,
                        CSTRestaurantProvider.Columns.ID.key + " = ?",
                        new String[]{
                                id
                        }, null
                );
        if (cursor != null && cursor.moveToFirst()) {
            try {
                return getRestaurant(cursor);
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public static void saveRestaurant(Context context, CSTRestaurant restaurant) {
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(CSTRestaurantProvider.CONTENT_URI, getRestaurantValues(restaurant));
    }

    public static void deleteAllRestaurent(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(CSTRestaurantProvider.CONTENT_URI, null, null);
    }

    public static Loader<Cursor> getDataCursor(Context context, String[] projection,
            String selection, String[] selectionArgs, String sortOrder) {
        return new CursorLoader(context, CSTRestaurantProvider.CONTENT_URI, projection, selection,
                selectionArgs, sortOrder);
    }

    public static void saveRestaurantList(Context context, CSTRestaurant restaurant) {
        ContentResolver resolver = context.getContentResolver();
        resolver.bulkInsert(CSTRestaurantProvider.CONTENT_URI, getRestaurantListValues(restaurant));
    }

    private static ContentValues[] getRestaurantListValues(CSTRestaurant restaurant) {
        List<ContentValues> valuesList = new ArrayList<ContentValues>();
        for (CSTRestaurant singleRestaurant : restaurant.itemList) {
            valuesList.add(getRestaurantValues(singleRestaurant));
        }
        return valuesList.toArray(new ContentValues[valuesList.size()]);
    }

    private static ContentValues getRestaurantValues(CSTRestaurant restaurant) {
        ContentValues values = new ContentValues();
        values.put(CSTRestaurantProvider.Columns.ID.key, restaurant.id);
        values.put(CSTRestaurantProvider.Columns.NAME.key, restaurant.name);
        values.put(CSTRestaurantProvider.Columns.PICTURE.key, restaurant.picture);
        values.put(CSTRestaurantProvider.Columns.ADDRESS.key, restaurant.address);
        values.put(CSTRestaurantProvider.Columns.HOTLINE.key, restaurant.hotLine);
        values.put(CSTRestaurantProvider.Columns.BUSINESS_HOURS.key, restaurant.businessHours);
        values.put(CSTRestaurantProvider.Columns.DESCRIPTION.key, restaurant.description);
        values.put(CSTRestaurantProvider.Columns.RESTAURANT_MENU.key,
                CSTSerialUtil.serialize(restaurant.restaurantMenu));
        return values;
    }
}

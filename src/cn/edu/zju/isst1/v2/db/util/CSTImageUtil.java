package cn.edu.zju.isst1.v2.db.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import cn.edu.zju.isst1.R;

/**
 * Created by lqynydyxf on 15/1/11.
 */
public class CSTImageUtil {

    public static void loadImageVolley(Context context, ImageView iv, String path) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final LruCache<String, Bitmap> lurcache = new LruCache<String, Bitmap>(
                20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {

            @Override
            public void putBitmap(String key, Bitmap value) {
                lurcache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {

                return lurcache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        ImageLoader.ImageListener listener = imageLoader.getImageListener(iv,
                R.drawable.ic_launcher, R.drawable.ic_launcher);
        imageLoader.get(path, listener);
    }
}

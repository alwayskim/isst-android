package cn.edu.zju.isst1.v2.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import cn.edu.zju.isst1.R;

/**
 * Created by i308844 on 7/28/14.
 */
public class VolleyRequestManager {

    private static VolleyRequestManager mInstance;

    private static Context mContext;

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    private VolleyRequestManager(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                }
        );
    }

    public static synchronized void createInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyRequestManager(context.getApplicationContext());
        }
    }

    public static synchronized VolleyRequestManager getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException(VolleyRequestManager.class.getSimpleName() +
                    " is not initialized, call createInstance(..) method first.");
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
//        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(mRequestQueue,
//                    new ImageLoader.ImageCache() {
//                        private final LruCache<String, Bitmap>
//                                cache = new LruCache<String, Bitmap>(20);
//
//                        @Override
//                        public Bitmap getBitmap(String url) {
//                            return cache.get(url);
//                        }
//
//                        @Override
//                        public void putBitmap(String url, Bitmap bitmap) {
//                            cache.put(url, bitmap);
//                        }
//                    }
//            );
//        }
        return this.mImageLoader;
    }

    public  void addLoadImageRequest(ImageView iv, String path,int defaultImg) {
        ImageLoader.ImageListener listener = getImageLoader().getImageListener(iv,
                defaultImg, defaultImg);
        getImageLoader().get(path, listener);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}

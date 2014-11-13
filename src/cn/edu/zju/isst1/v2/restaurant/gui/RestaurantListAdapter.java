package cn.edu.zju.isst1.v2.restaurant.gui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.v2.data.CSTRestaurant;
import cn.edu.zju.isst1.v2.restaurant.data.CSTRestaurantDataDelegate;

/**
 * Created by lqynydyxf on 2014/8/28.
 */
public class RestaurantListAdapter extends CursorAdapter {

    public RestaurantListAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.restaurant_list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final CSTRestaurant restaurant = CSTRestaurantDataDelegate.getRestaurant(cursor);
        view.setTag(restaurant);

        final ViewHolder holder = getBindViewHolder(view);
        holder.nameTxv.setText(restaurant.name);
        final Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                holder.resIcon.setImageBitmap((Bitmap) msg.obj);
            }
        };
        Runnable show_icon = new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] data = getImage(restaurant.picture);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Message msg = mHandler.obtainMessage();
                    msg.obj = bitmap;
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(show_icon).start();

        holder.hotlineTxv.setText(restaurant.hotLine);
        holder.dialIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Judge.isNullOrEmpty(holder.hotlineTxv.getText())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                            .parse("tel://" + holder.hotlineTxv.getText()));
                    context.startActivity(intent);
                }
            }
        });
    }

    protected ViewHolder getBindViewHolder(View view) {
        final ViewHolder holder = new ViewHolder();
        holder.resIcon = (ImageView) view
                .findViewById(R.id.restaurant_list_item_icon_imgv);
        holder.nameTxv = (TextView) view
                .findViewById(R.id.restaurant_list_item_name_txv);
        holder.hotlineTxv = (TextView) view
                .findViewById(R.id.restaurant_list_item_hotline_txv);
        holder.dialIBtn = (ImageButton) view
                .findViewById(R.id.restaurant_list_item_dial_ibtn);
        return holder;
    }

    protected final class ViewHolder {

        public ImageView resIcon;

        public TextView nameTxv;

        public TextView hotlineTxv;

        public ImageButton dialIBtn;
    }

    public static byte[] getImage(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(5000);
        InputStream inputStream = conn.getInputStream();
        byte[] data = readInputStream(inputStream);
        return data;
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}

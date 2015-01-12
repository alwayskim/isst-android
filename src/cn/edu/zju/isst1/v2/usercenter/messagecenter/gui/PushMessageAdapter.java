package cn.edu.zju.isst1.v2.usercenter.messagecenter.gui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.CSTMessage;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.CSTMessageDataDelegate;

/**
 * Created by lqynydyxf on 15/1/11.
 */
public class PushMessageAdapter extends CursorAdapter {

    public PushMessageAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.push_messages_activity_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final CSTMessage message = CSTMessageDataDelegate.getMessage(cursor);
        view.setTag(message);
        final ViewHolder holder = getBindViewHolder(view);
        Lgr.i("Message Content", message.content);
        holder.titleTxv.setText(message.title);
        holder.contentTxv.setText(message.content);
        holder.createdTimeTxv.setText(TSUtil.toFull(Long.parseLong(message.createdAt)));
    }

    protected ViewHolder getBindViewHolder(View view) {
        final ViewHolder holder = new ViewHolder();
        holder.titleTxv = (TextView) view
                .findViewById(R.id.push_msg_title);
        holder.contentTxv = (TextView) view
                .findViewById(R.id.push_msg_content);
        holder.createdTimeTxv = (TextView) view.findViewById(R.id.push_msg_date);
        return holder;
    }

    protected final class ViewHolder {

        TextView titleTxv;

        TextView contentTxv;

        TextView createdTimeTxv;
    }
}

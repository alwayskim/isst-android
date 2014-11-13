package cn.edu.zju.isst1.v2.archive.gui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.v2.archive.data.CSTArchive;
import cn.edu.zju.isst1.v2.archive.data.CSTArchiveDataDelegate;

/**
 * Created by i308844 on 8/12/14.
 */
public class ArchiveListAdapter extends CursorAdapter {

    public ArchiveListAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.archive_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        CSTArchive archive = CSTArchiveDataDelegate.getArchive(cursor);
        view.setTag(archive);

        ViewHolder holder = getBindViewHolder(view);
        holder.titleTxv.setText(archive.title);
        holder.dateTxv.setText(TSUtil.toYMD(archive.updateTime));
        if (archive.publisherId != 0) {
            holder.publisherTxv.setText(archive.publisher.name);
        }
        holder.descriptionTxv.setText(archive.description);
    }

    protected ViewHolder getBindViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.titleTxv = (TextView) view
                .findViewById(R.id.title_txv);
        holder.dateTxv = (TextView) view
                .findViewById(R.id.date_txv);
        holder.publisherTxv = (TextView) view
                .findViewById(R.id.publisher_txv);
        holder.descriptionTxv = (TextView) view
                .findViewById(R.id.description_txv);
        return holder;
    }

    protected final class ViewHolder {

        public TextView titleTxv;

        public TextView dateTxv;

        public TextView publisherTxv;

        public TextView descriptionTxv;
    }
}

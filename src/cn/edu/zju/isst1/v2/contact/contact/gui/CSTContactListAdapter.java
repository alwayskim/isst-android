package cn.edu.zju.isst1.v2.contact.contact.gui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTAddressListDataDelegate;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTAlumni;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTAlumniDataDelegate;

/**
 * Created by i308844 on 8/12/14.
 */
public class CSTContactListAdapter extends CursorAdapter {

    private ViewHolder holder;
    private BaseContactListFragment.FilterType mFilterType;

    public CSTContactListAdapter(Context context, Cursor c,BaseContactListFragment.FilterType filterType) {
        super(context, c, 0);
        mFilterType = filterType;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Lgr.i("ContactListAdapter", "——newView");
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.contact_note_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Lgr.i("ContactListAdapter", "——bindView");
        CSTAlumni alumni, alumniPrevious;
        String chPrevious, chCurrent;
        if(mFilterType == BaseContactListFragment.FilterType.MY_CITY)
            alumni = CSTAlumniDataDelegate.getAlumni(cursor);
        else
            alumni = CSTAddressListDataDelegate.getAlumni(cursor);
        chCurrent = PinYinUtil.converterToFirstSpell(alumni.name).substring(0, 1);

        view.setTag(alumni);
        holder = getBindViewHolder(view);
        holder.nameTxv.setText(alumni.name);

        holder = getBindViewHolder(view);
        if (cursor.moveToPrevious()) {
            alumniPrevious = CSTAlumniDataDelegate.getAlumni(cursor);
            chPrevious = PinYinUtil.converterToFirstSpell(alumniPrevious.name).substring(0, 1);
            cursor.moveToNext();
        } else {
            chPrevious = "";
        }

        if (!chCurrent.equals(chPrevious)) {
            holder.indexTxv.setVisibility(View.VISIBLE);
            holder.indexTxv.setText(chCurrent);
        } else {
            holder.indexTxv.setVisibility(View.GONE);
        }
    }

    protected ViewHolder getBindViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.nameTxv = (TextView) view
                .findViewById(R.id.contact_note_list_item_name_txv);
        holder.indexTxv = (TextView) view.findViewById(R.id.contact_note_list_item_index_txv);
        return holder;
    }

    protected final class ViewHolder {

        public TextView nameTxv;

        //索引TextView
        private TextView indexTxv;
    }
}

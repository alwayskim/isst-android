package cn.edu.zju.isst.v2.contact.contact.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.edu.zju.isst.R;
import cn.edu.zju.isst.ui.contact.ContactFilter;
import cn.edu.zju.isst.v2.contact.contact.data.CSTAlumni;


/**
 * Created by lenovo on 2014/9/17.
 */
public class CSTSerachedAlumniAdapter extends BaseAdapter {

    private List<CSTAlumni> alumniList;

    private Context mContext;

    private ViewHolder holder;

    public CSTSerachedAlumniAdapter(Context mCx, List<CSTAlumni> mList) {
        this.mContext = mCx;
        this.alumniList = mList;

    }


    @Override
    public int getCount() {
        return alumniList.size();
    }

    @Override
    public Object getItem(int position) {
        return alumniList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CSTAlumni alumni, alumniPrevious;
        String chPrevious, chCurrent;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.contact_note_list_item, null);
            holder.nameTxv = (TextView) convertView
                    .findViewById(R.id.contact_note_list_item_name_txv);
            holder.indexTxv = (TextView) convertView
                    .findViewById(R.id.contact_note_list_item_index_txv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        alumni = alumniList.get(position);
        chCurrent = PinYinUtil.converterToFirstSpell(alumni.name).substring(0, 1);
        holder.nameTxv.setText(alumni.name);

        if (position > 0) {
            alumniPrevious = alumniList.get(position - 1);
            chPrevious = PinYinUtil.converterToFirstSpell(alumniPrevious.name).substring(0, 1);
        } else {
            chPrevious = "";
        }

        if (!chPrevious.equals(chCurrent)) {
            holder.indexTxv.setVisibility(View.VISIBLE);
            holder.indexTxv.setText(chCurrent);
        } else {
            holder.indexTxv.setVisibility(View.GONE);
        }

        return convertView;
    }


    private class ViewHolder {

        //姓名TextView
        TextView nameTxv;

        //索引TextView
        TextView indexTxv;
    }
}

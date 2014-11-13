package cn.edu.zju.isst1.v2.contact;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.ui.contact.ContactFilter;
import cn.edu.zju.isst1.util.Judge;

/**
 * Created by always on 9/13/2014.
 */
public class ContactFilterListAdapter extends CursorAdapter {

    ContactFilter mContactFilter;

    public ContactFilterListAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.contact_filter_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mContactFilter = CSTContactFilterDelegate.getFilter(cursor);
        view.setTag(mContactFilter);
        ViewHolder holder = getBindViewHolder(view);
        StringBuilder sb = new StringBuilder();

        if (!Judge.isNullOrEmpty(mContactFilter.name)) {
            sb.append(" 姓名：" + mContactFilter.name);
        }
        if (mContactFilter.gender != 0) {
            sb.append(" 性别：" + mContactFilter.genderString);
        }
        if (mContactFilter.grade != 0) {
            sb.append(" 年级：" + mContactFilter.grade);
        }
        if (!Judge.isNullOrEmpty(mContactFilter.major)) {
            sb.append(" 方向：" + mContactFilter.major);
        }
        if (!Judge.isNullOrEmpty(mContactFilter.company)) {
            sb.append(" 公司：" + mContactFilter.company);
        }
        if (mContactFilter.cityId != 0) {
            sb.append(" 城市：" + mContactFilter.cityString);
        }

        holder.filterTxv.setText(sb);
    }

    protected ViewHolder getBindViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.filterTxv = (TextView) view
                .findViewById(R.id.contact_filter_txv);
        return holder;
    }

    protected final class ViewHolder {
        public TextView filterTxv;
    }
}

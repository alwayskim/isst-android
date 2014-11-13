package cn.edu.zju.isst1.ui.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.edu.zju.isst1.R;

public class NoteBookadapter extends BaseAdapter {

    List<NoteBookItem> list;

    private Context ctx;

    private ViewHolder holder;

    public NoteBookadapter(Context context, List<NoteBookItem> list) {
        this.ctx = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(ctx).inflate(
                        R.layout.contact_note_list_item, null);
                holder.tvName = (TextView) convertView
                        .findViewById(R.id.contact_note_list_item_name_txv);
                holder.index = (TextView) convertView
                        .findViewById(R.id.contact_note_list_item_index_txv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 绑定数据
            NoteBookItem item = list.get(position);
            holder.tvName.setText(item.name);

            // 显示index
            String currentStr = item.index;
            // 上一项的index
            String previewStr = (position - 1) >= 0 ? list.get(position - 1).index
                    : " ";
            /**
             * 判断是否上一次的存在
             */
            if (!previewStr.equals(currentStr)) {
                holder.index.setVisibility(View.VISIBLE);
                holder.index.setText(currentStr);// 中奖提示的文本显示当前滑动的字母
            } else {
                holder.index.setVisibility(View.GONE);
            }
        } catch (OutOfMemoryError e) {
            Runtime.getRuntime().gc();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {

        //姓名TextView
        TextView tvName;

        //索引TextView
        TextView index;
    }
}

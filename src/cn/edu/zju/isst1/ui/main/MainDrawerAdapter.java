/**
 *
 */

package cn.edu.zju.isst1.ui.main;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.constant.Nav;
import cn.edu.zju.isst1.constant.NavGroup;

/**
 * @author theasir
 */
public class MainDrawerAdapter extends BaseAdapter {

    private Activity mActivity;

    public MainDrawerAdapter(Activity activity) {
        this.mActivity = activity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return Nav.values().length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        return Nav.values()[position];
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Nav nav = (Nav) getItem(position);

        ViewHolder holder = null;

        // if (convertView == null) {
        holder = new ViewHolder();
        convertView = mActivity.getLayoutInflater().inflate(
                R.layout.drawer_child_item, null);

        holder.groupAreaView = convertView.findViewById(R.id.drawer_group_area);
        holder.groupTitleTextView = (TextView) convertView
                .findViewById(R.id.drawer_group_title);
        holder.titleTextView = (TextView) convertView
                .findViewById(R.id.drawer_child_title);
        holder.iconImageView = (ImageView) convertView
                .findViewById(R.id.drawer_child_icon);

        // convertView.setTag(holder);
        // } else {
        // holder = (ViewHolder) convertView.getTag();
        // }

        if (nav.getIndexOfGroup() > 0) {
            holder.groupAreaView.setVisibility(View.GONE);
        }

        holder.groupTitleTextView.setText(NavGroup.values()[nav.getIndex()]
                .getName());
        holder.titleTextView.setText(nav.getName());

        return convertView;
    }

    private class ViewHolder {

        public View groupAreaView;

        public TextView groupTitleTextView;

        public TextView titleTextView;

        public ImageView iconImageView;
    }

}

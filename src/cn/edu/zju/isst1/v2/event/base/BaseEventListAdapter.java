package cn.edu.zju.isst1.v2.event.base;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.v2.event.campus.data.CSTCampusEvent;
import cn.edu.zju.isst1.v2.event.campus.data.CSTCampusEventDataDelegate;
import cn.edu.zju.isst1.v2.event.city.data.CSTCityEvent;
import cn.edu.zju.isst1.v2.event.city.data.CSTCityEventDataDelegate;

/**
 * Created by always on 21/08/2014.
 */
public class BaseEventListAdapter extends CursorAdapter {

    private EventCategory mEventCategory;

    private CSTCampusEvent campusEvent;

    private CSTCityEvent cityEvent;

    public BaseEventListAdapter(Context context, Cursor c, EventCategory eventCategory) {
        super(context, c, 0);
        mEventCategory = eventCategory;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.activity_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (mEventCategory.getName() == EventCategory.CAMPUSEVENT.getName()) {
            campusEvent = CSTCampusEventDataDelegate.getCampusEvent(cursor);
            view.setTag(campusEvent);
            ViewHolder holder = getBindViewHolder(view);
            holder.titleTxv.setText(campusEvent.title);
//            holder.updateTimeTxv.setText(TSUtil.toYMD(campusEvent.updatedAt));
//            holder.startTimeTxv.setText(TSUtil.toHM(campusEvent.startTime));
//            holder.expireTimeTxv.setText(TSUtil.toHM(campusEvent.expireTime));
            holder.updateTimeTxv.setText(TSUtil.toYMD(Long.parseLong(campusEvent.updatedAt)));
            holder.startTimeTxv.setText(TSUtil.toHM(Long.parseLong(campusEvent.startTime)));
            holder.expireTimeTxv.setText(TSUtil.toHM(Long.parseLong(campusEvent.expireTime)));
            holder.descriptionTxv.setText(campusEvent.description);
        } else {
            cityEvent = CSTCityEventDataDelegate.getCityevent(cursor);
            view.setTag(cityEvent);
            ViewHolder holder = getBindViewHolder(view);
            holder.titleTxv.setText(cityEvent.title);
            holder.updateTimeTxv.setText(TSUtil.toYMD(cityEvent.updatedAt));
            holder.startTimeTxv.setText(TSUtil.toHM(cityEvent.startTime));
            holder.expireTimeTxv.setText(TSUtil.toHM(cityEvent.expireTime));
            holder.descriptionTxv.setText(cityEvent.description);
        }

    }

    protected ViewHolder getBindViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.titleTxv = (TextView) view
                .findViewById(R.id.activity_list_item_title_txv);
        holder.updateTimeTxv = (TextView) view
                .findViewById(R.id.activity_list_item_updatetime_txv);
        holder.startTimeTxv = (TextView) view
                .findViewById(R.id.activity_list_item_starttime_txv);
        holder.expireTimeTxv = (TextView) view
                .findViewById(R.id.activity_list_item_expiretime_txv);
        holder.descriptionTxv = (TextView) view
                .findViewById(R.id.activity_list_item_description_txv);
        holder.indicatorView = (View) view
                .findViewById(R.id.activity_list_item_indicator_view);
        return holder;
    }

    protected final class ViewHolder {

        public TextView titleTxv;

        public TextView updateTimeTxv;

        public TextView startTimeTxv;

        public TextView expireTimeTxv;

        public TextView descriptionTxv;

        public ImageView headImgv;

        public View indicatorView;
    }
}

package cn.edu.zju.isst1.v2.contact.contact.gui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.constant.Constants;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.contact.ContactFilterActivity;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTAddressListDataDelegate;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTAlumni;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTAlumniDataDelegate;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTContactFilter;
import cn.edu.zju.isst1.v2.contact.contact.net.ContactResponse;
import cn.edu.zju.isst1.v2.globaldata.citylist.CSTCity;
import cn.edu.zju.isst1.v2.globaldata.citylist.CSTCityDataDelegate;
import cn.edu.zju.isst1.v2.gui.CSTBaseFragment;
import cn.edu.zju.isst1.v2.login.net.UpDateLogin;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;
import cn.edu.zju.isst1.v2.user.data.CSTUser;
import cn.edu.zju.isst1.v2.user.data.CSTUserDataDelegate;
import cn.edu.zju.isst1.v2.user.data.CSTUserProvider;
import cn.edu.zju.isst1.widget.PinnedSectionListView;


/**
 * Created by tan on 8/26/14.
 */
public class BaseContactListFragment extends CSTBaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private static BaseContactListFragment INSTANCE_MYCLASS = new BaseContactListFragment();

    private static BaseContactListFragment INSTANCE_MYCITY = new BaseContactListFragment();

    private PinnedSectionListView mListView;

    private List<CSTAlumni> mAlumniList;

    private CSTAlumni mAlumni;

    private CSTContactFilter mFilter = new CSTContactFilter();

//    private CSTContactListAdapter mAdapter;

    private CSTSerachedAlumniAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

//    private AutoCompleteTextView autoCompleteTextView;

    private Handler mHandler;

    private CSTUser mUser;

    private TextView clazzTvx;

    private ImageButton searchBtn;

    private AutoCompleteTextView autoCompleteTextView;

    private boolean IS_FIRST = true;

    private FilterType m_ft;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    public enum FilterType {
        MY_CLASS, MY_CITY, MY_FILTER
    }

    public static BaseContactListFragment getInstance(FilterType ft) {
        if (ft == FilterType.MY_CLASS) {
            INSTANCE_MYCLASS.setM_ft(FilterType.MY_CLASS);
            return INSTANCE_MYCLASS;
        }
        INSTANCE_MYCITY.setM_ft(FilterType.MY_CITY);
        return INSTANCE_MYCITY;
    }


    public void setM_ft(FilterType m_ft) {
        this.m_ft = m_ft;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Constants.RESULT_CODE_BETWEEN_CONTACT) {
//            mFilter.setContactFilter((CSTContactFilter) data.getExtras().getSerializable(
//                    "mFilter"));
//            m_ft = FilterType.MY_FILTER;
//            try {
//                requestData();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Lgr.d("BaseContactListFragment", "——onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Lgr.d("BaseContactListFragment", "——onCreateView");
        return inflater.inflate(R.layout.classmates_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Lgr.d("BaseContactListFragment", "——onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);

//        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        Lgr.d("BaseContactListFragment", "——onActivityCreated");
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // TODO Auto-generated method stub
//        super.onCreateOptionsMenu(menu, inflater);
//        Lgr.d("BaseContactListFragment", "——onCreateOptionsMenu");
//        if (m_ft == FilterType.MY_CLASS || m_ft == FilterType.MY_FILTER) {
//            inflater.inflate(R.menu.alumni_list_fragment_ab_menu, menu);
//        }
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_alumni_filter:
//                Intent intent = new Intent(getActivity(),
//                        ContactFilterActivity.class);
//                startActivityForResult(intent, 20);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    protected void initComponent(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorScheme(R.color.deepskyblue, R.color.darkorange, R.color.darkviolet,
                R.color.lightcoral);
        mListView = (PinnedSectionListView) view.findViewById(R.id.simple_list);
        clazzTvx = (TextView) view.findViewById(R.id.filter_show_txv);
        searchBtn = (ImageButton) view.findViewById(R.id.filter_show_search_btn);
        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.filter_auto_list_txv);
        Cursor mCursor = getActivity().getContentResolver().query(CSTUserProvider.CONTENT_URI,
                null, null, null, null);
        mCursor.moveToFirst();
        mUser = CSTUserDataDelegate.getUser(mCursor);
        mCursor.close();
        if (m_ft == FilterType.MY_CLASS) {
            mFilter.clazzName = mUser.clazzName;
            mFilter.clazzId = mUser.clazzId;
            mFilter.grade = mUser.grade;
            clazzTvx.setText(mUser.grade + "级" + mUser.clazzName + "班");
        } else if (m_ft == FilterType.MY_CITY) {
            mFilter.cityId = mUser.cityId;
            clazzTvx.setText(getCityName(mFilter.cityId));
        }

        if (m_ft == FilterType.MY_CLASS) {
            mAlumniList = CSTAddressListDataDelegate.getALumniList(getActivity());
        } else {
            mAlumniList = CSTAlumniDataDelegate.getALumniList(getActivity());
        }

        initHandler();

        bindAdapter();

        setUpListener();

    }

    @Override
    public void onRefresh() {
        requestData();
    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//       /* return CSTAlumniDataDelegate.getDataCursor(getActivity(),null,null,null, CSTAlumniProvider
//                .Columns.NAME.key + " DESC");*/
//        if (m_ft == FilterType.MY_CITY)
//            return CSTAlumniDataDelegate.getDataCursor(getActivity(), null, null, null, null);
//        return CSTAddressListDataDelegate.getDataCursor(getActivity(), null, null, null, null);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        mAdapter.swapCursor(data);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        mAdapter.swapCursor(null);
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAlumni = mAlumniList.get(position);
        Lgr.i("Alumni Name", mAlumni.name);
        CSTAlumni alumni = mAlumniList.get(position);
        String jobTitle = alumni.jobTitle;
        String sign = alumni.sign;
        if (jobTitle != null && sign != null) {
            if (jobTitle.equals("section") && sign.equals("section")) {
            } else {
                Intent intent = new Intent(getActivity(), CSTContactDetailActivity.class);
                intent.putExtra("alumni", (alumni));
                getActivity().startActivity(intent);
            }
        } else {
            Intent intent = new Intent(getActivity(), CSTContactDetailActivity.class);
            intent.putExtra("alumni", (alumni));
            getActivity().startActivity(intent);
        }
    }

    private void bindAdapter() {
//        mAdapter = new CSTContactListAdapter(getActivity(), null, m_ft);
        mAdapter = new CSTSerachedAlumniAdapter(getActivity(), mAlumniList);
        mListView.setAdapter(mAdapter);
        autoCompleteTextView.setAdapter(mAdapter);
        requestData();
        if (IS_FIRST) {
            mSwipeRefreshLayout.setRefreshing(true);
            IS_FIRST = false;
        }
    }

    private void setUpListener() {
        mListView.setOnItemClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        ContactFilterActivity.class);
                startActivityForResult(intent, 20);
            }
        });
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (getActivity() != null)
                    switch (msg.what) {
                        case Constants.STATUS_REQUEST_SUCCESS:
                            mSwipeRefreshLayout.setRefreshing(false);
                            if (m_ft == FilterType.MY_CLASS) {
                                mAlumniList = CSTAddressListDataDelegate.getALumniList(getActivity());
                            } else {
                                mAlumniList = CSTAlumniDataDelegate.getALumniList(getActivity());
                            }
                            mAdapter = new CSTSerachedAlumniAdapter(getActivity(),
                                    mAlumniList);
                            mListView.setAdapter(mAdapter);
                            autoCompleteTextView.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before,
                                                          int count) {
                                    autoCompleteTextView.getText().toString();
                                    mAdapter.getFilter().filter(autoCompleteTextView.getText().toString());

                                }

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count,
                                                              int after) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    // TODO Auto-generated method stub

                                }
                            });
                            break;
                        case Constants.STATUS_NOT_LOGIN:
                            UpDateLogin.getInstance().updateLogin(getActivity());
                            requestData();
                            break;
                        default:
                            CSTHttpUtil.dispose(msg.what, getActivity());
                            break;
                    }
            }
        };
    }

    private void requestData() {
        //TODO replace code in this scope with new implemented volley-base network request
        if (NetworkConnection.isNetworkConnected(getActivity())) {
            ContactResponse activityResponse = new ContactResponse(getActivity(),
                    true, m_ft) {
                @Override
                public void onResponse(JSONObject result) {
                    super.onResponse(result);
                    Lgr.i(result.toString());
                    Message msg = mHandler.obtainMessage();
                    try {
                        msg.what = result.getInt("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    super.onErrorResponse(error);
                    Message msg = mHandler.obtainMessage();
                    msg.what = mErrorStatusCode;
                    mHandler.sendMessage(msg);
                }
            };
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("name", mFilter.name);
            paramsMap.put("gender", String.valueOf(mFilter.gender));
            paramsMap.put("grade", String.valueOf(mFilter.grade));
            paramsMap.put("classId", String.valueOf(mFilter.clazzId));
            paramsMap.put("className", null);
            paramsMap.put("major", mFilter.major);
            paramsMap.put("cityId", String.valueOf(mFilter.cityId));
            paramsMap.put("cityName", null);
            paramsMap.put("company", mFilter.company);
            String subUrl = "/api/alumni";
            try {
                subUrl = subUrl + (Judge.isNullOrEmpty(paramsMap) ? ""
                        : ("?" + CSTHttpUtil.paramsToString(paramsMap)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            CSTJsonRequest activityRequest = new CSTJsonRequest(CSTRequest.Method.GET, subUrl, null,
                    activityResponse);
            mEngine.requestJson(activityRequest);
        } else {
            Message msg = mHandler.obtainMessage();
            msg.what = Constants.NETWORK_NOT_CONNECTED;
            mHandler.sendMessage(msg);
        }

    }

    /**
     * 初始化城市列表
     */
    private String getCityName(int cityId) {
        List<CSTCity> mListCity = CSTCityDataDelegate.getCityList(this.getActivity());
        if (!Judge.isNullOrEmpty(mListCity)) {
            for (CSTCity city : mListCity) {
                if (cityId == city.id) {
                    return city.name;
                }
            }
            Lgr.i("get city name");
        }
        return null;
    }


    /**
     * 获取当前城市的名字
     */
    private static String getCityName(Context context) {
        LocationManager locationManager;
        String contextString = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) context.getSystemService(contextString);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String cityName = null;
        //
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider == null) {
            return null;
        }
        // 得到坐标相关的信息
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            return null;
        }

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            // 更具地理环境来确定编码
            Geocoder gc = new Geocoder(context, Locale.CHINA);
            try {
                // 取得地址相关的一些信息\经度、纬度
                List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    sb.append(address.getLocality()).append("\n");
                    cityName = sb.toString();
                    int index = cityName.indexOf("市");
                    cityName = (String) cityName.subSequence(0, index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cityName;
    }


    /**
     * 列表Adapter
     */
    public class CSTContactListAdapter extends CursorAdapter implements PinnedSectionListView.
            PinnedSectionListAdapter, Filterable {

        private ViewHolder holder;
        private BaseContactListFragment.FilterType mFilterType;
        private String SECTION = "section";
        private int mType = 0;


        public CSTContactListAdapter(Context context, Cursor c, BaseContactListFragment.FilterType filterType) {
            super(context, c, 0);
            mFilterType = filterType;

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            Lgr.i("ContactListAdapter", "——newView");
            LayoutInflater inflater = LayoutInflater.from(context);
            CSTAlumni alumni;
            if (mFilterType == BaseContactListFragment.FilterType.MY_CITY)
                alumni = CSTAlumniDataDelegate.getAlumni(cursor);
            else
                alumni = CSTAddressListDataDelegate.getAlumni(cursor);
            String sign = alumni.sign;
            String jobTitle = alumni.jobTitle;
            if (jobTitle != null || sign != null) {
                if (jobTitle.equals(SECTION) && sign.equals(SECTION)) {
                    mType = 1;
                    return inflater.inflate(R.layout.contact_section_list_item, parent, false);
                }
            }
            mType = 0;
            return inflater.inflate(R.layout.contact_note_list_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Lgr.i("ContactListAdapter", "——bindView");
            CSTAlumni alumni;
            String chCurrent;
            if (mFilterType == BaseContactListFragment.FilterType.MY_CITY)
                alumni = CSTAlumniDataDelegate.getAlumni(cursor);
            else
                alumni = CSTAddressListDataDelegate.getAlumni(cursor);

            chCurrent = PinYinUtil.converterToFirstSpell(alumni.name).substring(0, 1);

            view.setTag(alumni);

            ViewHolder holder = new ViewHolder();
            String sign = alumni.sign;
            String jobTitle = alumni.jobTitle;
            if (jobTitle != null || sign != null) {
                if (jobTitle.equals(SECTION) && sign.equals(SECTION)) {
                    holder.nameTxv = (TextView) view
                            .findViewById(R.id.contact_note_list_item_index_txv);
                    view.setBackgroundColor(view.getResources().getColor(R.color.deepskyblue));
                    holder.nameTxv.setText(chCurrent);
                } else {
                    holder.nameTxv = (TextView) view
                            .findViewById(R.id.contact_note_list_item_name_txv);
                    holder.nameTxv.setText(alumni.name);
                }

            } else {
                holder.nameTxv = (TextView) view
                        .findViewById(R.id.contact_note_list_item_name_txv);
                holder.nameTxv.setText(alumni.name);
            }
        }


        private int getItemViewType(Cursor cursor) {
            String sign = cursor.getString(cursor.getColumnIndex("sign"));
            String jobTitle = cursor.getString(cursor.getColumnIndex("jobTitle"));
            if (jobTitle != null || sign != null) {
                if (jobTitle.equals(SECTION) && sign.equals(SECTION)) {
                    return 1;
                }
            }
            return 0;
        }


        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            Cursor cursor = (Cursor) getItem(position);
            return getItemViewType(cursor);
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {

            return viewType == 1;
        }

        private final class ViewHolder {

            public TextView nameTxv;

            //索引TextView
//        private TextView indexTxv;
        }


    }

    private class CSTSerachedAlumniAdapter extends BaseAdapter implements PinnedSectionListView.
            PinnedSectionListAdapter, Filterable {

        private List<CSTAlumni> alumniList;

        private Context mContext;

        private ViewHolder holder;

        private ListFilter listFilter;

        private final Object mLock = new Object();

        private String SECTION = "section";

        private ArrayList<CSTAlumni> mOriginalValues;

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
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return toGetItemViewType(position);
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CSTAlumni alumni;
            alumni = alumniList.get(position);
            String sign = alumni.sign;
            String jobTitle = alumni.jobTitle;

            if (convertView == null) {
                holder = new ViewHolder();
                if (jobTitle != null && sign != null && jobTitle.equals(SECTION) && sign.equals(SECTION)) {
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.contact_section_list_item, null);
                    holder.nameTxv = (TextView) convertView
                            .findViewById(R.id.contact_note_list_item_index_txv);

                } else {
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.contact_note_list_item, null);
                    holder.nameTxv = (TextView) convertView
                            .findViewById(R.id.contact_note_list_item_name_txv);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (jobTitle != null && sign != null) {
                if (jobTitle.equals(SECTION) && sign.equals(SECTION)) {
                    holder.nameTxv.setText(PinYinUtil.converterToFirstSpell(alumni.name).substring(0, 1));
                } else {
                    holder.nameTxv.setText(alumni.name);
                }
            } else {
                holder.nameTxv.setText(alumni.name);
            }

            return convertView;
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return viewType == 1;
        }

        @Override
        public Filter getFilter() {
            if (listFilter == null) {
                listFilter = new ListFilter();
            }
            return listFilter;
        }

        private int toGetItemViewType(int position) {
            CSTAlumni alumni = alumniList.get(position);
            String sign = alumni.sign;
            String jobTitle = alumni.jobTitle;
            if (jobTitle != null && sign != null) {
                if (jobTitle.equals(SECTION) && sign.equals(SECTION)) {
                    return 1;
                } else {
                    return 0;
                }
            }
            return 0;
        }

        private class ViewHolder {

            //姓名TextView
            TextView nameTxv;
//        //索引TextView
//        TextView indexTxv;

        }

        class ListFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                Lgr.i("prefix String", prefix.toString());

                // 持有过滤操作完成之后的数据。该数据包括过滤操作之后的数据的值以及数量。 count:数量 values包含过滤操作之后的数据的值
                FilterResults results = new FilterResults();

                if (mOriginalValues == null) {
                    synchronized (mLock) {
                        // 将list的用户 集合转换给这个原始数据的ArrayList
                        mOriginalValues = new ArrayList<CSTAlumni>(alumniList);
                    }
                }
                if (prefix == null || prefix.length() == 0) {
                    synchronized (mLock) {
                        ArrayList<CSTAlumni> list = new ArrayList<CSTAlumni>(
                                mOriginalValues);
                        results.values = list;
                        results.count = list.size();
                        mAlumniList = list;
                    }
                } else {
                    // 做正式的筛选
                    String prefixString = PinYinUtil.getPingYin(prefix.toString());

                    // 声明一个临时的集合对象 将原始数据赋给这个临时变量
                    final ArrayList<CSTAlumni> values = mOriginalValues;

                    final int count = values.size();

                    // 新的集合对象
                    final ArrayList<CSTAlumni> newValues = new ArrayList<CSTAlumni>(
                            count);

                    for (int i = 0; i < count; i++) {
                        // 如果姓名的前缀相符或者电话相符就添加到新的集合
                        final CSTAlumni value = (CSTAlumni) values.get(i);

                        Lgr.i("coder", "PinyinUtils.getAlpha(value.getUsername())"
                                + PinYinUtil.getPingYin(value.name));
                        if (PinYinUtil.getPingYin(value.name).startsWith(prefixString)) {

                            newValues.add(value);
                        }
                    }
                    mAlumniList = newValues;
                    // 然后将这个新的集合数据赋给FilterResults对象
                    results.values = newValues;
                    results.count = newValues.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                // 重新将与适配器相关联的List重赋值一下
                alumniList = (List<CSTAlumni>) results.values;
//                mAlumniList = (List<CSTAlumni>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

        }
    }


}

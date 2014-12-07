package cn.edu.zju.isst1.v2.contact.contact.gui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.gui.CSTBaseFragment;
import cn.edu.zju.isst1.v2.login.net.UpDateLogin;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;
import cn.edu.zju.isst1.v2.user.data.CSTUser;
import cn.edu.zju.isst1.v2.user.data.CSTUserDataDelegate;
import cn.edu.zju.isst1.v2.user.data.CSTUserProvider;


/**
 * Created by tan on 8/26/14.
 */
public class BaseContactListFragment extends CSTBaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {

    private static BaseContactListFragment INSTANCE_MYCLASS = new BaseContactListFragment();

    private static BaseContactListFragment INSTANCE_MYCITY = new BaseContactListFragment();

    private ListView mListView;

    private CSTAlumni mAlumni;

    private CSTContactFilter mFilter = new CSTContactFilter();

    private CSTContactListAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Handler mHandler;

    private CSTUser mUser;

    private TextView clazzTvx;

    private Button searchBtn;

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

        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        Lgr.d("BaseContactListFragment", "——onActivityCreated");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        Lgr.d("BaseContactListFragment", "——onCreateOptionsMenu");
        if (m_ft == FilterType.MY_CLASS || m_ft == FilterType.MY_FILTER) {
            inflater.inflate(R.menu.alumni_list_fragment_ab_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_alumni_filter:
                Intent intent = new Intent(getActivity(),
                        ContactFilterActivity.class);
                startActivityForResult(intent, 20);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void initComponent(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorScheme(R.color.deepskyblue, R.color.deepskyblue, R.color.white,
                R.color.white);
        mListView = (ListView) view.findViewById(R.id.simple_list);
        clazzTvx = (TextView) view.findViewById(R.id.filter_show_txv);
        searchBtn = (Button) view.findViewById(R.id.filter_show_search_btn);
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
            clazzTvx.setText(mUser.cityName);
        }

        initHandler();

        bindAdapter();

        setUpListener();

    }

    @Override
    public void onRefresh() {
        requestData();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
       /* return CSTAlumniDataDelegate.getDataCursor(getActivity(),null,null,null, CSTAlumniProvider
                .Columns.NAME.key + " DESC");*/
        if (m_ft == FilterType.MY_CITY)
            return CSTAlumniDataDelegate.getDataCursor(getActivity(), null, null, null, null);
        return CSTAddressListDataDelegate.getDataCursor(getActivity(), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAlumni = (CSTAlumni) view.getTag();
        Lgr.i("Alumni Name", mAlumni.name);
        Intent intent = new Intent(getActivity(), CSTContactDetailActivity.class);

        intent.putExtra("alumni", ((CSTAlumni) view.getTag()));

        getActivity().startActivity(intent);
    }

    private void bindAdapter() {
        mAdapter = new CSTContactListAdapter(getActivity(), null, m_ft);
        mListView.setAdapter(mAdapter);
        requestData();
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
}

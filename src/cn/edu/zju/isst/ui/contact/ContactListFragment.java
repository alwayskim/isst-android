/**
 *
 */
package cn.edu.zju.isst.ui.contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cn.edu.zju.isst.R;
import cn.edu.zju.isst.api.AlumniApi;
import cn.edu.zju.isst.constant.Constants;
import cn.edu.zju.isst.db.DataManager;
import cn.edu.zju.isst.db.User;
import cn.edu.zju.isst.exception.ExceptionWeeder;
import cn.edu.zju.isst.exception.HttpErrorWeeder;
import cn.edu.zju.isst.net.CSTResponse;
import cn.edu.zju.isst.net.NetworkConnection;
import cn.edu.zju.isst.net.RequestListener;
import cn.edu.zju.isst.util.Judge;
import cn.edu.zju.isst.util.Lgr;

import static cn.edu.zju.isst.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * 通讯录列表页
 *
 * @author yyy
 *         <p/>
 *         TODO WIP
 */
public class ContactListFragment extends Fragment {

    private static ContactListFragment INSTANCE_MYCLASS = new ContactListFragment();

    private static ContactListFragment INSTANCE_MYCITY = new ContactListFragment();

    private final List<User> m_listUser = new ArrayList<User>();

    private HandlerAlumniList m_handlerAlumniList;

    private ListView m_lvAlumni;

    private TextView m_tvFilterCondition;

    private Button m_btnClearFilter;

    private ContactFilter m_userFilter = new ContactFilter();

    private List<NoteBookItem> m_noteBookList = new ArrayList<NoteBookItem>();

    private NoteBookadapter m_noteBookAdapter;

    //表示实例的调用类型，显示本班还是本城市
    //private static FilterType m_flag;
    private FilterType m_ft;

    public ContactListFragment() {
    }

    public static ContactListFragment getInstance(FilterType ft) {
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

    /**
     * 初始化本班通讯录列表，若有缓存则读取缓存，无缓存请求数据
     */
    private void initDate() {
        // 获取用户班级ID
        User user = DataManager.getCurrentUser();
        m_userFilter.classId = user.getClassId();

        // 初始化通讯录列表
        List<User> dbAlumniList = DataManager.getClassMateList();
        if (!m_listUser.isEmpty()) {
            m_listUser.clear();
        }
        if (!Judge.isNullOrEmpty(dbAlumniList)) {
            for (User Alumni : dbAlumniList) {
                m_listUser.add(Alumni);
            }
            getNoteBookData();
        } else {
            requestData();
        }
    }

    /**
     * 请求数据
     *
     * @param type 加载方式
     */
    private void requestData() {
        if (NetworkConnection.isNetworkConnected(getActivity())) {
            getUserListFromApi(m_userFilter);

        } else {
            Message msg = m_handlerAlumniList.obtainMessage();
            msg.what = NETWORK_NOT_CONNECTED;
            m_handlerAlumniList.sendMessage(msg);
        }
    }

    /**
     * 填充m_noteBookList数据
     */
    private void getNoteBookData() {
        if (m_noteBookList.size() != 0) {
            m_noteBookList.clear();
        }
        for (int i = 0; i < m_listUser.size(); i++) {
            NoteBookItem n = new NoteBookItem();
            if (m_listUser.get(i).getName() != null) {
                n.name = m_listUser.get(i).getName();
                n.index = Pinyin4j.getHanyuPinyi(n.name.charAt(0));
                m_noteBookList.add(n);
            }
        }
    }

    /**
     * 显示筛选条件
     */
    private void showFilterConditon() {
        StringBuilder sb = new StringBuilder();

        if (m_ft == FilterType.MY_CLASS) {
            sb.append(" 班级：" + "本班");
        }
        if (!Judge.isNullOrEmpty(m_userFilter.name)) {
            sb.append(" 姓名：" + m_userFilter.name);
        }
        if (m_userFilter.gender != 0) {
            sb.append(" 性别：" + m_userFilter.genderString);
        }
        if (m_userFilter.grade != 0) {
            sb.append(" 年级：" + m_userFilter.grade);
        }
        if (!Judge.isNullOrEmpty(m_userFilter.major)) {
            sb.append(" 方向：" + m_userFilter.major);
        }
        if (!Judge.isNullOrEmpty(m_userFilter.company)) {
            sb.append(" 公司：" + m_userFilter.company);
        }
        if (m_userFilter.cityId != 0) {
            sb.append(" 城市：" + m_userFilter.cityString);
        }
        m_tvFilterCondition.setText(sb.toString());
    }

    /**
     * 调用AlumniApi.getUserList
     */
    private void getUserListFromApi(ContactFilter uf) {
        AlumniApi.getUserList(uf.id, uf.name, uf.gender, uf.grade, uf.classId,
                uf.major, uf.cityId, uf.company, new BaseListRequestListener());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.RESULT_CODE_BETWEEN_CONTACT) {
            m_noteBookList.clear();
            m_noteBookAdapter.notifyDataSetChanged();
            m_userFilter = (ContactFilter) data.getExtras().getSerializable(
                    "data");
            m_ft = FilterType.MY_FILTER;
            getUserListFromApi(m_userFilter);
            HideClearFilterButtonOrNot();
            showFilterConditon();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_list_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        m_handlerAlumniList = new HandlerAlumniList();

        // 控件
        m_lvAlumni = (ListView) view
                .findViewById(R.id.contact_list_fragment_contacts_lsv);
        m_tvFilterCondition = (TextView) view
                .findViewById(R.id.contact_list_fragment_conditon_content_txv);
        m_btnClearFilter = (Button) view
                .findViewById(R.id.contact_list_fragment_clear_condition_btn);

        //清空筛选条件
        m_userFilter.clear();
        //如果是筛选本班
        if (m_ft == FilterType.MY_CLASS) {
            // 初始化数据
            initDate();
        }
        //筛选同城
        else if (m_ft == FilterType.MY_CITY) {
            m_userFilter.cityId = DataManager.getCurrentUser().getCityId();
            Lgr.i("yyy" + m_userFilter.cityId);
            m_userFilter.cityString = "同城";
            getUserListFromApi(m_userFilter);
        }

        m_noteBookAdapter = new NoteBookadapter(getActivity(), m_noteBookList);
        m_lvAlumni.setAdapter(m_noteBookAdapter);

        m_lvAlumni.setOnItemClickListener(new onNotebookItemClickListener());
        m_btnClearFilter.setOnClickListener(new onClearFilterClickListner());

        HideClearFilterButtonOrNot();
        showFilterConditon();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        if (m_ft == FilterType.MY_CLASS || m_ft == FilterType.MY_FILTER) {
            inflater.inflate(R.menu.alumni_list_fragment_ab_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_alumni_filter:
                Intent intent = new Intent(getActivity(),
                        cn.edu.zju.isst.v2.contact.ContactFilterActivity.class);
                startActivityForResult(intent, 20);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void HideClearFilterButtonOrNot() {
        if (m_ft != FilterType.MY_FILTER) {
            m_btnClearFilter.setVisibility(View.GONE);
        } else {
            m_btnClearFilter.setVisibility(View.VISIBLE);
        }
    }

    public enum FilterType {
        MY_CLASS, MY_CITY, MY_FILTER
    }

    private class BaseListRequestListener implements RequestListener {

        @Override
        public void onComplete(Object result) {
            Message msg = m_handlerAlumniList.obtainMessage();
            try {
                msg.what = ((JSONObject) result).getInt("status");
                JSONArray jsonArray = ((JSONObject) result)
                        .getJSONArray("body");
                m_listUser.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        m_listUser.add(new User((JSONObject) jsonArray.get(i)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                Lgr.i(this.getClass().getName() + " onComplete!");
                e.printStackTrace();
            }

            m_handlerAlumniList.sendMessage(msg);
        }

        @Override
        public void onHttpError(CSTResponse response) {
            Lgr.i(this.getClass().getName() + " onHttpError!");
            Message msg = m_handlerAlumniList.obtainMessage();
            HttpErrorWeeder.fckHttpError(response, msg);
            m_handlerAlumniList.sendMessage(msg);
        }

        @Override
        public void onException(Exception e) {
            Lgr.i(this.getClass().getName() + " onException!");
            Message msg = m_handlerAlumniList.obtainMessage();
            ExceptionWeeder.fckException(e, msg);
            m_handlerAlumniList.sendMessage(msg);
        }
    }

    private class onNotebookItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
            Intent intent = new Intent(getActivity(),
                    ContactDetailActivity.class);
            intent.putExtra("user", m_listUser.get(arg2));
            ContactListFragment.this.getActivity().startActivity(intent);
        }
    }

    private class HandlerAlumniList extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STATUS_REQUEST_SUCCESS:
                    Collections.sort(m_listUser, new Pinyin4j.PinyinComparator());
                    if (m_ft == FilterType.MY_CLASS) {
                        DataManager.syncClassMateList(m_listUser);
                    }
                    getNoteBookData();
                    m_noteBookAdapter.notifyDataSetChanged();
                    break;
                case STATUS_NOT_LOGIN:
                    break;
                default:
                    break;
            }
        }
    }

    private class onClearFilterClickListner implements OnClickListener {

        @Override
        public void onClick(View v) {
            //清空筛选条件
            m_userFilter.clear();
            showFilterConditon();
            m_ft = FilterType.MY_CLASS;
            initDate();
            m_noteBookAdapter.notifyDataSetChanged();
            HideClearFilterButtonOrNot();
        }

    }
}

package cn.edu.zju.isst1.v2.contact;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.ui.contact.ContactFilter;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTAlumni;
import cn.edu.zju.isst1.v2.contact.contact.data.Pinyin4j;
import cn.edu.zju.isst1.v2.contact.contact.gui.CSTSearchedAlumniActivity;
import cn.edu.zju.isst1.v2.data.CSTMajor;
import cn.edu.zju.isst1.v2.globaldata.citylist.CSTCity;
import cn.edu.zju.isst1.v2.globaldata.citylist.CSTCityDataDelegate;
import cn.edu.zju.isst1.v2.globaldata.majorlist.CSTMajorDataDelegate;
import cn.edu.zju.isst1.v2.gui.CSTBaseFragment;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * Created by always on 9/11/2014.
 */
public class ContactFilterFragment extends CSTBaseFragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {

    private ListView mDrawerList;

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private RelativeLayout mDrawerRelative;

    private AlertDialog.Builder mAldDelete;

    private CSTAlumni mAlumni;

    private ContactFilter contactFilter = new ContactFilter();

    private List<CSTAlumni> mListAlumni = new ArrayList<CSTAlumni>();

    private List<CSTCity> mListCity = new ArrayList<CSTCity>();

    private List<CSTMajor> mListMajor = new ArrayList<CSTMajor>();

    private ArrayList<String> mArrayListCity = new ArrayList<String>();

    private ArrayList<String> mArrayListMajor = new ArrayList<String>();

    private ArrayList<String> mArrayListGrade = new ArrayList<String>();

    // 控件
    private EditText mEdtName;

    private RadioGroup mRdgGender;

    private Spinner mSpnGrade;

    private EditText mEdtCompany;

    private Spinner mSpnMajor;

    private Spinner mSpinner;

    private Button mBtnOK;

    private Button mBtnCancel;

    private ContactFilterListAdapter mAdapter;

    private TextView mDeleteAllHistory;

    private boolean mIsDefaultSet = true;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private Handler mHandler;

    private final static String ALUMNI_RESULT = "alumniResult";

    private final static String CONTACT_FILTER = "contactFilter";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_filter_drawer_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
        initAlertDialog();
        bindAdapter();
        getLoaderManager().initLoader(0, null, this);
        setUpListener();
        setUpDrawer(view);

        // 获取数据库中的城市列表，专业列表
        getCityList();
        getMajorList();
        //获取年级列表
        getGradeList();
        initHandler();

        // 设置下拉框
        initSpanner(mSpinner, mArrayListCity);
        initSpanner(mSpnMajor, mArrayListMajor);
        initSpanner(mSpnGrade, mArrayListGrade);

        if (CSTContactFilterDelegate.isFilterRecordsEmpty(getActivity())) {
            mDeleteAllHistory.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void initComponent(View view) {
        mEdtName = (EditText) view.findViewById(R.id.contact_filter_activity_name_edtx);
        mRdgGender = (RadioGroup) view.findViewById(R.id.contact_filter_activity_gender_rdg);
        mSpnGrade = (Spinner) view.findViewById(R.id.contact_filter_activity_grade_spn);
        mSpnMajor = (Spinner) view.findViewById(R.id.contact_filter_activity_major_spn);
        mSpinner = (Spinner) view.findViewById(R.id.contact_filter_activity_city_spn);
        mEdtCompany = (EditText) view.findViewById(R.id.contact_filter_activity_company_edtx);
        mBtnOK = (Button) view.findViewById(R.id.contact_filter_activity_confirm_btn);
        mBtnCancel = (Button) view.findViewById(R.id.contact_filter_activity_cancel_btn);
        mDrawerLayout = (DrawerLayout) view
                .findViewById(R.id.contact_filter_drawer_fragment_layout);
        mDrawerRelative = (RelativeLayout) view.findViewById(
                R.id.contact_filter_right_drawer);
        mDrawerList = (ListView) view.findViewById(R.id.contact_filter_right_drawer_list);
        mDeleteAllHistory = (TextView) view.findViewById(R.id.footer_delete_all_history);
    }

    void setUpListener() {
        mBtnOK.setOnClickListener(new onBtnOkClickListener());
        mBtnCancel.setOnClickListener(new onBtnCancelClickListener());
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDeleteAllHistory.setOnClickListener(new onTxvDeleteHistoryClickListener());

    }

    private void setUpDrawer(View view) {
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().getActionBar().setTitle(R.string.filter_filter_history);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().getActionBar().setTitle(R.string.action_filter);
                getActivity().invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return CSTContactFilterDelegate.getDataCursor(getActivity(), null, null, null,
                null);
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

    }

    /**
     * 自定义函数，初始化下拉框
     *
     * @param spanner 下拉框控件
     * @param list    绑定字符串数组
     */
    private void initSpanner(Spinner spanner, ArrayList<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        // 设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将adapter 添加到spinner中
        spanner.setAdapter(adapter);
    }

    private void bindAdapter() {
        mAdapter = new ContactFilterListAdapter(getActivity(), null);
        mDrawerList.setAdapter(mAdapter);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            mDrawerList.setItemChecked(position, true);
            ContactFilter uf;
            uf = (ContactFilter) view.getTag();
            mEdtName.setText(uf.name);
            mEdtCompany.setText(uf.company);
            switch (uf.gender) {
                case 0:
                    mRdgGender.check(R.id.contact_filter_activity_gender_unset_rdbtn);
                    break;
                case 1:
                    mRdgGender.check(R.id.contact_filter_activity_gender_male_rdbtn);
                    break;
                case 2:
                    mRdgGender.check(R.id.contact_filter_activity_gender_female_rdbtn);
                    break;
                default:
                    break;
            }

            for (int i = 0; i < mArrayListCity.size(); i++) {
                if (mArrayListCity.get(i).equals(uf.cityString)) {
                    mSpinner.setSelection(i, true);
                    break;
                }
                if (i == mArrayListCity.size() - 1) {
                    mSpinner.setSelection(0, true);
                }
            }

            for (int i = 0; i < mArrayListGrade.size(); i++) {
                if (mArrayListGrade.get(i).equals(String.valueOf(uf.grade))) {
                    mSpnGrade.setSelection(i, true);
                    break;
                }
                if (i == mArrayListGrade.size() - 1) {
                    mSpnGrade.setSelection(0, true);
                }
            }

            for (int i = 0; i < mArrayListMajor.size(); i++) {
                if (mArrayListMajor.get(i).equals(uf.major)) {
                    mSpnMajor.setSelection(i, true);
                    break;
                }
                if (i == mArrayListMajor.size() - 1) {
                    mSpnMajor.setSelection(0, true);
                }
            }

            mDrawerLayout.closeDrawer(mDrawerRelative);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_filter_history:
                if (mDrawerLayout.isDrawerOpen(mDrawerRelative)) {
                    mDrawerLayout.closeDrawer(mDrawerRelative);
                } else {
                    mDrawerLayout.openDrawer(mDrawerRelative);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initAlertDialog() {
        mAldDelete = new AlertDialog.Builder(getActivity());
        mAldDelete.setTitle(R.string.filter_delete_all_alert_title);
        mAldDelete.setMessage(R.string.filter_delete_all_alert_message);
        mAldDelete.setPositiveButton(R.string.OK,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CSTContactFilterDelegate.deleteAllFilter(getActivity());
                        mDeleteAllHistory.setVisibility(View.GONE);
                    }
                }
        );
        mAldDelete.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }
        );
        mAldDelete.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        Collections.sort(mListAlumni, new Pinyin4j.PinyinComparator());
                        Intent intent = new Intent(getActivity(), CSTSearchedAlumniActivity.class);
                        intent.putExtra(ALUMNI_RESULT, (java.io.Serializable) mListAlumni);
                        intent.putExtra(CONTACT_FILTER, contactFilter);
                        startActivity(intent);
                        // 关闭掉这个Activity
                        getActivity().finish();
                        break;
                    case NETWORK_NOT_CONNECTED:
                        CroMan.showAlert(getActivity(), R.string.network_not_connected);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    class onBtnOkClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mIsDefaultSet = true;

            // 姓名
            String name = mEdtName.getText().toString().trim();
            int genderId = 0;
            String genderString = "";
            int radioBtnId = mRdgGender.getCheckedRadioButtonId();
            switch (radioBtnId) {
                case R.id.contact_filter_activity_gender_unset_rdbtn:
                    break;
                case R.id.contact_filter_activity_gender_male_rdbtn:
                    genderId = 1;
                    genderString = "男";
                    break;
                case R.id.contact_filter_activity_gender_female_rdbtn:
                    genderId = 2;
                    genderString = "女";
                    break;
                default:
                    break;
            }
            // 城市ID
            int cityId = 0;
            String cityString = "";
            int selectedCityPosition = mSpinner.getSelectedItemPosition() - 1;
            if (selectedCityPosition >= 0) {
                cityId = (mListCity.get(selectedCityPosition)).id;
                cityString = (mListCity.get(selectedCityPosition)).name;
                mIsDefaultSet = false;
            }
            // 专业
            String major = "";
            int selectedmajorPosition = mSpnMajor.getSelectedItemPosition() - 1;
            if (selectedmajorPosition >= 0) {
                major = (mListMajor.get(selectedmajorPosition))
                        .name;
                mIsDefaultSet = false;
            }
            // 年级Id
            int grade = 0;
            int selectGradePosition = mSpnGrade.getSelectedItemPosition();
            if (selectGradePosition >= 1) {
                grade = Integer.valueOf(mArrayListGrade.get(selectGradePosition));
                mIsDefaultSet = false;
            }
            // 公司
            String company = mEdtCompany.getText().toString().trim();
            if (!Judge.isNullOrEmpty(company) || !Judge.isNullOrEmpty(name)) {
                mIsDefaultSet = false;
            }

            contactFilter.name = name;
            contactFilter.gender = genderId;
            contactFilter.cityId = cityId;
            contactFilter.grade = grade;
            contactFilter.major = major;
            contactFilter.company = company;
            contactFilter.cityString = cityString;
            contactFilter.genderString = genderString;

            StringBuilder filter = new StringBuilder();
            filter.append(
                    name + String.valueOf(genderId) + String.valueOf(cityId) + String.valueOf(grade)
                            + major + company
            );
            contactFilter.filterString = filter.toString();

            if (!mIsDefaultSet) {
                CSTContactFilterDelegate.saveFilter(getActivity(), contactFilter);
                mDeleteAllHistory.setVisibility(View.VISIBLE);
            }
            Intent intent = new Intent(getActivity(), CSTSearchedAlumniActivity.class);
            intent.putExtra(CONTACT_FILTER, contactFilter);
            startActivity(intent);
            // 关闭掉这个Activity
            getActivity().finish();

        }
    }

    class onTxvDeleteHistoryClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mAldDelete.show();
        }
    }

    /**
     * 初始化城市列表
     */
    private void getCityList() {
        mArrayListCity
                .add(String.valueOf(getResources().getString(R.string.filter_gender_default)));
        mListCity = CSTCityDataDelegate.getCityList(getActivity());

        if (!Judge.isNullOrEmpty(mListCity)) {
            for (CSTCity city : mListCity) {
                mArrayListCity.add(city.name);
            }
        }
        Lgr.i(" yyy getCityList");
    }

    /**
     * 初始化专业列表
     */
    private void getMajorList() {
        mArrayListMajor
                .add(String.valueOf(getResources().getString(R.string.filter_gender_default)));
        mListMajor = CSTMajorDataDelegate.getMajorList(getActivity());
        if (!Judge.isNullOrEmpty(mListMajor)) {
            for (CSTMajor major : mListMajor) {
                mArrayListMajor.add(major.name);
            }
        }
        Lgr.i(" yyy getMajorList");
    }

    /**
     * 设置年级列表,假设从2009年开始
     */
    private void getGradeList() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        mArrayListGrade
                .add(String.valueOf(getResources().getString(R.string.filter_gender_default)));
        for (int i = 2009; i <= year; i++) {
            mArrayListGrade.add(String.valueOf(i));
        }

    }


    class onBtnCancelClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            getActivity().finish();
        }
    }

}

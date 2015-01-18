package cn.edu.zju.isst1.v2.contact.contact.gui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.constant.Constants;
import cn.edu.zju.isst1.net.BetterAsyncWebServiceRunner;
import cn.edu.zju.isst1.net.NetworkConnection;
import cn.edu.zju.isst1.ui.contact.ContactFilter;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTAlumni;
import cn.edu.zju.isst1.v2.contact.contact.data.Pinyin4j;
import cn.edu.zju.isst1.v2.contact.contact.net.ContactSearchResponse;
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.login.net.UpDateLogin;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;
import cn.edu.zju.isst1.widget.PinnedSectionListView;

import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;


public class CSTSearchedAlumniActivity extends BaseActivity
        implements AdapterView.OnItemClickListener {

    private List<CSTAlumni> alumniList = new ArrayList<CSTAlumni>();

    private List<CSTAlumni> mAlumniList = new ArrayList<CSTAlumni>();

    private PinnedSectionListView mLvAlumni;

    private AutoCompleteTextView autoCompleteTextView;

    private TextView mFilter;

    private CSTSerachedAlumniAdapter alumniAdapter;

    private ContactFilter mContactFilter;

    boolean isOpened = false;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cstsearched_contact);
        mContactFilter = (ContactFilter) getIntent().getExtras().get("contactFilter");
//        MainSearchLayout searchLayout= new MainSearchLayout(this, null);
//        setContentView(searchLayout);

//        setListenerToRootView();
        setUpActionBar();

        initView();
        initHandler();
        requestData();
    }

    private void initView() {
        mLvAlumni = (PinnedSectionListView) findViewById(R.id.list_Alumni);
        mFilter = (TextView) findViewById(R.id.filter_show_txv);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.filter_auto_list_txv);
        autoCompleteTextView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        StringBuilder sb = new StringBuilder();
        sb.append("");
        if (!Judge.isNullOrEmpty(mContactFilter.name)) {
            sb.append("姓名：" + mContactFilter.name);
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
        if (sb.length() <= 1) {
            sb.append("所有校友");
        }
        mFilter.setText(sb.toString());
        mLvAlumni.setOnItemClickListener(this);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.cstsearched_alumni, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CSTSearchedAlumniActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAlumniList.get(position).type != 1) {
            Intent intent = new Intent(CSTSearchedAlumniActivity.this,
                    CSTContactDetailActivity.class);
            intent.putExtra("alumni", mAlumniList.get(position));
            startActivity(intent);
        }
    }

    @Override
    protected void setUpActionBar() {
        super.setUpActionBar();
        setTitle("筛选结果");
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        alumniAdapter = new CSTSerachedAlumniAdapter(CSTSearchedAlumniActivity.this,
                                mAlumniList);
                        mLvAlumni.setAdapter(alumniAdapter);
                        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before,
                                                      int count) {
                                autoCompleteTextView.getText().toString();
                                alumniAdapter.getFilter().filter(autoCompleteTextView.getText().toString());

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
                    case STATUS_NOT_LOGIN:
                        UpDateLogin.getInstance().updateLogin(CSTSearchedAlumniActivity.this);
                        requestData();
                        break;
                    default:
                        CSTHttpUtil.dispose(msg.what, CSTSearchedAlumniActivity.this);
                        break;
                }
            }
        };
    }

    public void requestData() {
        if (NetworkConnection.isNetworkConnected(this)) {
            ContactSearchResponse activityResponse = new ContactSearchResponse(this) {
                @Override
                public void onResponse(JSONObject result) {

                    CSTAlumni alumni = (CSTAlumni) CSTJsonParser
                            .parseJson((JSONObject) result, new CSTAlumni());
                    for (int i = 0; i < alumni.itemList.size(); i++) {

                        alumniList.add((CSTAlumni) alumni.itemList.get(i));
                    }
                    Collections.sort(alumniList, new Pinyin4j.PinyinComparator());

                    String chCurrent, chPre;
                    chPre = "0";
                    for (int i = 0; i < alumniList.size(); i++) {
                        chCurrent = PinYinUtil.converterToFirstSpell(alumniList.get(i).name).substring(0, 1);
                        if (!chCurrent.equals(chPre)) {
                            CSTAlumni section = new CSTAlumni();
                            section.sign = "section";
                            section.jobTitle = "section";
                            section.name = alumniList.get(i).name;
                            section.type = 1;
                            chPre = chCurrent;
                            mAlumniList.add(section);
                        }
                        mAlumniList.add(alumniList.get(i));
                    }
                    Lgr.i(result.toString());
                    Message msg = mHandler.obtainMessage();
                    msg.what = STATUS_REQUEST_SUCCESS;
                    mHandler.sendMessage(msg);
                }
            };

            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("name", mContactFilter.name);
            paramsMap.put("gender", String.valueOf(mContactFilter.gender));
            paramsMap.put("grade", String.valueOf(mContactFilter.grade));
            paramsMap.put("classId", String.valueOf(mContactFilter.classId));
            paramsMap.put("className", null);
            paramsMap.put("major", mContactFilter.major);
            paramsMap.put("cityId", String.valueOf(mContactFilter.cityId));
            paramsMap.put("company", mContactFilter.company);
            String subUrl = "/api/alumni";

            try {
                subUrl = subUrl + (Judge.isNullOrEmpty(paramsMap) ? ""
                        : ("?" + BetterAsyncWebServiceRunner.getInstance()
                        .paramsToString(paramsMap)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            CSTJsonRequest activityRequest = new CSTJsonRequest(CSTRequest.Method.GET, subUrl,
                    null,
                    activityResponse);
            mEngine.requestJson(activityRequest);
        } else {
            Message msg = mHandler.obtainMessage();
            msg.what = Constants.NETWORK_NOT_CONNECTED;
            mHandler.sendMessage(msg);
        }
    }
//
//    public class MainSearchLayout extends LinearLayout {
//
//        public MainSearchLayout(Context context, AttributeSet attributeSet) {
//            super(context, attributeSet);
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            inflater.inflate(R.layout.cstsearched_contact, this);
//        }
//
//        @Override
//        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            Log.d("Search Layout", "Handling Keyboard Window shown");
//
//            final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
//            final int actualHeight = getHeight();
//
//            if (actualHeight > proposedheight){
//                // Keyboard is shown
////                Toast.makeText(getApplicationContext(), "softkeyborad Up!!!", Toast.LENGTH_SHORT).show();
//                autoCompleteTextView.setFocusable(true);
//                autoCompleteTextView.setFocusableInTouchMode(true);
//                autoCompleteTextView.requestFocus();
//            } else {
//                // Keyboard is hidden
////                Toast.makeText(getApplicationContext(), "softkeyborad Down!!!", Toast.LENGTH_SHORT).show();
//                mFilter.setFocusable(true);
//                mFilter.setFocusableInTouchMode(true);
//                mFilter.requestFocus();
//            }
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        }
//    }

    private class CSTSerachedAlumniAdapter extends BaseAdapter implements PinnedSectionListView.
            PinnedSectionListAdapter,Filterable{

        private List<CSTAlumni> alumniList;

        private Context mContext;

        private ViewHolder holder;

        private ListFilter listFilter;

        private final Object mLock = new Object();

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
            return alumniList.get(position).type;
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CSTAlumni alumni;
            alumni = alumniList.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                if (alumni.type == 1) {
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


            if (alumni.type == 1) {
                holder.nameTxv.setText(PinYinUtil.converterToFirstSpell(alumni.name).substring(0, 1));
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


        private class ViewHolder {

            //姓名TextView
            TextView nameTxv;
//        //索引TextView
//        TextView indexTxv;

        }

        class ListFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                Lgr.i("prefix String",prefix.toString());

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
                mAlumniList = (List<CSTAlumni>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

        }
    }


}

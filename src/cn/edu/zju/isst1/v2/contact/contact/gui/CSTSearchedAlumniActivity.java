package cn.edu.zju.isst1.v2.contact.contact.gui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.contact.contact.data.CSTAlumni;
import cn.edu.zju.isst1.v2.contact.contact.data.Pinyin4j;
import cn.edu.zju.isst1.v2.contact.contact.net.ContactResponse;
import cn.edu.zju.isst1.v2.data.CSTJsonParser;
import cn.edu.zju.isst1.v2.login.net.UpDateLogin;
import cn.edu.zju.isst1.v2.net.CSTHttpUtil;
import cn.edu.zju.isst1.v2.net.CSTJsonRequest;
import cn.edu.zju.isst1.v2.net.CSTNetworkEngine;
import cn.edu.zju.isst1.v2.net.CSTRequest;

import static cn.edu.zju.isst1.constant.Constants.NETWORK_NOT_CONNECTED;
import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;


public class CSTSearchedAlumniActivity extends BaseActivity
        implements AdapterView.OnItemClickListener {

    private List<CSTAlumni> alumniList = new ArrayList<CSTAlumni>();

    private ListView mLvAlumni;

    private TextView mFilter;

    private ContactFilter mContactFilter;

    private CSTNetworkEngine mEngine = CSTNetworkEngine.getInstance();

    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cstsearched_contact);
        mContactFilter = (ContactFilter) getIntent().getExtras().get("contactFilter");
        setUpActionbar();
        initView();
        initHandler();
        requestData();
    }

    private void initView() {
        mLvAlumni = (ListView) findViewById(R.id.list_Alumni);
        mFilter = (TextView) findViewById(R.id.filter_show_txv);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(CSTSearchedAlumniActivity.this,
                CSTContactDetailActivity.class);
        intent.putExtra("alumni", alumniList.get(position));
        startActivity(intent);
    }


    private void setUpActionbar() {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_REQUEST_SUCCESS:
                        mLvAlumni.setAdapter(
                                new CSTSerachedAlumniAdapter(CSTSearchedAlumniActivity.this,
                                        alumniList)
                        );
                        break;
                    case STATUS_NOT_LOGIN:
                        UpDateLogin.getInstance().updateLogin(CSTSearchedAlumniActivity.this);
                        requestData();
                        break;
                    default:
                        CSTHttpUtil.dispose(msg.what,CSTSearchedAlumniActivity.this);
                        break;
                }
            }
        };
    }

    public void requestData() {
        if (NetworkConnection.isNetworkConnected(this)) {
            ContactResponse activityResponse = new ContactResponse(this,
                    true) {
                @Override
                public void onResponse(JSONObject result) {

                    CSTAlumni alumni = (CSTAlumni) CSTJsonParser
                            .parseJson((JSONObject) result, new CSTAlumni());
                    for (int i = 0; i < alumni.itemList.size(); i++) {
                        alumniList.add((CSTAlumni) alumni.itemList.get(i));
                    }
                    Collections.sort(alumniList, new Pinyin4j.PinyinComparator());
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
}

/**
 *
 */
package cn.edu.zju.isst1.v2.usercenter.messagecenter.gui;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.api.PushMessageApi;
import cn.edu.zju.isst1.db.PushMessage;
import cn.edu.zju.isst1.net.CSTResponse;
import cn.edu.zju.isst1.net.RequestListener;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.CSTMessage;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.CSTMessageDataDelegate;

import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * @author theasir
 */
public class PushMessagesActivity extends BaseActivity {

    private Handler mHandler;

    private MsgListAdapter mAdapter;

    private ListView mMsgListView;

    private Button mDeleteAllMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_messages_activity);

        CSTMessage mMessage = new CSTMessage();

        Intent intent = getIntent();

        mMessage.title = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
        mMessage.content = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
        mMessage.id = intent.getIntExtra("id", 0);
        CSTMessageDataDelegate.saveMessage(this, mMessage);

        setTitle(R.string.message_center);

        setUpActionbar();

        initComponent();

        setUpAdapter();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                PushMessagesActivity.this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpActionbar() {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        mMsgListView = (ListView) findViewById(R.id.push_msg_list);
//        mDeleteAllMessage = (Button) findViewById(R.id.delete_all_msg);
//        mDeleteAllMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CSTMessageDataDelegate.deleteAllMessage(PushMessagesActivity.this);
//            }
//        });
    }

//    private void initHandler() {
//        mHandler = new Handler() {
//
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case STATUS_REQUEST_SUCCESS:
//                        refreshData((JSONObject) msg.obj);
//                        mAdapter.notifyDataSetChanged();
//                        break;
//
//                    default:
//                        break;
//                }
//            }
//
//        };
//    }

    private void setUpAdapter() {
        mAdapter = new MsgListAdapter(PushMessagesActivity.this);
        mMsgListView.setAdapter(mAdapter);
    }


//    private void refreshData(JSONObject jsonObject) {
//        if (!mMessages.isEmpty()) {
//            mMessages.clear();
//        }
//        try {
//            if (!Judge.isValidJsonValue("body", jsonObject)) {
//                return;
//            }
//            JSONArray jsonArray = jsonObject.getJSONArray("body");
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                mMessages.add(new PushMessage((JSONObject) jsonArray.get(i)));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private final class ViewHolder {

        TextView titleTxv;

        TextView contentTxv;

        TextView createdTimeTxv;
    }

    private class MsgListAdapter extends BaseAdapter {

        CSTMessage mMessageList = CSTMessageDataDelegate.getAllMessage(getApplicationContext());

        private LayoutInflater inflater;

        public MsgListAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mMessageList.itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessageList.itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater
                        .inflate(R.layout.archive_list_item, null);
                holder.titleTxv = (TextView) convertView
                        .findViewById(R.id.title_txv);
                holder.contentTxv = (TextView) convertView
                        .findViewById(R.id.description_txv);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.titleTxv.setText(mMessageList.itemList.get(position).title);
            holder.contentTxv.setText(mMessageList.itemList.get(position).content);

            convertView.findViewById(R.id.publisher_txv)
                    .setVisibility(View.GONE);

            return convertView;
        }

    }

}

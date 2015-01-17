/**
 *
 */
package cn.edu.zju.isst1.ui.job;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.zju.isst1.R;
import cn.edu.zju.isst1.api.JobCommentApi;
import cn.edu.zju.isst1.db.Comment;
import cn.edu.zju.isst1.exception.ExceptionWeeder;
import cn.edu.zju.isst1.exception.HttpErrorWeeder;
import cn.edu.zju.isst1.net.CSTResponse;
import cn.edu.zju.isst1.net.RequestListener;
import cn.edu.zju.isst1.ui.main.BaseActivity;
import cn.edu.zju.isst1.util.CroMan;
import cn.edu.zju.isst1.util.Judge;
import cn.edu.zju.isst1.util.TSUtil;
import cn.edu.zju.isst1.widget.PullToRefeshView;
import cn.edu.zju.isst1.widget.PullToRefeshView.PullToRefreshListener;

import static cn.edu.zju.isst1.constant.Constants.STATUS_NOT_LOGIN;
import static cn.edu.zju.isst1.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * 新闻列表页
 *
 * @author xyj
 */
public class JobCommentListActivity extends BaseActivity implements
        OnScrollListener {

    private final String PUBLIC_SUCCESS = "发布成功";

    private PullToRefeshView m_ptrView;

    private ListView m_commentAreaLstv;

    private ImageButton m_commentSendImgbtn;

    private EditText m_commentSendTxt;

    private SimpleAdapter m_listAdapter;

    private Handler m_handler;

    private List<Map<String, String>> m_commentDate = new ArrayList<Map<String, String>>();

    private int m_jobId;

    private LoadType m_loadType;

    private RequestType m_requestType;

    private int m_nVisibleLastIndex;

    private int m_nCurrentPage;

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setUpActionBar();
        setContentView(R.layout.job_comment_list_activity);
        m_ptrView = (PullToRefeshView) findViewById(R.id.job_comment_list_activity_ptr_view);
        m_ptrView.setOnRefreshListener(new PullToRefreshListener() {

            @Override
            public void onRefresh() {
                requestData(LoadType.REFRESH);
            }
        }, 0);

        m_commentAreaLstv = (ListView) findViewById(android.R.id.list);
        m_commentAreaLstv.setOnScrollListener(this);
        m_commentSendImgbtn = (ImageButton) findViewById(R.id.job_comment_send_imgbtn);
        m_commentSendTxt = (EditText) findViewById(R.id.job_comment_editText);
        m_commentSendImgbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                JobCommentApi.sendJobComment(m_commentSendTxt.getText()
                        .toString(), m_jobId, new SendCommentRequestListener());
            }
        });

        m_nVisibleLastIndex = 0;
        initHandler();
        initListData();
        initAdapter();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem
     * )
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                JobCommentListActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreateView(android.view.View,
     * java.lang.String, android.content.Context, android.util.AttributeSet)
     */
    @Override
    public View onCreateView(View parent, String name, Context context,
            AttributeSet attrs) {
        // TODO Auto-generated method stub
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void setUpActionBar() {
        super.setUpActionBar();
        setTitle("评论");
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE
                && m_nVisibleLastIndex == m_listAdapter.getCount() - 1) {
            requestData(LoadType.LOADMORE);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        m_nVisibleLastIndex = firstVisibleItem + visibleItemCount - 1;
    }

    private void initHandler() {
        m_handler = new Handler() {

            /*
             * (non-Javadoc)
             *
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (m_requestType) {
                    case GET:
                        dealMsgAfterGet(msg);
                        break;
                    case SEND:
                        dealMsgAfterSend(msg);
                        break;
                    default:
                        break;
                }

            }

        };
    }

    private void requestData(LoadType loadType) {
        m_loadType = loadType;
        switch (loadType) {
            case REFRESH:
                m_nCurrentPage = 0;
                JobCommentApi.getJobCommentList(1, m_jobId, 20,
                        new JobCommentListRequestListener());
                break;
            case LOADMORE:
                JobCommentApi.getJobCommentList(m_nCurrentPage + 1, m_jobId, 20,
                        new JobCommentListRequestListener());
            default:
                break;
        }
    }

    private void initListData() {
        m_jobId = this.getIntent().getIntExtra("id", -1);
        requestData(LoadType.REFRESH);
    }

    private void initAdapter() {
        String[] From = new String[]{"name", "date", "content"};
        int[] To = new int[]{R.id.job_comment_list_item_name_txv,
                R.id.job_comment_list_item_date_txv,
                R.id.job_comment_list_item_content_txv};
        // L.i("comment", m_commentDate.toString());
        m_listAdapter = new SimpleAdapter(this, m_commentDate,
                R.layout.job_comment_list_item, From, To);
        m_commentAreaLstv.setAdapter(m_listAdapter);
    }

    private void refresh(JSONObject obj) {
        m_commentDate.clear();
        loadMore(obj);
    }

    private void loadMore(JSONObject obj) {
        m_nCurrentPage++;
        JSONArray jsonArray = new JSONArray();
        try {
            if (Judge.isValidJsonValue("body", obj)) {
                jsonArray = obj.getJSONArray("body");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Comment comment = new Comment(jsonObject);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", comment.getUser().getName());
                    map.put("date", getDataSting(comment.getCreatedAt()));
                    map.put("content", comment.getContent());
                    m_commentDate.add(map);
                }
            }
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private String getDataSting(long date) {
        return TSUtil.toMD(date);
    }

    private void dealMsgAfterGet(Message msg) {
        switch (msg.what) {
            case STATUS_REQUEST_SUCCESS:
                switch (m_loadType) {
                    case REFRESH:
                        m_ptrView.finishRefreshing();
                        refresh((JSONObject) msg.obj);
                        break;
                    case LOADMORE:
                        loadMore((JSONObject) msg.obj);
                        break;
                    default:
                        break;
                }
                m_listAdapter.notifyDataSetChanged();
                break;
            case STATUS_NOT_LOGIN:// TODO
                super.updateLogin();
                requestData(m_loadType);
                break;
            default:
                super.dispose(msg);
                break;
        }
    }

    private void dealMsgAfterSend(Message msg) {
        m_commentSendTxt.setText("");
        collapseSoftInputMethod();
        //L.i("comment",String.valueOf(msg.what));
        switch (msg.what) {

            case STATUS_REQUEST_SUCCESS:
                CroMan.showInfo(this, PUBLIC_SUCCESS);
                requestData(LoadType.REFRESH);
                break;
            case STATUS_NOT_LOGIN:// TODO
                super.updateLogin();
                requestData(m_loadType);
                break;
            default:
                super.dispose(msg);
                break;
        }
    }

    /**
     * 收起软键盘并设置提示文字
     */
    public void collapseSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(m_commentSendTxt.getWindowToken(),
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    enum LoadType {
        REFRESH, LOADMORE
    }

    ;

    enum RequestType {
        SEND, GET
    }

    ;

    public class JobCommentListRequestListener implements RequestListener {

        Message msg = m_handler.obtainMessage();

        @Override
        public void onComplete(Object result) {
            // TODO Auto-generated method stub
            m_requestType = RequestType.GET;
            try {
                msg.what = ((JSONObject) result).getInt("status");
                JSONObject jsonObject = (JSONObject) result;
                msg.obj = (JSONObject) result;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            m_handler.sendMessage(msg);
        }

        @Override
        public void onHttpError(CSTResponse response) {
            HttpErrorWeeder.fckHttpError(response, msg);
            m_handler.sendMessage(msg);
        }

        @Override
        public void onException(Exception e) {
            ExceptionWeeder.fckException(e, msg);
            m_handler.sendMessage(msg);
        }


    }

    public class SendCommentRequestListener implements RequestListener {

        Message msg = m_handler.obtainMessage();

        @Override
        public void onComplete(Object result) {
            // TODO Auto-generated method stub
            m_requestType = RequestType.SEND;
            try {
                msg.what = ((JSONObject) result).getInt("status");
                //	L.i("comment", msg.obj.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            m_handler.sendMessage(msg);
        }

        @Override
        public void onHttpError(CSTResponse response) {
            HttpErrorWeeder.fckHttpError(response, msg);
            m_handler.sendMessage(msg);
        }

        @Override
        public void onException(Exception e) {
            ExceptionWeeder.fckException(e, msg);
            m_handler.sendMessage(msg);
        }

    }

}
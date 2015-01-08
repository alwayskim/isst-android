package cn.edu.zju.isst1.baidupush;

import android.content.Context;
import android.content.Intent;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.frontia.api.FrontiaPushMessageReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.edu.zju.isst1.settings.CSTSettings;
import cn.edu.zju.isst1.ui.main.NewMainActivity;
import cn.edu.zju.isst1.util.Lgr;
import cn.edu.zju.isst1.v2.splash.gui.LoadingActivity;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.CSTMessage;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.CSTMessageDataDelegate;
import cn.edu.zju.isst1.v2.usercenter.messagecenter.gui.PushMessagesActivity;

/**
 * Created by alwayking on 15/1/8.
 */
public class MyPushMessageReceiver extends FrontiaPushMessageReceiver {

    /**
     * 调用 PushManager.startWork 后,sdk 将对 push server 发起绑定请求,这个过程是异步
     * 的。绑定请求的结果通过 onBind 返回。
     */
    @Override
    public void onBind(Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId + " requestId=" + requestId;
    }

    /**
     * 接收透传消息的函数。
     */
    @Override
    public void onMessage(Context context, String message, String customContentString) {

        String messageString = "透传消息 message=" + message +
                " customContentString=" + customContentString;
        Lgr.d(TAG, messageString);

        // 自定义内容获取方式,mykey 和 myvalue 对应透传消息推送时自定义内容中设置的键和值
        if (customContentString != null & customContentString != "") {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
            }
        }
    }

    //接收通知点击的函数。注:推送通知被用户点击前,应用无法通过接口获取通知的内容
    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        String notifyString = "通知点击 title=" + title + " description="
                + description + " customContent=" + customContentString;
        Lgr.d(TAG, notifyString);

        // 自定义内容获取方式,mykey 和 myvalue 对应通知推送时自定义内容中设置
        if (customContentString != null & customContentString != "") {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block e.printStackTrace();
            }
        }

        CSTMessage message = CSTMessageDataDelegate.getAllMessage(context);
        CSTMessage message1 = new CSTMessage();
        int id = message.itemList.size() + 1;
        Long createtime = System.currentTimeMillis();
        message1.id = id;
        message1.title = title;
        message1.content = description;
        message1.createdAt = Long.toString(createtime);
        CSTMessageDataDelegate.saveMessage(context, message1);
        showActivity(context);

    }

    private void showActivity(final Context context) {
        // TODO Auto-generated method stub


        Intent aIntent = new Intent();

        aIntent.putExtra("push", true);
        if (!CSTSettings.isAutoLogin(context)) {
            aIntent.setClass(context, LoadingActivity.class);
            aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(aIntent);
        } else if(CSTSettings.isPushActivityOn(context)){
        }else if (CSTSettings.isNewMainOn(context)) {
            aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            aIntent.setClass(context, PushMessagesActivity.class);
            context.startActivity(aIntent);
        } else {
            aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            aIntent.setClass(context, NewMainActivity.class);
            context.startActivity(aIntent);
        }

    }

    /**
     * setTags() 的回调函数。
     */
    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode + " sucessTags="
                + sucessTags + " failTags=" + failTags + " requestId=" + requestId;
    }

    /**
     * delTags() 的回调函数。
     */
    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode + " sucessTags="
                + sucessTags + " failTags=" + failTags + " requestId=" + requestId;
    }

    /**
     * listTags() 的回调函数。
     */
    @Override
    public void onListTags(Context context, int errorCode,
                           List<String> tags, String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags=" + tags;
    }

    /**
     * PushManager.stopWork() 的回调函数。
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode + " requestId = " + requestId;
    }



}

package com.jinjunhang.contract.service;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import com.jinjunhang.contract.db.ApprovalNotificationStore;
import com.jinjunhang.contract.model.Approval;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushReceiver;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by lzn on 16/5/22.
 */
public class MyPushReceiver extends XGPushBaseReceiver {

    private final static String TAG = "MyPushReceiver";

    public final static String INTENT_NAME = "com.jinjunhang.contract.controller.getApproval";
    public final static String EXTRA_APPROVAL = "com.jinjunhang.contract.controller.getApproval.approval";
    private Intent intent = new Intent(INTENT_NAME);



    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        Log.d(TAG, "onTextMessage called");



        Log.d(TAG, "titel = " + xgPushTextMessage.getTitle());
        Log.d(TAG, "content = " + xgPushTextMessage.getContent());
        Log.d(TAG, "customContent = " + xgPushTextMessage.getCustomContent());


    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        Log.d(TAG, "onNotifactionShowedResult");
        if (context == null || xgPushShowedResult == null) {
            return;
        }


        Log.d(TAG, "customContent = " + xgPushShowedResult.getCustomContent());


        Log.d(TAG, "titel = " + xgPushShowedResult.getTitle());
        Log.d(TAG, "content = " + xgPushShowedResult.getContent());
        Log.d(TAG, "customContent = " + xgPushShowedResult.getCustomContent());
        Log.d(TAG, "notificationActionType = " + xgPushShowedResult.getNotificationActionType());
        Log.d(TAG, "activity = " + xgPushShowedResult.getActivity());

        //解析xgPushSHowedResult
        Approval approval = new Approval();
        approval.setApprovalObject("approvalObject");
        approval.setReporter("jjh");

        intent.putExtra(EXTRA_APPROVAL, approval);

        //将信息保存到数据库，然后Activity从数据库中进行读取
        int badgeCount = getBadge(xgPushShowedResult.getCustomContent());
        ApprovalNotificationStore.getInstance(context).save(approval, badgeCount);

        ShortcutBadger.applyCount(context, badgeCount); //for 1.1.4
       // ShortcutBadger.with(context).count(badgeCount); //for 1.1.3
        context.sendBroadcast(intent);


    }

    public int getBadge(String customContent) {
        try {
            final JSONObject obj = new JSONObject(customContent);
            return obj.getInt("badge");

        } catch (JSONException ex) {
            Log.e(TAG, ex.toString());
            return 0;
        }
    }
}

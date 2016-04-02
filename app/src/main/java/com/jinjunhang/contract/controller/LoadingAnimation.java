package com.jinjunhang.contract.controller;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by lzn on 16/4/2.
 */
public class LoadingAnimation {

    private ProgressDialog mDialog;

    public LoadingAnimation(Context context) {
        mDialog = new ProgressDialog(context);
    }

    public void show(String message) {
        if (message == null || message.isEmpty())
            message = "加载中...";
        mDialog.setMessage(message);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }


}

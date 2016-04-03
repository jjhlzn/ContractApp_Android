package com.jinjunhang.contract.controller;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by lzn on 16/4/3.
 */
public class Utils {

    public static void showMessage(Context context, String message) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setPositiveButton("OK", null);
        //dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public static void showServerErrorDialog(Context context){
        showMessage(context, "服务器返回出错!");
    }
}

package com.jinjunhang.contract.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by lzn on 16/4/3.
 */
public class Utils {

    public static void showMessage(Context context, String message) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setPositiveButton("确定", null);
        //dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }


    public static void showConfirmMessage(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setPositiveButton("确定", listener);
        dlgAlert.setNegativeButton("取消", null);
       // dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }


    public static void showServerErrorDialog(Context context){
        showMessage(context, "服务器返回出错!");
    }
}

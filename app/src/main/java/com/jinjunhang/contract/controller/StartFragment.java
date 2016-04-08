package com.jinjunhang.contract.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinjunhang.contract.R;

/**
 * Created by lzn on 16/4/7.
 */
public class StartFragment extends android.support.v4.app.Fragment {
    private final static String TAG = "StartFragment";

    private final int SPLASH_DELAY_MILLIS = 1000;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        Intent i = new Intent(getActivity(), MainActivity2.class);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                goHome();
            }
        }, SPLASH_DELAY_MILLIS);

        return v;
    }

    private void goHome() {
        String isLoginValue = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_ISLOGIN_KEY, "0");
        Log.d(TAG, "isLoginValue = " + isLoginValue);
        boolean isLogin =  isLoginValue.equals("1");
        Intent intent = null;
        if (isLogin) {
            intent = new Intent(getActivity(), MainActivity2.class);
        } else {
            intent = new Intent(getActivity(), LoginActivity.class);
        }

        startActivity(intent);
    }
}

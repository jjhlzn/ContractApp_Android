package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/4/7.
 */
public class LoginActivity extends SingleFragmentActivity {

    @Override
    protected String getActivityTitle() {
        return "登录";
    }

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }
}

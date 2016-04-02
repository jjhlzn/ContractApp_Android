package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/4/3.
 */
public class MyInfoActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MyInfoFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "我";
    }
}

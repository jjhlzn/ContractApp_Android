package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/5/7.
 */
public class ConfigurationActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ConfigurationFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "服务器配置";
    }
}

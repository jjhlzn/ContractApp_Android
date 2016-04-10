package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/3/25.
 */
public class OrderShouhuiActivity extends SingleFragmentActivity {

    @Override
    protected String getActivityTitle() {
        return "收汇信息";
    }

    @Override
    protected Fragment createFragment() {
        return new OrderShouhuiFragment();
    }
}

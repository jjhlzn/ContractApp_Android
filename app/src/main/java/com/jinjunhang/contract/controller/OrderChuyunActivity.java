package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/3/24.
 */
public class OrderChuyunActivity extends SingleFragmentActivity {

    @Override
    protected String getActivityTitle() {
        return "出运信息";
    }

    @Override
    protected Fragment createFragment() {
        return new OrderChuyunFragment();
    }
}

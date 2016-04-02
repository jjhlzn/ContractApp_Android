package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/3/24.
 */
public class OrderFukuangActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new OrderFukuangFragment();
    }
}

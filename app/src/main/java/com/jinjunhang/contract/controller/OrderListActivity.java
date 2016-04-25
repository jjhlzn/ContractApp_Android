package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/3/23.
 */
public class OrderListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new OrderListFragment();
    }


    @Override
    protected boolean isNeedPushDownFresh() {
        return true;
    }

    @Override
    protected String getActivityTitle() {
        return "订单列表";
    }
}

package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/3/24.
 */
public class OrderBasicInfoActivity extends SingleFragmentActivity  {

    @Override
    protected Fragment createFragment() {
        return new OrderBasicInfoFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "合同基本信息";
    }
}

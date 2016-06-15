package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/6/14.
 */
public class PriceReportDetailActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new PriceReportDetailFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "报价单详情";
    }
}

package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/6/15.
 */
public class PriceReportSearchActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PriceReportSearchFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "报价";
    }
}

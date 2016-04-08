package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/4/8.
 */
public class ProductDetailActivity extends SingleFragmentActivity {

    @Override
    protected String getActivityTitle() {
        return "商品详情";
    }

    @Override
    protected Fragment createFragment() {
        return new ProductDetailFragment();
    }
}

package com.example.bottombar.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/3/22.
 */
public class SearchOrderActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SearchOrderFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "订单查询";
    }
}

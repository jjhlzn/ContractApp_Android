package com.example.bottombar.sample;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/3/25.
 */
public class OrderShouhuiActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new OrderShouhuiFragment();
    }
}

package com.example.bottombar.sample;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/3/24.
 */
public class OrderMenuActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new OrderMenuFragment();
    }
}

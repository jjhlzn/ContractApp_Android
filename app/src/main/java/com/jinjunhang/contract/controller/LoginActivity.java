package com.jinjunhang.contract.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinjunhang.contract.R;

/**
 * Created by lzn on 16/4/7.
 */
public class LoginActivity extends android.support.v4.app.FragmentActivity {

    /*
    @Override
    protected String getActivityTitle() {
        return "登录";
    }
    */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new LoginFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }



    }
    /*
    @Override
    protected boolean hasActionBar() {
        return false;
    }*/
}

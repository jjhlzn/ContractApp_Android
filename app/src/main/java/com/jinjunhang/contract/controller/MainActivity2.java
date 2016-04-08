package com.jinjunhang.contract.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jinjunhang.contract.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;
import com.roughike.bottombar.OnTabSelectedListener;

/**
 * Created by lzn on 16/4/3.
 */
public class MainActivity2 extends AppCompatActivity {

    public final static String EXTRA_TAB = "selecttab";
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        SearchOrderFragment orderFragment = new SearchOrderFragment();
        ApprovalSearchFragment approvalFragment = new ApprovalSearchFragment();
        mBottomBar.setFragmentItems(getSupportFragmentManager(), R.id.fragmentContainer,
                new BottomBarFragment(orderFragment, R.drawable.order, "订单"),
                new BottomBarFragment(approvalFragment, R.drawable.shenpi, "审批"),
                new BottomBarFragment(approvalFragment, R.drawable.search, "商品"),
                new BottomBarFragment(new MyInfoFragment(), R.drawable.me, "我")
        );

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, 0xFF5D4037);
        mBottomBar.mapColorForTab(2, 0xFF5D4037);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.actionbar_text)).setText("订单");
        mBottomBar.setOnItemSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                String title = "";
                switch (position) {
                    case 0:
                        title = "订单";
                        break;
                    case 1:
                        title = "审批";
                        break;
                    case 2:
                        title = "我";
                        break;
                }
                ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.actionbar_text)).setText(title);

            }
        });



        int selectTab = getIntent().getIntExtra(EXTRA_TAB, 0);
        switch (selectTab) {
            case 0:
                if (getIntent().getSerializableExtra(SearchOrderFragment.EXTRA_QUERYOBJECT) != null) {
                     //orderFragment.set

                }
                break;
            case 1:
                //getIntent().putExtra(ApprovalSearchFragment.EXTRA_QUERYOBJECT, getIntent().getSerializableExtra(App))
                break;
        }


        mBottomBar.selectTabAtPosition(selectTab, true);



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }
}

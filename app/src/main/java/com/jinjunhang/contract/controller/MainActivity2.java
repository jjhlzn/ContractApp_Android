package com.jinjunhang.contract.controller;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jinjunhang.contract.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.roughike.bottombar.OnTabSelectedListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzn on 16/4/3.
 */
public class MainActivity2 extends AppCompatActivity {
    private final static String TAG = "MainActivity2";

    public final static String EXTRA_TAB = "selecttab";
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);

        fragmentMap = new HashMap();


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.actionbar_main, null);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        final MainActivity2 that = this;

        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.actionbar_text)).setText("订单");
        final ImageButton searchApprovalButton = (ImageButton)getSupportActionBar().getCustomView().findViewById(R.id.actionbar_searchApprovalButton);
        searchApprovalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(that, ApprovalSearchActivity.class);
                that.startActivity(i);
            }
        });


        searchApprovalButton.setVisibility(View.INVISIBLE);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.bottommenu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                String title = "";
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = null;
                switch (menuItemId) {
                    case R.id.bottomBarOrder:
                        title = "订单";

                        searchApprovalButton.setVisibility(View.INVISIBLE);
                        fragment = getFragment(SearchOrderFragment.class);
                        //mBottomBar.
                        break;
                    case R.id.bottomBarShenpi:
                        title = "审批";
                        searchApprovalButton.setVisibility(View.VISIBLE);
                        fragment = getFragment(ApprovalListFragment.class);

                        break;
                    case R.id.bottomBarMe:
                        title = "我";
                        searchApprovalButton.setVisibility(View.INVISIBLE);
                        fragment = getFragment(MyInfoFragment.class);
                        break;
                }

                android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragmentContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.actionbar_text)).setText(title);
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarOrder) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });

        mBottomBar.noTopOffset();
        mBottomBar.hideShadow();



        //mBottomBar.useDarkTheme();
        //mBottomBar.setScaleX(0.8f);



        int selectTab = getIntent().getIntExtra(EXTRA_TAB, 0);
        Log.d(TAG, "selectTab = " + selectTab);
        mBottomBar.selectTabAtPosition(selectTab, true);

        (getSupportActionBar().getCustomView().findViewById(R.id.actionbar_searchButton)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new IntentIntegrator(that).setCaptureActivity(ProductSearchActivity.class).initiateScan();
            }
        });
    }


    private Map<Class, Fragment> fragmentMap;
    private <T extends Fragment> Fragment getFragment( Class<T> fragmentClass) {
        Fragment fragment = fragmentMap.get(fragmentClass);
        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
            } catch (Exception ex) {

            }
            fragmentMap.put(fragmentClass, fragment);
        }
        return fragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                String scanCode = result.getContents();
                Log.d(TAG, "scan code = " + scanCode);
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, ProductDetailActivity.class);
                i.putExtra(ProductDetailFragment.EXTRA_PRODUCTID, scanCode);
                startActivity(i);
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

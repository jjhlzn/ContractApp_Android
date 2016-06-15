package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinjunhang.contract.R;

/**
 * Created by lzn on 16/6/14.
 */
public class PriceReportCreateActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PriceReportCreateFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "新建报价单";
    }

    @Override
    protected void setActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.actionbar_scanbar, null);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent = (Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.actionbar_text)).setText(getActivityTitle());
        ((Button)getSupportActionBar().getCustomView().findViewById(R.id.actionbar_finishButton)).setText("提交");


        if (NavUtils.getParentActivityName(this) == null) {
            getSupportActionBar().getCustomView().findViewById(R.id.actionbar_back_button).setVisibility(View.INVISIBLE);
        } else {
            ImageButton backButton = (ImageButton) getSupportActionBar().getCustomView().findViewById(R.id.actionbar_back_button);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }
}

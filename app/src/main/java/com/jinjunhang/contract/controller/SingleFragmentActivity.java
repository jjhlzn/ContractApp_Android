package com.jinjunhang.contract.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinjunhang.contract.R;

/**
 * Created by lzn on 16/3/19.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {



    protected abstract Fragment createFragment();

    protected String getActivityTitle() {
        return "ERP系统";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }



        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.actionbar_text)).setText(getActivityTitle());
        ((ImageButton)getSupportActionBar().getCustomView().findViewById(R.id.actionbar_searchButton)).setVisibility(View.INVISIBLE);
        if (NavUtils.getParentActivityName(this) == null) {
            getSupportActionBar().getCustomView().findViewById(R.id.actionbar_back_button).setVisibility(View.INVISIBLE);
        } else {
            ImageButton backButton = (ImageButton)getSupportActionBar().getCustomView().findViewById(R.id.actionbar_back_button);
            backButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

    }

    protected OnBackPressedListener onBackPressedListener;
    protected PreOnBackPressedListiner mPreOnBackPressedListiner;

    public interface OnBackPressedListener {
        void doBack();
    }

    public interface PreOnBackPressedListiner {
        void handle();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }


    @Override
    public void onBackPressed() {

        if (onBackPressedListener != null)
            onBackPressedListener.doBack();
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        onBackPressedListener = null;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

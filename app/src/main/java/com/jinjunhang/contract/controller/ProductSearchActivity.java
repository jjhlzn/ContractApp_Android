package com.jinjunhang.contract.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinjunhang.contract.R;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample Activity extending from ActionBarActivity to display a Toolbar.
 */
public class ProductSearchActivity extends AppCompatActivity {
    private final static String TAG = "ProductSearchActivity";

    public final static String EXTRA_FIRSTIN = "EXTRA_FIRSTIN";
    public final static String EXTRA_DATA = "EXTRA_DATA";
    public final static String EXTRA_ALL_CODES = "EXTRA_ALL_CODES";


    private CaptureManager capture;
    private CompoundBarcodeView barcodeScannerView;
    private List<String> mAllCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (NavUtils.getParentActivityName(this) != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.capture_appcompat);

        Log.d(TAG, "EXTRA_ALL_CODES = " + getIntent().getSerializableExtra(EXTRA_ALL_CODES));
        String codeString = getIntent().getStringExtra(EXTRA_ALL_CODES);
        mAllCodes = MainActivity2.parseCodes(codeString);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.actionbar_scanbar, null);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);


        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.actionbar_text)).setText("扫描");
        ((Button)getSupportActionBar().getCustomView().findViewById(R.id.actionbar_finishButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查是否有商品扫描
                if (mAllCodes.size() == 0) {
                    Utils.showMessage(ProductSearchActivity.this, "没有扫描到任何商品");
                    return;
                }

                Intent i = new Intent(ProductSearchActivity.this, PriceReportCreateActivity.class)
                        .putExtra(PriceReportCreateFragment.EXTRA_ALL_CODES, (ArrayList<String>)mAllCodes);
                startActivity(i);
            }
        });
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

        barcodeScannerView = (CompoundBarcodeView)findViewById(R.id.zxing_barcode_scanner);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        if (!getIntent().getBooleanExtra(EXTRA_FIRSTIN, false)) {
            showConfirmMessage(this, getIntent().getStringExtra(EXTRA_DATA), new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //检查是否有商品扫描
                    if (mAllCodes.size() == 0) {
                        Utils.showMessage(ProductSearchActivity.this, "没有扫描到任何商品");
                        return;
                    }

                    Intent i = new Intent(ProductSearchActivity.this, PriceReportCreateActivity.class)
                            .putExtra(PriceReportCreateFragment.EXTRA_ALL_CODES, (ArrayList<String>)mAllCodes);
                    startActivity(i);
                }
            });
        }
    }

    public void showConfirmMessage(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setNegativeButton("继续", null);
        dlgAlert.setPositiveButton("完成", listener);
        dlgAlert.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
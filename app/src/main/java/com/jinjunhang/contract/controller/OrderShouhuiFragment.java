package com.jinjunhang.contract.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bottombar.sample.R;
import com.jinjunhang.contract.model.OrderShouhuiInfo;

/**
 * Created by lzn on 16/3/25.
 */
public class OrderShouhuiFragment extends android.support.v4.app.Fragment {

    public final static String EXTRA_ORDER_SHOUHUI = "orderShouhui";

    private OrderShouhuiInfo mShouhuiInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getActivity().getIntent();
        mShouhuiInfo = (OrderShouhuiInfo)i.getSerializableExtra(EXTRA_ORDER_SHOUHUI);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_order_shouhui, null);

        TextView dateTextView = (TextView)v.findViewById(R.id.order_shouhui_date);
        dateTextView.setText(mShouhuiInfo.getDate());

        TextView amountTextView = (TextView)v.findViewById(R.id.order_shouhui_amount);
        amountTextView.setText(mShouhuiInfo.getAmount() + "");

        return v;
    }
}

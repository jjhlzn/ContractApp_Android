package com.jinjunhang.contract.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.model.OrderChuyunInfo;

/**
 * Created by lzn on 16/3/24.
 */
public class OrderChuyunFragment extends android.support.v4.app.Fragment {

    public final static String EXTRA_ORDER_CHUYUN = "orderChuyun";

    private OrderChuyunInfo mChuyunInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (NavUtils.getParentActivityName(getActivity()) != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getActivity().getIntent();
        mChuyunInfo = (OrderChuyunInfo)i.getSerializableExtra(EXTRA_ORDER_CHUYUN);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_order_chuyun, null);

        TextView detailNoTextView = (TextView)v.findViewById(R.id.order_chuyun_detailNo);
        detailNoTextView.setText(mChuyunInfo.getDetailNo());

        TextView dateTextView = (TextView)v.findViewById(R.id.order_chuyun_date);
        dateTextView.setText(mChuyunInfo.getDate());

        TextView amountTextView = (TextView)v.findViewById(R.id.order_chuyun_amount);
        amountTextView.setText(mChuyunInfo.getAmount() + "");

        return v;
    }
}

package com.jinjunhang.contract.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bottombar.sample.R;
import com.jinjunhang.contract.model.OrderBasicInfo;

/**
 * Created by lzn on 16/3/24.
 */
public class OrderBasicInfoFragment extends android.support.v4.app.Fragment {

    public final static String EXTRA_ORDER_BASIC_INFO = "orderBasicInfo";

    private OrderBasicInfo mBasicInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getActivity().getIntent();
        mBasicInfo = (OrderBasicInfo)i.getSerializableExtra(EXTRA_ORDER_BASIC_INFO);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_basicinfo, container, false);

        TextView timeLimitTextView = (TextView)v.findViewById(R.id.order_basicInfo_timeLimit);
        timeLimitTextView.setText(mBasicInfo.getTimeLimit());

        TextView startPortTextView = (TextView)v.findViewById(R.id.order_basicInfo_startPort);
        startPortTextView.setText(mBasicInfo.getStartPort());

        TextView destPortTextView = (TextView)v.findViewById(R.id.order_basicInfo_destPort);
        destPortTextView.setText(mBasicInfo.getDestPort());

        TextView shouhuiTextView = (TextView)v.findViewById(R.id.order_basicInfo_shouhuiType);
        shouhuiTextView.setText(mBasicInfo.getGetMoneyType());

        TextView priceRuleTextView = (TextView)v.findViewById(R.id.order_basicInfo_priceRule);
        priceRuleTextView.setText(mBasicInfo.getPriceRule());

        return v;
    }
}

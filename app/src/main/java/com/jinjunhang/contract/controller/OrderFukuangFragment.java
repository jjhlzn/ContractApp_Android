package com.jinjunhang.contract.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bottombar.sample.R;
import com.jinjunhang.contract.model.OrderPurchaseInfo;
import com.jinjunhang.contract.model.OrderPurchaseItem;

/**
 * Created by lzn on 16/3/24.
 */
public class OrderFukuangFragment extends android.support.v4.app.ListFragment {

    public final static String EXTRA_ORDER_FUKUANG = "orderFukuang";

    private OrderPurchaseInfo mFukuangInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getActivity().getIntent();
        mFukuangInfo = (OrderPurchaseInfo)i.getSerializableExtra(EXTRA_ORDER_FUKUANG);

        FukuangAdpter adpter = new FukuangAdpter(mFukuangInfo);
        setListAdapter(adpter);
    }

    private class FukuangAdpter extends ArrayAdapter<OrderPurchaseItem> {
        public FukuangAdpter(OrderPurchaseInfo fukuangInfo) {
            super(getActivity(), 0, fukuangInfo.getItems());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_shougouinfo, null);
            }

            OrderPurchaseItem item = getItem(position);
            TextView contractTextView = (TextView)convertView.findViewById(R.id.order_shougouInfo_contractNo);
            contractTextView.setText(item.getContract());

            TextView dateTextView = (TextView)convertView.findViewById(R.id.order_shougouInfo_date);
            dateTextView.setText(item.getDate());

            TextView factoryTextView = (TextView)convertView.findViewById(R.id.order_shougouInfo_factory);
            factoryTextView.setText(item.getFactory());

            TextView amountTextView = (TextView)convertView.findViewById(R.id.order_shougouInfo_amount);
            amountTextView.setText(item.getAmount() + "");

            return convertView;
        }
    }
}

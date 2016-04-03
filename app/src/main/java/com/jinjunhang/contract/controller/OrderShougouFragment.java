package com.jinjunhang.contract.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.model.Order;
import com.jinjunhang.contract.model.OrderPurchaseInfo;
import com.jinjunhang.contract.model.OrderPurchaseItem;

import java.util.List;

/**
 * Created by lzn on 16/3/24.
 */
public class OrderShougouFragment extends ListFragment {

    public final static String EXTRA_SHOUGOU_INFO = "orderShougouInfo";

    private OrderPurchaseInfo mShougouInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getActivity().getIntent();
        mShougouInfo = (OrderPurchaseInfo)i.getSerializableExtra(EXTRA_SHOUGOU_INFO);

        ShougouAdapter shougouAdapter = new ShougouAdapter(mShougouInfo);
        setListAdapter(shougouAdapter);

        if (NavUtils.getParentActivityName(getActivity()) != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private class ShougouAdapter extends ArrayAdapter<OrderPurchaseItem> {
        public ShougouAdapter(OrderPurchaseInfo shougouInfo) {
            super(getActivity(), 0, shougouInfo.getItems());
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

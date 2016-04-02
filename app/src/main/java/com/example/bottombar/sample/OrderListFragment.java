package com.example.bottombar.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.contract.model.Order;

import java.util.List;

/**
 * Created by lzn on 16/3/23.
 */
public class OrderListFragment extends ListFragment {

    private List<Order> mOrders;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getActivity().getIntent();
        mOrders = (List<Order>) i.getSerializableExtra(SearchOrderFragment.EXTRA_ORDERS);



        OrderAdapter orderAdapter = new OrderAdapter(mOrders);
        setListAdapter(orderAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        return v;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Order order = ((OrderAdapter) getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), OrderMenuActivity.class);
        i.putExtra(OrderMenuFragment.EXTRA_ORDER_NO, order.getContractNo());
        startActivity(i);
    }

    private class OrderAdapter extends ArrayAdapter<Order> {
        public OrderAdapter(List<Order> orders) {
            super(getActivity(), 0, orders);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_order, null);
            }

            Order order = getItem(position);
            TextView businessTextView = (TextView) convertView.findViewById(R.id.order_list_item_businessPersonTextView);
            businessTextView.setText(order.getBusinessPerson());

            TextView contractNoTextView = (TextView) convertView.findViewById(R.id.order_list_item_contractNoTextView);
            contractNoTextView.setText(order.getContractNo());

            TextView orderNoTextView = (TextView) convertView.findViewById(R.id.order_list_item_orderNoTextView);
            orderNoTextView.setText(order.getOrderNo());

            TextView amountTextView = (TextView) convertView.findViewById(R.id.order_list_item_amountTextView);
            amountTextView.setText(order.getAmount() + "");

            TextView guestNameTextView = (TextView) convertView.findViewById(R.id.order_list_item_guestNameTextView);
            guestNameTextView.setText(order.getGuestName());

            return convertView;
        }
    }
}

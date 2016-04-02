package com.example.bottombar.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jinjunhang.contract.model.Order;
import com.jinjunhang.contract.service.SearchOrderResponse;
import com.jinjunhang.contract.service.OrderService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lzn on 16/3/22.
 */
public class SearchOrderFragment extends android.support.v4.app.Fragment {
    private final static String TAG = "SearchOrderFragment";

    public final static String EXTRA_ORDERS = "orders";
    private static final int REQUEST_START_DATE = 0;
    private static final int REQUEST_END_DATE = 1;
    public final static String DIALOG_DATE = "date";

    private Button mStartDateButton;
    private Button mEndDateButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_order, container, false);
        Button searchButton = (Button)v.findViewById(R.id.search_order_searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchOrderTask().execute();

            }
        });


        Calendar cal = Calendar.getInstance();
        final Date today = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        final Date oneMonthAgo = cal.getTime();

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

        mStartDateButton = (Button)v.findViewById(R.id.search_order_startDate);
        mStartDateButton.setText(dt.format(oneMonthAgo));
        mStartDateButton.setGravity(Gravity.CENTER);
        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "start date button clicked");
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(oneMonthAgo);
                datePickerFragment.setTargetFragment(SearchOrderFragment.this, REQUEST_START_DATE);
                datePickerFragment.show(fm, DIALOG_DATE);
            }
        });

        mEndDateButton = (Button)v.findViewById(R.id.search_order_endDate);
        mEndDateButton.setText(dt.format(today));
        mEndDateButton.setGravity(Gravity.CENTER);
        mEndDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "start date button clicked");
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(today);
                datePickerFragment.setTargetFragment(SearchOrderFragment.this, REQUEST_END_DATE);
                datePickerFragment.show(fm, DIALOG_DATE);
            }
        });

        return v;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private class SearchOrderTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            SearchOrderResponse resp = new OrderService().search("", null, null, 0, 10);
            List<Order> orders = resp.getOrders();
            Intent i = new Intent(getActivity(), OrderListActivity.class);
            i.putExtra(EXTRA_ORDERS, (ArrayList<Order>) orders);
            startActivity(i);
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult called");
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == REQUEST_START_DATE) {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            mStartDateButton.setText(dt.format(date));
        } else if (requestCode == REQUEST_END_DATE) {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            mEndDateButton.setText(dt.format(date));
        }
    }



}

package com.jinjunhang.contract.controller;

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
import android.widget.EditText;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.model.Order;
import com.jinjunhang.contract.service.OrderQueryObject;
import com.jinjunhang.contract.service.SearchOrderResponse;
import com.jinjunhang.contract.service.OrderService;
import com.jinjunhang.contract.service.ServerResponse;

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
    public final static String EXTRA_QUERYOBJECT = "orderqueryobject";

    private static final int REQUEST_START_DATE = 0;
    private static final int REQUEST_END_DATE = 1;
    public final static String DIALOG_DATE = "date";

    private EditText mKeywordEditText;
    private Button mStartDateButton;
    private Button mEndDateButton;
    private LoadingAnimation mLoading;

    private OrderQueryObject mQueryObject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_order, container, false);
        mKeywordEditText = (EditText)v.findViewById(R.id.search_order_keyword);

        Button searchButton = (Button)v.findViewById(R.id.search_order_searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.compareDate(mStartDateButton.getText().toString(), mEndDateButton.getText().toString())  > 0) {
                    Utils.showMessage(getActivity(), "开始日期不能晚于结束日期");
                    return;
                }

                new SearchOrderTask().execute(mKeywordEditText.getText().toString(),
                        mStartDateButton.getText().toString(),
                        mEndDateButton.getText().toString());
            }
        });

        Calendar cal = Calendar.getInstance();
        final Date today;
        final Date oneMonthAgo;
        final SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

        if (mQueryObject != null) {
            mKeywordEditText.setText(mQueryObject.getKeyword());
            try {
                today = dt.parse(mQueryObject.getEndDate());
                oneMonthAgo = dt.parse(mQueryObject.getStartDate());
            }
            catch (Exception ex){
                //NOT RETURN HERE
                Log.e(TAG, "NOT RETURN HERE: ", ex);
                return v;
            }
        } else {

            today = cal.getTime();
            cal.add(Calendar.MONTH, -80);
            oneMonthAgo = cal.getTime();
        }

        mStartDateButton = (Button)v.findViewById(R.id.search_order_startDate);
        mStartDateButton.setText(dt.format(oneMonthAgo));
        mStartDateButton.setGravity(Gravity.CENTER);
        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "start date button clicked");
                final FragmentManager fm = getActivity().getSupportFragmentManager();

                DatePickerFragment datePickerFragment = null;
                try {
                    datePickerFragment = DatePickerFragment.newInstance(dt.parse(mStartDateButton.getText().toString()));
                }catch (Exception ex){
                    Log.e(TAG, ex.toString());
                }
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

                final FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment datePickerFragment = null;
                try {
                    datePickerFragment = DatePickerFragment.newInstance(dt.parse(mEndDateButton.getText().toString()));
                }catch (Exception ex){
                    Log.e(TAG, ex.toString());
                }
                datePickerFragment.setTargetFragment(SearchOrderFragment.this, REQUEST_END_DATE);
                datePickerFragment.show(fm, DIALOG_DATE);
            }
        });

        return v;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoading = new LoadingAnimation(getActivity());
        Intent i = getActivity().getIntent();
        mQueryObject = (OrderQueryObject)i.getSerializableExtra(EXTRA_QUERYOBJECT);
    }

    private class SearchOrderTask extends AsyncTask<String, Void, SearchOrderResponse> {

        private String mKeyword;
        private String mStartDate;
        private String mEndDate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("");
        }

        @Override
        protected SearchOrderResponse doInBackground(String... params) {
            mKeyword = params[0];
            mStartDate = params[1];
            mEndDate = params[2];
            SearchOrderResponse resp = new OrderService().search(mKeyword, mStartDate, mEndDate, 0, Utils.PAGESIZE_ORDER);


            return resp;
        }

        @Override
        protected void onPostExecute(SearchOrderResponse resp) {
            super.onPostExecute(resp);

            mLoading.dismiss();
            if (resp.getStatus() == ServerResponse.FAIL) {
                Utils.showMessage(getActivity(), "服务器返回出错！");
                return;
            }

            List<Order> orders = resp.getOrders();
            Intent i = new Intent(getActivity(), OrderListActivity.class);
            i.putExtra(EXTRA_ORDERS, (ArrayList<Order>) orders);
            OrderQueryObject queryObject = new OrderQueryObject();
            queryObject.setKeyword(mKeyword);
            queryObject.setStartDate(mStartDate);
            queryObject.setEndDate(mEndDate);
            queryObject.setIndex(0);
            queryObject.setPageSize(Utils.PAGESIZE_ORDER);
            i.putExtra(EXTRA_QUERYOBJECT, queryObject);
            startActivity(i);


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

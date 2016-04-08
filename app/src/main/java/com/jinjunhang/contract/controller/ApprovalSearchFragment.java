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
import android.widget.ToggleButton;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.model.Approval;
import com.jinjunhang.contract.model.Order;
import com.jinjunhang.contract.service.ApprovalQueryObject;
import com.jinjunhang.contract.service.ApprovalService;
import com.jinjunhang.contract.service.OrderQueryObject;
import com.jinjunhang.contract.service.OrderService;
import com.jinjunhang.contract.service.SearchApprovalResponse;
import com.jinjunhang.contract.service.SearchOrderResponse;
import com.jinjunhang.contract.service.ServerResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lzn on 16/4/3.
 */
public class ApprovalSearchFragment extends android.support.v4.app.Fragment {


    private final static String TAG = "ApprovalSearchFragment";

    public final static String EXTRA_APPROVALS = "approvals";
    public final static String EXTRA_QUERYOBJECT = "approvalqueryobject";


    private static final int REQUEST_START_DATE = 0;
    private static final int REQUEST_END_DATE = 1;
    public final static String DIALOG_DATE = "date";

    private EditText mKeywordEditText;
    private Button mStartDateButton;
    private Button mEndDateButton;
    private ToggleButton mContainApproved;
    private ToggleButton mContainUnapproved;
    private LoadingAnimation mLoading;
    private int pageSize = 15;

    private ApprovalQueryObject mQueryObject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_search_approval, container, false);
        mKeywordEditText = (EditText)v.findViewById(R.id.search_approval_keyword);

        Button searchButton = (Button)v.findViewById(R.id.search_approval_searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoading.show("");
                //TDOO: userId hardcode
                new SearchApprovalTask().execute("0001",
                        mKeywordEditText.getText().toString(),
                        mContainApproved.isChecked() + "",
                        mContainUnapproved.isChecked() + "",
                        mStartDateButton.getText().toString(),
                        mEndDateButton.getText().toString());
            }
        });

        mContainApproved = (ToggleButton)v.findViewById(R.id.search_approval_approved);
        mContainUnapproved = (ToggleButton)v.findViewById(R.id.search_approval_unapproved);

        Calendar cal = Calendar.getInstance();
        final Date today;
        final Date oneMonthAgo;
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

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
            mContainApproved.setChecked(mQueryObject.isContainApproved());
            mContainUnapproved.setChecked(mQueryObject.isContainUnapproved());
        } else {

            today = cal.getTime();
            cal.add(Calendar.MONTH, -80);
            oneMonthAgo = cal.getTime();
        }

        mStartDateButton = (Button)v.findViewById(R.id.search_approval_startDate);
        mStartDateButton.setText(dt.format(oneMonthAgo));
        mStartDateButton.setGravity(Gravity.CENTER);
        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "start date button clicked");
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(oneMonthAgo);
                datePickerFragment.setTargetFragment(ApprovalSearchFragment.this, REQUEST_START_DATE);
                datePickerFragment.show(fm, DIALOG_DATE);
            }
        });

        mEndDateButton = (Button)v.findViewById(R.id.search_approval_endDate);
        mEndDateButton.setText(dt.format(today));
        mEndDateButton.setGravity(Gravity.CENTER);
        mEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "start date button clicked");
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(today);
                datePickerFragment.setTargetFragment(ApprovalSearchFragment.this, REQUEST_END_DATE);
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
        mQueryObject = (ApprovalQueryObject)i.getSerializableExtra(EXTRA_QUERYOBJECT);
        Log.d(TAG, String.format("mQueryObject == null --> %s",mQueryObject == null));
    }

    private class SearchApprovalTask extends AsyncTask<String, Void, SearchApprovalResponse> {

        private String mUserId;
        private String mKeyword;
        private String mStartDate;
        private String mEndDate;
        private boolean mContainApproved;
        private boolean mContainUnapproved;

        @Override
        protected SearchApprovalResponse doInBackground(String... params) {
            mUserId = params[0];
            mKeyword = params[1];
            mContainApproved = Boolean.parseBoolean(params[2]);
            mContainUnapproved = Boolean.parseBoolean(params[3]);
            mStartDate = params[4];
            mEndDate = params[5];

            SearchApprovalResponse resp = new ApprovalService().search(mUserId, mKeyword, mContainApproved, mContainUnapproved,
                    mStartDate, mEndDate, 0, pageSize);
            return resp;
        }

        @Override
        protected void onPostExecute(SearchApprovalResponse resp) {
            super.onPostExecute(resp);

            mLoading.dismiss();
            if (resp.getStatus() == ServerResponse.FAIL) {
                Utils.showMessage(getActivity(), "服务器返回出错！");
                return;
            }

            List<Approval> approvals = resp.getApprovals();
            Intent i = new Intent(getActivity(), ApprovalListActivity.class);
            i.putExtra(EXTRA_APPROVALS, (ArrayList<Approval>) approvals);
            ApprovalQueryObject queryObject = new ApprovalQueryObject();
            queryObject.setContainApproved(mContainApproved);
            queryObject.setContainUnapproved(mContainUnapproved);
            queryObject.setKeyword(mKeyword);
            queryObject.setStartDate(mStartDate);
            queryObject.setEndDate(mEndDate);
            queryObject.setIndex(0);
            queryObject.setPageSize(pageSize);
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

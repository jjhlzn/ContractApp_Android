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
import com.kyleduo.switchbutton.SwitchButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lzn on 16/4/3.
 */
public class ApprovalSearchFragment extends android.support.v4.app.Fragment implements SingleFragmentActivity.OnBackPressedListener {

    private final static String TAG = "ApprovalSearchFragment";

    public final static String EXTRA_APPROVALS = "approvals";
    public final static String EXTRA_QUERYOBJECT = "approvalqueryobject";


    private static final int REQUEST_START_DATE = 0;
    private static final int REQUEST_END_DATE = 1;
    public final static String DIALOG_DATE = "date";

    private EditText mKeywordEditText;
    private EditText mStartDateButton;
    private EditText mEndDateButton;
    private SwitchButton mContainApproved;
    private SwitchButton mContainUnapproved;

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
                if (Utils.compareDate(mStartDateButton.getText().toString(), mEndDateButton.getText().toString())  > 0) {
                    Utils.showMessage(getActivity(), "开始日期不能晚于结束日期");
                    return;
                }


                Intent i = new Intent(getActivity(), ApprovalListActivity.class);
                ApprovalQueryObject queryObject = new ApprovalQueryObject();
                queryObject.setContainApproved(mContainApproved.isChecked());
                queryObject.setContainUnapproved(mContainUnapproved.isChecked());
                queryObject.setKeyword(mKeywordEditText.getText().toString());
                queryObject.setStartDate(mStartDateButton.getText().toString());
                queryObject.setEndDate(mEndDateButton.getText().toString());
                queryObject.setIndex(-1);
                queryObject.setPageSize(Utils.PAGESIZE_APPROVAL);
                i.putExtra(EXTRA_QUERYOBJECT, queryObject);
                i.putExtra(ApprovalListFragment.EXTRA_FROMSEARCH, true);
                startActivity(i);
            }
        });

        mContainApproved = (SwitchButton)v.findViewById(R.id.search_approval_approved);
        mContainUnapproved = (SwitchButton)v.findViewById(R.id.search_approval_unapproved);

        Calendar cal = Calendar.getInstance();
        final Date tomorrow;
        final Date oneMonthAgo;
        final SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

        if (mQueryObject != null) {
            mKeywordEditText.setText(mQueryObject.getKeyword());
            try {
                tomorrow = dt.parse(mQueryObject.getEndDate());
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

            cal.add(Calendar.DAY_OF_MONTH, -31);
            oneMonthAgo = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 32);
            tomorrow = cal.getTime();
        }

        mStartDateButton = (EditText)v.findViewById(R.id.search_approval_startDate);
        mStartDateButton.setFocusable(false);
        mStartDateButton.setClickable(true);

        mStartDateButton.setText(dt.format(oneMonthAgo));
        mStartDateButton.setGravity(Gravity.CENTER);
        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment datePickerFragment = null;
                try {
                    datePickerFragment = DatePickerFragment.newInstance(dt.parse(mStartDateButton.getText().toString()));
                }catch (Exception ex){
                    Log.e(TAG, ex.toString());
                }
                datePickerFragment.setTargetFragment(ApprovalSearchFragment.this, REQUEST_START_DATE);
                datePickerFragment.show(fm, DIALOG_DATE);
            }
        });

        mEndDateButton = (EditText)v.findViewById(R.id.search_approval_endDate);
        mEndDateButton.setText(dt.format(tomorrow));
        mEndDateButton.setGravity(Gravity.CENTER);
        mEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "start date button clicked");
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment datePickerFragment = null;
                try {
                    datePickerFragment = DatePickerFragment.newInstance(dt.parse(mEndDateButton.getText().toString()));
                }catch (Exception ex){
                    Log.e(TAG, ex.toString());
                }
                datePickerFragment.setTargetFragment(ApprovalSearchFragment.this, REQUEST_END_DATE);
                datePickerFragment.show(fm, DIALOG_DATE);
            }
        });

        return v;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getActivity().getIntent();
        mQueryObject = (ApprovalQueryObject)i.getSerializableExtra(EXTRA_QUERYOBJECT);
        Log.d(TAG, String.format("mQueryObject == null --> %s",mQueryObject == null));
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

    @Override
    public void doBack() {
        Intent i = new Intent(getActivity(), MainActivity2.class);
        i.putExtra(MainActivity2.EXTRA_TAB, 1);
        startActivity(i);
    }


}

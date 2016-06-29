package com.jinjunhang.contract.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.service.ApprovalQueryObject;
import com.jinjunhang.contract.service.PriceReportQueryObject;
import com.jinjunhang.contract.service.SearchPriceReportsRequest;
import com.jinjunhang.framework.controller.PagableController;
import com.jinjunhang.contract.model.PriceReport;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.PagedServerResponse;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lzn on 16/6/14.
 */
public class PriceReportListFragment extends android.support.v4.app.Fragment implements
        AbsListView.OnScrollListener {

    private final static String TAG = "PriceReportListFragment";

    private PagableController mPagableController;
    private PriceReportQueryObject mQueryObject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View v = inflater.inflate(R.layout.activity_fragement_pushdownrefresh, container, false);

        ListView listView = (ListView) v.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PriceReport priceReport = (PriceReport)((mPagableController.getPagableArrayAdapter()).getItem(position));
                Intent i = new Intent(getActivity(), PriceReportDetailActivity.class)
                        .putExtra(PriceReportDetailFragment.EXTRA_PRICEREPORT, priceReport);
                startActivity(i);

            }
        });

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);

        if (mPagableController == null) {
            Log.d(TAG, "create PagableController");
            mPagableController = new PagableController();
        }
        mPagableController.setActivity(getActivity());
        mPagableController.setListView(listView);

        if ( mPagableController.getPagableArrayAdapter() != null) {
            mPagableController.setPagableArrayAdapter(new PriceReportAdapter(mPagableController, mPagableController.getPagableArrayAdapter().getDataSet()));
        } else {
            mPagableController.setPagableArrayAdapter(new PriceReportAdapter(mPagableController, new ArrayList<PriceReport>()));
        }


        mPagableController.setPagableRequestHandler(new PriceReportRequestHandler());
        mPagableController.setSwipeRefreshLayout(swipeRefreshLayout);
        mPagableController.setOnScrollListener(this);

        final ImageButton searchApprovalButton = (ImageButton)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().
                findViewById(R.id.actionbar_searchApprovalButton);
        searchApprovalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PriceReportSearchActivity.class)
                        .putExtra(PriceReportSearchFragment.EXTRA_QUERYOBJECT, mQueryObject);
                startActivity(i);
            }
        });


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated called");
        if (mQueryObject == null) {
            Intent i = getActivity().getIntent();
            mQueryObject = (PriceReportQueryObject) i.getSerializableExtra(PriceReportSearchFragment.EXTRA_QUERYOBJECT);
            //if (i.getBooleanExtra(EXTRA_FROMSEARCH, false)) {
             //   mMoreDataAvailable = true;
            //}
        }

        if (mQueryObject == null) {
            createQueryObject();
        }
    }

    private void createQueryObject() {
        mQueryObject = new PriceReportQueryObject();

        Calendar cal = Calendar.getInstance();
        final Date tomorrow;
        final Date oneMonthAgo;
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DAY_OF_MONTH, -31);
        oneMonthAgo = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 32);
        tomorrow = cal.getTime();

        mQueryObject.setKeyword("");
        mQueryObject.setStartDate(dt.format(oneMonthAgo));
        mQueryObject.setEndDate(dt.format(tomorrow));

        mQueryObject.setPageSize(Utils.PAGESIZE_PRICEREPORT);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        mPagableController.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }

    class PriceReportRequestHandler implements PagableController.PagableRequestHandler {
        @Override
        public PagedServerResponse handle() {
            SearchPriceReportsRequest req = new SearchPriceReportsRequest();
            req.setKeyword(mQueryObject.getKeyword());
            req.setStartDate(mQueryObject.getStartDate());
            req.setEndDate(mQueryObject.getEndDate());
            req.setUserId(PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, ""));
            req.setPageSize(mQueryObject.getPageSize());
            req.setPageIndex(mPagableController.getPageIndex());
            return new BasicService().sendRequest(req);
        }
    }


    class PriceReportAdapter extends PagableController.PagableArrayAdapter<PriceReport> implements Serializable {

        public PriceReportAdapter(PagableController controller, List<PriceReport> dataSet) {
            super(controller, dataSet);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_pricereport, null);
            }

            PriceReport report = getItem(position);
            TextView idTextView = (TextView) convertView.findViewById(R.id.pricereport_list_item_id);
            idTextView.setText(report.getId());

            TextView validDaysTextView = (TextView) convertView.findViewById(R.id.pricereport_list_item_validDays);
            validDaysTextView.setText(report.getValidDays()+"天");

            TextView statusTextView = (TextView) convertView.findViewById(R.id.pricereport_list_item_status);
            statusTextView.setText(report.getStatus());

            TextView reportTextView = (TextView) convertView.findViewById(R.id.pricereport_list_item_reporter);
            reportTextView.setText(report.getReporter());

            TextView dateTextView = (TextView) convertView.findViewById(R.id.pricereport_list_item_date);
            dateTextView.setText(report.getDate());

            TextView detailInfoTextView = (TextView) convertView.findViewById(R.id.pricereport_list_item_detailInfo);
            detailInfoTextView.setText(report.getDetailInfo());

            return convertView;
        }
    }


}

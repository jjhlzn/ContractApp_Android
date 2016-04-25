package com.jinjunhang.contract.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.model.Approval;
import com.jinjunhang.contract.service.ApprovalQueryObject;
import com.jinjunhang.contract.service.ApprovalService;
import com.jinjunhang.contract.service.SearchApprovalResponse;
import com.jinjunhang.contract.service.ServerResponse;
import com.journeyapps.barcodescanner.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lzn on 16/4/6.
 */
public class ApprovalListFragment extends android.support.v4.app.Fragment
        implements SingleFragmentActivity.OnBackPressedListener, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ApprovalListFragment";

    public static final String EXTRA_FROMSEARCH = "extra_fromsearch";
    private List<Approval> mApprovals;
    private ApprovalQueryObject mQueryObject;


    //用于load more
    private ApprovalAdapter mApprovalAdapter;
    private View mFooterView;
    private boolean mIsLoading;
    private boolean mMoreDataAvailable;

    private ListView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragement_pushdownrefresh, container, false);

        mListView = (ListView) v.findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Approval approval = (Approval)mListView.getItemAtPosition(position);

                Intent i = new Intent(getActivity(), ApprovalDetailActivity.class);
                i.putExtra(ApprovalDetailFragment.EXTRA_APPROVAL, approval);
                i.putExtra(ApprovalDetailFragment.EXTRA_POSITON, position);
                startActivityForResult(i, 1);
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "resume");

        long time = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_APPROVAL_UPDATE_TIME, -1);
        if (time == -1)
            return;

        long nowTime = Calendar.getInstance().getTimeInMillis();
        long delta = nowTime - time;
        if (delta > Utils.UPDATE_TIME_DELTA) {
            Log.d(TAG, "time is out, need to refresh");
            if (!mIsLoading) {
                new SearchApprovalTask(true).execute();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated called");
        if (mQueryObject == null) {
            Intent i = getActivity().getIntent();
            //mApprovals = (List<Approval>) i.getSerializableExtra(ApprovalSearchFragment.EXTRA_APPROVALS);
            mQueryObject = (ApprovalQueryObject) i.getSerializableExtra(ApprovalSearchFragment.EXTRA_QUERYOBJECT);
            if (i.getBooleanExtra(EXTRA_FROMSEARCH, false)) {
                    mMoreDataAvailable = true;
            }
        }

        if (mQueryObject == null) {
            createQueryObject();
        }

        Log.d(TAG, "mMoreDataAvailable = " + mMoreDataAvailable);
        initLoadApprovals();

    }

    private void createQueryObject() {
        mQueryObject = new ApprovalQueryObject();

        Calendar cal = Calendar.getInstance();
        final Date today;
        final Date oneMonthAgo;
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        today = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -31);
        oneMonthAgo = cal.getTime();

        mQueryObject.setKeyword("");
        mQueryObject.setStartDate(dt.format(oneMonthAgo));
        mQueryObject.setEndDate(dt.format(today));


        mQueryObject.setPageSize(Utils.PAGESIZE_APPROVAL);
        mQueryObject.setContainApproved(false);
        mQueryObject.setContainUnapproved(true);

        mMoreDataAvailable = true;
    }

    private void initLoadApprovals() {
        Log.d(TAG, "initLoadApprovals called");

        if (mApprovals == null)
            mApprovals = new ArrayList<>();

        mApprovalAdapter = new ApprovalAdapter(mApprovals);
        mListView.setAdapter(mApprovalAdapter);

        setFootView();


    }

    private void setFootView() {
        mFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.loading_view, null);

        resetFootView();
        mListView.addFooterView(mFooterView, null, false);
        mListView.setOnScrollListener(this);
    }

    private void resetFootView() {
        if (!mMoreDataAvailable) {
            mFooterView.findViewById(R.id.loading_progressbar).setVisibility(View.GONE);
            if (mApprovals.size() > 0)
                ((TextView)mFooterView.findViewById(R.id.loading_message)).setText("已加载全部数据");
            else {
                ((TextView) mFooterView.findViewById(R.id.loading_message)).setText("没有找到任何审批");
                ((TextView) mFooterView.findViewById(R.id.loading_message)).setTextSize(15);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (!mMoreDataAvailable)
            return;

        int lastInScreen = firstVisibleItem + visibleItemCount;

        if (!mIsLoading) {
            if (lastInScreen == totalItemCount) {
                Log.d(TAG, "start loading more");
                new SearchApprovalTask().execute();
            }
        }
    }

    @Override
    public void onRefresh() {
        if (!mIsLoading) {
            new SearchApprovalTask(true).execute();
        }

    }

    private class SearchApprovalTask extends AsyncTask<Void, Void, SearchApprovalResponse> {

        private boolean mIsRefresh = false;
        public SearchApprovalTask() {
        }

        public SearchApprovalTask(boolean isRefresh) {
            this.mIsRefresh = isRefresh;
        }

        @Override
        protected SearchApprovalResponse doInBackground(Void... params) {
            mQueryObject.setIndex(mQueryObject.getIndex() + 1);

            return new ApprovalService().search(PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, ""), mQueryObject.getKeyword(), mQueryObject.isContainApproved(),
                    mQueryObject.isContainUnapproved(), mQueryObject.getStartDate(),
                    mQueryObject.getEndDate(), mQueryObject.getIndex(), mQueryObject.getPageSize());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mIsLoading = true;
            if (mIsRefresh) {
                mSwipeRefreshLayout.setRefreshing(true);
                Log.d(TAG, "start refresh");
                mQueryObject.setIndex(-1);
            }

            //纪录刷新的时间
            if (mQueryObject.getIndex() == -1) {
               long time = Calendar.getInstance().getTimeInMillis();
                PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_APPROVAL_UPDATE_TIME, time);
            }
        }

        @Override
        protected void onPostExecute(SearchApprovalResponse resp) {
            super.onPostExecute(resp);

            if (mIsRefresh) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            if (resp.getStatus() == ServerResponse.FAIL) {
                mIsLoading = false;
                Utils.showMessage(getActivity(), "服务器返回出错！");
                return;
            }

            List<Approval> approvals = resp.getApprovals();

            if (mIsRefresh) {
                mApprovalAdapter.clear();
                mApprovalAdapter.mApprovals = approvals;
            } else {
                mApprovalAdapter.mApprovals.addAll(approvals);
            }

            if (resp.getTotalNumber() <= mApprovalAdapter.getCount()) {
                mMoreDataAvailable = false;
            } else {
                mMoreDataAvailable = true;
            }

            mIsLoading = false;
            Log.d(TAG, "loading complete");

            mApprovalAdapter.addMoreOrders();

            resetFootView();
        }
    }


    private class ApprovalAdapter extends ArrayAdapter<Approval> {
        private List<Approval> mApprovals;

        public ApprovalAdapter(List<Approval> approvals) {
            super(getActivity(), 0, approvals);
            mApprovals = approvals;
        }

        public void addMoreOrders() {
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mApprovals.size();
        }

        @Override
        public Approval getItem(int position) {
            return mApprovals.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_approval, null);
            }

            Approval approval = getItem(position);
            TextView approvalObjectTextView = (TextView) convertView.findViewById(R.id.approval_list_item_approvalObject);
            approvalObjectTextView.setText(approval.getApprovalObject());

            TextView typeTextView = (TextView) convertView.findViewById(R.id.approval_list_item_type);
            typeTextView.setText(approval.getType());

            TextView statusTextView = (TextView) convertView.findViewById(R.id.approval_list_item_status);
            if (approval.getStatus().equals("待批"))
                statusTextView.setText(approval.getStatus());
            else
                statusTextView.setText(approval.getApprovalResult());


            TextView amountTextView = (TextView) convertView.findViewById(R.id.approval_list_item_amount);
            amountTextView.setText("¥" + String.format("%.2f", approval.getAmount()));

            TextView keywordTextView = (TextView) convertView.findViewById(R.id.approval_list_item_keyword);
            keywordTextView.setText(approval.getKeyword());

            TextView reporterTextView = (TextView) convertView.findViewById(R.id.approval_list_item_reporter);
            reporterTextView.setText(approval.getReporter());

            TextView dateTextView = (TextView) convertView.findViewById(R.id.approval_list_item_date);
            dateTextView.setText(approval.getReportDate());

            return convertView;
        }
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
        if (requestCode == 1) {
            int position = data.getIntExtra(ApprovalDetailFragment.EXTRA_POSITON, -1);
            Log.d(TAG, "position = " + position);
            if (position != -1) {
                Approval approval = (Approval)data.getSerializableExtra(ApprovalDetailFragment.EXTRA_APPROVAL);
                Log.d(TAG, "approval = " + approval);
                if (approval != null) {
                    Approval item = mApprovalAdapter.getItem(position);
                    Log.d(TAG, "approval.status = " + approval.getStatus());
                    if (!approval.getStatus().equals("待批")) {

                        item.setApprovalResult(approval.getApprovalResult());
                        item.setStatus(approval.getStatus());

                        View v = mListView.getChildAt(position - mListView.getFirstVisiblePosition());

                        if(v == null) {
                            Log.d(TAG, "v is null");
                            return;
                        }

                        Log.d(TAG, "update list view");
                        TextView statusTextView = (TextView) v.findViewById(R.id.approval_list_item_status);
                        statusTextView.setText(item.getApprovalResult());
                    }
                }
            }
        }
    }

    @Override
    public void doBack() {
        Intent i = new Intent(getActivity(), MainActivity2.class);
        i.putExtra(ApprovalSearchFragment.EXTRA_QUERYOBJECT, mQueryObject);
        i.putExtra(MainActivity2.EXTRA_TAB, 1);
        startActivity(i);
    }



}

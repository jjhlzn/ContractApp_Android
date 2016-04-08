package com.jinjunhang.contract.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.List;

/**
 * Created by lzn on 16/4/6.
 */
public class ApprovalListFragment extends android.support.v4.app.ListFragment implements SingleFragmentActivity.OnBackPressedListener, AbsListView.OnScrollListener {
    private static final String TAG = "ApprovalListFragment";
    private List<Approval> mApprovals;
    private ApprovalQueryObject mQueryObject;


    //用于load more
    private ApprovalAdapter mApprovalAdapter;
    private View mFooterView;
    private boolean mIsLoading = false;
    private boolean mMoreDataAvailable = true;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");

        Intent i = getActivity().getIntent();
        mApprovals = (List<Approval>) i.getSerializableExtra(ApprovalSearchFragment.EXTRA_APPROVALS);

        mQueryObject = (ApprovalQueryObject)i.getSerializableExtra(ApprovalSearchFragment.EXTRA_QUERYOBJECT);


        mApprovalAdapter = new ApprovalAdapter(mApprovals);
        setListAdapter(mApprovalAdapter);
        if (mApprovals.size() == 0) {
            mMoreDataAvailable = false;
        }

        ((SingleFragmentActivity) getActivity()).setOnBackPressedListener(this);


        mFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.loading_view, null);

        if (mApprovals.size() == 0) {
            mFooterView.findViewById(R.id.loading_progressbar).setVisibility(View.GONE);
            TextView textView = ((TextView)mFooterView.findViewById(R.id.loading_message));
            textView.setText("没有找到任何审批");
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.offsetTopAndBottom(30);
        }
        getListView().addFooterView(mFooterView, null, false);
        getListView().setOnScrollListener(this);
    }





    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (mApprovals.size() == 0)
            return;

        int lastInScreen = firstVisibleItem + visibleItemCount;

        if (!mIsLoading && mMoreDataAvailable) {
            if (lastInScreen == totalItemCount) {
                Log.d(TAG, "start loading more");
                new SearchApprovalTask().execute();
            }
        }

        if (!mMoreDataAvailable) {
            mFooterView.findViewById(R.id.loading_progressbar).setVisibility(View.GONE);
            ((TextView)mFooterView.findViewById(R.id.loading_message)).setText("已加载全部数据");

        }
    }

    private class SearchApprovalTask extends AsyncTask<Void, Void, SearchApprovalResponse> {
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

        }

        @Override
        protected void onPostExecute(SearchApprovalResponse resp) {
            super.onPostExecute(resp);

            if (resp.getStatus() == ServerResponse.FAIL) {
                mIsLoading = false;
                Utils.showMessage(getActivity(), "服务器返回出错！");
                return;
            }

            List<Approval> approvals = resp.getApprovals();
            mApprovalAdapter.mApprovals.addAll(approvals);

            if (resp.getTotalNumber() <= mApprovalAdapter.getCount()) {
                mMoreDataAvailable = false;
            }

            mIsLoading = false;
            Log.d(TAG, "loading complete");

            mApprovalAdapter.addMoreOrders();
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        Approval approval = ((ApprovalAdapter) getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), ApprovalDetailActivity.class);
        i.putExtra(ApprovalDetailFragment.EXTRA_APPROVAL, approval);
        i.putExtra(ApprovalDetailFragment.EXTRA_POSITON, position);
        startActivityForResult(i, 1);
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

                        View v = getListView().getChildAt(position - getListView().getFirstVisiblePosition());

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

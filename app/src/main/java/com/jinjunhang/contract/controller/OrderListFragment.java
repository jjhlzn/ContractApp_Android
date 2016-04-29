package com.jinjunhang.contract.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.model.Order;
import com.jinjunhang.contract.service.OrderQueryObject;
import com.jinjunhang.contract.service.OrderService;
import com.jinjunhang.contract.service.SearchOrderResponse;
import com.jinjunhang.contract.service.ServerResponse;

import java.util.List;

/**
 * Created by lzn on 16/3/23.
 */
public class OrderListFragment extends android.support.v4.app.Fragment implements SingleFragmentActivity.OnBackPressedListener,
        AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener
{
    private static final String TAG = "OrderListFragment";
    private List<Order> mOrders;
    private OrderQueryObject mQueryObject;


    //用于load more
    private OrderAdapter mOrderAdapter;
    private View mFooterView;
    private boolean mIsLoading = false;
    private boolean mMoreDataAvailable = true;

    private ListView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragement_pushdownrefresh, container, false);

        mListView = (ListView) v.findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = mOrderAdapter.getItem(position);

                Intent i = new Intent(getActivity(), OrderMenuActivity.class);
                i.putExtra(OrderMenuFragment.EXTRA_ORDER_NO, order.getContractNo());
                startActivity(i);
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent i = getActivity().getIntent();
        mOrders = (List<Order>) i.getSerializableExtra(SearchOrderFragment.EXTRA_ORDERS);

        mQueryObject = (OrderQueryObject)i.getSerializableExtra(SearchOrderFragment.EXTRA_QUERYOBJECT);



        mOrderAdapter = new OrderAdapter(mOrders);
        mListView.setAdapter(mOrderAdapter);
        if (mOrders.size() == 0) {
            mMoreDataAvailable = false;
        }

        ((SingleFragmentActivity) getActivity()).setOnBackPressedListener(this);


        mFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.loading_view, null);

        if (mOrders.size() == 0) {
            mFooterView.findViewById(R.id.loading_progressbar).setVisibility(View.GONE);
            TextView textView = ((TextView)mFooterView.findViewById(R.id.loading_message));
            textView.setText("没有找到任何订单");
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.offsetTopAndBottom(30);
        }
        mListView.addFooterView(mFooterView, null, false);
        mListView.setOnScrollListener(this);
    }



    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (mOrders.size() == 0)
            return;

        int lastInScreen = firstVisibleItem + visibleItemCount;

        if (!mIsLoading && mMoreDataAvailable) {
            if (lastInScreen == totalItemCount) {
                Log.d(TAG, "start loading more");
                new SearchOrderTask().execute();
            }
        }

        if (!mMoreDataAvailable) {
            mFooterView.findViewById(R.id.loading_progressbar).setVisibility(View.GONE);
            ((TextView)mFooterView.findViewById(R.id.loading_message)).setText("已加载全部数据");

        }
    }

    @Override
    public void onRefresh() {
        if (!mIsLoading) {
            new SearchOrderTask(true).execute();
        }
    }

    private class SearchOrderTask extends AsyncTask<Void, Void, SearchOrderResponse> {
        private boolean mIsRefresh = false;
        public SearchOrderTask() {
        }

        public SearchOrderTask(boolean isRefresh) {
            this.mIsRefresh = isRefresh;
        }

        @Override
        protected SearchOrderResponse doInBackground(Void... params) {
            mQueryObject.setIndex( mQueryObject.getIndex() + 1);

            return new OrderService().search(PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, ""),
                    mQueryObject.getKeyword(), mQueryObject.getStartDate(),
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
        }

        @Override
        protected void onPostExecute(SearchOrderResponse resp) {
            super.onPostExecute(resp);
            if (mIsRefresh) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            if (resp.getStatus() == ServerResponse.FAIL) {
                mIsLoading = false;
                Utils.showMessage(getActivity(), "服务器返回出错！");
                return;
            }

            List<Order> orders = resp.getOrders();
            if (mIsRefresh) {
                mOrderAdapter.mOrders.clear();
                mOrderAdapter.mOrders = orders;
            } else {
                mOrderAdapter.mOrders.addAll(orders);
            }

            if (resp.getTotalNumber() <= mOrderAdapter.getCount()) {
                mMoreDataAvailable = false;
            } else {
                mMoreDataAvailable = true;
            }

            mIsLoading = false;
            Log.d(TAG, "loading complete");

            mOrderAdapter.addMoreOrders();
        }
    }



    private class OrderAdapter extends ArrayAdapter<Order> {
        private List<Order> mOrders;

        public OrderAdapter(List<Order> orders) {
            super(getActivity(), 0, orders);
            mOrders = orders;
        }

        public void addMoreOrders() {
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mOrders.size();
        }

        @Override
        public Order getItem(int position) {
            return mOrders.get(position);
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
            amountTextView.setText(order.getMoneyType() + String.format("%.2f", order.getAmount()));

            TextView guestNameTextView = (TextView) convertView.findViewById(R.id.order_list_item_guestNameTextView);
            guestNameTextView.setText(order.getGuestName());

            return convertView;
        }
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void doBack() {
        Intent i = new Intent(getActivity(), MainActivity2.class);
        i.putExtra(SearchOrderFragment.EXTRA_QUERYOBJECT, mQueryObject);
        startActivity(i);
    }





}

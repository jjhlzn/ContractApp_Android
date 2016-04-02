package com.example.bottombar.sample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.contract.model.Order;
import com.jinjunhang.contract.service.OrderQueryObject;
import com.jinjunhang.contract.service.OrderService;
import com.jinjunhang.contract.service.SearchOrderResponse;

import java.util.List;

/**
 * Created by lzn on 16/3/23.
 */
public class OrderListFragment extends ListFragment implements SingleFragmentActivity.OnBackPressedListener, AbsListView.OnScrollListener {
    private static final String TAG = "OrderListFragment";
    private List<Order> mOrders;
    private OrderQueryObject mQueryObject;


    //用于load more
    private final int AUTOLOAD_THRESHOLD = 4;
    private OrderAdapter mOrderAdapter;
    private View mFooterView;
    private boolean mIsLoading = false;
    private boolean mMoreDataAvailable = true;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getActivity().getIntent();
        mOrders = (List<Order>) i.getSerializableExtra(SearchOrderFragment.EXTRA_ORDERS);
        mQueryObject = (OrderQueryObject)i.getSerializableExtra(SearchOrderFragment.EXTRA_QUERYOBJECT);

        mOrderAdapter = new OrderAdapter(mOrders);
        setListAdapter(mOrderAdapter);
        mHandler = new Handler();

        ((SingleFragmentActivity) getActivity()).setOnBackPressedListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        if (NavUtils.getParentActivityName(getActivity()) != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mHandler = new Handler();

        mFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.loading_view, null);
        getListView().addFooterView(mFooterView, null, false);
        getListView().setOnScrollListener(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Order order = ((OrderAdapter) getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), OrderMenuActivity.class);
        i.putExtra(OrderMenuFragment.EXTRA_ORDER_NO, order.getContractNo());
        startActivity(i);
    }


    @Override
    public void doBack() {
        Intent i = new Intent(getActivity(), SearchOrderActivity.class);
        i.putExtra(SearchOrderFragment.EXTRA_QUERYOBJECT, mQueryObject);
        startActivity(i);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (!mIsLoading && mMoreDataAvailable) {
            Log.d(TAG, "onScroll");
            if (totalItemCount - AUTOLOAD_THRESHOLD <= firstVisibleItem + visibleItemCount) {
                Log.d(TAG, "loading more");
                mIsLoading = true;
                mHandler.post(mAddItemsRunnable);
            }
        }
    }

    private Runnable mAddItemsRunnable = new Runnable() {
        @Override
        public void run() {
            mOrderAdapter.LoadMoreOrders();
            mIsLoading = false;
            Log.d(TAG, "loading complete");
        }
    };



    private class OrderAdapter extends ArrayAdapter<Order> {
        private List<Order> mOrders;

        public OrderAdapter(List<Order> orders) {
            super(getActivity(), 0, orders);
            mOrders = orders;
        }

        public void LoadMoreOrders() {
            new SearchOrderTask().execute();
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
            amountTextView.setText(order.getAmount() + "");

            TextView guestNameTextView = (TextView) convertView.findViewById(R.id.order_list_item_guestNameTextView);
            guestNameTextView.setText(order.getGuestName());

            return convertView;
        }
    }

    private class SearchOrderTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            SearchOrderResponse resp = new OrderService().search(mQueryObject.getKeyword(), mQueryObject.getStartDate(), mQueryObject.getEndDate(), mQueryObject.getIndex(), mQueryObject.getPageSize());
            List<Order> orders = resp.getOrders();
            mOrderAdapter.mOrders.addAll(orders);
            return null;
        }
    }


}

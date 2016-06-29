package com.jinjunhang.contract.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.model.PriceReport;
import com.jinjunhang.contract.model.Product;
import com.jinjunhang.contract.service.GetPriceReportRequest;
import com.jinjunhang.contract.service.PriceReportQueryObject;
import com.jinjunhang.framework.controller.PagableController;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.PagedServerResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/14.
 */
public class PriceReportDetailFragment extends android.support.v4.app.Fragment implements AbsListView.OnScrollListener {

    private static final String TAG = "PriceReportDetail";
    public final static String EXTRA_PRICEREPORT = "EXTRA_PRICEREPORT";


    private PriceReport mPriceReport;
    private PagableController mPagableController;
    private PriceReportQueryObject mQueryObject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragement_pushdownrefresh, container, false);

        ListView listView = (ListView) v.findViewById(R.id.listView);
        mPriceReport = (PriceReport) getActivity().getIntent().getSerializableExtra(EXTRA_PRICEREPORT);

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);

        mPagableController = new PagableController();
        mPagableController.setActivity(getActivity());
        mPagableController.setListView(listView);
        mPagableController.setSwipeRefreshLayout(swipeRefreshLayout);
        mPagableController.setPagableArrayAdapter(new PriceReportDetailAdapter(mPagableController, new ArrayList<Product>()));
        mPagableController.setPagableRequestHandler(new PriceReportDetailRequestHandler());
        mPagableController.setOnScrollListener(this);

        return v;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mPagableController.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }

    class PriceReportDetailRequestHandler implements PagableController.PagableRequestHandler {
        @Override
        public PagedServerResponse handle() {
            GetPriceReportRequest req = new GetPriceReportRequest();
            req.setUserId(PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, ""));
            req.setReportId(mPriceReport.getId());
            return new BasicService().sendRequest(req);
        }
    }

    class PriceReportDetailAdapter extends PagableController.PagableArrayAdapter<Product> {
        public PriceReportDetailAdapter(PagableController pagableController, List<Product> dataSet) {
            super(pagableController, dataSet);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_product, null);
            }

            Product product = getItem(position);
            TextView nameTextView = (TextView) convertView.findViewById(R.id.product_list_item_name);
            nameTextView.setText(product.getChineseName());

            TextView huohaoTextView = (TextView) convertView.findViewById(R.id.product_list_item_huohao);
            huohaoTextView.setText(product.getHuohao());

            TextView unitTextView = (TextView) convertView.findViewById(R.id.product_list_item_unit);
            unitTextView.setText(product.getUnit());

            TextView priceTextView = (TextView) convertView.findViewById(R.id.product_list_item_price);
            priceTextView.setText(product.getMoneyType() + String.format("%.2f", product.getSellPrice()));

            TextView englishNameTextView = (TextView) convertView.findViewById(R.id.product_list_item_englishName);
            englishNameTextView.setText(product.getEnglishName());

            TextView guigeTextView = (TextView) convertView.findViewById(R.id.product_list_item_guige);
            guigeTextView.setText(product.getGuige());

            return convertView;
        }
    }
}

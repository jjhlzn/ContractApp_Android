package com.example.bottombar.sample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jinjunhang.contract.service.GetOrderBasicInfoResponse;
import com.jinjunhang.contract.service.GetOrderChuyunInfoResponse;
import com.jinjunhang.contract.service.GetOrderFukuangInfoResponse;
import com.jinjunhang.contract.service.GetOrderShougouInfoResponse;
import com.jinjunhang.contract.service.GetOrderShouhuiInfoResponse;
import com.jinjunhang.contract.service.OrderService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/3/24.
 */
public class OrderMenuFragment extends android.support.v4.app.ListFragment {

    public final static String EXTRA_ORDER_NO = "orderNo";

    private List<String> mItems;

    private String mOrderNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItems = new ArrayList<String>();
        mItems.add("合同基本信息");
        mItems.add("收购合同信息");
        mItems.add("出运信息");
        mItems.add("出厂付款信息");
        mItems.add("收汇信息");

        Intent i = getActivity().getIntent();
        mOrderNo = i.getStringExtra(EXTRA_ORDER_NO);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mItems);
        setListAdapter(adapter);

        if (NavUtils.getParentActivityName(getActivity()) != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        switch (position) {
            case 0:
                new GetBasicInfoTask().execute();
                break;
            case 1:
                new GetShougouInfoTask().execute();
                break;
            case 2:
                new GetChuyunInfoTask().execute();
                break;
            case 3:
                new GetFukuangInfoTask().execute();
                break;
            case 4:
                new GetShouhuiInfoTask().execute();
                break;
        }
    }


    private class GetBasicInfoTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetOrderBasicInfoResponse resp = new OrderService().getBasicInfo(mOrderNo);
            Intent i = new Intent(getActivity(), OrderBasicInfoActivity.class);
            i.putExtra(OrderBasicInfoFragment.EXTRA_ORDER_BASIC_INFO, resp.getBasicInfo());

            startActivity(i);
            return null;
        }
    }

    private class GetShougouInfoTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            /*
            GetOrderShougouInfoResponse resp = new OrderService().getShougouInfo(mOrderNo);
            Intent i = new Intent(getActivity(), OrderShougouActivity.class);
            i.putExtra(OrderShougouFragment.EXTRA_SHOUGOU_INFO, resp.getShougouInfo());
            startActivity(i); */
            return null;
        }
    }

    private class GetChuyunInfoTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetOrderChuyunInfoResponse resp = new OrderService().getChuyunInfo(mOrderNo);
            Intent i = new Intent(getActivity(), OrderChuyunActivity.class);
            i.putExtra(OrderChuyunFragment.EXTRA_ORDER_CHUYUN, resp.getChuyunInfo());
            startActivity(i);
            return null;
        }
    }

    private class GetFukuangInfoTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetOrderFukuangInfoResponse resp = new OrderService().getFukuangInfo(mOrderNo);
            Intent i = new Intent(getActivity(), OrderFukuangActivity.class);
            i.putExtra(OrderFukuangFragment.EXTRA_ORDER_FUKUANG, resp.getFukuangInfo());
            startActivity(i);
            return null;
        }
    }

    private class GetShouhuiInfoTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetOrderShouhuiInfoResponse resp = new OrderService().getShouhuiInfo(mOrderNo);
            Intent i = new Intent(getActivity(), OrderShouhuiActivity.class);
            i.putExtra(OrderShouhuiFragment.EXTRA_ORDER_SHOUHUI, resp.getShouhuiInfo());
            startActivity(i);
            return null;
        }
    }
 }

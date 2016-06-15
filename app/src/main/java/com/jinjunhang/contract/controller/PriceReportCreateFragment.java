package com.jinjunhang.contract.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.model.OrderPurchaseInfo;
import com.jinjunhang.contract.model.OrderPurchaseItem;
import com.jinjunhang.contract.model.Product;
import com.jinjunhang.contract.service.GetOrderChuyunInfoResponse;
import com.jinjunhang.contract.service.OrderService;
import com.jinjunhang.contract.service.SearchProductRequest;
import com.jinjunhang.contract.service.SearchProductResponse;
import com.jinjunhang.contract.service.ServerResponse;
import com.jinjunhang.contract.service.SubmitReportRequest;
import com.jinjunhang.contract.service.SubmitReportResponse;
import com.jinjunhang.framework.service.BasicService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/14.
 */
public class PriceReportCreateFragment extends android.support.v4.app.Fragment {
    public final static String EXTRA_ALL_CODES = "PriceReportCreateFragment_EXTRA_CODES";

    private static final String TAG = "PriceCreateFragment";

    private List<String> mAllCodes;
    private ListView mListView;
    private LoadingAnimation mLoading;
    private ProductAdapter mProductAdapter;

    private Dialog mDialog;
    private EditText mCompanyName;
    private EditText mContactName;
    private EditText mContactPhone;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragement_list, container, false);
        mListView = (ListView) v.findViewById(R.id.listView);

        Intent i = getActivity().getIntent();
        mAllCodes = (List<String>)i.getSerializableExtra(EXTRA_ALL_CODES);

        mLoading = new LoadingAnimation(getActivity());

        mProductAdapter = new ProductAdapter( new ArrayList<Product>());
        mListView.setAdapter(mProductAdapter);


        new SearchProductTask().execute();


        mDialog = new Dialog(getActivity());
        mDialog.setContentView(R.layout.dialog_customer);
        mDialog.setTitle("报价单客户信息");


        // set the custom dialog components - text, image and button
        mCompanyName = (EditText) mDialog.findViewById(R.id.customer_company_name);
        mContactName = (EditText) mDialog.findViewById(R.id.customer_contact_name);
        mContactPhone = (EditText) mDialog.findViewById(R.id.customer_contact_phone);

        ((Button) mDialog.findViewById(R.id.custom_button_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "cancel pressed");
                mDialog.dismiss();
            }
        });

        ((Button) mDialog.findViewById(R.id.custom_button_confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyName = mCompanyName.getText().toString();
                String contactName = mContactName.getText().toString();
                String phone = mContactPhone.getText().toString();
                if ("".equals(companyName.trim()) || "".equals(contactName.trim()) || "".equals(phone.trim()) ){
                    Utils.showMessage(getActivity(), "请填写全部客户信息");
                    return;
                }

                mDialog.dismiss();
                new SubmitReportTask().execute();
            }
        });


        ((Button)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.actionbar_finishButton))
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog


                mDialog.show();
                //new SubmitReportTask().execute();
            }
        });

        return v;
    }

    private String getCodeString() {
        String result = "";
        for (String code : mAllCodes) {
            result += code + "#@#";
        }
        return result;
    }

    private class SearchProductTask extends AsyncTask<Void, Void, SearchProductResponse> {
        @Override
        protected SearchProductResponse doInBackground(Void... params) {
            SearchProductRequest req = new SearchProductRequest();
            req.setUserId(PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, ""));
            req.setCodes(getCodeString());
            return new BasicService().sendRequest(req);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("");
        }

        @Override
        protected void onPostExecute(SearchProductResponse resp) {
            super.onPostExecute(resp);
            mLoading.dismiss();
            if (resp.getStatus() != ServerResponse.SUCCESS) {
                Utils.showServerErrorDialog(getActivity());
                return;
            }

            mProductAdapter.setProducts(resp.getProducts());
            Log.d(TAG, "resp.getProducts.size = " + resp.getProducts().size());
        }
    }

    private class SubmitReportTask extends AsyncTask<Void, Void, SubmitReportResponse> {
        @Override
        protected SubmitReportResponse doInBackground(Void... params) {
            SubmitReportRequest req = new SubmitReportRequest();
            req.setUserId(PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, ""));
            req.setCodesString(getCodeString());
            return new BasicService().sendRequest(req);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("");
        }

        @Override
        protected void onPostExecute(SubmitReportResponse resp) {
            super.onPostExecute(resp);
            mLoading.dismiss();
            if (resp.getStatus() != 0) {
                Utils.showMessage(getActivity(), resp.getErrorMessage());
                return;
            }

            Utils.showMessage(getActivity(), "创建成功", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(getActivity(), MainActivity2.class)
                            .putExtra(MainActivity2.EXTRA_TAB, 2);
                    startActivity(i);
                }
            });

        }
    }

    private class ProductAdapter extends ArrayAdapter<Product> {
        private List<Product> mProducts;

        public ProductAdapter(List<Product> products) {
            super(getActivity(), 0, products);
            mProducts = products;
        }

        public void setProducts(List<Product> products) {
            mProducts = products;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mProducts.size();
        }

        @Override
        public Product getItem(int position) {
            return mProducts.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_create_product, null);
            }

            Product product = getItem(position);
            TextView nameTextView = (TextView) convertView.findViewById(R.id.product_list_item_name);
            nameTextView.setText(product.getChineseName());

            TextView huohaoTextView = (TextView) convertView.findViewById(R.id.product_list_item_huohao);
            huohaoTextView.setText(product.getHuohao());


            TextView englishNameTextView = (TextView) convertView.findViewById(R.id.product_list_item_englishName);
            englishNameTextView.setText(product.getEnglishName());

            TextView guigeTextView = (TextView) convertView.findViewById(R.id.product_list_item_guige);
            guigeTextView.setText(product.getGuige());

            return convertView;
        }
    }


}

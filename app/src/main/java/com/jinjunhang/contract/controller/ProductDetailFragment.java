package com.jinjunhang.contract.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.model.Product;
import com.jinjunhang.contract.service.GetProductResponse;
import com.jinjunhang.contract.service.ProductService;
import com.jinjunhang.contract.service.ServerResponse;

import java.io.InputStream;

/**
 * Created by lzn on 16/4/8.
 */
public class ProductDetailFragment extends android.support.v4.app.Fragment implements SingleFragmentActivity.OnBackPressedListener{

    public final static String EXTRA_PRODUCTID = "productId";

    private LoadingAnimation mLoading;
    private String mProductId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product, container, false);

        mProductId = getActivity().getIntent().getStringExtra(EXTRA_PRODUCTID);
        return v;
    }

    @Override
    public void doBack() {
        Intent i = new Intent(getActivity(), MainActivity2.class);
        i.putExtra(MainActivity2.EXTRA_TAB, 2);
        startActivity(i);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLoading = new LoadingAnimation(getActivity());
        new GetProductTask().execute();
        //new DownloadImageTask().execute();
    }

    class GetProductTask extends AsyncTask<Void, Void, GetProductResponse> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("");
        }

        @Override
        protected void onPostExecute(GetProductResponse resp) {
            super.onPostExecute(resp);
            mLoading.dismiss();
            if (resp.getStatus() == ServerResponse.FAIL) {
                Utils.showServerErrorDialog(getActivity());
                return;
            }

            Product product = resp.getProduct();
            if (product == null){
                Utils.showMessage(getActivity(), "该商品不存在！");
                return;
            }

            ((TextView)getActivity().findViewById(R.id.product_chineseName)).setText(product.getChineseName());
            ((TextView)getActivity().findViewById(R.id.product_englishName)).setText(product.getEnglishName());
            ((TextView)getActivity().findViewById(R.id.product_huohao)).setText(product.getHuohao());
            ((TextView)getActivity().findViewById(R.id.product_xinghao)).setText(product.getXinghao());
            ((TextView)getActivity().findViewById(R.id.product_chengbenPrice)).setText("¥" + String.format("%.2f", product.getChengbenPrice()));
            ((TextView)getActivity().findViewById(R.id.product_sellPrice)).setText("¥" + String.format("%.2f", product.getSellPrice()));
            ((TextView)getActivity().findViewById(R.id.product_chineseDesc)).setText(product.getChineseDesc());
            ((TextView)getActivity().findViewById(R.id.product_englishDesc)).setText(product.getEnglishDesc());

        }

        @Override
        protected GetProductResponse doInBackground(Void... params) {
            return new ProductService().GetProductById("");
        }
    }

     class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             mLoading.show("");
         }

         protected Bitmap doInBackground(Void... params) {
            String urldisplay = "http://www.cnpps.org/attachement/jpg/site15/20140922/bc305bae4037158a06ea62.jpg";
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            mLoading.dismiss();
            ImageView imageView = ((ImageView) getActivity().findViewById(R.id.product_image));
            imageView.setImageBitmap(result);

        }
    }
}

package com.jinjunhang.contract.service;



import com.jinjunhang.contract.model.Product;
import com.jinjunhang.framework.service.ServerRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/14.
 */
public class SearchProductResponse extends com.jinjunhang.framework.service.ServerResponse {

    private List<Product> mProducts;

    public List<Product> getProducts() {
        return mProducts;
    }

    public void setProducts(List<Product> products) {
        mProducts = products;
    }


    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        mProducts = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("products");
        for (int i = 0; i < jsonArray.length(); i++ ) {
            JSONObject json = jsonArray.getJSONObject(i);
            Product product = new Product();
            product.setId(json.getString("id"));
            product.setChineseName(json.getString("name"));
            product.setHuohao(json.getString("huohuo"));
            product.setGuige(json.getString("specification"));
            product.setSellPrice(json.getDouble("price"));
            product.setMoneyType(json.getString("moneyType"));
            product.setEnglishName(json.getString("englishName"));
            product.setUnit(json.getString("unit"));
            mProducts.add(product);
        }


    }

}

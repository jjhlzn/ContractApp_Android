package com.jinjunhang.contract.service;

import com.jinjunhang.contract.model.Product;
import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerRequest;

import com.jinjunhang.contract.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lzn on 16/6/14.
 */
public class GetPriceReportResponse extends PagedServerResponse<Product> {

    public GetPriceReportResponse() {
        mResultSet = new ArrayList<>();
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        super.parse(request, jsonObject);

        JSONArray jsonArray = jsonObject.getJSONArray("products");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Product product = new Product();
            product.setId(json.getString("id"));
            product.setChineseName(json.getString("name"));
            product.setGuige(json.getString("specification"));
            product.setSellPrice(json.getDouble("price"));
            product.setMoneyType(json.getString("moneyType"));
            product.setEnglishName(json.getString("englishName"));
            product.setUnit(json.getString("unit"));
            product.setHuohao(json.getString("huohuo"));
            mResultSet.add(product);
        }

    }
}

package com.jinjunhang.contract.service;

import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerRequest;
import com.jinjunhang.contract.model.PriceReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lzn on 16/6/14.
 */
public class SearchPriceReportsResponse extends PagedServerResponse<PriceReport> {

    public SearchPriceReportsResponse() {
        mResultSet = new ArrayList<>();
    }

    @Override
    public void parse(ServerRequest request, JSONObject jsonObject) throws JSONException {
        super.parse(request, jsonObject);

        JSONArray jsonArray = jsonObject.getJSONArray("reports");

        for (int i = 0 ; i <jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            PriceReport priceReport = new PriceReport();
            priceReport.setId(json.getString("id"));
            priceReport.setReporter(json.getString("reporter"));
            priceReport.setDate(json.getString("date"));
            priceReport.setStatus(json.getString("status"));
            priceReport.setDetailInfo(json.getString("detailInfo"));
            priceReport.setValidDays(json.getString("validDays"));
            mResultSet.add(priceReport);
        }
    }


}

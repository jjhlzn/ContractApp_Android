package com.jinjunhang.contract.service;

import android.app.Service;

import com.jinjunhang.contract.model.Order;
import com.jinjunhang.contract.model.OrderBasicInfo;
import com.jinjunhang.contract.model.OrderChuyunInfo;
import com.jinjunhang.contract.model.OrderPurchaseInfo;
import com.jinjunhang.contract.model.OrderPurchaseItem;
import com.jinjunhang.contract.model.OrderShouhuiInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;

/**
 * Created by lzn on 16/3/23.
 */
public class OrderService extends BasicService {
    public SearchOrderResponse search(String userId, String keyword, String startDate, String endDate, int pageNo, int pageSize) {

        Map<String, String> parameters = new LinkedHashMap<String, String>();
        parameters.put("userid", userId);
        parameters.put("keyword", keyword);
        parameters.put("startdate", startDate);
        parameters.put("enddate", endDate);
        parameters.put("index", pageNo+"");
        parameters.put("pagesize", pageSize+"");


        return sendRequest(ServiceConfiguration.SeachOrderUrl, parameters, SearchOrderResponse.class, new ResponseHandler(){
            @Override
            public ServerResponse handle(ServerResponse response, JSONObject json) throws JSONException {
                SearchOrderResponse resp = (SearchOrderResponse)response;
                resp.setTotalNumber(json.getInt("totalNumber"));
                JSONArray array = json.getJSONArray("orders");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    Order order = new Order();
                    order.setBusinessPerson(item.getString("businessPerson"));
                    order.setAmount(item.getDouble("amount"));
                    order.setContractNo(item.getString("contractNo"));
                    order.setGuestName(item.getString("guestName"));
                    order.setOrderNo(item.getString("orderNo"));
                    order.setMoneyType(item.getString("moneyType"));
                    resp.addOrder(order);
                }
                return resp;
            }
        });
    }

    public GetOrderBasicInfoResponse getBasicInfo(String orderNo) {
        Map<String, String> params = new LinkedHashMap();
        params.put("orderid", orderNo);
        return sendRequest(ServiceConfiguration.GetBasicInfoUrl, params, GetOrderBasicInfoResponse.class, new ResponseHandler()  {
            @Override
            public ServerResponse handle(ServerResponse response, JSONObject json) throws JSONException {
                GetOrderBasicInfoResponse resp = (GetOrderBasicInfoResponse) response;

                OrderBasicInfo basicInfo = new OrderBasicInfo();
                JSONObject basicInfoJson = json.getJSONObject("basicInfo");
                basicInfo.setTimeLimit(basicInfoJson.getString("timeLimit"));
                basicInfo.setStartPort(basicInfoJson.getString("startPort"));
                basicInfo.setDestPort(basicInfoJson.getString("destPort"));
                basicInfo.setGetMoneyType(basicInfoJson.getString("getMoneyType"));
                basicInfo.setPriceRule(basicInfoJson.getString("priceRules"));

                resp.setBasicInfo(basicInfo);
                return resp;
            }
        });
    }


    public GetOrderShougouInfoResponse getShougouInfo(String orderNo) {
        Map<String, String> params = new LinkedHashMap();
        params.put("orderid", orderNo);
        return sendRequest(ServiceConfiguration.GetOrderPurcaseInfoUrl, params, GetOrderShougouInfoResponse.class, new ResponseHandler() {
            @Override
            public ServerResponse handle(ServerResponse response, JSONObject json) throws JSONException {
                GetOrderShougouInfoResponse resp = (GetOrderShougouInfoResponse)response;
                OrderPurchaseInfo shougouInfo = new OrderPurchaseInfo();
                List<OrderPurchaseItem> items = new ArrayList<OrderPurchaseItem>();
                shougouInfo.setItems(items);
                JSONArray jsonArray = json.getJSONArray("purchaseInfo");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    OrderPurchaseItem item = new OrderPurchaseItem();
                    item.setContract(jsonItem.getString("contract"));
                    item.setAmount(jsonItem.getDouble("amount"));
                    item.setDate(jsonItem.getString("date"));
                    item.setFactory(jsonItem.getString("factory"));
                    items.add(item);
                }
                resp.setShougouInfo(shougouInfo);
                return resp;
            }
        });
    }

    public GetOrderChuyunInfoResponse getChuyunInfo(String orderNo) {
        Map<String, String> params = new LinkedHashMap();
        params.put("orderid", orderNo);
        return sendRequest(ServiceConfiguration.GetOrderChuyunInfoUrl, params, GetOrderChuyunInfoResponse.class, new ResponseHandler() {
            @Override
            public ServerResponse handle(ServerResponse response, JSONObject json) throws JSONException {
                GetOrderChuyunInfoResponse resp = (GetOrderChuyunInfoResponse)response;
                JSONObject chuyunJson = json.getJSONObject("chuyunInfo");
                OrderChuyunInfo chuyunInfo = new OrderChuyunInfo();
                chuyunInfo.setDetailNo(chuyunJson.getString("detailNo"));
                chuyunInfo.setDate(chuyunJson.getString("date"));
                chuyunInfo.setAmount(chuyunJson.getDouble("amount"));
                resp.setChuyunInfo(chuyunInfo);
                return resp;
            }
        });
    }


    public GetOrderFukuangInfoResponse getFukuangInfo(String orderNo) {
        Map<String, String> params = new LinkedHashMap();
        params.put("orderid", orderNo);
        return sendRequest(ServiceConfiguration.GetOrderFukuangInfoUrl, params, GetOrderFukuangInfoResponse.class, new ResponseHandler() {
            @Override
            public ServerResponse handle(ServerResponse response, JSONObject json) throws JSONException {
                GetOrderFukuangInfoResponse resp = (GetOrderFukuangInfoResponse)response;
                OrderPurchaseInfo shougouInfo = new OrderPurchaseInfo();
                List<OrderPurchaseItem> items = new ArrayList<OrderPurchaseItem>();
                shougouInfo.setItems(items);
                JSONArray jsonArray = json.getJSONArray("fukuangInfo");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    OrderPurchaseItem item = new OrderPurchaseItem();
                    item.setContract(jsonItem.getString("contract"));
                    item.setAmount(jsonItem.getDouble("amount"));
                    item.setDate(jsonItem.getString("date"));
                    item.setFactory(jsonItem.getString("factory"));
                    items.add(item);
                }
                resp.setFukuangInfo(shougouInfo);
                return resp;
            }
        });
    }

    public GetOrderShouhuiInfoResponse getShouhuiInfo(String orderNo) {
        Map<String, String> params = new LinkedHashMap();
        params.put("orderid", orderNo);
        return sendRequest(ServiceConfiguration.GetOrderShouhuiInfoUrl, params, GetOrderShouhuiInfoResponse.class, new ResponseHandler() {
            @Override
            public ServerResponse handle(ServerResponse response, JSONObject json) throws JSONException {
                GetOrderShouhuiInfoResponse resp = (GetOrderShouhuiInfoResponse)response;
                OrderShouhuiInfo shouhuiInfo = new OrderShouhuiInfo();
                JSONObject shouhuiJson = json.getJSONObject("shouhuiInfo");
                shouhuiInfo.setDate(shouhuiJson.getString("date"));
                shouhuiInfo.setAmount(shouhuiJson.getDouble("amount"));
                resp.setShouhuiInfo(shouhuiInfo);
                return resp;
            }
        });
    }

}



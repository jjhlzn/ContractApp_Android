package com.jinjunhang.contract.service;

import com.jinjunhang.contract.model.Order;
import com.jinjunhang.contract.model.OrderBasicInfo;
import com.jinjunhang.contract.model.OrderChuyunInfo;
import com.jinjunhang.contract.model.OrderPurchaseInfo;
import com.jinjunhang.contract.model.OrderPurchaseItem;
import com.jinjunhang.contract.model.OrderShouhuiInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ServiceConfigurationError;

/**
 * Created by lzn on 16/3/23.
 */
public class OrderService extends BasicService {



    public SearchOrderResponse search(String keyword, String startDate, String endDate, int pageNo, int pageSize) {
        String url = makeSearchOrderUrl(keyword, startDate, endDate, pageNo, pageSize);
        return sendRequest(url, SearchOrderResponse.class, new ResponseHandler(){
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
                    resp.addOrder(order);
                }

                return resp;
            }
        });
    }

    private String makeSearchOrderUrl(String keyword, String startDate, String endDate, int pageNo, int pageSize) {
        return ServiceConfiguration.SeachOrderUrl +
                String.format("?keyword=%s&startdate=%s&enddate=%s&index=%d&pagesize=%d",
                        keyword, startDate, endDate, pageNo, pageSize);
    }

    public GetOrderBasicInfoResponse getBasicInfo(String orderNo) {
        String url = makeGetOrderBasicInfoUrl(orderNo);
        return sendRequest(url, GetOrderBasicInfoResponse.class, new ResponseHandler()  {
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

    private String makeGetOrderBasicInfoUrl(String orderNo) {
        return ServiceConfiguration.GetBasicInfoUrl + "?orderId=" + orderNo;
    }


    public GetOrderShougouInfoResponse getShougouInfo(String orderNo) {
        String url = makeGetOrderShougouInfoUrl(orderNo);
        return sendRequest(url, GetOrderShougouInfoResponse.class, new ResponseHandler() {
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

    private String makeGetOrderShougouInfoUrl(String orderNo) {
        return ServiceConfiguration.GetOrderPurcaseInfoUrl + "?orderId=" + orderNo;
    }

    public GetOrderChuyunInfoResponse getChuyunInfo(String orderNo) {
        String url = makeGetOrderChuyunInfoUrl(orderNo);
        return sendRequest(url, GetOrderChuyunInfoResponse.class, new ResponseHandler() {
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

    private String makeGetOrderChuyunInfoUrl(String orderNo) {
        return ServiceConfiguration.GetOrderChuyunInfoUrl + "?orderId=" + orderNo;
    }

    public GetOrderFukuangInfoResponse getFukuangInfo(String orderNo) {
        String url = makeGetOrderFukuagnInfoUrl(orderNo);
        return sendRequest(url, GetOrderFukuangInfoResponse.class, new ResponseHandler() {
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

    private String makeGetOrderFukuagnInfoUrl(String orderNo) {
        return ServiceConfiguration.GetOrderFukuangInfoUrl + "?orderId=" + orderNo;
    }

    public GetOrderShouhuiInfoResponse getShouhuiInfo(String orderNo) {
        String url = makeGetOrderShouhuiInfoUrl(orderNo);
        return sendRequest(url, GetOrderShouhuiInfoResponse.class, new ResponseHandler() {
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

    private String makeGetOrderShouhuiInfoUrl(String orderNo) {
        return ServiceConfiguration.GetOrderShouhuiInfoUrl + "?orderId=" + orderNo;
    }
}



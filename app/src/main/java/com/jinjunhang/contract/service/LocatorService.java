package com.jinjunhang.contract.service;

import android.util.Log;

import com.jinjunhang.contract.BuildConfig;
import com.jinjunhang.contract.model.ServiceLocator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lzn on 16/4/30.
 */
public class LocatorService extends BasicService {

    public GetServiceLocatorResponse getServiceLocator() {

        Map<String, String> params = new LinkedHashMap();
        params.put("app", "huayuan_contract");
        params.put("platform", "android");
        params.put("version", BuildConfig.VERSION_NAME);

        return sendRequest("http://serviceLocator.hengdianworld.com:9000/servicelocator", params, GetServiceLocatorResponse.class,new ResponseHandler() {
            @Override
            public ServerResponse handle(ServerResponse response, JSONObject json) throws JSONException {
                GetServiceLocatorResponse resp = (GetServiceLocatorResponse)response;
                JSONObject resultJson = json.getJSONObject("result");
                ServiceLocator locator = new ServiceLocator();
                locator.setHttp(resultJson.getString("http"));
                locator.setServerName(resultJson.getString("serverName"));
                locator.setPort(resultJson.getInt("port"));
                resp.setServiceLocator(locator);

                return resp;
            }
        });
    }

}

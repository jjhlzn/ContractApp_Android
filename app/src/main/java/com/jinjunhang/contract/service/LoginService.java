package com.jinjunhang.contract.service;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lzn on 16/4/7.
 */
public class LoginService extends BasicService {


    private final static String TAG = "LoginService";

    public LoginResponse login(String userName, String password) {

        Map<String, String> params = new LinkedHashMap();
        params.put("x", userName);
        params.put("y", password);
        return sendRequest(ServiceConfiguration.LoginUrl(), params, LoginResponse.class, new ResponseHandler() {
            @Override
            public ServerResponse handle(ServerResponse response, JSONObject json) throws JSONException {
                LoginResponse resp = (LoginResponse)response;
                JSONObject resultJson = json.getJSONObject("result");
                if (resultJson.getBoolean("success")) {
                    resp.setSuccess(true);
                    resp.setName(resultJson.getString("name"));
                    resp.setDepartment(resultJson.getString("department"));
                } else {
                    resp.setSuccess(false);
                    resp.setErrMessage(resultJson.getString("errorMessage"));
                }

                Log.d(TAG, String.format("resp.status = %d", resp.getStatus()));
                Log.d(TAG, "success = " + resp.isSuccess());

                return resp;
            }
        });

    }

}

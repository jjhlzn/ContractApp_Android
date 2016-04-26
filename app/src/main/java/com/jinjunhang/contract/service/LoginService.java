package com.jinjunhang.contract.service;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by lzn on 16/4/7.
 */
public class LoginService extends BasicService {


    private final static String TAG = "LoginService";

    public LoginResponse login(String userName, String password) {

        String url = makeLoginUrl(userName, password);

        return sendRequest(url, LoginResponse.class, new ResponseHandler() {
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

    private String makeLoginUrl(String userName, String password) {
        String queryStr = "";
        try {
            queryStr = String.format("x=%s&y=%s", URLEncoder.encode(userName, "UTF-8"), URLEncoder.encode(password, "UTF-8"));

        }catch (UnsupportedEncodingException ex) {

        }
        return String.format(ServiceConfiguration.LoginUrl + "?%s", queryStr);
    }

}

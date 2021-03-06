package com.jinjunhang.framework.service;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lzn on 16/3/23.
 */
public class BasicService {

    private final static String TAG = "BasicService";

    private OkHttpClient client = new OkHttpClient();

    private String send(ServerRequest request) throws IOException {
        String method = "POST";
        Map<String, Object> params = addMoreRequestInfo(request.getParams());

        Gson gson = new Gson();

        String paramsString = gson.toJson(params);
        Log.d(TAG, "paramsString = " + paramsString);

        String url = request.getServiceUrl();

        if (method == "GET") {
            url = url + "?" + paramsString;
            Log.d(TAG, "send request: " + url);
            return get(url);
        } else {
            Log.d(TAG, "send request: " + url);

            return post(url, makePostBody(request.getParams()));
        }
    }

    private RequestBody makePostBody(Map<String, Object> params) {
        FormBody.Builder fe = new FormBody.Builder();
        try {
            for (Map.Entry<String, Object> pair : params.entrySet()) {
                fe.add(pair.getKey(), URLEncoder.encode(pair.getValue().toString(), "UTF-8"));

            }
        }catch (UnsupportedEncodingException ex){

        }
        return fe.build();
    }

    String post(String url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private Map<String, Object> addMoreRequestInfo(Map<String, Object> params) {
        Map<String, Object> newParams = new LinkedHashMap<>();
        newParams.put("request", params);
        //newParams["client"] = getClientInfo()
        //newParams["userInfo"] = getUserInfo()
        return newParams;

    }

    private String makeQueringString(Map<String, String> params) {
        if (params.size() == 0) {
            return "";
        }

        String result = "";

        for (Map.Entry<String, String> pair : params.entrySet()) {
            try {
                result += pair.getKey() + "=" + URLEncoder.encode(pair.getValue(), "UTF-8") + "&";
            }catch (UnsupportedEncodingException ex) {

            }

        }

        return  result;
    }


    private String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }




    public <T extends ServerResponse> T sendRequest(ServerRequest request) {
        T resp = null;
        try {
            resp = (T)request.getServerResponseClass().newInstance();

            String httpMethod = "POST";
            Log.d(TAG, "httpMethod = " +httpMethod);
            String jsonString = send(request);

            Log.d(TAG, "get response: " + jsonString);
            JSONObject json =  new JSONObject(jsonString);
            resp.setStatus(json.getInt("status"));
            resp.setErrorMessage(json.getString("errorMessage"));
            if (json.getInt("status") != ServerResponse.SUCCESS) {
                return resp;
            }
            resp.parse(request, json);
            return resp;

        } catch (InstantiationException e) {
            return null;
        }
        catch (IllegalAccessException e){
            return null;
        }
        catch (IOException ioe) {
            Log.e(TAG, "Fetch " + request.getServiceUrl() + " failed: ", ioe);
            resp.setStatus(ServerResponse.FAIL);
            resp.setErrorMessage("IOException happen");
            return resp;
        } catch (JSONException e) {
            Log.e(TAG, "Parse response of " + request.getServiceUrl() + " failed: ", e);
            resp.setStatus(ServerResponse.FAIL);
            resp.setErrorMessage("JSON parse exception happen");
            return resp;
        }
    }


}


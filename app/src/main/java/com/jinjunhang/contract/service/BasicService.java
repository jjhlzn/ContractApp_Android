package com.jinjunhang.contract.service;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lzn on 16/3/23.
 */
public abstract class BasicService {

    private final static String TAG = "BasicService";

    private OkHttpClient client = new OkHttpClient();

    private String send(String url, Map<String, String> params, String method) throws IOException {
        String paramsString = makeQueringString(params);
        Log.d(TAG, "paramsString = " + paramsString);
        if (method == "GET") {
            url = url + "?" + paramsString;
            Log.d(TAG, "send request: " + url);
            return get(url);
        } else {
            Log.d(TAG, "send request: " + url);
            return post(url, makePostBody(params));
        }
    }

    String post(String url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
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



    private RequestBody makePostBody(Map<String, String> params) {
        FormBody.Builder fe = new FormBody.Builder();
        try {
            for (Map.Entry<String, String> pair : params.entrySet()) {
                fe.add(pair.getKey(), URLEncoder.encode(pair.getValue(), "UTF-8"));

            }
        }catch (UnsupportedEncodingException ex){

        }
        return fe.build();
    }



    protected <T extends ServerResponse> T sendRequest(String url, Map<String, String> parameters,
                                                       Class<T> c, ResponseHandler handler) {
        T resp = null;
        try {
            resp = c.newInstance();

            String httpMethod = "POST";
            Log.d(TAG, "httpMethod = " +httpMethod);
            String jsonString = send(url, parameters, httpMethod);


            Log.d(TAG, "get response: " + jsonString);
            JSONObject json =  new JSONObject(jsonString);
            if (json.getInt("status") == ServerResponse.FAIL) {
                resp.setStatus(ServerResponse.FAIL);
                resp.setErrorMessage("Server return fail response");
                return resp;
            }
            resp.setStatus(ServerResponse.SUCCESS);

            return (T)handler.handle(resp, json);

        } catch (InstantiationException e) {
            return null;
        }
        catch (IllegalAccessException e){
            return null;
        }
        catch (IOException ioe) {
            Log.e(TAG, "Fetch " + url + " failed: ", ioe);
            resp.setStatus(ServerResponse.FAIL);
            resp.setErrorMessage("IOException happen");
            return resp;
        } catch (JSONException e) {
            Log.e(TAG, "Parse response of " + url + " failed: ", e);
            resp.setStatus(ServerResponse.FAIL);
            resp.setErrorMessage("JSON parse exception happen");
            return resp;
        }
    }


}


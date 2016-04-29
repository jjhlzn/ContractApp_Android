package com.jinjunhang.contract.service;

import android.util.Log;

import com.jinjunhang.contract.service.ResponseHandler;
import com.jinjunhang.contract.service.ServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lzn on 16/3/23.
 */
public abstract class BasicService {

    private final static String TAG = "BasicService";


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    private String send(String url, Map<String, String> params, String method) throws IOException {
        String paramsString = makeString(params, method);
        Log.d(TAG, "paramsString = " + paramsString);
        if (method == "GET") {
            url = url + "?" + paramsString;
            Log.d(TAG, "send request: " + url);
            return get(url, "");
        } else {
            Log.d(TAG, "send request: " + url);
            return post(url, paramsString);
        }
    }

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    String get(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String makeString(Map<String, String> params, String httpMethod) {
        if (params.size() == 0) {
            return "";
        }

        String result = "";
        for (Map.Entry<String, String> pair : params.entrySet()) {
            if (httpMethod == "GET") {
                try {
                    result += pair.getKey() + "=" + URLEncoder.encode(pair.getValue(), "UTF-8") + "&";
                }catch (UnsupportedEncodingException ex) {

                }
            } else {
                result += pair.getKey() + "=" + pair.getValue() + "&";
            }
        }

        return  result;
    }

    protected <T extends ServerResponse> T sendRequest(String url,
                                                       Class<T> c, ResponseHandler handler) {
        return sendRequest(url, new HashMap<String, String>(), c, handler);
    }

    protected <T extends ServerResponse> T sendRequest(String url, Map<String, String> parameters,
                                                       Class<T> c, ResponseHandler handler) {
        T resp = null;
        try {
            resp = c.newInstance();

            String httpMethod = "GET";
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


    /*
    protected <T extends ServerResponse> T sendRequest1(String url, Class<T> c, ResponseHandler handler) {
        T resp = null;
        try {
            resp = c.newInstance();
            String jsonString = getUrl(url);
            Log.d(TAG, "send request: " + url);
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

    byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();

        } finally {
            connection.disconnect();
        }
    }

    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }*/

}


package com.jinjunhang.contract.service;

import android.util.Log;

import com.jinjunhang.contract.service.ResponseHandler;
import com.jinjunhang.contract.service.ServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lzn on 16/3/23.
 */
public abstract class BasicService {

    private final static String TAG = "BasicService";

    //protected abstract ServerResponse createResponse();


    protected <T extends ServerResponse> T sendRequest(String url, Class<T> c, ResponseHandler handler) {
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
    }

}


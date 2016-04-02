package com.jinjunhang.contract.service;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzn on 16/3/23.
 */

public interface ResponseHandler {
    public ServerResponse handle(ServerResponse resp, JSONObject json) throws JSONException;
}
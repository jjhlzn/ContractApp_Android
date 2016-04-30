package com.jinjunhang.contract.model;

/**
 * Created by lzn on 16/4/30.
 */
public class ServiceLocator {

    private String mHttp;
    private String mServerName;
    private int    mPort;

    public String getHttp() {
        return mHttp;
    }

    public String getServerName() {
        return mServerName;
    }

    public int getPort() {
        return mPort;
    }

    public void setHttp(String http) {
        mHttp = http;
    }

    public void setServerName(String serverName) {
        mServerName = serverName;
    }

    public void setPort(int port) {
        mPort = port;
    }
}
